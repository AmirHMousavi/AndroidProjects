package com.example.assign2.alarmClock;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.assign2.R;

public class AlarmClock extends Activity {
	private TimePicker timePicker;
	private int pickedHour;
	private int pickedMinute;
	private Calendar calendar;
	private static final int hoursInMilis = 3600000;
	private static final int minutesInMilis = 60000;
	private long diffTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm_clock);
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(android.text.format.DateFormat
				.is24HourFormat(this));
		Button setButton = (Button) findViewById(R.id.setAlarm);
		setButton.setOnClickListener(SETbtnOnclickListener);
		Button cancelButton = (Button) findViewById(R.id.cacelAlarm);
		cancelButton.setOnClickListener(CANCELbtnOnclickListener);
		Button setButton2=(Button) findViewById(R.id.setAlarm2);
		setButton2.setOnClickListener(SET2btnListener);
		Button cancelButton2=(Button) findViewById(R.id.cancelAlarm2);
		cancelButton2.setOnClickListener(CANCEL2btnListener);

	}

	private OnClickListener SETbtnOnclickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(),
					AlarmBroadcastReciever.class);
			intent.putExtra("message", "alarm 1 starts");
			PendingIntent alarmIntent = PendingIntent.getBroadcast(
					getBaseContext(), 0, intent, 0);
			long startTime = calculateStartTime() + System.currentTimeMillis();
			AlarmManager alm = (AlarmManager) getSystemService(ALARM_SERVICE);
			alm.set(AlarmManager.RTC_WAKEUP, startTime, alarmIntent);
			Toast.makeText(
					getApplicationContext(),
					"alarm 1 is set in "
							+ String.format("%02d hours and %02d minutes", diffTime
									/ hoursInMilis, (diffTime % hoursInMilis)
									/ minutesInMilis) + " = " + diffTime+" milliseconds",
					Toast.LENGTH_LONG).show();
		}
	};

	private OnClickListener CANCELbtnOnclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			String msg = "alarm 1 canceled";
			Toast.makeText(AlarmClock.this, msg, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getBaseContext(),
					AlarmBroadcastReciever.class);
			PendingIntent alarmIntent = PendingIntent.getBroadcast(
					getBaseContext(), 0, intent, 0);
			AlarmManager alm = (AlarmManager) getSystemService(ALARM_SERVICE);
			alm.cancel(alarmIntent);

		}
	};
	
	private OnClickListener SET2btnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getBaseContext(),
					AlarmBroadcastReciever.class);
			intent.putExtra("message", "alarm 2 starts");
			PendingIntent alarmIntent = PendingIntent.getBroadcast(
					getBaseContext(), 1, intent, 1);
			long startTime = calculateStartTime() + System.currentTimeMillis();
			AlarmManager alm = (AlarmManager) getSystemService(ALARM_SERVICE);
			alm.set(AlarmManager.RTC_WAKEUP, startTime, alarmIntent);
			Toast.makeText(
					getApplicationContext(),
					"alarm 2 is set in "
							+ String.format("%02d hours and %02d minutes", diffTime
									/ hoursInMilis, (diffTime % hoursInMilis)
									/ minutesInMilis) + " = " + diffTime+" milliseconds",
					Toast.LENGTH_LONG).show();
		}
	};
	
	private OnClickListener CANCEL2btnListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String msg = "alarm 2 canceled";
			Toast.makeText(AlarmClock.this, msg, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(getBaseContext(),
					AlarmBroadcastReciever.class);
			PendingIntent alarmIntent = PendingIntent.getBroadcast(
					getBaseContext(), 1, intent, 1);
			AlarmManager alm = (AlarmManager) getSystemService(ALARM_SERVICE);
			alm.cancel(alarmIntent);
			
		}
	};
	

	// ****time difference calculator
	// this method calculates the difference between set time and current
	// system time
	private long calculateStartTime() {
		pickedHour = timePicker.getCurrentHour();
		pickedMinute = timePicker.getCurrentMinute();
		long currentTime = System.currentTimeMillis();
		diffTime = 0;
		calendar = Calendar.getInstance();
		calendar.setTimeInMillis(currentTime);
		calendar.set(Calendar.HOUR_OF_DAY, pickedHour);
		calendar.set(Calendar.MINUTE, pickedMinute);
		diffTime = calendar.getTimeInMillis() - currentTime;
		if (diffTime < 0)
			diffTime += 24 * hoursInMilis;	
		return diffTime;
	}

}
