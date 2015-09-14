package com.fjmob.ponto;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.formatter.MyValueFormatter;
import com.fjmob.ponto.persistence.HistoricoDAO;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

public class GraficoFragment extends Fragment {

    @SuppressLint("SimpleDateFormat")
    private DateFormat sdfData = new SimpleDateFormat("yyyy/MM/dd");

    public GraficoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_grafico,
                container, false);

        Intent intent = getActivity().getIntent();
        String dataExt = intent.getStringExtra("date");

        SimpleDateFormat sdfMesExtAno = new SimpleDateFormat("MMMM/yy");
        SimpleDateFormat sdfMesAno = new SimpleDateFormat("yyyy/MM");


        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        Typeface robotoMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Medium.ttf");
        TextView txtEplc = (TextView) rootView.findViewById(R.id.txtEplcGraf);
        txtEplc.setTypeface(robotoLight);
        TextView txtMes = (TextView) rootView.findViewById(R.id.txtMesGraf);
        txtMes.setTypeface(robotoMedium);
        txtMes.setText(dataExt);

        String dataGrafico;
        try {
            dataGrafico = sdfMesAno.format(sdfMesExtAno.parse(dataExt));
        } catch (ParseException e1) {
            dataGrafico = sdfMesAno.format(new Date());
        }

        SimpleDateFormat sdfDia = new SimpleDateFormat("dd");
        List<Historico> historicos = HistoricoDAO.getInstance(getActivity()).recuperarTodos();

        TreeMap<String, List<Historico>> mapaHistorico = montarMapaHistorico(historicos);

        ArrayList<Entry> entries = new ArrayList<Entry>();
        ArrayList<String> labels = new ArrayList<String>();
        int xIndex = 0;
        for (java.util.Map.Entry<String, List<Historico>> entry : mapaHistorico.entrySet()) {

            if(entry.getKey().startsWith(dataGrafico)) {
                // Se nao for igual a data atual
                if(!sdfData.format(new Date()).equals(entry.getKey())) {

                    List<Historico> listaHistorico = entry.getValue();

                    if(listaHistorico.size() % 2 == 0) {

                        long tempoPresente = 0;
                        for(int i = 0 ; i < listaHistorico.size() ; i++) {
                            long tempoInicial = listaHistorico.get(i).getDataGravacao().getTime();
                            long tempoFinal = listaHistorico.get(i+1).getDataGravacao().getTime();
                            tempoPresente = tempoPresente + (tempoFinal - tempoInicial);
                            i++;
                        }
                        float hours   =  new Long(tempoPresente).floatValue() / 3600000;
                        entries.add(new Entry(hours, xIndex));
                        try {
                            labels.add(sdfDia.format(sdfData.parse(entry.getKey())));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        xIndex++;

                    }



                }
            }
        }






        LineDataSet dataset = new LineDataSet(entries, getResources().getString(R.string.txt_horas_trabalhadas));
        dataset.setDrawFilled(true);
        dataset.setDrawCircles(true);
        dataset.setDrawValues(false);
        dataset.setDrawCubic(true);
        dataset.setCubicIntensity(0.05f);
        dataset.setValueTextSize(14f);
        dataset.setColor(Color.rgb(255, 87, 34));
        dataset.setFillColor(Color.rgb(255, 87, 34));
        dataset.setCircleColor(Color.rgb(255, 87, 34));
        dataset.setValueTextColor(Color.rgb(255, 87, 34));

        LineChart chart = new LineChart(getActivity());




        LineData data = new LineData(labels, dataset);
        data.setValueFormatter(new MyValueFormatter());

        chart.setData(data);

        chart.setDescriptionTextSize(14f);
        chart.setDescriptionColor(Color.rgb(255, 87, 34));
        chart.setDescription("");

        chart.animateY(2000);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setTextSize(10f);

        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setValueFormatter(new MyValueFormatter());

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setValueFormatter(new MyValueFormatter());

        chart.invalidate();
        RelativeLayout viewLayout = (RelativeLayout) rootView.findViewById(R.id.viewGrafico);
        viewLayout.addView(chart);
        dataset.setValueTextSize(14f);
        return rootView;
    }

    public TreeMap<String, List<Historico>> montarMapaHistorico(List<Historico> historicos) {
        TreeMap<String, List<Historico>> mapaHistorico = new TreeMap<String, List<Historico>>();

        Collections.sort(historicos, new Comparator<Historico>() {

            @Override
            public int compare(Historico his1, Historico his2) {
                return his1.getDataGravacao().compareTo(his2.getDataGravacao());
            }
        });

        for (Historico historico : historicos) {
            String dataKey = sdfData.format(historico.getDataGravacao());
            if(mapaHistorico.get(dataKey) == null) {
                mapaHistorico.put(dataKey, new ArrayList<Historico>());
            }
            mapaHistorico.get(dataKey).add(historico);
        }
        return mapaHistorico;
    }

}