package com.fjmob.ponto.task;

import java.util.List;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class RegistrarSaldoTask extends AsyncTask<String, Integer, String> {

	private Activity activity;
	private long tempoSaldo;
	
	public RegistrarSaldoTask(Activity activity, long tempoSaldo) {
		this.activity = activity;
		this.tempoSaldo = tempoSaldo;
	}
	
	
	@Override
	protected String doInBackground(String... params) {
		
		final String uid = params[0];
		
		final String email = getEmail(activity) != null ? getEmail(activity) : "ND";

		
		ParseQuery<ParseObject> query = ParseQuery.getQuery("SaldoUsuario");
		query.whereEqualTo("uidUser", uid);
		
		query.findInBackground(new FindCallback<ParseObject>() {
		    public void done(List<ParseObject> saldos, ParseException e) {
		        if (e == null) {
		        	if(saldos.size() > 0) {
		        		for (ParseObject saldo : saldos) {
		        			saldo.put("saldoUser", tempoSaldo);
		        			saldo.put("emailUser", email);
		        			saldo.saveInBackground();
						}
		        	} else {
		        		ParseObject saldo = new ParseObject("SaldoUsuario");
		        		saldo.put("saldoUser", tempoSaldo);
	        			saldo.put("uidUser", uid);
	        			saldo.put("emailUser", email);
	        			saldo.saveInBackground();
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
}
