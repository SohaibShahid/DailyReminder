package com.app.dailyreminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Sohaib shahid on 2/21/2017.
 */


public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification mNotifyBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.ic_stat_logo)
                .setContentTitle("Daily Reminder")
                .setContentText(MainActivity.TEXT_VALUE)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000})
                .setStyle(new NotificationCompat.BigTextStyle().bigText(MainActivity.TEXT_VALUE))
                .build();

        notificationManager.notify(0,mNotifyBuilder);
    }
}
