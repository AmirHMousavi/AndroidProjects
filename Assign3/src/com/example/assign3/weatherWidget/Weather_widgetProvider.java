package com.example.assign3.weatherWidget;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.assign3.R;

public class Weather_widgetProvider extends AppWidgetProvider {
	private static final String TAG = "Weather_widgetProvider";
	Context currentContext;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		Log.d(TAG, " onUpdate method calls ");
		currentContext = context;

		System.out.println("updating(1) widgets: "
				+ Arrays.toString(appWidgetIds));
		updateWeatherWidget(context, appWidgetManager, appWidgetIds);

	}

	public void updateWeatherWidget(Context context,
			AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		System.out.println("updating (2) widgets: "
				+ Arrays.toString(appWidgetIds));

		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			String[] cityANDLink = Weather_widgetConfig.loadPrefs(context,
					appWidgetId);
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.weather_widget);

			// ---Refresh Click Intent
			Intent refreshIntent = new Intent(context,
					Weather_widgetProvider.class);
			refreshIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS,
					appWidgetId);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					appWidgetId, refreshIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.weather_refresh, pendingIntent);
			// -----------------------
			// ------got o main activity intent-------
			Intent mainActivity = new Intent(context,
					Weather_widgetProvider.class);
			mainActivity.setAction("START_NEW_TASK");
			mainActivity.putExtra("theCityName", cityANDLink[0]);
			mainActivity.putExtra("theCityLink", cityANDLink[1]);
			PendingIntent activityPendinIntent = PendingIntent.getBroadcast(
					context, appWidgetId, mainActivity,
					PendingIntent.FLAG_UPDATE_CURRENT);
			views.setOnClickPendingIntent(R.id.item_rain, activityPendinIntent);
			views.setOnClickPendingIntent(R.id.icon_image, activityPendinIntent);
			views.setOnClickPendingIntent(R.id.item_date, activityPendinIntent);
			views.setOnClickPendingIntent(R.id.item_period,
					activityPendinIntent);
			views.setOnClickPendingIntent(R.id.iem_temp, activityPendinIntent);
			views.setOnClickPendingIntent(R.id.item_windSpeed,
					activityPendinIntent);
			views.setOnClickPendingIntent(R.id.item_windDirection,
					activityPendinIntent);
			views.setOnClickPendingIntent(R.id.weather_cityName,
					activityPendinIntent);

			// --------------------------------------
			try {
				URL url = new URL(cityANDLink[1]);
				new WeatherRetriever(appWidgetId, appWidgetManager,
						cityANDLink, views).execute(url);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}
	}

	// *******Retriever *****************
	private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport> {
		int currentWidgetID = 0;
		String[] currentCityandLink;
		RemoteViews currentView;
		AppWidgetManager currentAppWidgetManager;

		public WeatherRetriever(int appWidgetId,
				AppWidgetManager appWidgetManager, String[] cityANDLink,
				RemoteViews views) {
			currentWidgetID = appWidgetId;
			currentCityandLink = cityANDLink;
			currentView = views;
			currentAppWidgetManager = appWidgetManager;
		}

		protected WeatherReport doInBackground(URL... urls) {
			try {
				return WeatherHandler.getWeatherReport(urls[0]);
			} catch (Exception e) {
				Toast.makeText(null, "internet connection error! ",
						Toast.LENGTH_SHORT).show();
				throw new RuntimeException(e);
			}
		}

		protected void onProgressUpdate(Void... progress) {

		}

		protected void onPostExecute(WeatherReport result) {
			populateWidgetView(result, currentWidgetID,
					currentAppWidgetManager, currentCityandLink, currentView);
		}

	}

	// ***populate view********
	private void populateWidgetView(WeatherReport result, int currentWidgetID,
			AppWidgetManager currentAppWidgetManager, String[] cityandLink2,
			RemoteViews currentView) {
		Context context = currentContext;
		if (result == null) {
			System.out.println("null weather report! ");
			Toast.makeText(context, "weather report is null",
					Toast.LENGTH_SHORT).show();
		} else {
			WeatherForecast currentForcat = null;
			Iterator<WeatherForecast> it = result.iterator();
			if (it.hasNext())
				currentForcat = (WeatherForecast) it.next();
			else
				System.out.println("error in iteration over report");

			// *** start filling the view
			currentView.setTextViewText(R.id.item_period, cityandLink2[0]);
			currentView.setTextViewText(R.id.item_date,
					currentForcat.getStartYYMMDD());
			currentView.setTextViewText(R.id.iem_temp, currentForcat.getTemp()
					+ "°");
			currentView.setTextViewText(
					R.id.weather_cityName,
					currentForcat.getStartHHMM() + " to "
							+ currentForcat.getEndHHMM());
			currentView.setTextViewText(R.id.item_windSpeed,
					currentForcat.getWindSpeed() + " Km/h");
			currentView.setTextViewText(R.id.item_windDirection,
					currentForcat.getWindDirection() + "");
			currentView.setTextViewText(R.id.item_rain, currentForcat.getRain()
					+ " cm");
			int weatherCode = currentForcat.getWeatherCode();
			currentView.setImageViewResource(R.id.icon_image,
					getTheImageSource(weatherCode));

			currentAppWidgetManager.updateAppWidget(currentWidgetID,
					currentView);
		}
	}

	private int getTheImageSource(int weatherCode) {
		if (weatherCode == 1)
			return R.drawable.wi1;
		else if (weatherCode == 2)
			return R.drawable.wi2;
		else if (weatherCode == 3)
			return R.drawable.wi3;
		else if (weatherCode == 4)
			return R.drawable.wi4;
		else if (weatherCode == 5)
			return R.drawable.wi5;
		else if (weatherCode == 6)
			return R.drawable.wi6;
		else if (weatherCode == 7)
			return R.drawable.wi7;
		else if (weatherCode == 8)
			return R.drawable.wi8;
		else if (weatherCode == 9)
			return R.drawable.wi9;
		else if (weatherCode == 10)
			return R.drawable.wi10;
		else if (weatherCode == 11)
			return R.drawable.wi11;
		else if (weatherCode == 12)
			return R.drawable.wi12;
		else if (weatherCode == 13)
			return R.drawable.wi13;
		else if (weatherCode == 14)
			return R.drawable.wi14;
		else if (weatherCode == 15)
			return R.drawable.wi15;
		return 0;

	}

	// *****other overridden methods***********
	@Override
	public void onAppWidgetOptionsChanged(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId,
			Bundle newOptions) {
		super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId,
				newOptions);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		Log.w(TAG, "onReceive method called: " + intent.getAction());

		if (intent.getAction().equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
				|| intent.getAction().equals(PendingIntent.FLAG_UPDATE_CURRENT)) {
			System.out.println("refresh is clicked");
			updateTheWidget(context);
		}

		if (intent.getAction().equals("START_NEW_TASK")) {
			System.out.println("rain is clicked");
			Bundle bundle = intent.getExtras();
			Intent newTaskIntent = new Intent(context, Weather.class);
			newTaskIntent.putExtra("theCityLink",
					bundle.getString("theCityLink"));
			newTaskIntent.putExtra("theCityName",
					bundle.getString("theCityName"));
			startTheActivity(context, newTaskIntent);
		}
	}

	private void startTheActivity(Context context, Intent newTaskIntent) {
		newTaskIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(newTaskIntent);

	}

	private void updateTheWidget(Context context) {
		System.out.println("updating (3) called");
		ComponentName thisWidget = new ComponentName(context,
				Weather_widgetProvider.class);
		AppWidgetManager appWidgetManager = AppWidgetManager
				.getInstance(context);
		int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		updateWeatherWidget(context, appWidgetManager, appWidgetIds);

	}

	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}

	// private void updateWeather(Context context) {
	//
	// ComponentName thisWidget = new ComponentName(context,
	// Weather_widgetProvider.class);
	// AppWidgetManager appWidgetManager = AppWidgetManager
	// .getInstance(context);
	// int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
	// onUpdate(context, appWidgetManager, appWidgetIds);
	// }
}
