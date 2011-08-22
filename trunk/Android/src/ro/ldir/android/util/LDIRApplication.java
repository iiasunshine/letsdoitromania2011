package ro.ldir.android.util;

import ro.ldir.android.entities.Garbage;
import android.app.Application;

public class LDIRApplication extends Application
{
	
	private Garbage cachedGarbage;

	public void putCachedGarbage(Garbage cachedGarbage) {
		this.cachedGarbage = cachedGarbage;
	}

	public Garbage popCachedGarbage() {
		Garbage garbage = cachedGarbage;
		cachedGarbage = null;
		return garbage;
	}
	
	

}
