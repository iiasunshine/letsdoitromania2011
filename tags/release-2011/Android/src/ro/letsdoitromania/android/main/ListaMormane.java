package ro.letsdoitromania.android.main;

import android.app.ListActivity;
//import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.view.*;

public class ListaMormane extends ListActivity {
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //>dummy mormane
        String mormane[] = new String[5];
        
        mormane[0] = "1. 24.45/55.33";
        mormane[1] = "2. 24.45/55.33";
        mormane[2] = "3. 24.45/55.33";
        mormane[3] = "4. 24.45/55.33";
        mormane[4] = "5. 24.45/55.33";
        //<dummy
        
        setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mormane));
        //setContentView(R.layout.listamormane);

       
    };

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
	}
    
   
}
