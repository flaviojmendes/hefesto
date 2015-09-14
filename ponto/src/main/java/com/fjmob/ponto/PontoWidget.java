package com.fjmob.ponto;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.fjmob.ponto.entity.Configuracoes;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.persistence.ConfiguracoesDAO;

public class PontoWidget extends AppWidgetProvider {

	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onEnabled(context);

        Intent intent = new Intent(context, NFCActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);


		RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.ponto_widget);

		rv.setOnClickPendingIntent(R.id.titlePontoWidget, pendingIntent);
		

		ComponentName cn = new ComponentName(context, PontoWidget.class);
		AppWidgetManager.getInstance(context).updateAppWidget(cn, rv);
	}


	

}
