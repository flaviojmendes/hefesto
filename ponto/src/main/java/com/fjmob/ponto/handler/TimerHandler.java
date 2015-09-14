package com.fjmob.ponto.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TreeMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.widget.Chronometer;
import android.widget.RemoteViews;

import com.fjmob.ponto.CronometroWidget;
import com.fjmob.ponto.MainActivity;
import com.fjmob.ponto.R;
import com.fjmob.ponto.entity.Historico;
import com.fjmob.ponto.publisher.NotificationPublisher;
import com.fjmob.ponto.util.Constantes;

public class TimerHandler {

	private Chronometer chrono;
	private Activity activity;
	
	public TimerHandler(Chronometer chrono, Activity activity) {
		this.chrono = chrono;
		this.activity = activity;
	} 

	@SuppressLint("SimpleDateFormat")
	public void verificaIniciaTimer(TreeMap<String, List<Historico>> mapaHistorico) {

		if(chrono != null) {
			chrono.stop();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		List<Historico> historicoHoje = mapaHistorico.get(sdf.format(new Date()));

		if(historicoHoje != null) {
			if(historicoHoje.size() % 2 > 0) {

				long tempoPresente = 0;
				for(int i = 0 ; i < historicoHoje.size() - 1 ; i++) {
					long tempoInicial = historicoHoje.get(i).getDataGravacao().getTime();
					long tempoFinal = historicoHoje.get(i+1).getDataGravacao().getTime();
					tempoPresente += tempoFinal - tempoInicial;
					i++;
				}

				long tempoInicial = historicoHoje.get(historicoHoje.size()-1).getDataGravacao().getTime();
				long tempoFinal = new Date().getTime();
				tempoPresente += tempoFinal - tempoInicial;

				JornadaHandler jornadaHandler = new JornadaHandler(activity);

				long chronoBase = SystemClock.elapsedRealtime() - tempoPresente;



				long notificationTime = SystemClock.elapsedRealtime() + (jornadaHandler.recuperarTempoJornada() - tempoPresente);

				chrono.setBase(chronoBase);
				scheduleNotification(getNotification("teste"), notificationTime);
				chrono.start();



				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(chrono.getContext());
				RemoteViews remoteViews = new RemoteViews(chrono.getContext().getPackageName(), R.layout.cronometro_widget);


				remoteViews.setChronometer(R.id.chronoWidget, chrono.getBase(), null, true);

				ComponentName thisWidget = new ComponentName(chrono.getContext(), CronometroWidget.class);
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);


			} else {

				long tempoPresente = 0;
				for(int i = 0 ; i < historicoHoje.size() ; i++) {
					long tempoInicial = historicoHoje.get(i).getDataGravacao().getTime();
					long tempoFinal = historicoHoje.get(i+1).getDataGravacao().getTime();
					tempoPresente += tempoFinal - tempoInicial;
					i++;
				}

				chrono.setBase(SystemClock.elapsedRealtime() - tempoPresente);


				AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(chrono.getContext());
				RemoteViews remoteViews = new RemoteViews(chrono.getContext().getPackageName(), R.layout.cronometro_widget);


				remoteViews.setChronometer(R.id.chronoWidget, chrono.getBase(), null, false);

				ComponentName thisWidget = new ComponentName(chrono.getContext(), CronometroWidget.class);
				appWidgetManager.updateAppWidget(thisWidget, remoteViews);

				unschedule(getNotification("teste"));
			}
		}
	}



	private void scheduleNotification(Notification notification, long notificationTime) {
		 
        Intent notificationIntent = new Intent(activity.getApplicationContext(), NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 04200420, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        
        
        
        AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
		try {
			alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, notificationTime, pendingIntent);
		} catch (Exception e) {
			alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, notificationTime, pendingIntent);
		}
    }

	private void unschedule(Notification notification) {

		Intent notificationIntent = new Intent(activity.getApplicationContext(), NotificationPublisher.class);
		notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
		notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, notification);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 04200420, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT );



		AlarmManager alarmManager = (AlarmManager)activity.getSystemService(Context.ALARM_SERVICE);
		try {
			alarmManager.cancel(pendingIntent);
		} catch (Exception e) {

		}
	}
 
    private Notification getNotification(String content) {
    	
    	PendingIntent contentIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, new Intent(activity, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

    	Notification.Builder builder = new Notification.Builder(activity)
        .setContentTitle(activity.getString(R.string.title_notification_jornada))
        .setContentText(activity.getString(R.string.text_notification_jornada))
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentIntent(contentIntent)
        .setAutoCancel(true)
        .setLights(Color.MAGENTA, 200, 200);
        
        
        return builder.build();
    }

}
