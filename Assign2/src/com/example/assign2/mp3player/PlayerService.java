package com.example.assign2.mp3player;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Random;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.assign2.R;

public class PlayerService extends Service implements OnCompletionListener,
		OnClickListener, OnSeekBarChangeListener {

	private WeakReference<ImageButton> btnRepeat, btnShuffle;
	private WeakReference<ImageView> btnPlay, btnForward, btnBackward, btnNext,
			btnPrevious;
	private WeakReference<SeekBar> songProgressBar;
	private WeakReference<TextView> songTitleLabel;
	private WeakReference<TextView> songCurrentDurationLabel;
	private WeakReference<TextView> songTotalDurationLabel;
	public static MediaPlayer mp;
	private Handler progressBarHandler = new Handler();;
	private TimeConverter utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	private ArrayList<Song> songsListingSD = new ArrayList<Song>();
	public static int currentSongIndex = -1;
	
	private PhoneStateListener phoneStateListener; //TODO 
	private TelephonyManager telephonyManager; // TODO  pause music on incoming calls 

	@Override
	public void onCreate() {
		mp = new MediaPlayer();
		mp.setOnCompletionListener(this);
		mp.reset();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);//
		utils = new TimeConverter();
		songsListingSD = Mp3_Player_main.songs;
		songCurrentDurationLabel = new WeakReference<TextView>(
				Mp3_Player_main.songCurrentDurationLabel);
		super.onCreate();

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		initUI();
		int songIndex = intent.getIntExtra("songIndex", 0);
		if (songIndex != currentSongIndex) {
			playSong(songIndex);
			initNotification(songIndex);
			currentSongIndex = songIndex;
		} else if (currentSongIndex != -1) {
			songTitleLabel.get().setText(
					songsListingSD.get(currentSongIndex).getName());
			if (mp.isPlaying())
				btnPlay.get().setImageResource(R.drawable.btn_pause);
			else
				btnPlay.get().setImageResource(R.drawable.btn_play);
		}
		return START_STICKY;		
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	private void initUI() {
		songTitleLabel = new WeakReference<TextView>(Mp3_Player_main.songTitle);
		songCurrentDurationLabel = new WeakReference<TextView>(
				Mp3_Player_main.songCurrentDurationLabel);
		songTotalDurationLabel = new WeakReference<TextView>(
				Mp3_Player_main.songTotalDurationLabel);

		btnPlay = new WeakReference<ImageView>(Mp3_Player_main.btnPlay);
		btnForward = new WeakReference<ImageView>(Mp3_Player_main.btnForward);
		btnBackward = new WeakReference<ImageView>(Mp3_Player_main.btnBackward);
		btnNext = new WeakReference<ImageView>(Mp3_Player_main.btnNext);
		btnPrevious = new WeakReference<ImageView>(Mp3_Player_main.btnPrevious);
		btnRepeat = new WeakReference<ImageButton>(Mp3_Player_main.btnRepeat);
		btnShuffle = new WeakReference<ImageButton>(Mp3_Player_main.btnShuffle);

		btnPlay.get().setOnClickListener(this);
		btnForward.get().setOnClickListener(this);
		btnBackward.get().setOnClickListener(this);
		btnNext.get().setOnClickListener(this);
		btnPrevious.get().setOnClickListener(this);
		btnRepeat.get().setOnClickListener(this);
		btnShuffle.get().setOnClickListener(this);


		songProgressBar = new WeakReference<SeekBar>(
				Mp3_Player_main.songProgressBar);
		songProgressBar.get().setOnSeekBarChangeListener(this);
	}

	// -------------------------------------------------------------------------//
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnPlay:
			if (currentSongIndex != -1) {
				if (mp.isPlaying()) {
					if (mp != null) {
						mp.pause();
						// Changing button image to play button
						btnPlay.get().setImageResource(R.drawable.btn_play);
						Log.d("Player Service", "Pause");

					}
				} else {
					// Resume song
					if (mp != null) {
						mp.start();
						// Changing button image to pause button
						btnPlay.get().setImageResource(R.drawable.btn_pause);
						Log.d("Player Service", "Play");
					}
				}
			}
			break;

		case R.id.btnNext:
			// check if next song is there or not
			Log.d("Player Service", "Next");
			if (currentSongIndex < (songsListingSD.size() - 1)) {
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			} else {
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
			break;

		case R.id.btnForward:

			// get current song position
			int currentPosition = mp.getCurrentPosition();
			// check if seekForward time is lesser than song duration
			if (currentPosition + seekForwardTime <= mp.getDuration()) {
				// forward song
				mp.seekTo(currentPosition + seekForwardTime);
			} else {
				// forward to end position
				mp.seekTo(mp.getDuration());
			}
			break;

		case R.id.btnBackward:
			// get current song position
			int currentPosition2 = mp.getCurrentPosition();
			// check if seekBackward time is greater than 0 sec
			if (currentPosition2 - seekBackwardTime >= 0) {
				// forward song
				mp.seekTo(currentPosition2 - seekBackwardTime);
			} else {
				// backward to starting position
				mp.seekTo(0);
			}
			break;

		case R.id.btnPrevious:

			if (currentSongIndex > 0) {
				playSong(currentSongIndex - 1);
				currentSongIndex = currentSongIndex - 1;
			} else {
				// play last song
				playSong(songsListingSD.size() - 1);
				currentSongIndex = songsListingSD.size() - 1;
			}
			break;

		case R.id.btnRepeat:

			if (isRepeat) {
				isRepeat = false;
				Toast.makeText(getApplicationContext(), "Repeat is OFF",
						Toast.LENGTH_SHORT).show();
				btnRepeat.get().setImageResource(R.drawable.btn_repeat);
			} else {
				// make repeat to true
				isRepeat = true;
				Toast.makeText(getApplicationContext(), "Repeat is ON",
						Toast.LENGTH_SHORT).show();
				// make shuffle to false
				isShuffle = false;
				btnRepeat.get().setImageResource(R.drawable.btn_repeat_focused);
				btnShuffle.get().setImageResource(R.drawable.btn_shuffle);
			}
			break;

		case R.id.btnShuffle:

			if (isShuffle) {
				isShuffle = false;
				Toast.makeText(getApplicationContext(), "Shuffle is OFF",
						Toast.LENGTH_SHORT).show();
				btnShuffle.get().setImageResource(R.drawable.btn_shuffle);
			} else {
				// make repeat to true
				isShuffle = true;
				Toast.makeText(getApplicationContext(), "Shuffle is ON",
						Toast.LENGTH_SHORT).show();
				// make shuffle to false
				isRepeat = false;
				btnShuffle.get().setImageResource(
						R.drawable.btn_shuffle_focused);
				btnRepeat.get().setImageResource(R.drawable.btn_repeat);
			}
			break;
		}
	}

	// -------------------------------------------------------------//

	/**
	 * @author www.9android.net
	 * @param songIndex
	 *            : index of song
	 */
	public void playSong(int songIndex) {
		// Play song
		try {
			initNotification(songIndex);
			mp.reset();
			mp.setDataSource(songsListingSD.get(songIndex).getPath());
			mp.prepare();
			mp.start();
			// Displaying Song title
			String songTitle = songsListingSD.get(songIndex).getName();
			songTitleLabel.get().setText(songTitle);
			// Changing Button Image to pause image
			btnPlay.get().setImageResource(R.drawable.btn_pause);
			// set Progress bar values
			songProgressBar.get().setProgress(0);
			songProgressBar.get().setMax(100);
			// Updating progress bar
			updateProgressBar();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// ----------------onSeekBar Change Listener------------------------------//
	/**
	 * Update timer on seekbar
	 * */
	public void updateProgressBar() {
		progressBarHandler.postDelayed(mUpdateTimeTask, 100);
	}

	/**
	 * Background Runnable thread
	 * */
	private Runnable mUpdateTimeTask = new Runnable() {
		public void run() {
			long totalDuration = 0;
			try {
				totalDuration = mp.getDuration();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			long currentDuration = 0;
			try {
				currentDuration = mp.getCurrentPosition();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
			// Displaying Total Duration time
			songTotalDurationLabel.get().setText(
					"" + utils.milliSecondsToTimer(totalDuration));
			// Displaying time completed playing
			songCurrentDurationLabel.get().setText(
					"" + utils.milliSecondsToTimer(currentDuration));

			// Updating progress bar
			int progress = (int) (utils.getProgressPercentage(currentDuration,
					totalDuration));
			// Log.d("Progress", ""+progress);
			songProgressBar.get().setProgress(progress);
			// Running this thread after 100 milliseconds
			progressBarHandler.postDelayed(this, 100);
			// Log.d("AndroidBuildingMusicPlayerActivity","Runable  progressbar");
		}
	};

	// ----------------on Seekbar Change
	// Listener---------------------------------------//
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromTouch) {
	}

	/**
	 * When user starts moving the progress handler
	 * */
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
	}

	/**
	 * When user stops moving the progress hanlder
	 * */
	public void onStopTrackingTouch(SeekBar seekBar) {
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(),
				totalDuration);

		// forward or backward to certain seconds
		mp.seekTo(currentPosition);

		// update timer progress again
		updateProgressBar();
	}

	/**
	 * On Song Playing completed if repeat is ON play same song again if shuffle
	 * is ON play random song
	 * */
	public void onCompletion(MediaPlayer arg0) {

		// check for repeat is ON or OFF
		if (isRepeat) {
			// repeat is on play same song again
			playSong(currentSongIndex);
		} else if (isShuffle) {
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand
					.nextInt((songsListingSD.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
		} else {
			// no repeat or shuffle ON - play next song
			if (currentSongIndex < (songsListingSD.size() - 1)) {
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			} else {
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
		}
	}

	// ---------------------------------------------------------//
	@Override
	public void onDestroy() {
		super.onDestroy();
		currentSongIndex = -1;
		// Remove progress bar update Hanlder callBacks
		progressBarHandler.removeCallbacks(mUpdateTimeTask);
		Log.d("Player Service", "Player Service Stopped");
		if (mp != null) {
			if (mp.isPlaying()) {
				mp.stop();
			}
			mp.release();
		}

	}

	// --------------------Push Notification
	// Set up the notification ID
	public static final int NOTIFICATION_ID = 1;
//	private NotificationManager mNotificationManager;

	// Create Notification
	private void initNotification(int songIndex) {
		String songName=songsListingSD.get(songIndex).getName();
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(this)
		        .setSmallIcon(R.drawable.musiclogo)
		        .setContentTitle("KhafanPlayer")
		        .setContentText(songName);
		mBuilder.setOngoing(true);
		Intent resultIntent = new Intent(this, Mp3_Player_main.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(Mp3_Player_main.class);
		stackBuilder.addNextIntent(resultIntent);
		PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
	}

}
