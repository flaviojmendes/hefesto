package com.fjmob.ponto.task;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;

import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.entity.Mood;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.fjmob.ponto.persistence.MoodDAO;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class VerificarAdicionarMoodTask extends AsyncTask<String, Integer, String> {

	private Activity activity;

	public VerificarAdicionarMoodTask(Activity activity) {
		this.activity = activity;
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		
		final String uid = params[0];
		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("Mood");
		query.whereEqualTo("uidUser", uid);
		
		final List<Mood> moods = MoodDAO.getInstance(activity).recuperarTodos();
		
		
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> moodsVos, ParseException e) {
		        if (e == null) {
		        	for(Mood mood : moods){
		        		boolean hasMood = false;
		        		for (ParseObject moodVo : moodsVos) {
							if(mood.getId().intValue() == moodVo.getInt("idMood")) {
								hasMood = true;
								break;
							}
						}
		        		
		        		if(!hasMood) {
		        			ParseObject moodVo = new ParseObject("Mood");
							moodVo.put("uidUser", uid);
							moodVo.put("idMood", mood.getId());
							moodVo.put("dtMood", mood.getDataGravacao());
							moodVo.put("email", getEmail(activity));
							if(mood.getMood() != null) {
								moodVo.put("txtMood", mood.getMood());
							}
							if(mood.getComentario() != null) {
								moodVo.put("comment", mood.getComentario());
							}

							moodVo.saveInBackground();
		        		}
		        	}
		        } else {
		        	
		        }
		    }
		});
		
		return params[0];
	}

	static String getEmail(Context context) {
		try {
			AccountManager accountManager = AccountManager.get(context);
			Account account = getAccount(accountManager);
			if (account == null) {
				return null;
			} else {
				return account.name;
			}
		} catch(Exception e) {
			return "nd";
		}
	}
	private static Account getAccount(AccountManager accountManager) {
		Account[] accounts = accountManager.getAccountsByType("com.google");
		Account account;
		if (accounts.length > 0) {
			account = accounts[0];
		} else {
			account = null;
		} return account;
	}


	@Override
	protected void onPostExecute(final String uid) {
		super.onPostExecute(uid);






	}
	
}
