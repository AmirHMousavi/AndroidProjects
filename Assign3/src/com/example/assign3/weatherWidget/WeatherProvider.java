package com.example.assign3.weatherWidget;

import java.util.Arrays;
import java.util.Calendar;

import com.example.assign3.R;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.RemoteViews;

public class WeatherProvider extends AppWidgetProvider {
	private static final String LOG = "com.example.assign3.weatherWidget.WeatherWidget";
	
	public void updateWeather(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.w(LOG, "updateWeather(3) method called");
		String time = DateFormat.format("hh:mm:ss", Calendar.getInstance().getTime()).toString();
		
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.weather_widget);
			views.setTextViewText(R.id.item_date, time);
			
			//registering onClickListener
			Intent clickIntent = new Intent(context, WeatherProvider.class);
			clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, new int[]{appWidgetId});
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId, clickIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.item_date, pendingIntent);
			
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}
	
	public void updateWeather(Context context) {
		Log.w(LOG, "onUpdate(1) method called");
		
		ComponentName thisWidget = new ComponentName(context, WeatherProvider.class);
		AppWidgetManager appWidgetManager =	AppWidgetManager.getInstance(context);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		updateWeather(context, appWidgetManager, appWidgetIds);
	}
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		Log.w(LOG, "onUpdate method called: " + Arrays.toString(appWidgetIds));

		updateWeather(context, appWidgetManager, appWidgetIds);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		
		Log.w(LOG, "onReceive method called: " + intent.getAction());
		
		if (intent.getAction().equals(Intent.ACTION_AIRPLANE_MODE_CHANGED))
			updateWeather(context);
	}
	

}
