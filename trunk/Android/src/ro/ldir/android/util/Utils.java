package ro.ldir.android.util;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.widget.Toast;

public class Utils {
	
	public static final void displayDialog(Context context, int messageId)
	{
		displayDialog(context, context.getResources().getString(messageId));
	}
	
	public static final void displayDialog(Context context, String message)
	{
		Builder builder = new Builder(context);
		builder.setMessage(message).setCancelable(true);
		builder.create().show(); 
	}
	
	/**
	 * Used to display notifications when activities are changing
	 */
	public static final void displayToast(Context context, String message)
	{
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

}
