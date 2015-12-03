/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader.rssstuff;

import java.io.InputStream;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * This code encapsulates RSS item data. Our application needs title and link
 * data.
 * 
 * @author ITCuties
 * 
 */
public class RssItem {

	private String title;
	private String link;
	private String description;
	private String date;
	private String thumbnailLink;
	private Drawable thumbnailImage; 

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getThumbnailLink() {
		return thumbnailLink;
	}

	public void setThumbnailLink(String thumbnailLink) {
		this.thumbnailLink = thumbnailLink;
		setThumbnailImage(thumbnailLink);
	}

	public Drawable getThumbnailImage() {
		return thumbnailImage;
	}

	public void setThumbnailImage(String thumbnailLink) {

	    try {
	        InputStream is = (InputStream) new URL(thumbnailLink).getContent();
	        Drawable d = Drawable.createFromStream(is, "src name");
	        this.thumbnailImage = d;
	    } catch (Exception e) {
	       Log.d("image loading error", "the thumbnial from link: "+thumbnailLink+" did not loaded");
	    }
		
	}

}
