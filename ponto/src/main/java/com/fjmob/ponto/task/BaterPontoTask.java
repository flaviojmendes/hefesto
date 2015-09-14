package com.fjmob.ponto.task;

import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjmob.ponto.R;
import com.fjmob.ponto.component.Cronometro;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.handler.HistoricoHandler;
import com.fjmob.ponto.handler.TimerHandler;
import com.fjmob.ponto.persistence.HistoricoDAO;

public class BaterPontoTask extends AsyncTask<String, Integer, Boolean>{

	private Activity activity;
	private View rootView;
	private Date dataCal;
	private Cronometro chrono;
	private String uid;
	
	public BaterPontoTask(Activity activity, View rootView, Date dataCal, Cronometro chrono, String uid) {
		this.activity = activity;
		this.rootView = rootView;
		this.dataCal = dataCal;
		this.chrono = chrono;
		this.uid = uid;
	}
	
	
	@Override
	protected Boolean doInBackground(String... params) {
		Historico historico = new Historico();
		historico.setDataGravacao(new Date());
		HistoricoDAO.getInstance(getActivity()).salvar(historico);

		VerificarAdicionarHistoricoTask histTask = new VerificarAdicionarHistoricoTask(activity);
		histTask.execute(uid);
		
		
		return true;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		LinearLayout linearLayout = (LinearLayout) getActivity().findViewById(R.id.listaHorarios);
		linearLayout.removeAllViews();

		Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");

		TextView saldoTotal = (TextView) rootView.findViewById(R.id.saldoTotal);
		LinearLayout layoutHorarios =  (LinearLayout) rootView.findViewById(R.id.listaHorarios);

		HistoricoHandler historicoHandler = new HistoricoHandler(getActivity(), layoutHorarios , saldoTotal, dataCal, saldoTotal);


		TreeMap<String, List<Historico>> mapaHistorico = historicoHandler.popularHistorico(robotoLight);

		TimerHandler timerHandler = new TimerHandler(chrono, getActivity());
		timerHandler.verificaIniciaTimer(mapaHistorico);
		super.onPostExecute(result);
	}

	public Activity getActivity() {
		return activity;
	}


	public void setActivity(Activity activity) {
		this.activity = activity;
	}
	
	

}
