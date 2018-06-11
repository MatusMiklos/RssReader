package com.example.pc.zadanie_cloudy;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

/**
 * Created by pc on 20.12.2017.
 */

public class Notification_reciever extends BroadcastReceiver{

    //tu sa vytvori notifikacia
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent activity = new Intent(context, LoginActivity.class);//kam nas to po kliknuti hodi
        activity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//vytvori novu aktivitu cize tu na pozadi zrusi
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, activity, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(android.R.drawable.arrow_up_float)//ikonka
                .setContentTitle("NOTIFIKACIA")//titulok
                .setContentText("NOTIFIKACIA TEXT")//textnotifikacie
                .setAutoCancel(true)//aby sa dala notifikacia zrusit palcom
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });

        notificationManager.notify(100, builder.build());
    }
}
