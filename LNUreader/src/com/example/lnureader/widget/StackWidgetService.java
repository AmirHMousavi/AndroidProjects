/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.lnureader.R;
import com.example.lnureader.rssstuff.RssItem;
import com.example.lnureader.rssstuff.RssReader;

public class StackWidgetService extends RemoteViewsService {
	@Override
	public RemoteViewsFactory onGetViewFactory(Intent intent) {
		Log.d("service", "onGetViewFactory() is called");
		return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
	}
}

class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
	private  List<RssItem> feedList;
	private Context mContext;
	private int mAppWidgetId;
	private String mWidgetLink;

	public StackRemoteViewsFactory(Context context, Intent intent) {
		Log.d("service", "stackRemoteViewFactory() is called");
		mContext = context;
		mWidgetLink = intent.getStringExtra("theLink");
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
	}

	public void onCreate() {
		Log.d("service", "onCreate() is called");

		feedList = new ArrayList<RssItem>();
		try {
			GetRSSDataTask task = new GetRSSDataTask();
			task.execute(mWidgetLink);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onDestroy() {
		feedList.clear();
	}

	public int getCount() {
		while (feedList.size() <= 0) {

		}
		return feedList.size();
	}

	public RemoteViews getViewAt(int position) {
		Log.d("service", "getViewAt() is called");

		// position will always range from 0 to getCount() - 1.

		// We construct a remote views item based on our widget item xml file,
		// and set the
		// text based on the position.
		RemoteViews rv = new RemoteViews(mContext.getPackageName(),
				R.layout.widget_item);
		rv.setTextViewText(R.id.W_titleView, feedList.get(position).getTitle());
		rv.setTextViewText(R.id.W_descView, feedList.get(position)
				.getDescription());
		rv.setTextViewText(R.id.W_dateView, feedList.get(position).getDate());
		Drawable d = feedList.get(position).getThumbnailImage();
		if (d == null) {
			rv.setImageViewResource(R.id.W_imageView, R.drawable.rss);
		} else {
			rv.setImageViewBitmap(R.id.W_imageView,
					((BitmapDrawable) d).getBitmap());
		}

		Intent fillInIntent = new Intent();
		fillInIntent.setData(Uri.parse(feedList.get(position).getLink()));
		rv.setOnClickFillInIntent(R.id.widget_item, fillInIntent);
		


		return rv;
	}

	public RemoteViews getLoadingView() {
		return null;
	}

	public int getViewTypeCount() {
		return 1;
	}

	public long getItemId(int position) {
		return position;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void onDataSetChanged() {

	}

	private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem>> {

		@Override
		protected List<RssItem> doInBackground(String... urls) {

			// Debug the task thread name
			Log.d("service ", Thread.currentThread().getName());

			try {
				RssReader rssReader = new RssReader(urls[0]);
				return rssReader.getItems();

			} catch (Exception e) {
				Log.e("service", e.getMessage());
				Log.d("widget service", "error in loading data");
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<RssItem> result) {
			feedList = result;

		}

	}
}
