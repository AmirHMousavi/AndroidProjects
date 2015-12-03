package com.example.assign1;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ColorDisplay extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_color_display);
	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.color_display, menu);
		return true;
	}

	public void changeColor(View v) {
		TextView tv = (TextView) findViewById(R.id.backGround);
		EditText red = (EditText) findViewById(R.id.redEdit);
		EditText green = (EditText) findViewById(R.id.greenEdit);
		EditText blue = (EditText) findViewById(R.id.blueEdit);

		if (red.getText().length() == 0 || green.getText().length() == 0
				|| blue.getText().length() == 0)
			Toast.makeText(getApplicationContext(), "please insert data",
					Toast.LENGTH_SHORT).show();

		else {
			int redVal = Integer.parseInt(red.getText().toString());
			int greenVal = Integer.parseInt(green.getText().toString());
			int blueVal = Integer.parseInt(blue.getText().toString());
			if (redVal > 255 || greenVal > 255 || blueVal > 255 || redVal < 0
					|| greenVal < 0 || blueVal < 0)
				Toast.makeText(getApplicationContext(), "numbers must be between 0-255",
						Toast.LENGTH_SHORT).show();
			else
				tv.setBackgroundColor(Color.rgb(redVal, greenVal, blueVal));
		}
	}
}
