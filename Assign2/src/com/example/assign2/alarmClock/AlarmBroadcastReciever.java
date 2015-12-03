package com.example.assign2.alarmClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmBroadcastReciever extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		String msg = intent.getStringExtra("message");
		Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
		// Intent alarm=new Intent();
		// alarm.setClassName("com.example.assign2.alarmClock",
		// "com.example.assign2.alarmClock.Alarm.class");
		// alarm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		// context.startService(alarm);
		Intent alarmPage = new Intent(context, Alarm.class);
		alarmPage.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(alarmPage);
	}

}
