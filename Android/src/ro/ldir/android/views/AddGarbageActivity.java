package ro.ldir.android.views;

import ro.ldir.R;
import ro.ldir.android.entities.Garbage;
import ro.ldir.android.location.LocationGetter;
import ro.ldir.android.sqlite.LdirDbManager;
import ro.ldir.android.util.LDIRApplication;
import ro.ldir.android.util.LLog;
import ro.ldir.android.util.Utils;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class AddGarbageActivity extends Activity 
{
	private static final String SAVED_GARBAGE = "ro.ldir.android.views.saved.garbage";
	protected static final String SAVED_GARBAGE_ID = "ro.ldir.android.views.saved.garbage.id";
	/**
	 * This object is received by the activity. it it is set, then the object is changed and updated to the local database
	 * If it is null, then the object is credated and added to the local database
	 */
	private Garbage garbage;
	
	private LocationGetter locationGetter;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_garbage);
        
        if (savedInstanceState == null)
        {
	        garbage = ((LDIRApplication)getApplication()).popCachedGarbage();
	        if (garbage == null)// add operation
	        {
	        	garbage = new Garbage();
	        }
	        else
	        {
	        	writeControls(garbage); //update operation
	        }
        }
        else
        {
        	//restore operation
        	garbage = (Garbage)savedInstanceState.get(SAVED_GARBAGE);
        	writeControls(garbage);
        }
        
        TextView txtNrPictures = (TextView)findViewById(R.id.txtNrPictures);
        int nrOfPicsAttached = garbage.getPictures().size();
        txtNrPictures.setText(getResources().getString(R.string.details_picture_nr, nrOfPicsAttached));
        
        Button btnGPS = (Button)findViewById(R.id.btnGPS);
        btnGPS.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				locationGetter = new LocationGetter(AddGarbageActivity.this);
				locationGetter.execute();
			}
		});
	}
	
	

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(SAVED_GARBAGE, garbage);
		super.onSaveInstanceState(outState);
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// add button
		menu.add(0, R.string.details_save, 0, R.string.details_save);
		// upload button
		menu.add(0, R.string.details_cancel, 0, R.string.details_cancel);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch(item.getItemId())
		{
		case R.string.details_save:
			saveGarbage(garbage);
			break;
		case R.string.details_cancel:
			cancel();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	private void writeControls(Garbage garbage)
	{
		((TextView)findViewById(R.id.txtLatitude)).setText(String.valueOf(garbage.getxLatitude()));
		((TextView)findViewById(R.id.txtLongitude)).setText(String.valueOf(garbage.getyLongitude()));
		 ((TextView)findViewById(R.id.txtDescriptionTitle)).setText(garbage.getDescription());
		 ((TextView)findViewById(R.id.txtDescription)).setText(garbage.getDetails());
		 ((TextView)findViewById(R.id.txtBagCount)).setText(String.valueOf(garbage.getBagCount()));
		 ((CheckBox)findViewById(R.id.checkDispersed)).setChecked(garbage.isDispersed());
		 
		 // TODO - add pictures
		 
		 ((TextView)findViewById(R.id.txtPlastic)).setText(String.valueOf(garbage.getPercentagePlastic()));
		 ((TextView)findViewById(R.id.txtMetal)).setText(String.valueOf(garbage.getPercentageMetal()));
		 ((TextView)findViewById(R.id.txtGlass)).setText(String.valueOf(garbage.getPercentageGlass()));
		 ((TextView)findViewById(R.id.txtWaste)).setText(String.valueOf(garbage.getPercentageWaste()));
	}
	
	
	private void readControls(Garbage garbage)
	{
		 String txt = ((TextView)findViewById(R.id.txtLatitude)).getText().toString();
		 if (txt != null && txt.trim().length() > 0)
		 {
			 garbage.setxLatitude(Double.parseDouble(txt));
		 }
		 
		 txt = ((TextView)findViewById(R.id.txtLongitude)).getText().toString();
		 if (txt != null && txt.trim().length() > 0)
		 {
			 garbage.setyLongitude(Double.parseDouble(txt));
		 }
		 
		 txt = ((TextView)findViewById(R.id.txtDescriptionTitle)).getText().toString();
		 garbage.setDescription(txt);
		 
		 txt = ((TextView)findViewById(R.id.txtDescription)).getText().toString();
		 garbage.setDetails(txt);
		 
		 txt = ((TextView)findViewById(R.id.txtBagCount)).getText().toString();
		 if (txt != null && txt.trim().length() > 0)
		 {
			 garbage.setBagCount(Integer.parseInt(txt));
		 }
		 
		 boolean dispersed = ((CheckBox)findViewById(R.id.checkDispersed)).isChecked();
		 garbage.setDispersed(dispersed);
		 
		 // TODO - add pictures
		 
		 txt = ((TextView)findViewById(R.id.txtPlastic)).getText().toString();
		 if (txt != null && txt.trim().length() > 0)
		 {
			 garbage.setPercentagePlastic(Integer.parseInt(txt));
		 }
		 
		 txt = ((TextView)findViewById(R.id.txtMetal)).getText().toString();
		 if (txt != null && txt.trim().length() > 0)
		 {
			 garbage.setPercentageMetal(Integer.parseInt(txt));
		 }
		 
		 txt = ((TextView)findViewById(R.id.txtGlass)).getText().toString();
		 if (txt != null && txt.trim().length() > 0)
		 {
			 garbage.setPercentageGlass(Integer.parseInt(txt));
		 }
		 
		 txt = ((TextView)findViewById(R.id.txtWaste)).getText().toString();
		 if (txt != null && txt.trim().length() > 0)
		 {
			 garbage.setPercentageWaste(Integer.parseInt(txt));
		 }
	}
	
	/**
	 * TODO - save garbage to database
	 * @param garbage
	 */
	private void saveGarbage(Garbage garbage)
	{
		LLog.d("Saving garbage...");
		readControls(garbage);
		if (validate(garbage))
		{
			LdirDbManager dbManager = new LdirDbManager();
			dbManager.open(this);
			long id = dbManager.save(garbage);
			dbManager.close();
			if (id != -1)
			{
				Utils.displayToast(this, getResources().getString(R.string.details_add_confirm, id));
				// TODO - add message for update
				Intent data = new Intent();
				data.putExtra(SAVED_GARBAGE_ID, id);
				setResult(RESULT_OK, data);
			}
			// TODO - add message for error
			finish();
		}
		
	}
	
	/**
	 * Called when saving is not wanted. Currently just finishes this activity.          
	 */
	private void cancel()
	{
		finish();
	}
	
	/**
	 * TODO - move to validators
	 * @param garbage
	 * @return
	 */
	private boolean validate(Garbage garbage)
	{
		int totalPercentage = garbage.getPercentageGlass() + garbage.getPercentageMetal() + garbage.getPercentagePlastic() + garbage.getPercentageWaste();
		if (totalPercentage != 100)
		{
			Utils.displayDialog(this, R.string.chart_err_overflow_percents);
			return false;
		}
		double coordinate = garbage.getxLatitude();
		if (coordinate < 43d || coordinate > 49d)// TODO - generalize for other countries
		{
			Utils.displayDialog(this, R.string.chart_err_latitude);
			return false;
		}
		
		coordinate = garbage.getyLongitude();
		if (coordinate < 20d || coordinate > 30d)// TODO - generalize for other countries
		{
			Utils.displayDialog(this, R.string.chart_err_longitude);
			return false;
		}
		int percentage = garbage.getPercentageGlass();
		if (percentage < 0 || percentage > 100)
		{
			Utils.displayDialog(this, R.string.chart_js_err_glass);
			return false;
		}
		percentage = garbage.getPercentageMetal();
		if (percentage < 0 || percentage > 100)
		{
			Utils.displayDialog(this, R.string.chart_js_err_metal);
			return false;
		}
		percentage = garbage.getPercentagePlastic();
		if (percentage < 0 || percentage > 100)
		{
			Utils.displayDialog(this, R.string.chart_js_err_plastic);
			return false;
		}
		percentage = garbage.getPercentageWaste();
		if (percentage < 0 || percentage > 100)
		{
			Utils.displayDialog(this, R.string.chart_js_err_waste);
			return false;
		}
		// TODO - implement check if the coordinates exist already in the database
		return true;
	}
	

}
