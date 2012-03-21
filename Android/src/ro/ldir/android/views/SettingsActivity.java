package ro.ldir.android.views;

import ro.ldir.R;
import ro.ldir.android.entities.User;
import ro.ldir.android.remote.BackendFactory;
import ro.ldir.android.remote.JsonBackend;
import ro.ldir.android.remote.RemoteConnError;
import ro.ldir.android.util.ErrorDialogHandler;
import ro.ldir.android.util.IDialogIds;
import ro.ldir.android.util.LDIRActivity;
import ro.ldir.android.util.LDIRApplication;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends LDIRActivity{
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        initWithTestdata();
        setupLoginButton(((LDIRApplication)getApplication()).isLoggedIn());
	}
	
	/**
	 * Change the text and functionality of the submit button according to 
	 * the state of the application: logged in or not 
	 * @param isLoggedIn true if user details exist and false if they are null
	 */
	private void setupLoginButton(boolean isLoggedIn)
	{
		Button submitButton = (Button)findViewById(R.id.btnLoginSubmit);
		TextView lblLoginEmail = ((TextView)findViewById(R.id.lblLoginEmail));		
        if (isLoggedIn)
        {
        	submitButton.setOnClickListener(new LogoutClicklistener());
        	submitButton.setText(R.string.logout_button_submit);
        	showLoginForm(View.INVISIBLE);
        	lblLoginEmail.setText(((LDIRApplication) getApplication()).getUserDetails().getEmail());
        	lblLoginEmail.setVisibility(View.VISIBLE);
        }
        else
        {
        	submitButton.setOnClickListener(new LoginClickListener());
        	submitButton.setText(R.string.login_button_submit);
        	showLoginForm(View.VISIBLE);
        	lblLoginEmail.setVisibility(View.INVISIBLE);
        }
	}
	/**
	 * Helper method used to show/hide (depends on param) entries for email and password
	 * @author Catalin Mincu
	 * @param visible
	 */
	private void showLoginForm(int visible)	{
		EditText txt = ((EditText)findViewById(R.id.txtLoginEmail));
		txt.setVisibility(visible);
		txt = ((EditText)findViewById(R.id.txtLoginPass));
		txt.setVisibility(visible);
						
		TextView lbl = ((TextView)findViewById(R.id.lblLoginPassword));
		lbl.setVisibility(visible);
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
	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		if (id == IDialogIds.DLG_PROGRESS)
		{
			// TODO - add cancel listener
			ProgressDialog pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pd.setMessage(getResources().getString(R.string.login_progress));
			return pd;
		}
		return super.onCreateDialog(id);
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
			showDialog(IDialogIds.DLG_PROGRESS);
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
			dismissDialog(IDialogIds.DLG_PROGRESS);
		}

		@Override
		protected User doInBackground(Void... params)
		{
			String email = ((EditText)findViewById(R.id.txtLoginEmail)).getText().toString();
			String password = ((EditText)findViewById(R.id.txtLoginPass)).getText().toString();
			
			JsonBackend backend = BackendFactory.createBackend();
			try
			{
				return backend.signIn(email, password);
			} catch (RemoteConnError e)
			{
				ErrorDialogHandler.showErrorDialog(SettingsActivity.this, e.getStatusCode());
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

}
