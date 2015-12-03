package com.example.assign1;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MyCountries extends Activity {
	static ArrayList<String> country_year = new ArrayList<String>();
	ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_countries);
		populateList();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.my_countries, menu);
		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.action_ADD)
			addCountry();

		return super.onOptionsItemSelected(item);

	}

	public void addCountry() {
		Intent intent = new Intent(this, InsertCountryActivity.class);
		startActivityForResult(intent, 0);

	}

	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		// Save state to the savedInstanceState
	}

	protected void onActivityResult(int requestedCode, int resultCode,
			Intent result) {
		if (resultCode == RESULT_OK) {
			country_year.add(result.getStringExtra("result"));
			populateList();
		}
	}

	private void populateList() {
		lv = (ListView) findViewById(R.id.countryList);
		ArrayAdapter<String> arrAdabpt = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, country_year);
		lv.setAdapter(arrAdabpt);

	}

}
