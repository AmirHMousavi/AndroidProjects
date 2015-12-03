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

import com.example.lnureader.ImageUtils;

import android.graphics.drawable.Drawable;
import android.util.Log;

public class ChannelInfo {

	private long id;
	private String description;
	private String channelName;
	private String channelURL;
	private String lastModification;
	private String logoLink;
	private byte[] logoImage;

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public String getLastModification() {
		return lastModification;
	}

	public void setLastModification(String lastModification) {
		this.lastModification = lastModification;
	}

	public String getLogoLink() {
		return logoLink;
	}

	public void setLogoLink(String logoLink) {
		this.logoLink = logoLink;
		setLogoImage(logoLink);
	}

	public byte[] getLogoImage() {
		return logoImage;
	}

	public void setLogoImage(String string) {
		try {
			InputStream is = (InputStream) new URL(string).getContent();
			Drawable d = Drawable.createFromStream(is, "src name");
			this.logoImage = new ImageUtils().drawableToByteArray(d);
		} catch (Exception e) {
			Log.d("image loading error", "the thumbnial from link: " + string
					+ " did not loaded");
		}

	}

	public String getChannelURL() {
		return channelURL;
	}

	public void setChannelURL(String channelURL) {
		this.channelURL = channelURL;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
