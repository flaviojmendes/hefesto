package com.fjmob.ponto.task;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;

import android.app.FragmentTransaction;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.fjmob.ponto.R;
import com.fjmob.ponto.entity.Falta;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.entity.Mood;
import com.fjmob.ponto.persistence.FaltaDAO;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.fjmob.ponto.persistence.MoodDAO;
import com.fjmob.ponto.HistoricoFragment;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.thoughtworks.xstream.XStream;

import java.util.List;

public class ImportarBackupTask extends AsyncTask<String, Integer, String> {

	private Activity activity;

	public ImportarBackupTask(Activity activity) {
		this.activity = activity;

	}

	@Override
	protected void onPreExecute() {
		ProgressBarCircularIndeterminate prog = (ProgressBarCircularIndeterminate) activity.findViewById(R.id.loadingIcon);
		prog.setVisibility(View.VISIBLE);
		prog.removeAllViews();
		prog.refreshDrawableState();

	}




	@Override
	protected String doInBackground(String... params) {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("Backup");
		query.whereEqualTo("uidUser", getEmail(activity));


		try {
			List<ParseObject> backups = query.find();


			if (backups.size() > 0) {
				ParseObject backup = backups.get(0);

				String historyXml = (String) backup.get("history");
				String moodXml = (String) backup.get("moods");
				String absencesXml = (String) backup.get("absences");


				XStream xStream = new XStream();
				List<Historico> historicos = (List<Historico>) xStream.fromXML(historyXml);


				for (Historico historico : historicos) {
					if (HistoricoDAO.getInstance(activity).recuperarPorId(historico.getId()) == null) {
						HistoricoDAO.getInstance(activity).salvar(historico);
					}
				}

				List<Mood> moods = (List<Mood>) xStream.fromXML(moodXml);

				for (Mood mood : moods) {
					if (MoodDAO.getInstance(activity).recuperarPorId(mood.getId()) == null) {
						MoodDAO.getInstance(activity).salvar(mood);
					}
				}


				List<Falta> faltas = (List<Falta>) xStream.fromXML(absencesXml);

				for (Falta falta : faltas) {
					if (FaltaDAO.getInstance(activity).recuperarPorId(falta.getId()) == null) {
						FaltaDAO.getInstance(activity).salvar(falta);
					}
				}

			} else {

			}
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return params[0];
	}



	@Override
	protected void onPostExecute(String s) {
		ProgressBarCircularIndeterminate prog = (ProgressBarCircularIndeterminate) activity.findViewById(R.id.loadingIcon);
		prog.setVisibility(View.INVISIBLE);
		prog.removeAllViews();
		prog.refreshDrawableState();


		removerOutrosFragments();
		FragmentTransaction frgTrns = ((FragmentActivity)activity).getFragmentManager().beginTransaction()
				.replace(R.id.container, new HistoricoFragment());
		frgTrns.addToBackStack(((FragmentActivity) activity).getResources().getString(R.string.action_main));
		frgTrns.commit();

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

	private void removerOutrosFragments() {
		FragmentManager fragmentManager = ((FragmentActivity)activity).getSupportFragmentManager();
		for(Fragment fragment : fragmentManager.getFragments()) {
			if(fragment != null && fragment.getId() != R.id.navigation_drawer) {
				fragmentManager.beginTransaction().remove(fragment).commit();
			}
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
