package ro.ldir.android.views;

import java.util.List;

import ro.ldir.R;
import ro.ldir.android.entities.Garbage;
import ro.ldir.android.entities.GarbageList;
import ro.ldir.android.remote.BackendFactory;
import ro.ldir.android.remote.GarbageStatus;
import ro.ldir.android.remote.JsonBackend;
import ro.ldir.android.remote.RemoteConnError;
import ro.ldir.android.util.ErrorDialogHandler;
import ro.ldir.android.util.GarbagesOverMap;
import ro.ldir.android.util.IErrDialogActivity;
import ro.ldir.android.util.LDIRApplication;
import ro.ldir.android.util.LLog;
import ro.ldir.android.util.Utils;
import ro.letsdoitromania.android.helpers.MyMapView;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 * This file is part of the LDIRAndroid - the Android client for the Let's Do It
 * Romania 2011 Garbage collection campaign. Copyright (C) 2011 by the LDIR
 * development team, further referred to as "authors".
 * 
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 * 
 * Filename: GarbageMapActivity.java Author(s): Catalin Mincu,
 * cata.mincu@gmail.com
 * 
 */

public class GarbageMapActivity extends MapActivity implements
		IErrDialogActivity {

	private MyMapView mapView;
	private MapController mc;
	private GeoPoint currentPoint, topLeft, bottomRight;
	private static final String DLG_ERROR_MESSAGE = "ro.ldir.views.error.msg";

	// coordinates to Bucharest center
	double lat = Double.parseDouble("44.437711");
	double lng = Double.parseDouble("26.097367");

	private String errorMessage;
	private GarbageList garbageList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.garbage_map);

		garbageList = new GarbageList();

		mapView = (MyMapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);
		
        // Add listener
        mapView.setOnChangeListener(new MapViewChangeListener());
        
		// center the map to the given coordinates
		currentPoint = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		mc = mapView.getController();
		mc.setZoom(13);
		mc.setCenter(currentPoint);
		mc.animateTo(currentPoint);

		// we need a thread to wait until map is ready for getting borders
		Runnable waitForMapTimeTask = new Runnable() {
			public void run() {
				if (mapView.getLatitudeSpan() == 0
						|| mapView.getLongitudeSpan() == 360*1E6) {
					mapView.postDelayed(this, 100);
				} else {
					drawGarbagesOnMap();
				}
			}
		};
		mapView.postDelayed(waitForMapTimeTask, 100);
		mapView.invalidate();		
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	
	@Override
	protected Dialog onCreateDialog(int id)
	{
		Dialog d = ErrorDialogHandler.onCreateDialog(this, id, errorMessage);
		if (d != null)
		{
			return d;
		}
		return super.onCreateDialog(id);
	}
	
	@Override
	protected void onPrepareDialog(int id, Dialog dialog)
	{
		ErrorDialogHandler.onPrepareDialog(id, dialog, errorMessage);
		super.onPrepareDialog(id, dialog);
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		errorMessage = savedInstanceState.getString(DLG_ERROR_MESSAGE);
	}


	@Override
	protected void onSaveInstanceState(Bundle outState)
	{
		outState.putString(DLG_ERROR_MESSAGE, errorMessage);
		super.onSaveInstanceState(outState);
	}
	
	public void setErrorMessage(String errMsg) {
		this.errorMessage = errMsg;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * Helper method that put garbage icons on map
	 */
	private void drawGarbagesOnMap() {
    	if (Utils.haveNetworkConnection(this)){
    		garbageList = getGarbagesInArea();
    	}	else	{
    		ErrorDialogHandler.showErrorDialog(this, 4010);
    	}
		
		// run on UI Thread to avoid ConcurrentModification exception
		runOnUiThread(new Runnable() {
		    public void run() {
		    	if (garbageList!= null)	
				for (Garbage garbage : garbageList.getGarbage()) {
					// add garbage on map
					addGarbageOnMap(mapView, garbage);
				}
		    }
		});
		
	}

	/**
	 * Helper method to add an overlay point to a mapView
	 * 
	 * @param mv
	 *            - map view
	 * @param garbage
	 *            - geoPoint
	 */
	private void addGarbageOnMap(MapView mv, Garbage garbage) {

		GeoPoint p = new GeoPoint((int) (garbage.getyLatitude() * 1E6),
				(int) (garbage.getxLongitude() * 1E6));
		int imageId = R.drawable.mm_20_red;
		String title = "Gunoi";
		String snipet = "De curatat";
		if (garbage.getStatus().equals(GarbageStatus.IDENTIFIED)) {
			imageId = R.drawable.mm_20_red;
			title = "Gunoi";
			snipet = "De curatat";
		} else if (garbage.getStatus().equals(GarbageStatus.ALLOCATED)) {
			imageId = R.drawable.mm_20_yellow;
			title = "Gunoi";
			snipet = "Alocat";
		} else if (garbage.getStatus().equals(GarbageStatus.CLEANED)) {
			imageId = R.drawable.mm_20_purple;
			title = "Gunoi";
			snipet = "Curatat";
		}

		List<Overlay> mapOverlays = mv.getOverlays();
		Drawable drawable = this.getResources().getDrawable(imageId);
		GarbagesOverMap garbagesOverlay = new GarbagesOverMap(drawable, this);
		
		OverlayItem overlayitem = new OverlayItem(p, title, snipet);

		garbagesOverlay.addOverlay(overlayitem);
		mapOverlays.add(garbagesOverlay);
	}

	/**
	 * Helper method that calls webservice and returns garbage list from an area
	 * defined by top-left and bottom-right corners
	 * 
	 * @return
	 */
	private GarbageList getGarbagesInArea() {

		GeoPoint lTopLeft = mapView.getProjection().fromPixels(
				mapView.getLeft(), mapView.getTop());
		GeoPoint lBottomRight = mapView.getProjection().fromPixels(
				mapView.getRight(), mapView.getBottom());

		// if we have same corners we exit without making the call to webservice
		if ((lTopLeft == topLeft)&&(lBottomRight == bottomRight))	{
			return null;
		}
		
		topLeft = lTopLeft;
		String topLeftX = String.valueOf(topLeft.getLongitudeE6() / 1E6);
		String topLeftY = String.valueOf(topLeft.getLatitudeE6() / 1E6);

		bottomRight = lBottomRight;
		String bottomRightX = String
				.valueOf(bottomRight.getLongitudeE6() / 1E6);
		String bottomRightY = String.valueOf(bottomRight.getLatitudeE6() / 1E6);

		JsonBackend backend = BackendFactory.createBackend();
		try {
			return backend.getGarbagesInArea(topLeftX, topLeftY, bottomRightX,
					bottomRightY);
		} catch (RemoteConnError e) {
			final int statusCode = e.getStatusCode();
			// we have to run through ui thread to show it on screen
			runOnUiThread(new Runnable() {
			    public void run() {
			    	ErrorDialogHandler.showErrorDialog(GarbageMapActivity.this,statusCode);
			    }
			});
		}
		return null;
	}
	
	private class MapViewChangeListener implements MyMapView.OnChangeListener
    {
 
        public void onChange(MapView view, GeoPoint newCenter, GeoPoint oldCenter, int newZoom, int oldZoom)
        {
            // Check values
            if ((!newCenter.equals(oldCenter)) && (newZoom != oldZoom))
            {
        		if (!newCenter.equals(currentPoint))	{
        			currentPoint = newCenter;
        			drawGarbagesOnMap();
        		}
            }
            else if (!newCenter.equals(oldCenter))
            {
        		if (!newCenter.equals(currentPoint))	{
        			currentPoint = newCenter;
        			drawGarbagesOnMap();
        		}
            }
            else if (newZoom != oldZoom)
            {
       			drawGarbagesOnMap();
            }
        }

		public void onDblTap(MapView view, GeoPoint point) {
			LLog.d("Dbl tap : " + point.getLatitudeE6()/1E6 + " - " + point.getLongitudeE6()/1E6);
			
			// open AddGarbage view to create a new garbage
			Garbage garbage = null;			
			((LDIRApplication)getApplication()).putCachedGarbage(garbage);			
			Intent intent = new Intent(getBaseContext(), AddGarbageActivity.class);
			startActivityForResult(intent, 0);
			
		}
    }
}
