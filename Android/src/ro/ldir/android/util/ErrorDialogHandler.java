package ro.ldir.android.util;

import ro.ldir.R;
import ro.ldir.android.views.GarbageMapActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

public class ErrorDialogHandler
{
	public static Dialog onCreateDialog(Activity activity, int id, String errorMessage)
	{
		if (id == IDialogIds.DLG_ERROR)
		{ 
			return Utils.displayDialog(activity, errorMessage);
		}
		return null;
	}
	
	public static void onPrepareDialog(int id, Dialog dialog, String errorMessage)
	{
		if (id == IDialogIds.DLG_ERROR)
		{
			((AlertDialog)dialog).setMessage(errorMessage);
		}
	}

	/**
	 * This method is called whenever an error message must be displayed, for a status code
	 * @param statusCode
	 */
	public static void showErrorDialog(LDIRActivity activity, int statusCode)
	{
		LLog.d("Showing error dialog for error code: " + statusCode);
		String errorMessage = activity.getResources().getString(getErrorMessage(statusCode));
		activity.setErrorMessage(errorMessage);
		activity.showDialog(IDialogIds.DLG_ERROR);
	}

	/**
	 * This method is called whenever an error message must be displayed, for a status code
	 * @param statusCode
	 */
	public static void showErrorDialog(GarbageMapActivity activity, int statusCode)
	{
		LLog.d("Showing error dialog for error code: " + statusCode);
		String errorMessage = activity.getResources().getString(getErrorMessage(statusCode));
		activity.setErrorMessage(errorMessage);
		activity.showDialog(IDialogIds.DLG_ERROR);
	}
	
	/**
	 * Converts the error code received from the backend into an id of the message
	 * @param statusCode status code received from the backend
	 * @return message id from string resources
	 */
	private static int getErrorMessage(int statusCode)
	{
		switch(statusCode)
		{
		case 403:
		case 401:
			return R.string.login_fail;
		case 400:
			return R.string.chart_err_coords; //add garbage
		case 404:
			return R.string.internal_err; // if the garbage tag does not exist when uploading an image
		case 500: 
			return R.string.internal_err; 
			default:
				return R.string.internal_err;
		}
	}
	private static String getErrorMessage(Activity activity, int statusCode)
	{
		return activity.getResources().getString(getErrorMessage(statusCode));
	}
}
