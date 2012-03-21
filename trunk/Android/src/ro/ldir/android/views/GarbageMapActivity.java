/**
 * 
 */
package ro.ldir.android.views;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;

import android.os.Bundle;
import ro.ldir.R;

/**
 * @author Catalin Mincu
 *
 */
public class GarbageMapActivity extends MapActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState)	{
	       super.onCreate(savedInstanceState);
	       setContentView(R.layout.garbage_map);
	       MapView mapView = (MapView) findViewById(R.id.mapView);	       	       
	       mapView.setBuiltInZoomControls(true);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}
	
	
}
