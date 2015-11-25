package com.fjmob.ponto.task;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.fjmob.ponto.R;
import com.fjmob.ponto.entity.Falta;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.entity.Mood;
import com.fjmob.ponto.handler.ManipulacaoDadosHandler;
import com.fjmob.ponto.persistence.FaltaDAO;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.fjmob.ponto.persistence.MoodDAO;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FazerBackupTask extends AsyncTask<String, Integer, String> {

	private Activity activity;


	public FazerBackupTask(Activity activity) {
		this.activity = activity;

	}


	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		ProgressBarCircularIndeterminate prog = (ProgressBarCircularIndeterminate) activity.findViewById(R.id.loadingIcon);
		prog.setVisibility(View.VISIBLE);
		prog.removeAllViews();
		prog.refreshDrawableState();
	}

	@Override
	protected String doInBackground(String... params) {


		final List<Historico> historicos = HistoricoDAO.getInstance(activity).recuperarTodos();
		final List<Mood> moods = MoodDAO.getInstance(activity).recuperarTodos();
		final List<Falta> faltas = FaltaDAO.getInstance(activity).recuperarTodos();


		ParseQuery<ParseObject> query = ParseQuery.getQuery("Backup");
		query.whereEqualTo("uidUser", getEmail(activity));


		try {
			List<ParseObject> backups = query.find();


			ManipulacaoDadosHandler handler = new ManipulacaoDadosHandler();
			String xmlRegistros = handler.exportarHistoricos(historicos);
			String xmlMoods = handler.exportarMoods(moods);
			String xmlFaltas = handler.exportarFaltas(faltas);

			if (backups.size() > 0) {
				for (ParseObject backup : backups) {

					backup.put("history", xmlRegistros);
					backup.put("moods", xmlMoods);
					backup.put("absences", xmlFaltas);

					backup.saveInBackground();
				}
			} else {
				ParseObject backup = new ParseObject("Backup");


				backup.put("history", xmlRegistros);
				backup.put("moods", xmlMoods);
				backup.put("absences", xmlFaltas);
				backup.put("uidUser", getEmail(activity));

				backup.saveInBackground();
			}

		} catch (ParseException e) {
			e.printStackTrace();
		}


		return params[0];
	}

	@Override
	protected void onPostExecute(String s) {
		super.onPostExecute(s);
		ProgressBarCircularIndeterminate prog = (ProgressBarCircularIndeterminate) activity.findViewById(R.id.loadingIcon);
		prog.setVisibility(View.INVISIBLE);
		prog.removeAllViews();
		prog.refreshDrawableState();

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
}
