package ro.letsdoitromania.android.main;

import ro.letsdoitromania.android.helpers.*;
//import ro.letsdoitromania.android.structuri.*;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

//Location
import android.content.Context;
import android.location.Location;
//import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Criteria;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivityForResult(new Intent(this,LogInActivity.class), 1);
        
        Connection con = new Connection();
        
        con.authenticate();
        
        //Restore authenticated session - if any
        setContentView(R.layout.main);
        
        button_add = (Button)findViewById(R.id.Button01);
        edit_lat   = (EditText)findViewById(R.id.EditText01);
        edit_long  = (EditText)findViewById(R.id.EditText02);
        
        //afișeză parametrii adiționali ai mormanului și trimite-l la server, afișează starea tranzacției
        button_add.setOnClickListener(new View.OnClickListener(){
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
        
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        
        Criteria provCriteria = new Criteria();
        provCriteria.setAccuracy(Criteria.ACCURACY_FINE);
        provCriteria.setAltitudeRequired(false);
        provCriteria.setBearingRequired(false);
        provCriteria.setSpeedRequired(false);
        provCriteria.setCostAllowed(true);
                
        String provider = locationManager.getBestProvider(provCriteria, true);//get best provider
        updateLocation(locationManager.getLastKnownLocation(provider)); //update values
       
    };
    public void updateLocation(Location loc){
    	if (loc != null){
    		try{
    			double lat = loc.getLatitude();
    			double lon = loc.getLongitude();
    	
    			edit_lat.setText(Double.toString(lat));
    			edit_long.setText(Double.toString(lon));
    		}
    		catch(Exception e){
    			
    		}
    	}
    }
    public void onActivityResult(int param){
    	if (param == 1){
    		//the log in ended
    		if (auth_result == "OK")
    	        setContentView(R.layout.main);
    	}
    	if (param == 2){
    		//adaugarea mormanului a avut loc
    		TextView mes = (TextView)findViewById(R.id.StatusAdaugat);
    		if (add_result){
    			mes.setText(R.string.adaugat_ok);
    		}
    		else{
    			mes.setText(R.string.adaugat_nok);
    		}
    	}
    }
    
    public static String  auth_result;//rezultatul autentificării
    public static boolean add_result;// resultatul adăugării mormanului
    
    Button button_add;
    EditText edit_lat;
    EditText edit_long;
    
}