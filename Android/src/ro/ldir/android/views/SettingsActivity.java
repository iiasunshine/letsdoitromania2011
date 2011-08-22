package ro.ldir.android.views;

import ro.ldir.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class SettingsActivity extends Activity {

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        
        Button submitButton = (Button)findViewById(R.id.btnLoginSubmit);
        submitButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String email = ((EditText)findViewById(R.id.txtLoginEmail)).getText().toString();
				String password = ((EditText)findViewById(R.id.txtLoginPass)).getText().toString();
				
				// TODO - start login
				// display indefinite progress dialog
				// after login is completed, change the text on the submit button into Logout
			}
		});
	}
}
