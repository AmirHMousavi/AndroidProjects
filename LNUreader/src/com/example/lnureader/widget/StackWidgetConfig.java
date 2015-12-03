/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.lnureader.R;
import com.example.lnureader.rssstuff.ChannelInfo;
import com.example.lnureader.rssstuff.ChannelReader;

public class StackWidgetConfig extends Activity {
	private static final String PREFS_NAME = "com.example.lnureader";
	int WidgetId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_stack_widget_config);
		setResult(RESULT_CANCELED);
		WidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			WidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		// if no widget then ignore!
		if (WidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
			finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.stack_widget_config, menu);
		return true;
	}

	public void saveLink(View v) {
		final Context context = v.getContext();
		EditText et = (EditText) findViewById(R.id.Config_addlink_field);
		String channelLink = et.getText().toString();
		if (et.getText().length() == 0)
			Toast.makeText(getApplicationContext(), "please insert url",
					Toast.LENGTH_SHORT).show();
		else {
			GetChannelDataTask channelInfo = new GetChannelDataTask(context,WidgetId);
			channelInfo.execute(channelLink);
	//		savePrefs(context, WidgetId, channelLink);
	//		letsGoForTheWidget();
		}

	}

	public void cancel(View v) {
		finish();
	}

	private void letsGoForTheWidget() {
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
		Intent intent1 = new Intent(this, StackWidgetProvider.class);
		intent1.setAction("android.appwidget.action.APPWIDGET_UPDATE");
		int ids[] = appWidgetManager.getAppWidgetIds(new ComponentName(
				getApplication(), StackWidgetProvider.class));
		intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
		sendBroadcast(intent1);

		RemoteViews views = new RemoteViews(this.getPackageName(),
				R.layout.widget_layout);

		Intent resultValue = new Intent();
		resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, WidgetId);
		setResult(RESULT_OK, resultValue);

		appWidgetManager.updateAppWidget(WidgetId, views);

		finish();
	}

	private static void savePrefs(Context context, int appWidgetId,
			String channelLink) {
		SharedPreferences.Editor prefs = context.getSharedPreferences(
				PREFS_NAME, 0).edit();
		prefs.putString("channelLink" + appWidgetId, channelLink);
		prefs.commit();

	}

	public static String loadPrefs(Context context, int appWidgetId) {
		SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
		String theLink = prefs.getString("channelLink" + appWidgetId, null);
		return theLink;

	}

	private class GetChannelDataTask extends
			AsyncTask<String, Void, ChannelInfo> {
		/**
		 * here we check if the URL inserted by the user is valid and in second
		 * if the URL is an RSS url so if the result is null it means URL does
		 * not give us an XML with RSS feeds style and format
		 */
		private String channelLink;
		private Context context;
		private int widgetId;

		public GetChannelDataTask(Context context, int widgetId) {
			this.context=context;
			this.widgetId=widgetId;
		}

		@Override
		protected ChannelInfo doInBackground(String... urls) {
			Log.d("channel check", Thread.currentThread().getName());
			try {
				ChannelReader channelReader = new ChannelReader(urls[0]);
				this.channelLink = urls[0];
				if (isCancelled())
					Toast.makeText(getApplicationContext(),
							"problem in fetching data", Toast.LENGTH_SHORT)
							.show();
				return channelReader.getInfo();
			} catch (Exception e) {
				Log.e("channel check", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(ChannelInfo result) {
			if (result == null) {
				Toast.makeText(getApplicationContext(), "the URL is not valid",
						Toast.LENGTH_SHORT).show();
			} else {
				// if the URL was giving us a correct xml which matches channel
				// info we create the widget
				savePrefs(context, widgetId, channelLink);
				letsGoForTheWidget();

			}
		}
	}

}
