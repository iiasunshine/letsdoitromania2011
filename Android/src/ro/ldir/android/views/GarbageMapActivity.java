/**
 * 
 */
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
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

/**
 *  This file is part of the LDIRAndroid - the Android client for the Let's Do It
 *  Romania 2011 Garbage collection campaign.
 *  Copyright (C) 2011 by the LDIR development team, further referred to 
 *  as "authors".
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Filename: GarbageMapActivity.java
 *  Author(s): Catalin Mincu, cata.mincu@gmail.com
 *
 */

public class GarbageMapActivity extends MapActivity implements IErrDialogActivity {

	MapView mapView;
	MapController mc;
	GeoPoint p;
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
		
		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);

		// center the map to the given coordinates
		p = new GeoPoint((int) (lat * 1E6), (int) (lng * 1E6));
		mc = mapView.getController();
		mc.setZoom(13);
		mc.setCenter(p);
		mc.animateTo(p);
		
		// we need a thread to wait until map is ready for getting borders
		Runnable waitForMapTimeTask = new Runnable() {
			public void run() {
				if(mapView.getLatitudeSpan()==0||mapView.getLongitudeSpan()== 360000000) {
					mapView.postDelayed(this, 100);
			    } else {
					drawGarbagesOnMap();
			    }
			}
		};	
		mapView.postDelayed(waitForMapTimeTask, 100);
		mapView.invalidate();
	}

	/**
	 * Helper method that put garbage icons on map
	 */
	private void drawGarbagesOnMap()	{
				
		garbageList = getGarbagesInArea();
		for (Garbage garbage : garbageList.getGarbage()) {
			// add garbage on map
			addGarbageOnMap(mapView, garbage);
		}
		mapView.invalidate();		
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

		GeoPoint p = new GeoPoint((int) (garbage.getyLatitude()* 1E6 ), (int) (garbage.getxLongitude()* 1E6));
		int imageId  = R.drawable.mm_20_red;
		if (garbage.getStatus().equals(GarbageStatus.IDENTIFIED))	{
			imageId = R.drawable.mm_20_red;
		}	else if (garbage.getStatus().equals(GarbageStatus.ALLOCATED))	{
			imageId = R.drawable.mm_20_yellow;
		}	else if (garbage.getStatus().equals(GarbageStatus.CLEANED))	{
			imageId = R.drawable.mm_20_purple;	
		}
		
		List<Overlay> mapOverlays = mv.getOverlays();		
		Drawable drawable = this.getResources().getDrawable(imageId);
		GarbagesOverMap garbagesOverlay = new GarbagesOverMap(drawable, this);

		OverlayItem overlayitem = new OverlayItem(p, "Garbage", "Clean it");

		garbagesOverlay.addOverlay(overlayitem);
		mapOverlays.add(garbagesOverlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	private GarbageList getGarbagesInArea() {
		
		GeoPoint topLeft = mapView.getProjection().fromPixels(
				mapView.getLeft(), mapView.getTop());
		String topLeftX = String.valueOf(topLeft.getLongitudeE6() / 1E6);
		String topLeftY = String.valueOf(topLeft.getLatitudeE6() / 1E6);

		GeoPoint bottomRight = mapView.getProjection().fromPixels(
				mapView.getRight(), mapView.getBottom());
		String bottomRightX = String
				.valueOf(bottomRight.getLongitudeE6() / 1E6);
		String bottomRightY = String.valueOf(bottomRight.getLatitudeE6() / 1E6);

		JsonBackend backend = BackendFactory.createBackend();
		try {
			return backend.getGarbagesInArea(topLeftX, topLeftY, bottomRightX,
					bottomRightY);
		} catch (RemoteConnError e) {
			ErrorDialogHandler.showErrorDialog(GarbageMapActivity.this,
					e.getStatusCode());
		}
		return null;
	}
	
	public void setErrorMessage(String errMsg)
	{
		this.errorMessage = errMsg;
	}

	public String getErrorMessage()
	{
		return errorMessage;
	}

}
