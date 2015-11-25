package com.fjmob.ponto.task;

import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class VerificarRemoverHistoricoTask extends AsyncTask<String, Integer, String> {

	private Activity activity;

	public VerificarRemoverHistoricoTask(Activity activity) {
		this.activity = activity;
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		
		final String uid = params[0];
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Horario");
		query.whereEqualTo("uidUser", uid);
		
		final List<Historico> historicos = HistoricoDAO.getInstance(activity).recuperarTodos();
		
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> horarios, ParseException e) {
		        if (e == null) {
		        	for (ParseObject horario : horarios) {
		        		boolean hasHistorico = false;
		        		for(Historico historico : historicos){
							if(horario.getInt("idHistoricoUser") == historico.getId().intValue()) {
								hasHistorico = true;
								break;
							}
						}
		        		
		        		if(!hasHistorico) {
		        			horario.deleteInBackground();
		        		}
		        	}
		        } else {
		        	
		        }
		    }
		});
		
		return params[0];
	}

	@Override
	protected void onPostExecute(final String uid) {
		super.onPostExecute(uid);






	}
	
}
