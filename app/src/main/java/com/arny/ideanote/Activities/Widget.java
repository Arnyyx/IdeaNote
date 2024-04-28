package com.arny.ideanote.Activities;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.arny.ideanote.R;

public class Widget extends AppWidgetProvider {
    String data = "not ok";
    int data2 = 0;

    public void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, Splash.class), PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget, configPendingIntent);

        views.setTextViewText(R.id.noteTitleWidget, data);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }


}