package com.fjmob.ponto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.widget.RemoteViews;

import com.fjmob.ponto.component.Cronometro;
import com.fjmob.ponto.entity.Configuracoes;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.handler.JornadaHandler;
import com.fjmob.ponto.persistence.ConfiguracoesDAO;
import com.fjmob.ponto.persistence.HistoricoDAO;

public class SaldoWidget extends AppWidgetProvider {

	@SuppressLint("SimpleDateFormat") @Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onEnabled(context);


		List<Historico> historicos = HistoricoDAO.getInstance(context).recuperarTodos();

		TreeMap<String, List<Historico>> mapaHistorico = montarMapaHistorico(historicos);

		long tempoJornada = 0; //TODO parametrizar jornada
		long tempoPresente = 0;
		String saldoTotal = "";
		SimpleDateFormat sdfData = new SimpleDateFormat("yyyy/MM/dd");
		
		for(Entry<String, List<Historico>> entry : mapaHistorico.entrySet())  {
			if(!entry.getKey().equals(sdfData.format(new Date()))) {

				List<Historico> listaHistorico = entry.getValue();
				if(listaHistorico.size() % 2 == 0) {
					for(int i = 0 ; i < listaHistorico.size() ; i++) {
						long tempoInicial = listaHistorico.get(i).getDataGravacao().getTime();
						long tempoFinal = listaHistorico.get(i+1).getDataGravacao().getTime();
						tempoPresente += tempoFinal - tempoInicial;
						i++;
					}

					tempoJornada += recuperarTempoJornada(context);
				}
			}

		}

		long tempoSaldo = tempoPresente - tempoJornada;
		int seconds = (int) TimeUnit.MILLISECONDS.toSeconds(tempoSaldo) % 60;
		int minutes = (int) TimeUnit.MILLISECONDS.toMinutes(tempoSaldo) % 60;
		int hours   = (int) TimeUnit.MILLISECONDS.toHours(tempoSaldo);

		if(tempoSaldo < 0) {
			String horasString = String.format("%03d",hours);
			saldoTotal = (horasString.equals("000") ? "-00" : horasString) + ":" + 
					String.format("%02d",minutes).replaceAll("-", "") + ":"	+ 
					String.format("%02d",seconds).replaceAll("-", "");
		} else {
			saldoTotal = "+"+String.format("%02d",hours) + ":" + 
					String.format("%02d",minutes).replaceAll("-", "") + ":"	+ 
					String.format("%02d",seconds).replaceAll("-", "");
		}


		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.saldo_widget);

		rv.setTextViewText(R.id.saldoClockWidget, saldoTotal);

		ComponentName cn = new ComponentName(context, SaldoWidget.class);
		AppWidgetManager.getInstance(context).updateAppWidget(cn, rv);
	}


	public TreeMap<String, List<Historico>> montarMapaHistorico(List<Historico> historicos) {

		DateFormat sdfData = new SimpleDateFormat("yyyy/MM/dd");

		TreeMap<String, List<Historico>> mapaHistorico = new TreeMap<String, List<Historico>>(Collections.reverseOrder());

		Collections.sort(historicos, new Comparator<Historico>() {

			@Override
			public int compare(Historico his1, Historico his2) {
				return his1.getDataGravacao().compareTo(his2.getDataGravacao());
			}
		});

		for (Historico historico : historicos) {
			String dataKey = sdfData.format(historico.getDataGravacao());
			if(mapaHistorico.get(dataKey) == null) {
				mapaHistorico.put(dataKey, new ArrayList<Historico>());
			}
			mapaHistorico.get(dataKey).add(historico);
		}
		return mapaHistorico;
	}
	
	private long recuperarTempoJornada(Context context) {
		long tempoJornada = 28800000;
		Configuracoes configuracoes = ConfiguracoesDAO.getInstance(context).recuperarPorId(1);
		if(configuracoes != null) {
			tempoJornada = configuracoes.getJornadaHoras() * 3600000;
			tempoJornada += configuracoes.getJornadaMinutos() * 60000;
		}
		
		return tempoJornada;
	}

}
