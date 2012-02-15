package ro.letsdoitromania.android.main;

//import ro.letsdoitromania.android.structuri.*;

import ro.ldir.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

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
        

        _seekBar1 = (SeekBar)findViewById(R.id.seekbar1);
        setSeekbarListner(_seekBar1);

        _seekBar2 = (SeekBar)findViewById(R.id.seekbar2);
        setSeekbarListner(_seekBar2);

        _seekBar3 = (SeekBar)findViewById(R.id.seekbar3);
        setSeekbarListner(_seekBar3);

        _seekBar4 = (SeekBar)findViewById(R.id.seekbar4);
        setSeekbarListner(_seekBar4);
       
    }
    
    public void setSeekbarListner(SeekBar bar){
    	bar.setMax(100);
    	
    	bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

    		public void onProgressChanged(SeekBar seekBar, int progress,
    		  boolean fromUser) {
    		 // TODO Auto-generated method stub
    		}

    		public void onStartTrackingTouch(SeekBar seekBar) {
    		 // TODO Auto-generated method stub
    			if (_seekBar1.getProgress() + _seekBar2.getProgress() + _seekBar3.getProgress() + _seekBar4.getProgress() >= 100){
    				seekBar.invalidate();
    			}
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
    SeekBar _seekBar1;
    SeekBar _seekBar2;
    SeekBar _seekBar3;
    SeekBar _seekBar4;
}
