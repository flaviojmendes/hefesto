package com.fjmob.ponto.task;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class VerificarAdicionarHistoricoTask extends AsyncTask<String, Integer, String> {

	private Activity activity;
	
	public VerificarAdicionarHistoricoTask(Activity activity) {
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
		        	for(Historico historico : historicos){
		        		boolean hasHistorico = false;
		        		for (ParseObject horario : horarios) {
							if(historico.getId().intValue() == horario.getInt("idHistoricoUser")) {
								hasHistorico = true;
								break;
							}
						}
		        		
		        		if(!hasHistorico) {
		        			ParseObject horario = new ParseObject("Horario");
		        			horario.put("uidUser", uid);
		        			horario.put("idHistoricoUser", historico.getId());
		        			horario.put("dtPonto", historico.getDataGravacao());
		        			horario.saveInBackground();
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
