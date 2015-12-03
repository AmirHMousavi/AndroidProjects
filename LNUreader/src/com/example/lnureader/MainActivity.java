/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader;

import java.util.Locale;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.lnureader.database.ChannelDataSource;
import com.example.lnureader.rssstuff.ChannelInfo;
import com.example.lnureader.rssstuff.ChannelReader;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {

	private ChannelDataSource channelDataSource;

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_add) {
			Intent intent = new Intent(this, AddChannel.class);
			startActivityForResult(intent, 0);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestedCode, int resultCode,
			Intent result) {
		if (requestedCode == 0) {
			if (resultCode == RESULT_OK) {
				String theURL = result.getStringExtra("result");
				if (isConnectedToInternet()) {
					new GetChannelDataTask().execute(theURL);
				} else {
					Toast.makeText(this, "internet connection problem",
							Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(
							Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
					ComponentName cName = new ComponentName(
							"com.android.phone",
							"com.android.phone.NetworkSetting");
					intent.setComponent(cName);
				}

			}
		}
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			// Fragment fragment = new DummySectionFragment();
			// Bundle args = new Bundle();
			// args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position +
			// 1);
			// fragment.setArguments(args);
			Fragment fragment = null;
			if (position == 0) {
				fragment = new ChannelListFragment();
			}
			if (position == 1) {
				fragment = new FavoriteListFragment();
			}

			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	// public static class DummySectionFragment extends Fragment {
	// /**
	// * The fragment argument representing the section number for this
	// * fragment.
	// */
	// public static final String ARG_SECTION_NUMBER = "section_number";
	//
	// public DummySectionFragment() {
	// }
	//
	// @Override
	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState) {
	// View rootView = inflater.inflate(R.layout.fragment_main_dummy,
	// container, false);
	// TextView dummyTextView = (TextView) rootView
	// .findViewById(R.id.section_label);
	// dummyTextView.setText(Integer.toString(getArguments().getInt(
	// ARG_SECTION_NUMBER)));
	// return rootView;
	// }
	// }

	private class GetChannelDataTask extends
			AsyncTask<String, Void, ChannelInfo> {
		/**
		 * here we check if the URL inserted by the user is valid and in second
		 * if the URL is an RSS url so if the result is null it means URL does
		 * not give us an XML with RSS feeds style and format
		 */
		String urlSource;

		@Override
		protected ChannelInfo doInBackground(String... urls) {
			Log.d("channel check", Thread.currentThread().getName());
			try {
				ChannelReader channelReader = new ChannelReader(urls[0]);
				urlSource = urls[0];
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
				// info
				// we create a new channel in database (only saving the link NOT
				// all channel info)
				result.setChannelURL(urlSource);
				channelDataSource = new ChannelDataSource(
						getApplicationContext());
				channelDataSource.open();
				Log.d("mainactivity logoLink", result.getLogoLink());
				channelDataSource.creatNewChannel(urlSource,
						result.getChannelName(), result.getDescription(),
						result.getLogoLink(), result.getLastModification());
				channelDataSource.close();
				if (mViewPager.getCurrentItem() != 0)
					mViewPager.setCurrentItem(0);
				Intent intent = new Intent("REFRESH_CHANNELS_LIST");
				intent.putExtra("msg", "refresh the channels list");
				LocalBroadcastManager.getInstance(getApplicationContext())
						.sendBroadcast(intent);
			}
		}
	}

	public boolean isConnectedToInternet() {
		ConnectivityManager connectivity = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
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

}
