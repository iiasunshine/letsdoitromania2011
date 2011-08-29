package ro.ldir.android.util;

import java.io.File;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class LdirPrefferences {
	
	private static final String PREFFERENCES = "ro.ldir.pref";
	
	private static final String LAST_CAPTURED_IMG_INDEX = "image.index";
	private static final String PICTURE_PREFIX = "capture";
	private static final String PICTURE_EXTENSION = ".jpg";

	public static final String getNextImageFileName(Context context)
	{
		SharedPreferences prefs = context.getSharedPreferences(PREFFERENCES, Context.MODE_PRIVATE);
		int index = prefs.getInt(LAST_CAPTURED_IMG_INDEX, 0);
		File imageFile = new File(Utils.getCameraDir(), PICTURE_PREFIX + (++index) + PICTURE_EXTENSION);
		return imageFile.getAbsolutePath();
	}
	
	/**
	 * /sdcard/LDIR/camera/capture5.jpg
	 * @param context
	 * @param name
	 */
	public static final void setLastImageFileName(Context context, String name)
	{
		SharedPreferences prefs = context.getSharedPreferences(PREFFERENCES, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		name = name.substring(name.indexOf(PICTURE_PREFIX));
		name = name.substring(PICTURE_PREFIX.length(), name.length() - PICTURE_EXTENSION.length());
		editor.putInt(LAST_CAPTURED_IMG_INDEX, Integer.parseInt(name));
		editor.commit();
	}
}
