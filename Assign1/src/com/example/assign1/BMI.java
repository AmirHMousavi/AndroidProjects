package com.example.assign1;

import java.text.DecimalFormat;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BMI extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bmi);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bmi, menu);
		return true;
	}

	public void BMIcalculator(View v) {
		EditText weight = (EditText) findViewById(R.id.editText1);
		EditText length = (EditText) findViewById(R.id.editText2);
		TextView bmiResult = (TextView) findViewById(R.id.textView3);
		if (weight.getText().length() == 0 || length.getText().length() == 0)
			Toast.makeText(getApplicationContext(), "please insert data",
					Toast.LENGTH_SHORT).show();
		else {

			float weightInput = Float.parseFloat(weight.getText().toString());
			float lengthInput = Float.parseFloat(length.getText().toString());
			float bmiValue = (float) (weightInput / ((lengthInput / 100) * (lengthInput / 100)));

			bmiResult.setText(new DecimalFormat("##.##").format(bmiValue) + " "
					+ interpurter(bmiValue));
		}

	}

	private String interpurter(float bmiValue) {
		if (bmiValue < 18)
			return "under weight";
		else if (bmiValue < 25)
			return "normal weight";
		else if (bmiValue < 32)
			return "overweight";
		else
			return "obese go to Doctor ;)";
	}

	public void resetFields(View v) {
		EditText weight = (EditText) findViewById(R.id.editText1);
		weight.setText("");
		EditText length = (EditText) findViewById(R.id.editText2);
		length.setText("");
		TextView bmiResult = (TextView) findViewById(R.id.textView3);
		bmiResult.setText("~~~~");
	}

}
