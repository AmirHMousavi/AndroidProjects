package com.example.assign3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class Assign3 extends ListActivity {

	private List<String> activities = new ArrayList<String>();
	private Map<String,Class> name2class = new HashMap<String,Class>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		/* Add Activities to list */
		 setup_activities();
	        setListAdapter(new ArrayAdapter<String>(this, R.layout.main_assign3, activities));
	        
	        /* Attach list item listener */
	        ListView lv = getListView();
	        lv.setOnItemClickListener(new OnItemClick()); 
	}
	
	  /* Private Help Entities */  
    private class OnItemClick implements OnItemClickListener {
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		/* Find selected activity */
    		String activity_name = activities.get(position);
    		Class activity_class = name2class.get(activity_name);

    		/* Start new Activity */
    		Intent intent = new Intent(Assign3.this,activity_class);
    		Assign3.this.startActivity(intent);
    	}   	
    }

	private void setup_activities() {
//	addActivity("Weather Widget MainClass", com.example.assign3.weatherWidget.Weather.class);
	addActivity("call log", com.example.assign3.callLog.CallLog_Main.class);
		
		
		
	}

	private void addActivity(String name,Class activity) {
		activities.add(name);
    	name2class.put(name, activity);
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assign3, menu);
		return true;
	}

}
