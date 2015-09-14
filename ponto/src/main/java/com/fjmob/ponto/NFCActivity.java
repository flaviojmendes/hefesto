package com.fjmob.ponto;

import java.util.Date;

import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.fjmob.ponto.task.VerificarAdicionarHistoricoTask;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings.Secure;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class NFCActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nfc);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.nfc, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_nfc, container,
					false);
			
			vibrar();
			
			baterPonto();
			
			navegar();
			
			
			return rootView;
		}

		private void baterPonto() {
			Historico historico = new Historico();
			historico.setDataGravacao(new Date());
			HistoricoDAO.getInstance(getActivity()).salvar(historico);
			
//			String uid = Secure.getString(getActivity().getContentResolver(),
//					Secure.ANDROID_ID);
//			VerificarAdicionarHistoricoTask histTask = new VerificarAdicionarHistoricoTask(getActivity());
//			histTask.execute(uid);
			
		}

		private void navegar() {
			Intent intent = new Intent(getActivity(), MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
		}

		private void vibrar() {
			Vibrator vibrator;
			vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(1000);
		}

		@Override
		public void onResume() {
			super.onResume();
			navegar();
		}
	}
}
