/**
 * 
 */
package ro.letsdoitromania.android.main;


import android.app.Activity;
import android.text.*;
import android.os.Bundle;
import android.widget.Button;
import android.widget.*;
import android.view.View;
import android.content.Intent;
import android.content.SharedPreferences;
/**
 * @author tudor
 *
 */
public class LogInActivity extends Activity 
	implements View.OnClickListener{
	
	private static final String PREFS_NAME = "LDIR_pref";
	private static final String USR_NAME   = "user";
	private static final String USR_PWD    = "pass";
	
	 /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    
        //dacă utilizatorul s-a mai logat, încearcă să folosești credențialele precedente
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings != null){
        
        	String user_name = settings.getString(USR_NAME, "");
        	String pswd      = settings.getString(USR_PWD, "");
        
        	if (user_name != "")
        		if (login(user_name,pswd))
        			returnOK();
        		else //poate și-a schimbat parola - pune la user numele de utilizator
        			((EditText)findViewById(R.id.EditText01)).setText(user_name);
        			 
        }
        
        //dacă nu există credențiale corecte - cere credențialele și încearcă din nou 	
        setContentView(R.layout.login);
        auth = (Button)findViewById(R.id.Button01);
        auth.setOnClickListener(this);
    }
    
    public void onClick(View view){
    	
    	//ia user name-ul și parola
    	 String user_name = ((Editable)((EditText)findViewById(R.id.EditText01)).getText()).toString();
    	 String pwd       = ((Editable)((EditText)findViewById(R.id.EditText02)).getText()).toString();

    	 //foloseștele ca să te logezi
    	if ((user_name != "") && (pwd != "")){
    	    //TODO logează

    		//TODO salvează usr și pwd dacă sunt ok
    		saveCredentials(user_name, pwd);
    		
    	}
    	else{
    		//TODO popout să bage ceva
    		
    	}
    	
    	//return
    	returnOK();
    }
    
    private void returnOK(){
    	Intent resultIntent = new Intent();
    	resultIntent.putExtra(MainActivity.auth_result, "OK");
    	this.finish();
    }
    
    private boolean login(String username, String passwd){
    	//TODO IMPLMENTEAZĂ PARTEA DE AUTHENTIFICARE
    	return true;
    }
    private void saveCredentials(String usr, String pwd){
    	 SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
         
    	 SharedPreferences.Editor editor = settings.edit();
         editor.putString(USR_NAME, usr);
         editor.putString(USR_PWD, pwd);
         
    }
    Button auth;

}
