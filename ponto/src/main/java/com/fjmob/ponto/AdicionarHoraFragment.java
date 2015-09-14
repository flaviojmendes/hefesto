package com.fjmob.ponto;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.persistence.HistoricoDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AdicionarHoraFragment extends Fragment {

    public AdicionarHoraFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_adicionar_hora,
                container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        TextView txtEplc = (TextView) rootView.findViewById(R.id.txtEplc);
        txtEplc.setTypeface(robotoLight);

        final TimePicker hora = (TimePicker) rootView.findViewById(R.id.hora);
        hora.setIs24HourView(true);

        final DatePicker data = (DatePicker) rootView.findViewById(R.id.data);

        Button btnSalvar = (Button) rootView.findViewById(R.id.btnSalvarHora);
        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                Integer horaInt = hora.getCurrentHour();
                Integer minutoInt = hora.getCurrentMinute();

                Integer dia = data.getDayOfMonth();
                Integer mes = data.getMonth() + 1;
                Integer ano = data.getYear();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                try {
                    Date dataAdicionar = sdf.parse(dia + "/" + mes + "/" + ano + " " + horaInt + ":" + minutoInt);
                    Historico historico = new Historico();
                    historico.setDataGravacao(dataAdicionar);
                    HistoricoDAO.getInstance(getActivity()).salvar(historico);


                    Toast.makeText(getActivity(), getResources().getString(R.string.horario_salvo_sucesso), Toast.LENGTH_LONG).show();;

                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        });

        return rootView;
    }
}