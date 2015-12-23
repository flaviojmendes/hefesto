package com.fjmob.ponto;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings.Secure;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fjmob.ponto.component.Cronometro;
import com.fjmob.ponto.entity.Falta;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.entity.Mood;
import com.fjmob.ponto.handler.HistoricoHandler;
import com.fjmob.ponto.handler.ManipulacaoDadosHandler;
import com.fjmob.ponto.handler.TimerHandler;
import com.fjmob.ponto.persistence.FaltaDAO;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.fjmob.ponto.persistence.MoodDAO;
import com.fjmob.ponto.task.FazerBackupTask;
import com.fjmob.ponto.task.ImportarBackupTask;
import com.fjmob.ponto.task.VerificarAdicionarHistoricoTask;
import com.fjmob.ponto.task.VerificarAdicionarMoodTask;
import com.fjmob.ponto.task.VerificarRemoverHistoricoTask;
import com.fjmob.ponto.task.VerificarRemoverMoodTask;
import com.fjmob.ponto.util.HistoricoFragment;

public class MainActivity extends ActionBarActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {

	private NavigationDrawerFragment mNavigationDrawerFragment;
	private CharSequence mTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		mNavigationDrawerFragment = (NavigationDrawerFragment)
				getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();





		getSupportActionBar().setElevation(0);
		
		String deviceId = Secure.getString(this.getContentResolver(),
				Secure.ANDROID_ID);
		
		VerificarAdicionarHistoricoTask histTask = new VerificarAdicionarHistoricoTask(this);
		histTask.execute(deviceId);
		
		VerificarRemoverHistoricoTask histRemTask = new VerificarRemoverHistoricoTask(this);
		histRemTask.execute(deviceId);

		VerificarAdicionarMoodTask moodTask = new VerificarAdicionarMoodTask(this);
		moodTask.execute(deviceId);

		VerificarRemoverMoodTask moodRemTask = new VerificarRemoverMoodTask(this);
		moodRemTask.execute(deviceId);
		
		
		
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP){
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.setStatusBarColor(getResources().getColor(R.color.primary_dark));
		} else{
		}

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
			.add(R.id.container, new HistoricoFragment()).commit();
		}

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.container, com.fjmob.ponto.PlaceholderFragment.newInstance(position + 1))
				.commit();
	}

	public void onSectionAttached(int number) {
		switch (number) {
			case 1:
				mTitle = getString(R.string.action_main);
                getSupportActionBar().setTitle(mTitle);
                removerOutrosFragments();
                FragmentTransaction frgTrns = getFragmentManager().beginTransaction()
                        .replace(R.id.container, new HistoricoFragment());
                frgTrns.addToBackStack(getResources().getString(R.string.action_main));
                frgTrns.commit();

				break;
			case 2:
                mTitle = getString(R.string.action_adicionar_horario);
                getSupportActionBar().setTitle(mTitle);
                removerOutrosFragments();
                FragmentTransaction frgTrnsHora = getFragmentManager().beginTransaction()
                        .replace(R.id.container, new AdicionarHoraFragment());
                frgTrnsHora.addToBackStack(getResources().getString(R.string.action_adicionar_horario));
                frgTrnsHora.commit();
                break;
            case 3:
                mTitle = getString(R.string.action_adicionar_falta);
                getSupportActionBar().setTitle(mTitle);
                removerOutrosFragments();
                FragmentTransaction frgTrnsFalta = getFragmentManager().beginTransaction()
                        .replace(R.id.container, new AdicionarFaltaFragment());
                frgTrnsFalta.addToBackStack(getResources().getString(R.string.action_adicionar_falta));
                frgTrnsFalta.commit();
                break;
            case 4:
                mTitle = getString(R.string.action_ranking);
                getSupportActionBar().setTitle(mTitle);
                removerOutrosFragments();
                FragmentTransaction frgTrnsRnk = getFragmentManager().beginTransaction()
                        .replace(R.id.container, new RankingFragment());
                frgTrnsRnk.addToBackStack(getResources().getString(R.string.action_ranking));
                frgTrnsRnk.commit();
                break;
            case 5:
                mTitle = getString(R.string.action_configuracoes);
                getSupportActionBar().setTitle(mTitle);
                removerOutrosFragments();
                FragmentTransaction frgTrnsCfg = getFragmentManager().beginTransaction()
                        .replace(R.id.container, new ConfiguracoesFragment());
                frgTrnsCfg.addToBackStack(getResources().getString(R.string.action_configuracoes));
                frgTrnsCfg.commit();
                break;

		}
	}

    private void removerOutrosFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        for(Fragment fragment : fragmentManager.getFragments()) {
            if(fragment != null && fragment.getId() != R.id.navigation_drawer) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }


    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();


		final Activity activity = this;

		if (id == R.id.action_exportar) {


			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
						case DialogInterface.BUTTON_POSITIVE:

							String deviceId = Secure.getString(getApplicationContext().getContentResolver(),
									Secure.ANDROID_ID);

							FazerBackupTask bkpTask = new FazerBackupTask(activity);
							bkpTask.execute(deviceId);

							break;

						case DialogInterface.BUTTON_NEGATIVE:
							break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(this.getResources().getString(R.string.exportar_confirmacao))
					.setPositiveButton(this.getResources().getString(R.string.sim), dialogClickListener)
					.setNegativeButton(this.getResources().getString(R.string.nao), dialogClickListener).show();




			return true;

		}
		if (id == R.id.action_importar) {



			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
						case DialogInterface.BUTTON_POSITIVE:


							String deviceId = Secure.getString(getApplicationContext().getContentResolver(),
									Secure.ANDROID_ID);

							ImportarBackupTask bkpTask = new ImportarBackupTask(activity);
							bkpTask.execute(deviceId);


							break;

						case DialogInterface.BUTTON_NEGATIVE:
							break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage(this.getResources().getString(R.string.importar_confirmacao))
					.setPositiveButton(this.getResources().getString(R.string.sim), dialogClickListener)
					.setNegativeButton(this.getResources().getString(R.string.nao), dialogClickListener).show();




			return true;

		}



		if(id == R.id.action_excluir_tudo) {
			DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					switch (which){
					case DialogInterface.BUTTON_POSITIVE:

						Typeface robotoLight = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");

						TextView saldoTotal = (TextView) findViewById(R.id.saldoTotal);
						LinearLayout layoutHorarios =  (LinearLayout) findViewById(R.id.listaHorarios);

						HistoricoHandler historicoHandler = new HistoricoHandler(activity, layoutHorarios , saldoTotal, new Date(), saldoTotal);


						TreeMap<String, List<Historico>> mapaHistorico = historicoHandler.popularHistorico(robotoLight);

						for(Entry<String, List<Historico>> entry : mapaHistorico.entrySet()) {
							for(Historico historico : entry.getValue()) {
								HistoricoDAO.getInstance(activity).deletar(historico);
							}
						}

						LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.listaHorarios);
						linearLayout.removeAllViews();

						historicoHandler.popularHistorico(robotoLight);

						TimerHandler timerHandler = new TimerHandler(Cronometro.getInstance(activity), activity);
						timerHandler.verificaIniciaTimer(mapaHistorico);
						historicoHandler.calcularSaldoTotal(mapaHistorico);


						break;

					case DialogInterface.BUTTON_NEGATIVE:
						break;
					}
				}
			};

			AlertDialog.Builder builder = new AlertDialog.Builder(activity);
			builder.setMessage(activity.getResources().getString(R.string.apagar_tudo_confirmacao))
			.setPositiveButton(activity.getResources().getString(R.string.sim), dialogClickListener)
			.setNegativeButton(activity.getResources().getString(R.string.nao), dialogClickListener).show();

			return true;

		}


		return super.onOptionsItemSelected(item);
		
		
		
	}



	@Override
    public void onBackPressed() {

        int count = getFragmentManager().getBackStackEntryCount();

        if (count == 0) {
            super.onBackPressed();
            //additional code
        } else {
            getFragmentManager().popBackStack();
        }

    }

}
