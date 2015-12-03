package com.example.assign2.myCountries;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CountriesDBhelper extends SQLiteOpenHelper {
	public static final String COUNTRY_TABLE_NAME = "ContryYear";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_COUNTRY = "Country";
	public static final String COLUMN_YEAR="Year";
	public static final String DATABASE_NAME = "MyCountries.db";
	public static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table if not exists "
			+ COUNTRY_TABLE_NAME + " (" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_COUNTRY
			+ " text not null, " + COLUMN_YEAR+" integer); ";

	public CountriesDBhelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(CountriesDBhelper.class.getName(), "onCreate Database==> " + DATABASE_CREATE);
		db.execSQL(DATABASE_CREATE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(CountriesDBhelper.class.getName(), "Upgrading database from version " 
	    		+ oldVersion + " to " + newVersion 
	    		+ ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + COUNTRY_TABLE_NAME);
		onCreate(db);

	}
}
