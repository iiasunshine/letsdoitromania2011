package ro.letsdoitromania.android.main;

//import ro.letsdoitromania.android.structuri.*;

import android.app.Activity;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.Button;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.content.Intent;
import android.view.View;

public class MormanParams extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        chk1 = chk2 = chk3 = chk4 = chk5 = false; 	    
        
        //Restore authenticated session - if any
        setContentView(R.layout.morman);
        
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.volum_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        //salvează și pasează datele
        ((Button)findViewById(R.id.ButtonMorman)).setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
		           //TODO întoarce datele - serializate eventual	
				saveAndReturn();
			}
		});
        
        //checkboxes - chestia asta sigur se poate face cu un ArrayAdapter dar ... pentru moment asta e
        //TODO - rescrie asta cu arrayadapter or something.
        
        ((CheckBox) findViewById(R.id.checkbox_1)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                    chk1 = true;//Toast.makeText(HelloFormStuff.this, "Selected", Toast.LENGTH_SHORT).show();
                } 
            }
        });
        
        ((CheckBox) findViewById(R.id.checkbox_2)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                    chk2 = true;//Toast.makeText(HelloFormStuff.this, "Selected", Toast.LENGTH_SHORT).show();
                } 
            }
        });
        
        ((CheckBox) findViewById(R.id.checkbox_3)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                    chk3 = true;//Toast.makeText(HelloFormStuff.this, "Selected", Toast.LENGTH_SHORT).show();
                } 
            }
        });
        
        ((CheckBox) findViewById(R.id.checkbox_4)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                    chk4 = true;//Toast.makeText(HelloFormStuff.this, "Selected", Toast.LENGTH_SHORT).show();
                } 
            }
        });
        
        ((CheckBox) findViewById(R.id.checkbox_5)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
                if (((CheckBox) v).isChecked()) {
                    chk5 = true;//Toast.makeText(HelloFormStuff.this, "Selected", Toast.LENGTH_SHORT).show();
                } 
            }
        });
        
       
    }
    

    private void saveAndReturn(){
    	//TODO serializează mormanul - dacă ai semnal, trimite-l, dacă nu salvează-l ca să-l trimiți data viitoare
    	Intent resultIntent = new Intent();
    	resultIntent.putExtra("ro.letsdoitromania.android.main.MainActivity.add_result", false);
    	finish();
    }
    
    static boolean add;//if the status activity wants to add a new one
    boolean chk1;
    boolean chk2;
    boolean chk3;
    boolean chk4;
    boolean chk5;
}
