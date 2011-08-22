package ro.ldir.android.location;

import ro.ldir.R;
import ro.ldir.android.util.Utils;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class LocationGetter extends AsyncTask<Void, Void, Location>  implements LocationListener
{
	private static final int MIN_ACCURACY = 10;//meters - TODO - investigate the right number
	private Location location;
	private Context context;
	private AlertDialog dialog;
	
	
	public LocationGetter(Context context) {
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
		Builder builder = new Builder(context);
		builder.setMessage(R.string.details_getting_location);
		
		OnClickListener arg1 = new OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				LocationGetter.this.cancel(true);
			}
		};
		builder.setNegativeButton(R.string.details_stop_getting_location, arg1);
		
		builder.setCancelable(true);
		builder.setOnCancelListener(new OnCancelListener() {
			
			public void onCancel(DialogInterface dialog) {
				LocationGetter.this.cancel(true);
			}
		});
		dialog = builder.create();
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

	private void startGPS(Context context)
	{
		LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		LocationProvider gpsProvider = manager.getProvider(LocationManager.GPS_PROVIDER);
		if (gpsProvider == null)
		{
			// phone doesn't have GPS antenna
			Utils.displayDialog(context, R.string.details_no_gps);
			return; 
		}
		
		// if gps disable enable or return
		if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
		{
			// TODO show dialog that asks the user if he wants to enable GPS
		}

		manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, context.getApplicationContext().getMainLooper());
	}
	
	private void stopGPS(Context context)
	{
		LocationManager manager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
		manager.removeUpdates(this);
	}
	
	public void onLocationChanged(Location location) {
		if (location.getAccuracy() <= MIN_ACCURACY)
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
