package com.example.assign1;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class Random extends Activity {
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_random);
			
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.random, menu);
		return true;
	}
	
	public void randGen(View v){
		java.util.Random rand=new java.util.Random();
		TextView tv=(TextView) findViewById(R.id.textView);
		int n=rand.nextInt(100)+1;
		tv.setText(n+"");
	}

}
