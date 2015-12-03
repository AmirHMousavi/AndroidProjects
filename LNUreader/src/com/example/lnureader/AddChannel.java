/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddChannel extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_channel);
		setTitle("add channel by url");
	}

	public void saveAndReturn(View v) {

		EditText url = (EditText) findViewById(R.id.addlink_field);
		if (url.getText().length() == 0)
			Toast.makeText(getApplicationContext(), "please insert url",
					Toast.LENGTH_SHORT).show();
		else {
			String theURL = url.getText().toString();
			Intent result = new Intent();
			result.putExtra("result", theURL);
			setResult(RESULT_OK, result);
			finish();
		}
	}

	public void returnWithoutSaving(View v) {
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_channel, menu);
		return true;
	}

}
