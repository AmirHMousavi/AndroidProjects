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

import com.example.lnureader.rssstuff.FavoriteRssItem;



public class FavoriteFeedsDataSource {

	private SQLiteDatabase database2;
	private FavoriteFeedsDBhandler dbHelper;
	private String[] allColumns = { FavoriteFeedsDBhandler.FEEDS_ID,
			FavoriteFeedsDBhandler.COLUMN_LINK, FavoriteFeedsDBhandler.COLUMN_TITLE,
			FavoriteFeedsDBhandler.COLUMN_DESCRIPTION, FavoriteFeedsDBhandler.COLUMN_DATE,
			FavoriteFeedsDBhandler.COLUMN_PICTURE };

	public FavoriteFeedsDataSource(Context context) {
		dbHelper = new FavoriteFeedsDBhandler(context, ChannelDBhandler.DATABASE_NAME,
				null, ChannelDBhandler.DATABASE_VERSION, null);
	}

	public void open() throws SQLException {
		database2 = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public FavoriteRssItem createNewFavoriteItem(String title, String link,
			String description, String date, byte[] image) throws SQLException {
		ContentValues values = new ContentValues();
		values.put(FavoriteFeedsDBhandler.COLUMN_LINK, link);
		values.put(FavoriteFeedsDBhandler.COLUMN_TITLE, title);
		values.put(FavoriteFeedsDBhandler.COLUMN_DESCRIPTION, description);
		values.put(FavoriteFeedsDBhandler.COLUMN_DATE, date);
		values.put(FavoriteFeedsDBhandler.COLUMN_PICTURE, image);
		long insertID = database2.insert(FavoriteFeedsDBhandler.FEEDS_TABLE_NAME,
				null, values);
		Cursor cursor = database2.query(FavoriteFeedsDBhandler.FEEDS_TABLE_NAME,
				allColumns, FavoriteFeedsDBhandler.FEEDS_ID + "=" + insertID, null,
				null, null, null);
		cursor.moveToFirst();
		FavoriteRssItem newFavorite = cursorToFeed(cursor);
		cursor.close();
		return newFavorite;

	}
	
	public void deleteFavoriteItem(FavoriteRssItem item){
		Long id = item.getId();
		System.out.println("feed deleted with id: " + id);
		database2.delete(FavoriteFeedsDBhandler.FEEDS_TABLE_NAME,
				FavoriteFeedsDBhandler.FEEDS_ID + "=" + id, null);
	}
	
	public FavoriteRssItem getFavoriteItem(long id) {
		String restrict = FavoriteFeedsDBhandler.FEEDS_ID + "=" + id;
		Cursor cursor = database2.query(true,
				FavoriteFeedsDBhandler.FEEDS_TABLE_NAME, allColumns, restrict,
				null, null, null, null, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();
			FavoriteRssItem item = cursorToFeed(cursor);
			return item;
		}
		cursor.close();
		return null;

	}
	
	public List<FavoriteRssItem> getAllFavoriteItems() {
		List<FavoriteRssItem> favoriteItemsList = new ArrayList<FavoriteRssItem>();
		Cursor cursor = database2.query(FavoriteFeedsDBhandler.FEEDS_TABLE_NAME,
				allColumns, null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FavoriteRssItem item = cursorToFeed(cursor);
			favoriteItemsList.add(item);
			cursor.moveToNext();
		}
		cursor.close();
		return favoriteItemsList;
	}
	
	

	private FavoriteRssItem cursorToFeed(Cursor cursor) {
		FavoriteRssItem newFeed = new FavoriteRssItem();
		newFeed.setId(cursor.getLong(0));
		newFeed.setLink(cursor.getString(1));
		newFeed.setTitle(cursor.getString(2));
		newFeed.setDescription(cursor.getString(3));
		newFeed.setDate(cursor.getString(4));
		newFeed.setImage(cursor.getBlob(5));
		return newFeed;
	}
	

}
