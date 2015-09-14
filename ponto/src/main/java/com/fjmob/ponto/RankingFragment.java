package com.fjmob.ponto;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fjmob.ponto.formatter.MyValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RankingFragment extends Fragment {

    public RankingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_ranking,
                container, false);


        Typeface robotoLight = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Light.ttf");
        Typeface robotoMedium = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Roboto-Medium.ttf");

        TextView txtRnkSaldo = (TextView) rootView.findViewById(R.id.txt_rnk_saldo);
        txtRnkSaldo.setTypeface(robotoMedium);
        TextView txtRnkEscala = (TextView) rootView.findViewById(R.id.txt_rnk_escala);
        txtRnkEscala.setTypeface(robotoMedium);
        TextView txtRnkUsuarios = (TextView) rootView.findViewById(R.id.txt_rnk_usuarios);
        txtRnkUsuarios.setTypeface(robotoLight);
        TextView txtRnkVcSaldo = (TextView) rootView.findViewById(R.id.txt_rnk_vc_saldo);
        txtRnkVcSaldo.setTypeface(robotoLight);
        TextView txtRnkVcEsta = (TextView) rootView.findViewById(R.id.txt_rnk_vc_esta);
        txtRnkVcEsta.setTypeface(robotoLight);
        TextView txtEplc = (TextView) rootView.findViewById(R.id.txtEplc);
        txtEplc.setTypeface(robotoLight);


        final String uid = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SaldoUsuario");


        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> saldos, ParseException e) {
                if (e == null) {
                    TreeMap<Long, ParseObject> mapSaldos = new TreeMap<Long, ParseObject>(Collections.reverseOrder());


                    for (ParseObject saldo : saldos) {
                        mapSaldos.put(saldo.getLong("saldoUser"), saldo);
                    }

                    int i=0;

                    for (Map.Entry<Long, ParseObject> entry : mapSaldos.entrySet()) {
                        i++;
                        if(entry.getValue().getString("uidUser").equals(uid)) {
                            TextView txtEscala = (TextView) rootView.findViewById(R.id.txt_rnk_escala);
                            txtEscala.setText(i+"/"+saldos.size());

                            TextView txtSaldo = (TextView) rootView.findViewById(R.id.txt_rnk_saldo);
                            txtSaldo.setText((saldos.size()-i)+"");
                            break;
                        }

                    }

                    montarGraficoRanking(rootView, mapSaldos);
                } else {

                }
            }
        });








        return rootView;
    }

    private void montarGraficoRanking(final View rootView, TreeMap<Long, ParseObject> mapSaldos) {
        ArrayList<BarEntry> entries = new ArrayList<com.github.mikephil.charting.data.BarEntry>();
        ArrayList<String> labels = new ArrayList<String>();
        int xIndex = 0;

        String uid = Settings.Secure.getString(rootView.getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        for (java.util.Map.Entry<Long, ParseObject> entry : mapSaldos.entrySet()) {
            float hours   =  new Long(entry.getKey()).floatValue() / 3600000;
            com.github.mikephil.charting.data.BarEntry entryGrafico = new com.github.mikephil.charting.data.BarEntry(hours, xIndex);
            entries.add(entryGrafico);

            if(entry.getValue().getString("uidUser").equals(uid)) {
                labels.add(rootView.getContext().getResources().getString(R.string.voce));
            } else {
//					labels.add((xIndex+1)+"º");
                labels.add("");
            }

            xIndex++;
        }






        BarDataSet dataset = new BarDataSet(entries, rootView.getContext().getResources().getString(R.string.txt_saldo_usuarios));
//			dataset.setDrawFilled(true);
//			dataset.setDrawCircles(true);
        dataset.setValueTextSize(14f);
        dataset.setDrawValues(false);
        dataset.setColor(Color.rgb(255, 87, 34));
//			dataset.setFillColor(Color.rgb(255, 87, 34));
//			dataset.setCircleColor(Color.rgb(255, 87, 34));
        dataset.setValueTextColor(Color.rgb(255, 87, 34));

        BarChart chart = new BarChart(rootView.getContext());




        BarData data = new BarData(labels, dataset);
        data.setValueFormatter(new MyValueFormatter());
        data.setValueTextColor(Color.rgb(255, 87, 34));
        data.setValueTextSize(14f);

        chart.setData(data);

        chart.setDescriptionTextSize(14f);
        chart.setDescriptionColor(Color.rgb(255, 87, 34));
        chart.setDescription("");
        chart.animateY(2000);


        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.rgb(255, 87, 34));


        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.setValueFormatter(new MyValueFormatter());

        YAxis yAxisRight = chart.getAxisRight();
        yAxisRight.setValueFormatter(new MyValueFormatter());

        chart.invalidate();
        LinearLayout viewLayout = (LinearLayout) rootView.findViewById(R.id.graficoRanking);
        viewLayout.addView(chart);
        dataset.setValueTextSize(14f);
    }
}