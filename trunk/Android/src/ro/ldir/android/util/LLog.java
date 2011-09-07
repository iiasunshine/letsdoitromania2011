package ro.ldir.android.util;

import android.util.Log;

public class LLog 
{
	
	private static final String LDIR_TAG = "LDIR";

	public static final void d(String msg)
	{
		Log.d(LDIR_TAG, msg);
	}
	
	public static final void e(Throwable tr)
	{
		Log.e(LDIR_TAG, "", tr);
		System.out.println(Log.getStackTraceString(tr));
	}
}
