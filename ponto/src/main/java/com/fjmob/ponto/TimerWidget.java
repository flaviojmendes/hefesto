package com.fjmob.ponto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.os.SystemClock;
import android.widget.RemoteViews;

import com.fjmob.ponto.component.Cronometro;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.handler.JornadaHandler;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.fjmob.ponto.util.Constantes;

public class TimerWidget extends AppWidgetProvider {

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onEnabled(context);
		
		Cronometro cronometro = Cronometro.getInstance(context);
		
		List<Historico> historicos = HistoricoDAO.getInstance(context).recuperarTodos();
		
		TreeMap<String, List<Historico>> mapaHistorico = montarMapaHistorico(historicos);
		
		DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		List<Historico> historicoHoje = mapaHistorico.get(sdf.format(new Date()));
		
		
		boolean chronoIniciado = false;
		long tempoChrono = SystemClock.elapsedRealtime();
		
		if(historicoHoje != null) {
			if(historicoHoje.size() > 0 && historicoHoje.size() % 2 > 0) {
				chronoIniciado = true;
				tempoChrono = cronometro.getBase();
				
				for(int i = 0 ; i < historicoHoje.size() ; i++) {
					long tempoInicial = historicoHoje.get(i).getDataGravacao().getTime();
					long tempoFinal = 0;
					if(i == (historicoHoje.size()-1)) {
						tempoFinal = cronometro.getBase();
					} else {
						tempoFinal = historicoHoje.get(i+1).getDataGravacao().getTime();
					}
					tempoChrono += tempoFinal - tempoInicial;
					i++;
				}
			} else {
				chronoIniciado = false;
				for(int i = 0 ; i < historicoHoje.size() ; i++) {
					long tempoInicial = historicoHoje.get(i).getDataGravacao().getTime();
					long tempoFinal = historicoHoje.get(i+1).getDataGravacao().getTime();
					tempoChrono += tempoFinal - tempoInicial;
					i++;
				}
			}
			JornadaHandler jornadaHandler = new JornadaHandler(null);
			tempoChrono -= tempoChrono - jornadaHandler.recuperarTempoJornada();
		} else {
			tempoChrono = cronometro.getBase();
		}
		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.timer_widget);
		
		rv.setChronometer(R.id.timerWidget, tempoChrono, null, chronoIniciado);
		
		
		ComponentName cn = new ComponentName(context, TimerWidget.class);
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
	
}
