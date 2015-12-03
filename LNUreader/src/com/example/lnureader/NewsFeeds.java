/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lnureader.database.FavoriteFeedsDataSource;
import com.example.lnureader.rssstuff.RssItem;
import com.example.lnureader.rssstuff.RssReader;

public class NewsFeeds extends ListActivity {
	private final int ACTIVITY_GROUP_ID = 333;
	private List<RssItem> feedList;
	private ArrayAdapter<RssItem> myFeedsAdapter;
	private FavoriteFeedsDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_feeds);
		// Show the Up button in the action bar.
		setupActionBar();
		//
		registerForContextMenu(getListView());
		// set adapter part
		feedList = new ArrayList<RssItem>();

		// read the data which caller put in the intent
		Bundle bundle = getIntent().getExtras();
		String theURL = bundle.getString("channelLink");
		String theName = bundle.getString("channelName");
		setTitle(theName);
		// get an instance of the favorite feeds database in case if user add
		// some news in to favorite list
		datasource = new FavoriteFeedsDataSource(NewsFeeds.this);
		datasource.open();
		// if Internet connection is available get the link and read news feeds
		if (isConnectedToInternet()) {
			GetRSSDataTask task = new GetRSSDataTask(NewsFeeds.this);
			task.execute(theURL);
		} else {
			Intent intent = new Intent(
					Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
			ComponentName cName = new ComponentName("com.android.phone",
					"com.android.phone.NetworkSetting");
			intent.setComponent(cName);
		}

	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.news_feeds, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(ACTIVITY_GROUP_ID, 0, 0, "add to favorite");
		menu.add(ACTIVITY_GROUP_ID, 1, 0, "share this feed");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getGroupId() == ACTIVITY_GROUP_ID) {
			if (item.getItemId() == 0) {
				RssItem ff = feedList.get(info.position);
				System.out.println("date: " + ff.getDate());
				byte[] img = new ImageUtils().drawableToByteArray(ff
						.getThumbnailImage());
				System.out.println("image: " + img);
				datasource.createNewFavoriteItem(ff.getTitle(), ff.getLink(),
						ff.getDescription(), ff.getDate(), img);
			}
			if (item.getItemId() == 1) {
				Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				RssItem shareItem=feedList.get(info.position);
				shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareItem.getTitle());
				shareIntent.putExtra(android.content.Intent.EXTRA_TITLE, shareItem.getDescription());
				shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareItem.getLink());
				PackageManager packageManager = getPackageManager();
				List<ResolveInfo> activities = packageManager.queryIntentActivities(shareIntent, 0);
				boolean isIntentSafe = activities.size() > 0;
				if (isIntentSafe) {
					String title = "Share via ...";
					Intent chooser = Intent.createChooser(shareIntent, title);
					startActivity(chooser);
				}else Toast.makeText(getApplicationContext(), "No application is responding to action",
						Toast.LENGTH_SHORT).show();
			}
		}
		return super.onContextItemSelected(item);
	}

	public boolean isConnectedToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null)
				for (int i = 0; i < info.length; i++)
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}

		}
		return false;
	}

	private class GetRSSDataTask extends AsyncTask<String, Void, List<RssItem>> {
		ProgressDialog spinner;

		public GetRSSDataTask(NewsFeeds newsFeeds) {
			spinner = ProgressDialog.show(newsFeeds, "", "please wait...");
		}

		@Override
		protected List<RssItem> doInBackground(String... urls) {

			// Debug the task thread name
			Log.d("FeedsItems ", Thread.currentThread().getName());

			try {
				RssReader rssReader = new RssReader(urls[0]);
				if (isCancelled())
					Toast.makeText(getApplicationContext(),
							"problem in fetching data", Toast.LENGTH_SHORT)
							.show();
				return rssReader.getItems();

			} catch (Exception e) {
				Log.e("FeedsItems", e.getMessage());
			}

			return null;
		}

		@Override
		protected void onPostExecute(List<RssItem> result) {
			feedList = result;
			myFeedsAdapter = new MyFeedsAdapter(NewsFeeds.this, feedList);
			setListAdapter(myFeedsAdapter);
			spinner.dismiss();

		}

	}

	private class MyFeedsAdapter extends ArrayAdapter<RssItem> {

		public MyFeedsAdapter(Context newsFeeds, List<RssItem> feedList) {
			super(newsFeeds, R.layout.itemview_news, feedList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = getLayoutInflater().inflate(
						R.layout.itemview_news, parent, false);
			}
			RssItem currentItem = feedList.get(position);
			String _title = currentItem.getTitle();
			String _description = currentItem.getDescription();
			String _link = currentItem.getLink();
			String _date = currentItem.getDate();
			String _thumbnail = currentItem.getThumbnailLink();
			Drawable image = currentItem.getThumbnailImage();

			TextView title = (TextView) convertView
					.findViewById(R.id.title_view);
			title.setText(_title);
			TextView description = (TextView) convertView
					.findViewById(R.id.description_view);
			description.setText(_description);
			TextView date = (TextView) convertView.findViewById(R.id.date_view);
			date.setText(_date);
			ImageView thumbnail = (ImageView) convertView
					.findViewById(R.id.imageView1);
			thumbnail.setImageDrawable(image);
			convertView.setTag(_link);

			return convertView;
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		String url = v.getTag().toString();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}

}
