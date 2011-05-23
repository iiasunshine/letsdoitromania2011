package ro.letsdoitromania.android.main;

import ro.letsdoitromania.android.helpers.*;
import ro.letsdoitromania.android.structuri.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

//Location
import android.content.Context;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;
import android.location.LocationListener;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements LocationListener{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //init login data 
        _usr = "";
        _pwd = "";
        _usrId = -1;
        
        startActivityForResult(new Intent(this,LogInActivity.class), 1);
        
        //Restore authenticated session - if any
        setContentView(R.layout.main);
        
        _button_add = (Button)findViewById(R.id.Button01);
        _edit_lat   = (EditText)findViewById(R.id.EditText01);
        _edit_long  = (EditText)findViewById(R.id.EditText02);
     
        
        //afișeză parametrii adiționali ai mormanului și trimite-l la server, afișează starea tranzacției
        _button_add.setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				 Intent myIntent = new Intent(view.getContext(), MormanParams.class);
	                startActivityForResult(myIntent, 2);
			}
		});
       
        // afisează lista de mormane adăugate deja de către utilizator
        // TODO - dacă afișăm lista ar trebui să poată selecta fiecare morman și să-i vadă paramtrii
        //      - plus eventual să-l editeze
       ((Button)findViewById(R.id.ButtonListaMormane)).setOnClickListener(new View.OnClickListener() {
    	   public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent theIntent = new Intent(arg0.getContext(), ListaMormane.class);
			startActivity(theIntent);
    	   }
		});
    	   
    	// ieși
        ((Button)findViewById(R.id.ButtonFinalIesire)).setOnClickListener(new View.OnClickListener() {
       	   public void onClick(View arg0) {
       		   ((Activity)arg0.getContext()).finish();
   		}
        });
        
        //location
        // Acquire a reference to the system Location Manager
        
        _locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        updateProvider();
        updateLocation(); //update values   
    };
    
    public void updateProvider(){
    	 Criteria provCriteria = new Criteria();
         provCriteria.setAccuracy(Criteria.ACCURACY_FINE);
         provCriteria.setAltitudeRequired(false);
         provCriteria.setBearingRequired(false);
         provCriteria.setSpeedRequired(false);
         provCriteria.setCostAllowed(true);
                 
         _provider = _locationManager.getBestProvider(provCriteria, true);//get best provider
    }
    public void updateLocation(){
    	//get location
    	
    	Location  loc    = _locationManager.getLastKnownLocation(_provider);
        //update
    	if (loc != null){
    		try{
    			double lat = loc.getLatitude();
    			double lon = loc.getLongitude();
    	
    			_edit_lat.setText(Double.toString(lat));
    			_edit_long.setText(Double.toString(lon));
    		}
    		catch(Exception e){
    			
    		}
    	}
    }
    
    /** Register for the updates when Activity is in foreground */
	@Override
	protected void onResume() {
		super.onResume();
		_locationManager.requestLocationUpdates(_provider, 20000, 1, this);
		updateLocation();
	}

	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		_locationManager.removeUpdates(this);
	}

	public void onLocationChanged(Location location) {
		//
	}

	public void onProviderDisabled(String provider) {
		// let okProvider be bestProvider
		// re-register for updates
		//provider is disabled
		updateProvider();	
	}

	public void onProviderEnabled(String provider) {
		// is provider better than bestProvider?
		// is yes, bestProvider = provider
		updateProvider();
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		//provider status changed
		updateProvider();
	}
    
	public void onActivityResult(int param, Intent intent){
    	if (param == 1){
    		//the login ended
    		if (auth_result == "OK"){
    			Bundle extras = intent.getExtras();
    			_usr = extras.getString("usr_name");
    			_pwd = extras.getString("usr_pwd");
    			_usrId = extras.getLong("userId");
    		}
    	    setContentView(R.layout.main);
    	}
    	if (param == 2){
    		//adaugarea mormanului a avut loc
    		Bundle extras = getIntent().getExtras();
    		Morman morman = (Morman)extras.get("morman");
    		morman.set_lat_Y(Double.parseDouble(_edit_lat.getText().toString()));
    		morman.set_long_X(Double.parseDouble(_edit_lat.getText().toString()));
    		
    		if (auth_result == "OK")//if we are connected and authenticated add new garbage
    		{
    			Connection con = new Connection();
    			con.addGarbage(_usr, _pwd, _usrId, morman);
    		}
    		
    		TextView mes = (TextView)findViewById(R.id.StatusAdaugat);
    		if (add_result){
    			mes.setText(R.string.adaugat_ok);
    		}
    		else{
    			mes.setText(R.string.adaugat_nok);
    		}
    	}
    }
    
    //public
    public static String  auth_result;//rezultatul autentificării
    public static boolean add_result;// resultatul adăugării mormanului

    ///private
    Button   			_button_add;
    EditText 			_edit_lat;
    EditText 			_edit_long;
    String              _provider;
    LocationManager 	_locationManager;
    
    String              _usr;
    String              _pwd;
    long                _usrId;
    
    
}