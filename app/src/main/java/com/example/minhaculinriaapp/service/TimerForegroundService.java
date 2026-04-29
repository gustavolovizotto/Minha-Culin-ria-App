package com.example.minhaculinriaapp.service;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.Nullable;

import com.example.minhaculinriaapp.notification.NotificationHelper;
import com.example.minhaculinriaapp.widget.CofreWidget;

import java.util.Locale;

public class TimerForegroundService extends Service {

    public static final String ACTION_START = "com.example.minhaculinriaapp.START_TIMER";
    public static final String ACTION_STOP = "com.example.minhaculinriaapp.STOP_TIMER";
    public static final String EXTRA_MINUTOS = "minutos";
    public static final String EXTRA_RECEITA_NOME = "receita_nome";
    public static final String EXTRA_PASSO_NUMERO = "passo_numero";

    private static final int ALARM_REQUEST_CODE = 42;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private int remainingSeconds = 0;
    private String receitaNome = "";
    private int passoNumero = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationHelper.createChannels(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) return START_NOT_STICKY;

        String action = intent.getAction();
        if (ACTION_START.equals(action)) {
            int minutos = intent.getIntExtra(EXTRA_MINUTOS, 0);
            receitaNome = intent.getStringExtra(EXTRA_RECEITA_NOME) != null
                    ? intent.getStringExtra(EXTRA_RECEITA_NOME) : "";
            passoNumero = intent.getIntExtra(EXTRA_PASSO_NUMERO, 1);

            if (minutos <= 0) {
                stopSelf();
                return START_NOT_STICKY;
            }

            remainingSeconds = minutos * 60;

            startForeground(NotificationHelper.NOTIF_TIMER_ONGOING,
                    NotificationHelper.buildOngoingNotification(
                            this,
                            "Passo " + passoNumero + " — " + receitaNome,
                            formatTime(remainingSeconds)));

            scheduleAlarm((long) minutos * 60);
            saveWidgetPrefs();
            startTick();

        } else if (ACTION_STOP.equals(action)) {
            stopTimer();
        }

        return START_NOT_STICKY;
    }

    private void startTick() {
        handler.removeCallbacksAndMessages(null);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (remainingSeconds <= 0) {
                    timerDone();
                    return;
                }
                remainingSeconds--;
                updateNotification();
                saveWidgetPrefs();
                handler.postDelayed(this, 1000);
            }
        }, 1000);
    }

    private void updateNotification() {
        ((NotificationManager) getSystemService(NOTIFICATION_SERVICE))
                .notify(NotificationHelper.NOTIF_TIMER_ONGOING,
                        NotificationHelper.buildOngoingNotification(
                                this,
                                "Passo " + passoNumero + " — " + receitaNome,
                                formatTime(remainingSeconds)));
    }

    private void timerDone() {
        cancelAlarm();
        handler.removeCallbacksAndMessages(null);
        NotificationHelper.notifyTimerDone(this, "Passo " + passoNumero + " — " + receitaNome);
        clearWidgetPrefs();
        stopSelf();
    }

    private void stopTimer() {
        cancelAlarm();
        handler.removeCallbacksAndMessages(null);
        clearWidgetPrefs();
        stopSelf();
    }

    private void scheduleAlarm(long segundos) {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAt = System.currentTimeMillis() + (segundos * 1000);

        getSharedPreferences(CofreWidget.PREFS_WIDGET, MODE_PRIVATE).edit()
                .putString(CofreWidget.KEY_TIMER_LABEL, "Passo " + passoNumero + " — " + receitaNome)
                .apply();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (am.canScheduleExactAlarms()) {
                am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt,
                        getPendingAlarmIntent());
            } else {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt,
                        getPendingAlarmIntent());
            }
        } else {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerAt,
                    getPendingAlarmIntent());
        }
    }

    private void cancelAlarm() {
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(getPendingAlarmIntent());
    }

    private PendingIntent getPendingAlarmIntent() {
        Intent intent = new Intent(this, TimerReceiver.class);
        intent.setAction(TimerReceiver.ACTION_TIMER_DONE);
        return PendingIntent.getBroadcast(this, ALARM_REQUEST_CODE, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    private void saveWidgetPrefs() {
        getSharedPreferences(CofreWidget.PREFS_WIDGET, MODE_PRIVATE).edit()
                .putString(CofreWidget.KEY_TIMER_LABEL, "Passo " + passoNumero + " — " + receitaNome)
                .putInt(CofreWidget.KEY_TIMER_REMAINING, remainingSeconds)
                .apply();
        refreshWidget();
    }

    private void clearWidgetPrefs() {
        getSharedPreferences(CofreWidget.PREFS_WIDGET, MODE_PRIVATE).edit()
                .remove(CofreWidget.KEY_TIMER_LABEL)
                .remove(CofreWidget.KEY_TIMER_REMAINING)
                .apply();
        refreshWidget();
    }

    private void refreshWidget() {
        AppWidgetManager mgr = AppWidgetManager.getInstance(this);
        int[] ids = mgr.getAppWidgetIds(new ComponentName(this, CofreWidget.class));
        CofreWidget.updateWidgets(this, mgr, ids);
    }

    private String formatTime(int seconds) {
        return String.format(Locale.getDefault(), "%02d:%02d", seconds / 60, seconds % 60);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
