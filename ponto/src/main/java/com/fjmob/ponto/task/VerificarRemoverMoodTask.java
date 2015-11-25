package com.fjmob.ponto.task;

import android.app.Activity;
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

public class VerificarRemoverMoodTask extends AsyncTask<String, Integer, String> {

	private Activity activity;

	public VerificarRemoverMoodTask(Activity activity) {
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
		        	for (ParseObject moodVo : moodsVos) {
		        		boolean hasMood = false;
		        		for(Mood mood : moods){
							if(moodVo.getInt("idMood") == mood.getId().intValue()) {
								hasMood = true;
								break;
							}
						}
		        		
		        		if(!hasMood) {
		        			moodVo.deleteInBackground();
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
