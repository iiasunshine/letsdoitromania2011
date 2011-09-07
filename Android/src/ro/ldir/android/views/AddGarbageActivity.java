package ro.ldir.android.views;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import ro.ldir.R;
import ro.ldir.android.entities.Garbage;
import ro.ldir.android.location.LocationGetter;
import ro.ldir.android.sqlite.LdirDbManager;
import ro.ldir.android.util.LDIRApplication;
import ro.ldir.android.util.LLog;
import ro.ldir.android.util.LdirPrefferences;
import ro.ldir.android.util.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AddGarbageActivity extends Activity 
{
	private static final String SAVED_GARBAGE = "ro.ldir.android.views.saved.garbage";
	protected static final String SAVED_GARBAGE_ID = "ro.ldir.android.views.saved.garbage.id";
	protected static final String SAVED_ERROR_MSG_ID = "ro.ldir.android.views.saved.garbage.id";
	
	private static final int DLG_NO_GPS = 10;
	private static final int DLG_GPS_DISABLED = 11;
	private static final int DLG_ERROR = 12;
	
	private static final int REQUEST_ENABLE_GPS = 100;
	private static final int REQUEST_FROM_CAMERA = 101;
	private static final int REQUEST_EDIT_GALLERY = 102;

	/**
	 * This object is received by the activity. it it is set, then the object is changed and updated to the local database
	 * If it is null, then the object is credated and added to the local database
	 */
	private Garbage garbage;
	
	private LocationGetter locationGetter;
	
	private int errorMessageId;
	
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
        	
        	errorMessageId = savedInstanceState.getInt(SAVED_ERROR_MSG_ID, 0);
        }
        
        ViewGroup layout = (ViewGroup)findViewById(R.id.garbage_layout);
        setReadOnly(layout, garbage.isUploaded());
        
        
//        TextView txtNrPictures = (TextView)findViewById(R.id.btnPictureGallery);
//        int nrOfPicsAttached = garbage.getPictures().size();
//        txtNrPictures.setText(String.valueOf(nrOfPicsAttached));
//        txtNrPictures.setText(getResources().getString(R.string.details_picture_nr, nrOfPicsAttached));
        
        Button btnGPS = (Button)findViewById(R.id.btnGPS);
        btnGPS.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				if (!hasGPSAntenna())
				{
					// phone doesn't have GPS antenna
					showDialog(DLG_NO_GPS);
					return; 
				}
				if (!isGPSEnabled())
				{
					// show dialog that asks the user if he wants to enable GPS
					showDialog(DLG_GPS_DISABLED);
					return;
				}
				locationGetter = new LocationGetter(AddGarbageActivity.this);
				locationGetter.execute();
			}
		});
	
        Button btnAddPicture = (Button)findViewById(R.id.btnAddPicture);
        btnAddPicture.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				  intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(Utils.getTempImageFile()));
				  startActivityForResult(intent, REQUEST_FROM_CAMERA);
			}
		});
        
        LinearLayout btnPictureGallery = (LinearLayout)findViewById(R.id.btnPictureGallery);
        btnPictureGallery.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				ArrayList<String> pictures = garbage.getPictures();
				if (pictures.isEmpty())
				{
					showErrorDialog(R.string.details_no_pictures);
				}
				else
				{
					Intent intent = new Intent(AddGarbageActivity.this, PictureGalleryActivity.class);
					intent.putStringArrayListExtra(PictureGalleryActivity.PICTURE_LIST, pictures);
					startActivityForResult(intent, REQUEST_EDIT_GALLERY);
				}
			}
		});
	}
	
	private void setReadOnly(ViewGroup parent, boolean readOnly)
	{
		int count = parent.getChildCount();
		for (int i = 0; i < count; i++)
		{
			View child = parent.getChildAt(i);
			child.setEnabled(!readOnly);
			if (child instanceof ViewGroup)
			{
				setReadOnly((ViewGroup)child, readOnly);
			}
		}
	}
	
	

	@Override
	protected Dialog onCreateDialog(int id) {
		switch(id)
		{
		case DLG_NO_GPS:
			return Utils.displayDialog(this, R.string.details_no_gps);
		case DLG_ERROR:
			return Utils.displayDialog(this, errorMessageId);
		case DLG_GPS_DISABLED:
		{
			AlertDialog.Builder builder = new AlertDialog.Builder(AddGarbageActivity.this);
			builder.setMessage(R.string.details_gps_disabled);
			DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					if (which == DialogInterface.BUTTON_NEGATIVE)
					{
						dialog.dismiss();
					}
					else if(which == DialogInterface.BUTTON_POSITIVE)
					{
						AddGarbageActivity.this.startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), REQUEST_ENABLE_GPS);
					}
				}
			};
			builder.setNegativeButton(R.string.details_cancel, clickListener);
			builder.setPositiveButton(R.string.details_ok, clickListener);
			return builder.create();
		}
		}
		return super.onCreateDialog(id);
	}



	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch(id)
		{
		case DLG_ERROR:
			((AlertDialog)dialog).setMessage(getResources().getString(errorMessageId));
			break;
		}
		super.onPrepareDialog(id, dialog);
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable(SAVED_GARBAGE, garbage);
		outState.putInt(SAVED_ERROR_MSG_ID, errorMessageId);
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
		((TextView)findViewById(R.id.txtLatitude)).setText(String.valueOf(garbage.getLatitude()));
		((TextView)findViewById(R.id.txtLongitude)).setText(String.valueOf(garbage.getLongitude()));
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
			 garbage.setLatitude(Double.parseDouble(txt));
		 }
		 
		 txt = ((TextView)findViewById(R.id.txtLongitude)).getText().toString();
		 if (txt != null && txt.trim().length() > 0)
		 {
			 garbage.setLongitude(Double.parseDouble(txt));
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
			int garbageId = garbage.getSqliteGarbageId();
			boolean success = false;
			if (garbageId == -1) // insert operation
			{
				garbageId = (int)dbManager.insert(garbage);
				if (garbageId != -1)
				{
					garbage.setSqliteGarbageId((int)garbageId);
					Utils.displayToast(this, getResources().getString(R.string.details_add_confirm, garbageId));
					success = true;
				}
				else
				{
					Utils.displayToast(this, getResources().getString(R.string.adaugat_nok));
				}
			}
			else // update operation
			{
				int rows = dbManager.update(garbage);
				if (rows == 0)
				{
					Utils.displayToast(this, getResources().getString(R.string.adaugat_nok));
				}
				else
				{
					Utils.displayToast(this, getResources().getString(R.string.details_modify_confirm, garbage.getSqliteGarbageId()));
					success = true;
				}
			}
			dbManager.close();
			if (success)
			{
				Intent data = new Intent();
				data.putExtra(SAVED_GARBAGE_ID, (long)garbageId);
				setResult(RESULT_OK, data);
				finish();
			}
		}
		
	}
	
	/**
	 * Called when saving is not wanted. Currently just finishes this activity.          
	 */
	private void cancel()
	{
		finish();
	}
	
	private void showErrorDialog(int msgId)
	{
		errorMessageId = msgId;
		showDialog(DLG_ERROR);
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
			showErrorDialog(R.string.chart_err_overflow_percents);
			return false;
		}
		double coordinate = garbage.getLatitude();
		double minLatitude = 43;//getResources().getFraction(R.fraction.min_latitude, 100, 1);
		double maxLatitude = 49;//getResources().getFraction(R.fraction.max_latitude, 100, 1);
		if (coordinate < minLatitude || coordinate > maxLatitude)
		{
			showErrorDialog(R.string.chart_err_latitude);
			return false;
		}
		
		coordinate = garbage.getLongitude();
		double minLongitude = 20;//getResources().getFraction(R.fraction.min_longitude, 100, 1);
		double maxLongitude = 30;//getResources().getFraction(R.fraction.max_longitude, 100, 1);
		if (coordinate < minLongitude || coordinate > maxLongitude)
		{
			showErrorDialog(R.string.chart_err_longitude);
			return false;
		}
		int percentage = garbage.getPercentageGlass();
		if (percentage < 0 || percentage > 100)
		{
			showErrorDialog(R.string.chart_js_err_glass);
			return false;
		}
		percentage = garbage.getPercentageMetal();
		if (percentage < 0 || percentage > 100)
		{
			showErrorDialog(R.string.chart_js_err_metal);
			return false;
		}
		percentage = garbage.getPercentagePlastic();
		if (percentage < 0 || percentage > 100)
		{
			showErrorDialog(R.string.chart_js_err_plastic);
			return false;
		}
		percentage = garbage.getPercentageWaste();
		if (percentage < 0 || percentage > 100)
		{
			showErrorDialog(R.string.chart_js_err_waste);
			return false;
		}
		// TODO - implement check if the coordinates exist already in the database
		return true;
	}
	
	private boolean hasGPSAntenna()
	{
		LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		LocationProvider gpsProvider = manager.getProvider(LocationManager.GPS_PROVIDER);
		return gpsProvider != null;
	}
	
	private boolean isGPSEnabled()
	{
		LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
		return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}



	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_ENABLE_GPS)
		{
			if (isGPSEnabled())
			{
				locationGetter = new LocationGetter(AddGarbageActivity.this);
				locationGetter.execute();
			}
			else
			{
				showDialog(DLG_GPS_DISABLED);
				
			}
		}
		else if (requestCode == REQUEST_FROM_CAMERA && resultCode == RESULT_OK) {
			InputStream is = null;
			File tempImageFile = Utils.getTempImageFile();
			try {
				is = new FileInputStream(tempImageFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			// On HTC Hero the requested file will not be created. Because HTC Hero has custom camera
			// app implementation and it works another way. It doesn't write to a file but instead
			// it writes to media gallery and returns uri in intent. More info can be found here:
			// http://stackoverflow.com/questions/1910608/android-actionimagecapture-intent
			// http://code.google.com/p/android/issues/detail?id=1480
			// So here's the workaround:
			if (is == null) {
				try {
					Uri u = data.getData();
					is = getContentResolver().openInputStream(u);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}

			// Now "is" stream contains the required photo, you can process it
			Bitmap photo = BitmapFactory.decodeStream(is);
			FileOutputStream out;
			try {
				String imageFileName = LdirPrefferences.getNextImageFileName(this);
				File file = new File(imageFileName);
				if (!file.exists())
				{
					file.createNewFile();
				}
				out = new FileOutputStream(imageFileName);
				photo.compress(Bitmap.CompressFormat.JPEG, 100, out);
				List<String> pictures = garbage.getPictures();
				if (!pictures.contains(imageFileName)){
					pictures.add(imageFileName);
				}
				LdirPrefferences.setLastImageFileName(this, imageFileName);
			} catch (FileNotFoundException e) {
				e.printStackTrace(); // TODO - add error message dialog
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			photo.recycle();
			photo = null;

			// don't forget to remove the temp file when it's not required.
			tempImageFile.delete();
		}
		else if (requestCode == REQUEST_EDIT_GALLERY /*&& resultCode == RESULT_OK*/)
		{
			// the picture list was edited. get the picture list from
			// the intent and set to the garbage
			ArrayList<String> pictures = data.getStringArrayListExtra(PictureGalleryActivity.PICTURE_LIST);
			garbage.setPictures(pictures);
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	

	/* btnAddImage:
	 * when pressed opens a dialog with options: capture image, select existing image
	 * capture image: 
	 * - opens camera
	 * - takes picture
	 * - saves picture on sd card if exists, or phone if it does not exist, on a location created by the application
	 * - adds the full path of the image to the list of images from the garbage.
	 * select existing image:
	 * - opens file explorer and selects an image. 
	 * - adds full path of image to the list of images from current garbage
	 */
	
	/*btnPictureGallery
	 * if the list of images is empty, disable this button
	 * when pressed opens a gallery with the images from the image list
	 * if the image was deleted from card/phone - display text - cannot find image
	 * the user can delete an image from the gallery 
	 */
}
