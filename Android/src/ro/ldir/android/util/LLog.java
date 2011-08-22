package ro.ldir.android.util;

import android.util.Log;

public class LLog 
{
	
	private static final String LDIR_TAG = "LDIR";

	public static final void d(String msg)
	{
		Log.d(LDIR_TAG, msg);
	}
}
