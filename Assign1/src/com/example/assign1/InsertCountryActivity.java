package com.example.assign1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class InsertCountryActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_country);
		// Show the Up button in the action bar.
		setupActionBar();
//		Context context=getApplicationContext();
//		CharSequence warning="please insret data";
//		int duaration=Toast.LENGTH_SHORT;
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
		getMenuInflater().inflate(R.menu.insert_country, menu);
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

	public void addCountryYear(View view){
		
		EditText country=(EditText) findViewById(R.id.countryEdit);
		EditText year=(EditText)findViewById(R.id.yearEdit);
		
		if(country.getText().length()==0||year.getText().length()==0)
			Toast.makeText(getApplicationContext(), "please insert data", Toast.LENGTH_SHORT).show();
		else{
		String yearCountry=country.getText().toString()+" --> "+year.getText().toString();
		
		Intent result=new Intent();
		result.putExtra("result", yearCountry);
		setResult(RESULT_OK, result);
		finish();}
	}
}
