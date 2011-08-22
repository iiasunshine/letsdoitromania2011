package ro.ldir.android.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.widget.Toast;

public class Utils {
	
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

}
