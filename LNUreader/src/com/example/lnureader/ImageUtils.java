/**
 * @title LNUreader the final project of course 2DV106 Android For Java Programmers
 * @author sm222cf (SeyedAmirhossein Mousavi)
 * @author sm222cf ms222jg (Mohsen Sadeghi gol)
 * @date autumn semester 2013
 * @university Linnaeus University Växjö
 */
package com.example.lnureader;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImageUtils {

	public  byte[] drawableToByteArray(Drawable d) {

	    if (d != null) {
	        Bitmap imageBitmap = ((BitmapDrawable) d).getBitmap();
	        ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
	        byte[] byteData = stream.toByteArray();

	        return byteData;
	    } else
	        return null;

	}


	public  Bitmap byteToBitmap(byte[] data) {

	    if (data == null)
	        return null;
	    else
	    	
	        return BitmapFactory.decodeByteArray(data, 0, data.length);
	}
}
