package com.example.assign2.mp3player;

import java.util.ArrayList;

import android.app.ListActivity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assign2.R;



public class Mp3_Player_main extends ListActivity implements OnClickListener {

	public static ImageView btnPlay, btnForward, btnBackward, btnNext,
			btnPrevious, listSongBtn;
	public static ImageButton btnShuffle, btnRepeat;
	public static SeekBar songProgressBar;

	// Songs list
	static ArrayList<Song> songs;
	public static TextView songTitle, songCurrentDurationLabel,
			songTotalDurationLabel;

	public Intent playerService;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		initViews();

		songs = songList();
		setListAdapter(new PlayListAdapter(this, songs));
		playerService = new Intent(this, PlayerService.class);
		playerService.putExtra("songIndex", PlayerService.currentSongIndex);
		 startService(playerService);

	}

	private class PlayListAdapter extends ArrayAdapter<Song> {
		public PlayListAdapter(Context context, ArrayList<Song> objects) {
			super(context, 0, objects);
		}

		@Override
		public View getView(int position, View row, ViewGroup parent) {
			Song data = getItem(position);

			row = getLayoutInflater().inflate(R.layout.mp3player_row_item,
					parent, false);

			TextView name = (TextView) row.findViewById(R.id.mp3_item1);
			name.setText(String.valueOf(data.getName()));
			row.setTag(data);

			return row;
		}
	}

	/**
	 * Check state of media storage. True if mounted;
	 * 
	 * @return
	 */
	private boolean isStoreageAvailable() {
		return Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
	}

	/**
	 * Reads song list from media storage.
	 * 
	 * @return
	 */
	private ArrayList<Song> songList() {
		ArrayList<Song> songs = new ArrayList<Song>();

		if (!isStoreageAvailable()) // Check for media storage
		{
			Toast.makeText(this, R.string.nosd, Toast.LENGTH_SHORT).show();
			return songs;
		}

		Cursor music = getContentResolver().query(
				// using content resolver to read music from media storage
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Audio.Media.ARTIST,
						MediaStore.Audio.Media.ALBUM,
						MediaStore.Audio.Media.DISPLAY_NAME,
						MediaStore.Audio.Media.DATA },
				MediaStore.Audio.Media.IS_MUSIC + " > 0 ", null, null);

		if (music.getCount() > 0) {
			music.moveToFirst();
			Song prev = null;
			do {
				Song song = new Song(music.getString(0), music.getString(1),
						music.getString(2), music.getString(3));

				if (prev != null) // here prev song linked to current one. To
									// simple play them in list
					prev.setNext(song);

				prev = song;
				songs.add(song);
			} while (music.moveToNext());
		}
		music.close();

		System.out.println(songs.size());

		for (int i = 0; i < songs.size(); i++) {
			if (i == 0) {
				songs.get(i).setPrevious(songs.get(songs.size() - 1));
			} else
				songs.get(i).setPrevious(songs.get(i - 1));
		}

		return songs;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (!PlayerService.mp.isPlaying()) {
			stopService(playerService);
			cancelNotification();
		}
		
	}

	// -- Cancel Notification
	public void cancelNotification() {
		String notificationServiceStr = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(notificationServiceStr);
		mNotificationManager.cancel(PlayerService.NOTIFICATION_ID);
	}

	/**
	 * Initiaze Views
	 */
	private void initViews() {

		btnPlay = (ImageView) findViewById(R.id.btnPlay);
		btnForward = (ImageView) findViewById(R.id.btnForward);
		btnBackward = (ImageView) findViewById(R.id.btnBackward);
		btnNext = (ImageView) findViewById(R.id.btnNext);
		btnPrevious = (ImageView) findViewById(R.id.btnPrevious);

		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);

		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);

		songTitle = (TextView) findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

		btnPlay.setOnClickListener(this);
		btnForward.setOnClickListener(this);
		btnBackward.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnPrevious.setOnClickListener(this);
		btnShuffle.setOnClickListener(this);
		btnRepeat.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mp3player_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(item.getItemId()==R.id.exitplayer)
			exitPlayer();
		return super.onOptionsItemSelected(item);
	}

	

	private void exitPlayer() {
		if(PlayerService.mp.isPlaying())
		PlayerService.mp.stop();
		finish();
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		System.out.println("itemclicked");
		playerService = new Intent(this, PlayerService.class);
		playerService.putExtra("songIndex", position);
		startService(playerService);
	}

	@Override
	public void onClick(View v) {	
	}

	

	
}
