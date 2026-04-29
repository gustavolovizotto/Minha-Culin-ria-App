package com.example.minhaculinriaapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.RemoteViews;

import com.example.minhaculinriaapp.R;

import java.util.Locale;

public class CofreWidget extends AppWidgetProvider {

    public static final String PREFS_WIDGET = "widget_prefs";
    public static final String KEY_TIMER_LABEL = "timer_label";
    public static final String KEY_TIMER_REMAINING = "timer_remaining";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updateWidgets(context, appWidgetManager, appWidgetIds);
    }

    public static void updateWidgets(Context ctx, AppWidgetManager mgr, int[] ids) {
        SharedPreferences prefs = ctx.getSharedPreferences(PREFS_WIDGET, Context.MODE_PRIVATE);
        String label = prefs.getString(KEY_TIMER_LABEL, null);
        int remaining = prefs.getInt(KEY_TIMER_REMAINING, 0);

        for (int id : ids) {
            RemoteViews views = new RemoteViews(ctx.getPackageName(), R.layout.widget_cofre);
            if (label != null) {
                views.setTextViewText(R.id.widget_timer_label, label);
                views.setTextViewText(R.id.widget_timer_remaining,
                        String.format(Locale.getDefault(), "%02d:%02d",
                                remaining / 60, remaining % 60));
                views.setViewVisibility(R.id.widget_active_layout, View.VISIBLE);
                views.setViewVisibility(R.id.widget_idle_text, View.GONE);
            } else {
                views.setViewVisibility(R.id.widget_active_layout, View.GONE);
                views.setViewVisibility(R.id.widget_idle_text, View.VISIBLE);
            }
            mgr.updateAppWidget(id, views);
        }
    }
}
