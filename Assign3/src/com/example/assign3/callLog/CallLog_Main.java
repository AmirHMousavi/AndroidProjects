package com.example.assign3.callLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assign3.R;

public class CallLog_Main extends ListActivity {
	ArrayList<CallInfo> callsList = new ArrayList<CallInfo>();

	@SuppressLint("SimpleDateFormat")
	DateFormat DateFormatter = new SimpleDateFormat("yyyy-MM-dd");
	@SuppressLint("SimpleDateFormat")
	DateFormat TimeFormatter = new SimpleDateFormat("HH:mm");

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_log_main);
		String[] strFields = { CallLog.Calls.NUMBER, CallLog.Calls.DATE,
				CallLog.Calls.TYPE, CallLog.Calls.CACHED_NAME,
				CallLog.Calls.CACHED_NUMBER_TYPE, CallLog.Calls.DURATION };
		Uri contacts = CallLog.Calls.CONTENT_URI;
		Cursor mCallCursor = getContentResolver().query(contacts, strFields,
				null, null, CallLog.Calls.DATE + " DESC");
		callsList = callsList(mCallCursor);
		System.out.println(callsList(mCallCursor).toString());
		setListAdapter(new myListAdabter(this, callsList));
		registerForContextMenu(getListView());

	}

	private ArrayList<CallInfo> callsList(Cursor callCursor) {
		ArrayList<CallInfo> calls = new ArrayList<CallInfo>();
		if (callCursor.getCount() > 0) {
			callCursor.moveToFirst();
			do {
				CallInfo callInfo = new CallInfo(callCursor.getString(0),
						callCursor.getLong(1), callCursor.getString(2),
						callCursor.getString(3), callCursor.getString(4),
						callCursor.getString(5));
				calls.add(callInfo);
			} while (callCursor.moveToNext());
		}
		return calls;

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.call_log__main, menu);
		return true;
	}

	// ******Adapter*****************************************************
	private class myListAdabter extends ArrayAdapter<CallInfo> {

		public myListAdabter(Context context, ArrayList<CallInfo> callsList) {
			super(context, 0, callsList);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View itemView = convertView;

			if (itemView == null) {
				itemView = getLayoutInflater().inflate(R.layout.log_itemview,
						parent, false);
			}
			CallInfo call = callsList.get(position);
			TextView _name = (TextView) itemView.findViewById(R.id.Name);
			if (call.getChachedName() == null)
				_name.setText("unKnown");
			else
				_name.setText(call.getChachedName());
			ImageView _type = (ImageView) itemView.findViewById(R.id.calltype);
			int calltype = Integer.parseInt(call.getType());
			if (calltype == 1)
				_type.setImageResource(R.drawable.incomingcall);
			else if (calltype == 2)
				_type.setImageResource(R.drawable.outgoingcall);
			else if (calltype == 3)
				_type.setImageResource(R.drawable.missedcall);
			TextView _number = (TextView) itemView.findViewById(R.id.number);
			_number.setText(call.getNumber());
			TextView _date = (TextView) itemView.findViewById(R.id.date);
			_date.setText(DateFormatter.format(new Date(call.getDate())));
			TextView _time = (TextView) itemView.findViewById(R.id.calltime);
			_time.setText(TimeFormatter.format(new Date(call.getDate())));
			itemView.setTag(call.getNumber());
			return itemView;
		}

	}

	// ************CONTEXT MENU AND LISTENER**********************


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		@SuppressWarnings("unused")
		long id = getListAdapter().getItemId(info.position);
		menu.add(0, 0, 0, "Call");
		menu.add(0, 1, 0, "Message");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// return super.onContextItemSelected(item);
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();
		if (item.getItemId() == 0) {
			CallInfo call = callsList.get(info.position);
			String number = "tel:"+call.getNumber();
			System.out.println("number is: " + number);
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse(number)); //creating call intent (ready)
			//check if telephony is enabled on device
			 TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
			 boolean telEnabled = tm != null && tm.getSimState()==TelephonyManager.SIM_STATE_READY;
			 System.out.println("is tel enabled? "+telEnabled);
			//check the apps respond to the intent
			PackageManager packageManager = getPackageManager();
			List<ResolveInfo> activities = packageManager
					.queryIntentActivities(callIntent, 0);
			boolean isIntentSafe = activities.size() > 0;
			System.out.println("isIntentSafe: " + isIntentSafe);
			
			if (isIntentSafe) {
				String title = "Complete action using...";
				Intent chooser = Intent.createChooser(callIntent, title);
				startActivity(chooser);
			}else Toast.makeText(getApplicationContext(), "No application is responding to action",
					Toast.LENGTH_SHORT).show();
		}
		if (item.getItemId() == 1) {
			// message to the number
			CallInfo call = callsList.get(info.position);
			Intent msgIntent = new Intent(Intent.ACTION_SENDTO);
			msgIntent.setData(Uri.parse("smsto:"));
			msgIntent.putExtra("sms_body", call.getNumber());
			//check the apps respond to the intent
			PackageManager packageManager = getPackageManager();
			List<ResolveInfo> activities = packageManager
					.queryIntentActivities(msgIntent, 0);
			boolean isIntentSafe = activities.size() > 0;
			System.out.println("isIntentSafe: " + isIntentSafe);
			if(isIntentSafe){
				String title = "Complete action using...";
				Intent chooser = Intent.createChooser(msgIntent, title);
				startActivity(chooser);
			}else Toast.makeText(getApplicationContext(), "No application is responding to action",
					Toast.LENGTH_SHORT).show();
			
		}
		return false;
	}
}
