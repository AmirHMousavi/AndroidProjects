package com.example.assign2.myCountries;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.assign2.R;

public class MyCountries extends Activity {
	private CountryDataSource datasource;
	private List<CountryList> values;
	private ArrayAdapter<CountryList> listAdapter;
	private boolean sortedByCountry = false;
	private boolean sortedByYear = false;
	private String bgColor;
	private String fsize;

	// ***************overridden methods*********************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_countries);

		// ******Preferences*************
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(this);
		sortedByCountry = setting.getBoolean("countrySort", false);
		sortedByYear = setting.getBoolean("yearSort", false);
		bgColor = setting.getString("bgColor", "#FF0000");
		fsize = setting.getString("fontSize", "24");

		// ******************************

		datasource = new CountryDataSource(this);
		datasource.open();

		if (sortedByCountry == true)
			sortByCountry();
		else if (sortedByYear == true)
			sortByYear();
		else {
			values = datasource.getAllCountries();

			ListView list = (ListView) findViewById(R.id.countryList);
			listAdapter = new myListAdabter();
			list.setAdapter(listAdapter);
			registerForContextMenu(list);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_countries, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_ADD)
			addCountry();
		if (item.getItemId() == R.id.menuSortCountry)
			sortByCountry();
		if (item.getItemId() == R.id.menuSortYear)
			sortByYear();
		if (item.getItemId() == R.id.menuSettings)
			toSettingPage();

		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.countryList) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			menu.setHeaderTitle(values.get(info.position).getCountry()
					.toString());
			menu.add(0, 0, 0, "Delete");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getItemId() == 0) { // delete Country
			CountryList country = values.get(info.position);
			datasource.deleteCountry(country);
			listAdapter.remove(country);
		}
		return true;
	}

	// *********************myListAdabter***********************************************
	private class myListAdabter extends ArrayAdapter<CountryList> {

		public myListAdabter() {
			super(MyCountries.this, R.layout.country_itemview, values);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// make sure we have a view to work with (may have been given null)
			View itemView = convertView;
			if (itemView == null) {
				itemView = getLayoutInflater().inflate(
						R.layout.country_itemview, parent, false);
			}
			CountryList country = values.get(position);
			TextView _country = (TextView) itemView
					.findViewById(R.id.country_itemview);
			_country.setBackgroundColor(Color.parseColor(bgColor));
			_country.setTextSize(Integer.parseInt(fsize));
			Log.d("country.getCountry ==> ", country.getCountry());

			_country.setText(country.getCountry());
			TextView _year = (TextView) itemView
					.findViewById(R.id.year_itemview);
			_year.setBackgroundColor(Color.parseColor(bgColor));
			_year.setTextSize(Integer.parseInt(fsize));
			Log.d("country.getYear ==> ", country.getYear().toString());
			_year.setText(country.getYear().toString());
			return itemView;
		}
	}

	// ****************************************************************
	@Override
	protected void onStop() {
		super.onStop();
		SharedPreferences setting = PreferenceManager
				.getDefaultSharedPreferences(this);
		SharedPreferences.Editor editor = setting.edit();
		editor.putBoolean("countrySort", sortedByCountry);
		editor.putBoolean("yearSort", sortedByYear);
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	//
	@Override
	protected void onPause() {
		super.onPause();
	}

	// ****************Intents to next activities*************************
	private void toSettingPage() {
		Intent intent1 = new Intent(this, Preferences.class);
		startActivity(intent1);
	}

	public void addCountry() {
		Intent intent0 = new Intent(this, InsertCountryActivity.class);
		startActivityForResult(intent0, 0);
	}

	// *************************************************************************

	// *****************Sorting methods*****************************************
	private void sortByYear() {
		sortedByYear = true;
		sortedByCountry = false;
		values = datasource.sotByYear();

		ListView list = (ListView) findViewById(R.id.countryList);
		listAdapter = new myListAdabter();
		list.setAdapter(listAdapter);
		registerForContextMenu(list);
	}

	private void sortByCountry() {
		sortedByCountry = true;
		sortedByYear = false;
		values = datasource.sortByCountry();

		ListView list = (ListView) findViewById(R.id.countryList);
		listAdapter = new myListAdabter();
		list.setAdapter(listAdapter);
		registerForContextMenu(list);
	}

	// ************************************************************************

	protected void onActivityResult(int requestedCode, int resultCode,
			Intent result) {
		if (requestedCode == 0) {
			if (resultCode == RESULT_OK) {
				String nnCountry = result.getStringExtra("result1");
				String nnYear = result.getStringExtra("result2");
				Integer nmYear = Integer.parseInt(nnYear);
				CountryList newCountry = datasource.creatCountry(nnCountry,
						nmYear);
				listAdapter.add(newCountry);
				listAdapter.notifyDataSetChanged();
			}
		}
	}

}
