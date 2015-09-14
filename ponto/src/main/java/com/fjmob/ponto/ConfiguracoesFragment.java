package com.fjmob.ponto;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fjmob.ponto.entity.Configuracoes;
import com.fjmob.ponto.persistence.ConfiguracoesDAO;
import com.gc.materialdesign.views.CheckBox;

public class ConfiguracoesFragment extends Fragment {

    public ConfiguracoesFragment() {
    }

    EditText tempoSaldoHoras;
    EditText tempoSaldoMinutos;
    CheckBox isNegativo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_configuracoes,
                container, false);

        Typeface robotoRegular = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        TextView jornadaLabel = (TextView) rootView.findViewById(R.id.tempoJornadaLabel);
        jornadaLabel.setTypeface(robotoRegular);

        TextView minutosLabel = (TextView) rootView.findViewById(R.id.saldoLabelMinutos);
        minutosLabel.setTypeface(robotoLight);

        TextView horasLabel = (TextView) rootView.findViewById(R.id.saldoLabelHoras);
        horasLabel.setTypeface(robotoLight);


        TextView negativoLabel = (TextView) rootView.findViewById(R.id.saldoLabelNegativo);
        negativoLabel.setTypeface(robotoLight);


        Button salvar = (Button) rootView.findViewById(R.id.btnSalvarTempoJornada);

        final TimePicker tempoJornada = (TimePicker) rootView.findViewById(R.id.tempoJornada);
        tempoJornada.setIs24HourView(true);

        tempoSaldoHoras = (EditText) rootView.findViewById(R.id.saldoHoras);
        tempoSaldoHoras.setTypeface(robotoRegular);

        tempoSaldoMinutos = (EditText) rootView.findViewById(R.id.saldoMinutos);
        tempoSaldoMinutos.setTypeface(robotoRegular);


        isNegativo  = (CheckBox) rootView.findViewById(R.id.checkBoxNegativo);

        Configuracoes config = ConfiguracoesDAO.getInstance(getActivity()).recuperarPorId(1);
        if(config != null) {
            tempoJornada.setCurrentHour(config.getJornadaHoras());
            tempoJornada.setCurrentMinute(config.getJornadaMinutos());

            if(config.getSaldoAcumulado() < 0) {
                tempoSaldoHoras.setText(Integer.toString(config.getSaldoAcumulado() * -1));
                isNegativo.post(new Runnable() {
                    @Override
                    public void run() {
                        isNegativo.setChecked(true);
                    }
                });
            } else {
                tempoSaldoHoras.setText(Integer.toString(config.getSaldoAcumulado()));
            }

            if(config.getSaldoAcumuladoMinutos() < 0) {
                tempoSaldoMinutos.setText(Integer.toString(config.getSaldoAcumuladoMinutos() * -1));

                isNegativo.post(new Runnable() {
                    @Override
                    public void run() {
                        isNegativo.setChecked(true);
                    }
                });
            } else {
                tempoSaldoMinutos.setText(Integer.toString(config.getSaldoAcumuladoMinutos()));
            }

        } else {
            tempoJornada.setCurrentHour(0);
            tempoJornada.setCurrentMinute(0);

            tempoSaldoHoras.setText(Integer.toString(0));
            tempoSaldoMinutos.setText(Integer.toString(0));
        }




        salvar.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                Configuracoes config = ConfiguracoesDAO.getInstance(getActivity()).recuperarPorId(1);
                if (config == null) {
                    config = new Configuracoes();
                    config.setJornadaHoras(1);
                    config.setJornadaMinutos(0);
                    config.setSaldoAcumulado(0);
                    config.setSaldoAcumuladoMinutos(0);
                    ConfiguracoesDAO.getInstance(getActivity()).salvar(config);
                    config = ConfiguracoesDAO.getInstance(getActivity()).recuperarPorId(1);
                }

                config.setJornadaHoras(tempoJornada.getCurrentHour());
                config.setJornadaMinutos(tempoJornada.getCurrentMinute());

                boolean negativo = isNegativo.isCheck();

                Integer horas = tempoSaldoHoras.getText().equals("") ? 0 : new Integer(String.valueOf(tempoSaldoHoras.getText()));
                Integer minutos = tempoSaldoMinutos.getText().equals("") ? 0 : new Integer(String.valueOf(tempoSaldoMinutos.getText()));

                config.setSaldoAcumulado(negativo ? (horas * -1) : (horas));
                config.setSaldoAcumuladoMinutos(negativo ? (minutos * -1) : (minutos));

                ConfiguracoesDAO.getInstance(getActivity()).editar(config);

                Toast.makeText(getActivity(), getResources().getString(R.string.configuracoes_salvas_sucesso), Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }
}