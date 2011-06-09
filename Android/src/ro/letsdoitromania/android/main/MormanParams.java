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

import android.widget.SeekBar;
import android.widget.SeekBar.*;

public class MormanParams extends Activity{
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
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
        

        SeekBar seekBar1 = (SeekBar)findViewById(R.id.seekbar1);
        setSeekbarListner(seekBar1);

        SeekBar seekBar2 = (SeekBar)findViewById(R.id.seekbar2);
        setSeekbarListner(seekBar2);

        SeekBar seekBar3 = (SeekBar)findViewById(R.id.seekbar3);
        setSeekbarListner(seekBar3);

        SeekBar seekBar4 = (SeekBar)findViewById(R.id.seekbar4);
        setSeekbarListner(seekBar4);
       
    }
    
    public void setSeekbarListner(SeekBar bar){
    	bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

    		public void onProgressChanged(SeekBar seekBar, int progress,
    		  boolean fromUser) {
    		 // TODO Auto-generated method stub
    		 //seekBarValue.setText(String.valueOf(progress));
    		}

    		public void onStartTrackingTouch(SeekBar seekBar) {
    		 // TODO Auto-generated method stub
    		}

    		public void onStopTrackingTouch(SeekBar seekBar) {
    		 // TODO Auto-generated method stub
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
}
