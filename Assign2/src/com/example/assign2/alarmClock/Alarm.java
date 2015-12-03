package com.example.assign2.alarmClock;

import java.io.IOException;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.assign2.R;

public class Alarm extends Activity {
	private MediaPlayer alarmMediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		setContentView(R.layout.alarm_reciever_activity);
		playAlarmSound(this, getAlarmUri());

		Button stopAlarm = (Button) findViewById(R.id.btnStopAlarm);
		Button snoozeAlarm = (Button) findViewById(R.id.btnSnoozAlarm);
		stopAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				alarmMediaPlayer.stop();
				finish();
			}
		});

		snoozeAlarm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						AlarmBroadcastReciever.class);
				intent.putExtra("message", "the alarm starts");
				PendingIntent alarmIntent = PendingIntent.getBroadcast(
						getBaseContext(), 0, intent, 0);
				AlarmManager alm = (AlarmManager) getSystemService(ALARM_SERVICE);
				alm.set(AlarmManager.RTC_WAKEUP,
						30000 + System.currentTimeMillis(), alarmIntent);
				finish();
			}
		});

	}

	private void playAlarmSound(Context context, Uri alarmUri) {
		alarmMediaPlayer = new MediaPlayer();
		try {
			alarmMediaPlayer.setDataSource(context, alarmUri);
			alarmMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
			alarmMediaPlayer.prepare();
			alarmMediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private Uri getAlarmUri() {
		Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		if (alert == null) {
			alert = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
			if (alert == null) {
				alert = RingtoneManager
						.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			}
		}
		return alert;
	}

	@Override
	protected void onStop() {
		alarmMediaPlayer.stop();
		super.onStop();
	}

}
