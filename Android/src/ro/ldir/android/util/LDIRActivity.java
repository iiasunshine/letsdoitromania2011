package ro.ldir.android.util;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

public class LDIRActivity extends Activity implements IErrDialogActivity
{
	private static final String DLG_ERROR_MESSAGE = "ro.ldir.views.error.msg";
	
	private String errorMessage;
	
	public void setErrorMessage(String errMsg)
	{
		this.errorMessage = errMsg;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog d = ErrorDialogHandler.onCreateDialog(this, id, errorMessage);
		if (d != null)
		{
			return d;
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
		ErrorDialogHandler.onPrepareDialog(id, dialog, errorMessage);
		super.onPrepareDialog(id, dialog);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		errorMessage = savedInstanceState.getString(DLG_ERROR_MESSAGE);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putString(DLG_ERROR_MESSAGE, errorMessage);
		super.onSaveInstanceState(outState);
	}
}
