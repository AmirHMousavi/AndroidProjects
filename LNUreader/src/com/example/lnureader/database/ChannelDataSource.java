/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.lnureader.rssstuff.ChannelInfo;

public class ChannelDataSource {

	private SQLiteDatabase database;
	private ChannelDBhandler dbHelper;
	private String[] allColumns = { ChannelDBhandler.CHANNEL_ID,
			ChannelDBhandler.COLUMN_CHANNELLINK,
			ChannelDBhandler.COLUMN_CHANNELNAME,
			ChannelDBhandler.COLUMN_CHANNELDESC,
			ChannelDBhandler.COLUMN_LOGOLINK,
			ChannelDBhandler.COLUMN_LASTBUILT};

	public ChannelDataSource(Context context) {
		dbHelper = new ChannelDBhandler(context, ChannelDBhandler.DATABASE_NAME,
				null, ChannelDBhandler.DATABASE_VERSION, null);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public ChannelInfo creatNewChannel(String link, String name, String description , String logoLink , String lastBuilt) {
		ContentValues values = new ContentValues();
		values.put(ChannelDBhandler.COLUMN_CHANNELLINK, link);
		values.put(ChannelDBhandler.COLUMN_CHANNELNAME, name);
		values.put(ChannelDBhandler.COLUMN_CHANNELDESC, description);
		values.put(ChannelDBhandler.COLUMN_LOGOLINK, logoLink);
		values.put(ChannelDBhandler.COLUMN_LASTBUILT, lastBuilt);
		long insertID = database.insert(ChannelDBhandler.CHANNELS_TABLE_NAME,
				null, values);
		Cursor cursor = database.query(ChannelDBhandler.CHANNELS_TABLE_NAME,
				allColumns, ChannelDBhandler.CHANNEL_ID + "=" + insertID, null,
				null, null, null);
		cursor.moveToFirst();
		ChannelInfo newChannel = cursorToChannel(cursor);
		cursor.close();
		return newChannel;

	}

	private ChannelInfo cursorToChannel(Cursor cursor) {
		ChannelInfo newChannel = new ChannelInfo();
		newChannel.setId(cursor.getLong(0));
		newChannel.setChannelURL(cursor.getString(1));
		newChannel.setChannelName(cursor.getString(2));
		newChannel.setDescription(cursor.getString(3));
		newChannel.setLogoLink(cursor.getString(4));
		newChannel.setLastModification(cursor.getString(5));
		return newChannel;
	}

	public void deleteChannel(ChannelInfo channel) {
		Long id = channel.getId();
		System.out.println("Channel deleted with id: " + id);
		database.delete(ChannelDBhandler.CHANNELS_TABLE_NAME,
				ChannelDBhandler.CHANNEL_ID + "=" + id, null);

	}

	public ChannelInfo getChannel(long id) {
		String restrict = ChannelDBhandler.CHANNEL_ID + "=" + id;
		Cursor cursor = database.query(true,
				ChannelDBhandler.CHANNELS_TABLE_NAME, allColumns, restrict,
				null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			ChannelInfo channel = cursorToChannel(cursor);
			return channel;
		}
		cursor.close();
		return null;

	}

	public boolean updateChannel(long id, String link) {
		ContentValues args = new ContentValues();
		args.put(ChannelDBhandler.COLUMN_CHANNELLINK, link);
		String restrict = ChannelDBhandler.CHANNEL_ID + "=" + id;
		return database.update(ChannelDBhandler.CHANNELS_TABLE_NAME, args,
				restrict, null) > 0;

	}

	public List<ChannelInfo> getAllChannels() {
		List<ChannelInfo> channelslists = new ArrayList<ChannelInfo>();
		Cursor cursor = database.query(ChannelDBhandler.CHANNELS_TABLE_NAME,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ChannelInfo channel = cursorToChannel(cursor);
			channelslists.add(channel);
			cursor.moveToNext();
		}
		cursor.close();
		return channelslists;

	}

	public List<ChannelInfo> sortByName() {
		List<ChannelInfo> Channelslists = new ArrayList<ChannelInfo>();
		Cursor cursor = database.query(ChannelDBhandler.CHANNELS_TABLE_NAME,
				allColumns, null, null, null, null,
				ChannelDBhandler.COLUMN_CHANNELNAME + " ASC ");
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			ChannelInfo channel = cursorToChannel(cursor);
			Channelslists.add(channel);
			cursor.moveToNext();
		}
		cursor.close();
		return Channelslists;
		// database.rawQuery("SELECT * FROM " /*+
		// CountriesDBhelper.COLUMN_COUNTRY + " FROM "*/
		// + CountriesDBhelper.COUNTRY_TABLE_NAME + " ORDER BY "
		// + CountriesDBhelper.COLUMN_COUNTRY + " DESC;", null );
	}

}
