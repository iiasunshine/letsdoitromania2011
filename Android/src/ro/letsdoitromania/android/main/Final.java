package ro.letsdoitromania.android.main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
//TODO - de văzut dacă trebuie clasa asta până la urmă
public class Final extends Activity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Restore authenticated session - if any
        setContentView(R.layout.sumar);
        
        ((Button)findViewById(R.id.ButtonIncaUnu)).setOnClickListener(new View.OnClickListener(){
			public void onClick(View view) {
				 finish();
			}
		});
		
        ((Button)findViewById(R.id.ButtonFinalIesire)).setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				// TODO Auto-generated method stub
				//exit();				
			}
		});
    }
}
