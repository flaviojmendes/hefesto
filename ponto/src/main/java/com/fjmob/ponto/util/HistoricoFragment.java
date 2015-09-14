package com.fjmob.ponto.util;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.fjmob.ponto.AdicionarFaltaFragment;
import com.fjmob.ponto.AdicionarHoraFragment;
import com.fjmob.ponto.GraficoActivity;
import com.fjmob.ponto.R;
import com.fjmob.ponto.component.Cronometro;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.handler.HistoricoHandler;
import com.fjmob.ponto.handler.TimerHandler;
import com.fjmob.ponto.task.BaterPontoTask;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class HistoricoFragment extends Fragment {


    private FloatingActionButton btnPonto;
    private FloatingActionButton btnAddFalta;
    private FloatingActionButton btnAddHora;
    private Cronometro chrono;
    Date dataCal = new Date();

    public HistoricoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container,
                false);


        SimpleDateFormat sdf = new SimpleDateFormat("MMMM/yy");



        TextView txtMes = (TextView) rootView.findViewById(R.id.txtMes);
        txtMes.setText(sdf.format(dataCal).toUpperCase());
        txtMes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hapticFeedback();
                Intent intent = new Intent(getActivity(), GraficoActivity.class);
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM/yy");
                intent.putExtra("date", sdf.format(dataCal));
                startActivity(intent);

            }
        });


        ImageView btnChart = (ImageView) rootView.findViewById(R.id.btnChart);
        btnChart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hapticFeedback();
                Intent intent = new Intent(getActivity(), GraficoActivity.class);
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM/yy");
                intent.putExtra("date", sdf.format(dataCal));
                startActivity(intent);

            }
        });


        final Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");

        chrono = Cronometro.getInstance(getActivity());


        montarBtnPonto(rootView, robotoRegular);


        populaHorario(rootView, robotoLight, dataCal);

        ImageView btnPrevMes = (ImageView) rootView.findViewById(R.id.btnPrevMes);
        btnPrevMes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hapticFeedback();
                Calendar c = Calendar.getInstance();
                c.setTime(dataCal);
                c.add(Calendar.MONTH, -1);
                dataCal = c.getTime();

                populaHorario(rootView, robotoLight, dataCal);
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM/yy");
                TextView txtMes = (TextView) rootView.findViewById(R.id.txtMes);
                txtMes.setText(sdf.format(dataCal).toUpperCase());
            }
        });

        ImageView btnNextMes = (ImageView) rootView.findViewById(R.id.btnNextMes);
        btnNextMes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hapticFeedback();
                Calendar c = Calendar.getInstance();
                c.setTime(dataCal);
                c.add(Calendar.MONTH, 1);
                dataCal = c.getTime();

                populaHorario(rootView, robotoLight, dataCal);
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM/yy");
                TextView txtMes = (TextView) rootView.findViewById(R.id.txtMes);
                txtMes.setText(sdf.format(dataCal).toUpperCase());
            }
        });


        FloatingActionMenu faMenu = (FloatingActionMenu) rootView.findViewById(R.id.fabMacro);
        faMenu.setClosedOnTouchOutside(true);


        return rootView;
    }

    private void populaHorario(View rootView, Typeface robotoLight, Date dataCal) {
        LinearLayout linearLayout = (LinearLayout) rootView.findViewById(R.id.listaHorarios);
        TextView saldoTotal = (TextView) rootView.findViewById(R.id.saldoTotal);
        TextView saldoMesTotal = (TextView) rootView.findViewById(R.id.saldoMesTotal);

        HistoricoHandler historicoHandler = new HistoricoHandler(getActivity(), linearLayout, saldoMesTotal, dataCal, saldoTotal);
        TreeMap<String, List<Historico>> mapaHistorico = historicoHandler.popularHistorico(robotoLight);




        TimerHandler timerHandler = new TimerHandler(chrono, getActivity());
        timerHandler.verificaIniciaTimer(mapaHistorico);

        historicoHandler.calcularSaldoTotal(mapaHistorico);
    }





    private void montarBtnPonto(final View rootView, Typeface robotoRegular) {
        btnPonto = (FloatingActionButton) rootView.findViewById(R.id.fab);
        btnPonto.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hapticFeedback();

                String uid = Settings.Secure.getString(getActivity().getContentResolver(),
                        Settings.Secure.ANDROID_ID);

                BaterPontoTask btrPontoTask = new BaterPontoTask(getActivity(), rootView, dataCal, chrono, uid);
                btrPontoTask.execute("");
            }
        });

        btnAddFalta = (FloatingActionButton) rootView.findViewById(R.id.fabAddFalta);
        btnAddFalta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hapticFeedback();

                String mTitle = getString(R.string.action_adicionar_falta);
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(mTitle);
                removerOutrosFragments();
                FragmentTransaction frgTrns = getFragmentManager().beginTransaction()
                        .replace(R.id.container, new AdicionarFaltaFragment());
                frgTrns.addToBackStack(getResources().getString(R.string.action_adicionar_falta));
                frgTrns.commit();
            }
        });

        btnAddHora = (FloatingActionButton) rootView.findViewById(R.id.fabAddHorario);
        btnAddHora.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                hapticFeedback();

                String mTitle = getString(R.string.action_adicionar_horario);
                ((ActionBarActivity)getActivity()).getSupportActionBar().setTitle(mTitle);
                removerOutrosFragments();
                FragmentTransaction frgTrns = getFragmentManager().beginTransaction()
                        .replace(R.id.container, new AdicionarHoraFragment());
                frgTrns.addToBackStack(getResources().getString(R.string.action_adicionar_horario));
                frgTrns.commit();
            }
        });
    }

    private void hapticFeedback() {
        Vibrator vibrator;
        vibrator = (Vibrator)getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(15);
    }

    private void removerOutrosFragments() {
        FragmentManager fragmentManager = ((ActionBarActivity)getActivity()).getSupportFragmentManager();
        for(android.support.v4.app.Fragment fragment : fragmentManager.getFragments()) {
            if(fragment != null && fragment.getId() != R.id.navigation_drawer) {
                fragmentManager.beginTransaction().remove(fragment).commit();
            }
        }
    }
}
