/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader.database;



import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ChannelDBhandler extends SQLiteOpenHelper{
	
	
	

	public static final String CHANNELS_TABLE_NAME = "ChannelsLinks";
	public static final String CHANNEL_ID = "id";
	public static final String COLUMN_CHANNELLINK = "link";
	public static final String COLUMN_CHANNELNAME = "channelName";
	public static final String COLUMN_CHANNELDESC = "channelDesc";
	public static final String COLUMN_LOGOLINK = "logoLink";
	public static final String COLUMN_LASTBUILT="lastBuilt";
	public static final String DATABASE_NAME = "LNUreader.db";
	public static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE = "create table if not exists "
			+ CHANNELS_TABLE_NAME + " (" + CHANNEL_ID
			+ " integer primary key autoincrement, " + COLUMN_CHANNELLINK
			+ " text not null, " + COLUMN_CHANNELNAME +" text, " + COLUMN_CHANNELDESC + " text," + COLUMN_LOGOLINK + " BLOB, " + COLUMN_LASTBUILT+ " text ); ";

	public ChannelDBhandler(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(ChannelDBhandler.class.getName(), "onCreate Database==> " + DATABASE_CREATE);
		db.execSQL(DATABASE_CREATE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(ChannelDBhandler.class.getName(), "Upgrading database from version " 
	    		+ oldVersion + " to " + newVersion 
	    		+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + CHANNELS_TABLE_NAME);
		onCreate(db);
		
	}

}
