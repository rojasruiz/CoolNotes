package com.alejandro.coolnotes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {

    private static String tittle = "";
    private static String description = "";
    private static int id = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifyLemubit")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(tittle)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(id, builder.build());
    }

    public static String getTittle() {
        return tittle;
    }

    public static void setTittle(String tittle) {
        ReminderBroadcast.tittle = tittle;
    }

    public static String getDescription() {
        return description;
    }

    public static void setDescription(String description) {
        ReminderBroadcast.description = description;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        ReminderBroadcast.id = id;
    }
}
