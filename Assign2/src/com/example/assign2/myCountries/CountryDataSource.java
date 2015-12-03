package com.example.assign2.myCountries;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CountryDataSource {

	private SQLiteDatabase database;
	private CountriesDBhelper dbHelper;
	private String[] allColumns = { CountriesDBhelper.COLUMN_ID,
			CountriesDBhelper.COLUMN_COUNTRY, CountriesDBhelper.COLUMN_YEAR };

	public CountryDataSource(Context context) {
		dbHelper = new CountriesDBhelper(context,
				CountriesDBhelper.DATABASE_NAME, null,
				CountriesDBhelper.DATABASE_VERSION);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public CountryList creatCountry(String country, Integer year) {
		ContentValues values = new ContentValues();
		values.put(CountriesDBhelper.COLUMN_COUNTRY, country);
		values.put(CountriesDBhelper.COLUMN_YEAR, year);
		long insertID = database.insert(CountriesDBhelper.COUNTRY_TABLE_NAME,
				null, values);
		Cursor cursor = database.query(CountriesDBhelper.COUNTRY_TABLE_NAME,
				allColumns, CountriesDBhelper.COLUMN_ID + "=" + insertID, null,
				null, null, null);
		cursor.moveToFirst();
		CountryList newList = cursorToCountry(cursor);
		cursor.close();
		return newList;

	}

	private CountryList cursorToCountry(Cursor cursor) {
		CountryList list = new CountryList();
		list.setId(cursor.getLong(0));
		list.setCountry(cursor.getString(1));
		list.setYear(cursor.getInt(2));
		return list;
	}

	public void deleteCountry(CountryList list) {
		Long id = list.getId();
		System.out.println("Contry deleted with id: " + id);
		database.delete(CountriesDBhelper.COUNTRY_TABLE_NAME,
				CountriesDBhelper.COLUMN_ID + "=" + id, null);

	}

	public CountryList getCountry(long countryID) {
		String restrict = CountriesDBhelper.COLUMN_ID + "=" + countryID;
		Cursor cursor = database.query(true,
				CountriesDBhelper.COUNTRY_TABLE_NAME, allColumns, restrict,
				null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			CountryList list = cursorToCountry(cursor);
			return list;
		}
		cursor.close();
		return null;

	}

	public boolean updateCountries(long countryID, String country, Integer year) {
		ContentValues args = new ContentValues();
		args.put(CountriesDBhelper.COLUMN_COUNTRY, country);
		args.put(CountriesDBhelper.COLUMN_YEAR, year);
		String restrict = CountriesDBhelper.COLUMN_ID + "=" + countryID;
		return database.update(CountriesDBhelper.COUNTRY_TABLE_NAME, args,
				restrict, null) > 0;

	}

	public List<CountryList> getAllCountries() {
		List<CountryList> lists = new ArrayList<CountryList>();
		Cursor cursor = database.query(CountriesDBhelper.COUNTRY_TABLE_NAME,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CountryList list = cursorToCountry(cursor);
			lists.add(list);
			cursor.moveToNext();
		}
		cursor.close();
		return lists;

	}

	public List<CountryList> sotByYear() {
		List<CountryList> lists = new ArrayList<CountryList>();
		Cursor cursor=database.query(CountriesDBhelper.COUNTRY_TABLE_NAME, allColumns, null,
				null, null, null, CountriesDBhelper.COLUMN_YEAR + " ASC ");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CountryList list = cursorToCountry(cursor);
			lists.add(list);
			cursor.moveToNext();
		}
		cursor.close();
		return lists;
		
		// database.rawQuery("SELECT * FROM "/* + CountriesDBhelper.COLUMN_YEAR
		// + " FROM "*/
		// + CountriesDBhelper.COUNTRY_TABLE_NAME + " ORDER BY "
		// + CountriesDBhelper.COLUMN_YEAR + " DESC;", null);

	}

	public List<CountryList> sortByCountry() {
		List<CountryList> lists = new ArrayList<CountryList>();
		Cursor cursor=database.query(CountriesDBhelper.COUNTRY_TABLE_NAME, allColumns, null,
				null, null, null, CountriesDBhelper.COLUMN_COUNTRY + " ASC ");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			CountryList list = cursorToCountry(cursor);
			lists.add(list);
			cursor.moveToNext();
		}
		cursor.close();
		return lists;
		// database.rawQuery("SELECT * FROM " /*+
		// CountriesDBhelper.COLUMN_COUNTRY + " FROM "*/
		// + CountriesDBhelper.COUNTRY_TABLE_NAME + " ORDER BY "
		// + CountriesDBhelper.COLUMN_COUNTRY + " DESC;", null );
	}

}
