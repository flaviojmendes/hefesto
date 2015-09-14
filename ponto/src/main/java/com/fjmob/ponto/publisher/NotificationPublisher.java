package com.fjmob.ponto.publisher;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Vibrator;

public class NotificationPublisher extends BroadcastReceiver {
	 
    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";
 
    public void onReceive(Context context, Intent intent) {
 
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
 
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        
        
        Vibrator vibrator;
		vibrator = (Vibrator)context.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(1000);
        
        int id = intent.getIntExtra(NOTIFICATION_ID, 1);
        
        notificationManager.notify(id, notification);
 
    }
}