package ro.ldir.android.views;

import ro.ldir.R;
import ro.ldir.android.entities.User;
import ro.ldir.android.remote.BackendFactory;
import ro.ldir.android.remote.IBackend;
import ro.ldir.android.remote.RemoteConnError;
import ro.ldir.android.util.LDIRApplication;
import ro.ldir.android.util.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity {
	
	private static final int DLG_ERROR = 401;
	private static final int DLG_PROGRESS = 101;
	private static final String DLG_ERROR_MESSAGE = "ro.ldir.views.settings.error";
	
	private String errorMessage;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        initWithTestdata();
        setupLoginButton(isLoggedIn());
	}
	
	/**
	 * Change the text and functionality of the submit button according to 
	 * the state of the application: logged in or not 
	 * @param isLoggedIn true if user details exist and false if they are null
	 */
	private void setupLoginButton(boolean isLoggedIn)
	{
		Button submitButton = (Button)findViewById(R.id.btnLoginSubmit);
        if (isLoggedIn)
        {
        	submitButton.setOnClickListener(new LogoutClicklistener());
        	submitButton.setText(R.string.logout_button_submit);
        }
        else
        {
        	submitButton.setOnClickListener(new LoginClickListener());
        	submitButton.setText(R.string.login_button_submit);
        }
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

	/**
	 * Method use during development. 
	 * TODO - remove calls to this method when the application is released
	 */
	private void initWithTestdata()
	{
		EditText txt = ((EditText)findViewById(R.id.txtLoginEmail));
		txt.setText("crl@mailinator.com");
		txt = ((EditText)findViewById(R.id.txtLoginPass));
		txt.setText("crl");
		
	}
	
	/**
	 * Converts the error code received from the backend into an id of the message
	 * TODO - move this code into a class that handles error codes from the backend
	 * @param statusCode status code received from the backend
	 * @return message id from string resources
	 */
	private int getErrorMessage(int statusCode)
	{
		switch(statusCode)
		{
		case 403:
		case 401:
			return R.string.login_fail;
			default:
				return R.string.internal_err;
		}
	}
	
	/**
	 * This method is called whenever an error message must be displayed, for a status code
	 * @param statusCode
	 */
	private void showErrorDialog(int statusCode)
	{
		errorMessage = getResources().getString(getErrorMessage(statusCode));
		showDialog(DLG_ERROR);
	}

	@Override
	protected Dialog onCreateDialog(int id)
	{
		if (id == DLG_ERROR)
		{
			return Utils.displayDialog(this, errorMessage);
		}
		else if (id == DLG_PROGRESS)
		{
			// TODO - add cancel listener
			ProgressDialog pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage(getResources().getString(R.string.login_progress));
			return pd;
		}
		return super.onCreateDialog(id);
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
		if (id == DLG_ERROR)
		{
			((AlertDialog)dialog).setMessage(errorMessage);
		}
		super.onPrepareDialog(id, dialog);
	}
	
	/**
	 * Listener called when the submit button is a LOGIN button
	 * @author Coralia Paunoiu
	 *
	 */
	private class LoginClickListener implements OnClickListener
	{
		public void onClick(View v) {
			LoginTask task = new LoginTask();
			task.execute();
		}
	}
	
	/**
	 * Listener called when the submit button is a LOGOUT button
	 * @author Coralia Paunoiu
	 *
	 */
	private class LogoutClicklistener implements OnClickListener{

		public void onClick(View v)
		{
			logout();
			setupLoginButton(false);
		}
		
	}
	
	/**
	 * Async task that handles the login on another thread in order to avoid blocking the interface 
	 * with remote operations like connecting to the backend
	 * @author Coralia Paunoiu
	 *
	 */
	private class LoginTask extends AsyncTask<Void, Void, User>
	{
		@Override
		protected void onPreExecute()
		{
			super.onPreExecute();
			showDialog(DLG_PROGRESS);
		}

		@Override
		protected void onPostExecute(User result)
		{
			super.onPostExecute(result);
			((LDIRApplication)getApplication()).setUserDetails(result);
			if (result != null)
			{
				setupLoginButton(true);
			}
			dismissDialog(DLG_PROGRESS);
		}

		@Override
		protected User doInBackground(Void... params)
		{
			String email = ((EditText)findViewById(R.id.txtLoginEmail)).getText().toString();
			String password = ((EditText)findViewById(R.id.txtLoginPass)).getText().toString();
			
			IBackend backend = BackendFactory.createBackend();
			try
			{
				return backend.signIn(email, password);
			} catch (RemoteConnError e)
			{
				showErrorDialog(e.getStatusCode());
				return null;
			}
		}
		
	}
	
	/**
	 * Logs the user out
	 */
	private void logout()
	{
		((LDIRApplication)getApplication()).setUserDetails(null);
	}
	
	/**
	 * Checks if the user is logged in in the application
	 * @return true if the user is logged in - there are user details and false otherwise
	 */
	private boolean isLoggedIn()
	{
		return ((LDIRApplication)getApplication()).getUserDetails() != null;
	}
}
