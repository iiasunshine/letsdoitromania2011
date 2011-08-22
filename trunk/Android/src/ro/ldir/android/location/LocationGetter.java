package ro.ldir.android.location;

import ro.ldir.R;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class LocationGetter extends AsyncTask<Void, Void, Location>  implements LocationListener
{
	protected Location location;
	protected Activity context;
	protected ProgressDialog dialog;
	
	
	public LocationGetter(Activity context) {
		super();
		this.context = context;
	}

	public synchronized Location getLocation() {
		return location;
	}

	public synchronized void setLocation(Location location) {
		this.location = location;
	}
	
	
	
	@Override
	protected void onPreExecute() {
		dialog = new ProgressDialog(context);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		
		CharSequence text = context.getResources().getString(R.string.details_getting_location);
		dialog.setMessage(text);
		
		OnClickListener arg1 = new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				LocationGetter.this.cancel(true);
			}
		};
		text = context.getResources().getString(R.string.details_stop_getting_location);
		dialog.setButton(ProgressDialog.BUTTON_NEGATIVE, text, arg1);
		
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				LocationGetter.this.cancel(true);
			}
		});
		dialog.show();
		super.onPreExecute();
	}
	
	

	@Override
	protected void onPostExecute(Location result) {
		if (dialog != null)
		{
			dialog.dismiss();
		}
		TextView text = (TextView)((Activity)context).findViewById(R.id.txtLatitude);
		text.setText(String.valueOf(result.getLatitude()));
		text = (TextView)((Activity)context).findViewById(R.id.txtLongitude);
		text.setText(String.valueOf(result.getLongitude()));
		super.onPostExecute(result);
	}

	@Override
	protected Location doInBackground(Void... params) {
		
		// start GPS and start listening for locations until a valid one is received
		startGPS(context);
		
		while (!isCancelled())
		{
			Location location = getLocation();
			if (location != null)
			{
				break;
			}
			try {
				synchronized (this) {
					wait(2000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// stop listening
		stopGPS(context);
		
		return location;
	}
	
	
	
	@Override
	protected void onCancelled() {
		stopGPS(context);
		super.onCancelled();
	}

	private void startGPS(Activity activity)
	{
		LocationManager manager = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE);
		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, activity.getApplicationContext().getMainLooper());
	}
	
	private void stopGPS(Activity context)
	{
		LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		manager.removeUpdates(this);
	}
	
	public void onLocationChanged(Location location) {
		int minAccuracy = context.getResources().getInteger(R.integer.min_accuracy);
		if (location.getAccuracy() <= minAccuracy)
		{
			setLocation(location);
			synchronized (this) {
				this.notify();
			}
		}
	}

	public void onProviderDisabled(String provider) {
		
	}

	public void onProviderEnabled(String provider) {
		
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		
	}
}
