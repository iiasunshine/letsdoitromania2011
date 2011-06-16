/**
 * 
 */
package ro.letsdoitromania.android.main;

import ro.letsdoitromania.android.helpers.*;

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
    
         	
        setContentView(R.layout.login);
        
        //dacă nu există credențiale corecte - cere credențialele și încearcă din nou	
        //dacă utilizatorul s-a mai logat, încearcă să folosești credențialele precedente
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        if (settings != null){
        
        	_usr = settings.getString(USR_NAME, "");
        	_pwd = settings.getString(USR_PWD, "");
        
        	if (_usr != "")
        		if (login())
        			returnOK();
        		else //poate și-a schimbat parola - pune la user numele de utilizator
        			((EditText)findViewById(R.id.EditName)).setText(_usr);
        			 
        }
        
        _auth = (Button)findViewById(R.id.Button01);
        _auth.setOnClickListener(this);
    }
    
    public void onClick(View view){
    	
    	//ia user name-ul și parola
    	 _usr = ((Editable)((EditText)findViewById(R.id.EditName)).getText()).toString();
    	 _pwd = ((Editable)((EditText)findViewById(R.id.EditPass)).getText()).toString();

    	 //foloseștele ca să te logezi
    	if ((_usr != "") && (_pwd!= "")){
            if (login()){
               	saveCredentials();
            	returnOK();
            }
            else{
            	Toast.makeText(this, R.string.log_error_msg, 7000).show();
            }
    		
    	}
    	else{
    		//TODO popout să bage ceva
    		Toast.makeText(this, R.string.log_offline_msg, 7000).show();
    		
    	}

       	//saveCredentials();
    	//return
    	returnOffline();
    }
    
    private void returnOffline(){
    	Intent resultIntent = new Intent();
    	resultIntent.putExtra(MainActivity.auth_result, "OFFLINE");
    	resultIntent.putExtra("userId", -1);
    	this.finish();
    }
    
    private void returnOK(){
    	Intent resultIntent = new Intent();
    	resultIntent.putExtra(MainActivity.auth_result, "OK");
    	resultIntent.putExtra("userId",_userId);
    	resultIntent.putExtra("usr_name", _usr);
    	resultIntent.putExtra("usr_pwd", _pwd);
    	this.finish();
    }
    
    private boolean login(){
    	if ((_usr != "") && (_pwd != "")){
    		Connection con = new Connection();	
    		_userId = con.authenticate(_usr, _pwd);
    		return (_userId != -1);
    	}
    	return false;
    }
    
    public void saveCredentials(){
    	 SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
         
    	 SharedPreferences.Editor editor = settings.edit();
         editor.putString(USR_NAME, _usr);
         editor.putString(USR_PWD, _pwd);
         
         editor.commit();
         
    }
    
    //member variables
    Button _auth;
    long   _userId;
    String _usr;
    String _pwd;
}
