package com.example.minhaculinriaapp.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.minhaculinriaapp.R;

public class NotificationHelper {

    public static final String CHANNEL_TIMER = "timer_channel";
    public static final int NOTIF_TIMER_ONGOING = 1001;
    private static int doneNotifId = 2000;

    public static void createChannels(Context ctx) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel ch = new NotificationChannel(
                    CHANNEL_TIMER,
                    "Timers de Receitas",
                    NotificationManager.IMPORTANCE_HIGH);
            ch.setDescription("Contagem regressiva dos passos da receita");
            ctx.getSystemService(NotificationManager.class).createNotificationChannel(ch);
        }
    }

    public static Notification buildOngoingNotification(Context ctx, String title, String content) {
        return new NotificationCompat.Builder(ctx, CHANNEL_TIMER)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentTitle(title)
                .setContentText(content)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();
    }

    public static void notifyTimerDone(Context ctx, String label) {
        Notification n = new NotificationCompat.Builder(ctx, CHANNEL_TIMER)
                .setSmallIcon(R.drawable.ic_timer)
                .setContentTitle("Timer concluído!")
                .setContentText(label)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();
        try {
            NotificationManagerCompat.from(ctx).notify(doneNotifId++, n);
        } catch (SecurityException ignored) {}
    }
}
