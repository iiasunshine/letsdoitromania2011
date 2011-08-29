package ro.ldir.android.util;

import java.io.File;
import java.io.IOException;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Environment;
import android.widget.Toast;

public class Utils {
	
	private static final String APP_PATH = Environment.getExternalStorageDirectory() + File.separator + "LDIR";
	private static final String CAMERA_PATH = APP_PATH + File.separator + "camera";
	
	public static final Dialog displayDialog(Context context, int messageId)
	{
		return displayDialog(context, context.getResources().getString(messageId));
	}
	
	public static final Dialog displayDialog(Context context, String message)
	{
		Builder builder = new Builder(context);
		builder.setMessage(message).setCancelable(true);
		return builder.create(); 
	}
	
	/**
	 * Used to display notifications when activities are changing
	 */
	public static final void displayToast(Context context, String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 
	 * @return /sdcard/LDIR/
	 */
	public static File getLDIRDirectory()
	{
		File ldirDir = new File(APP_PATH);
		if (!ldirDir.exists())
		{
			ldirDir.mkdirs();
		}
		return ldirDir;
	}
	
	/**
	 * 
	 * @return /sdcard/LDIR/camera/image.tmp
	 */
	public static File getTempImageFile()
	{
	    //it will return /sdcard/image.tmp
	    File tempImgFile = new File(getLDIRDirectory(), "image.tmp");
	    if (!tempImgFile.exists())
	    {
	    	try {
				tempImgFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
		return tempImgFile;
	}
	
	/**
	 * 
	 * @return /sdcard/LDIR/camera/
	 */
	public static final File getCameraDir()
	{
		File cameraDir = new File(CAMERA_PATH);
		if (!cameraDir.exists())
		{
			cameraDir.mkdirs();
		}
		return cameraDir;
	}
	

}
