package com.fjmob.ponto;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.fjmob.ponto.entity.Falta;
import com.fjmob.ponto.persistence.FaltaDAO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AdicionarFaltaFragment extends Fragment {

    public AdicionarFaltaFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_adicionar_falta,
                container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        TextView txtEplc = (TextView) rootView.findViewById(R.id.txtEplc);
        txtEplc.setTypeface(robotoLight);


        final DatePicker data = (DatePicker) rootView.findViewById(R.id.dataFalta);


        Button btnSalvar = (Button) rootView.findViewById(R.id.btnSalvarFalta);
        btnSalvar.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {

                Integer dia = data.getDayOfMonth();
                Integer mes = data.getMonth() + 1;
                Integer ano = data.getYear();

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date dataAdicionar = sdf.parse(dia + "/" + mes + "/" + ano);
                    Falta falta = new Falta();
                    falta.setDataGravacao(dataAdicionar);

                    List<Falta> faltas = FaltaDAO.getInstance(getActivity()).recuperarTodos();
                    boolean faltaExistente = false;
                    for (Falta faltaVo : faltas) {
                        if(sdf.format(faltaVo.getDataGravacao()).equals(sdf.format(dataAdicionar))) {
                            faltaExistente = true;
                            break;
                        }
                    }

                    if(!faltaExistente) {
                        FaltaDAO.getInstance(getActivity()).salvar(falta);
                    }

                    Toast.makeText(getActivity(), getResources().getString(R.string.falta_salva_sucesso), Toast.LENGTH_LONG).show();;

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