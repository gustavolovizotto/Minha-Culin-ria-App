package com.example.minhaculinriaapp.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.example.minhaculinriaapp.notification.NotificationHelper;
import com.example.minhaculinriaapp.widget.CofreWidget;

public class TimerReceiver extends BroadcastReceiver {

    public static final String ACTION_TIMER_DONE = "com.example.minhaculinriaapp.TIMER_DONE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!ACTION_TIMER_DONE.equals(intent.getAction())) return;

        SharedPreferences prefs = context.getSharedPreferences(
                CofreWidget.PREFS_WIDGET, Context.MODE_PRIVATE);
        String label = prefs.getString(CofreWidget.KEY_TIMER_LABEL, "Passo concluído");

        NotificationHelper.createChannels(context);
        NotificationHelper.notifyTimerDone(context, label);

        prefs.edit()
                .remove(CofreWidget.KEY_TIMER_LABEL)
                .remove(CofreWidget.KEY_TIMER_REMAINING)
                .apply();

        android.appwidget.AppWidgetManager mgr =
                android.appwidget.AppWidgetManager.getInstance(context);
        int[] ids = mgr.getAppWidgetIds(
                new android.content.ComponentName(context, CofreWidget.class));
        CofreWidget.updateWidgets(context, mgr, ids);
    }
}
