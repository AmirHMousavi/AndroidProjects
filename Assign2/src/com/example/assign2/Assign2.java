package com.example.assign2;

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

public class Assign2 extends ListActivity {

	private List<String> activities = new ArrayList<String>();
	private Map<String,Class> name2class = new HashMap<String,Class>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		/* Add Activities to list */
		 setup_activities();
	        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_assign2, activities));
	        
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
    		Intent intent = new Intent(Assign2.this,activity_class);
    		Assign2.this.startActivity(intent);
    	}   	
    }

	private void setup_activities() {
		addActivity("My Countries", com.example.assign2.myCountries.MyCountries.class);
		addActivity("My AlarmClock", com.example.assign2.alarmClock.AlarmClock.class);
		addActivity("My MP3 Player", com.example.assign2.mp3player.Mp3_Player_main.class);
		
		
	}

	private void addActivity(String name,Class activity) {
		activities.add(name);
    	name2class.put(name, activity);
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.assign2, menu);
		return true;
	}

}
