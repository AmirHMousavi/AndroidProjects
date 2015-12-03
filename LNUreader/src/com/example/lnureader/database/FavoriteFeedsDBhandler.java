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

public class FavoriteFeedsDBhandler extends SQLiteOpenHelper {
	

	public static final String FEEDS_TABLE_NAME = "favoritedFeeds";
	public static final String FEEDS_ID = "id";
	public static final String COLUMN_LINK= "link";
	public static final String COLUMN_TITLE = "title";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_PICTURE = "picData";
	public static final String DATABASE_NAME = "favoriteFeeds.db";
	public static final int DATABASE_VERSION = 1;
	
	private static final String DATABASE_CREATE= "create table if not exists "
			+ FEEDS_TABLE_NAME + " (" + FEEDS_ID
			+ " integer primary key autoincrement, " + COLUMN_LINK+ " text not null, " + COLUMN_TITLE
			+ " text not null, " + COLUMN_DESCRIPTION +" text, " + COLUMN_DATE + " text, " + COLUMN_PICTURE +" BLOB ); ";

	public FavoriteFeedsDBhandler(Context context, String name, CursorFactory factory,
			int version, DatabaseErrorHandler errorHandler) {
		super(context, DATABASE_NAME,null,DATABASE_VERSION);
		
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
		db.execSQL("DROP TABLE IF EXISTS " + FEEDS_TABLE_NAME);
		onCreate(db);
		
	}

}
