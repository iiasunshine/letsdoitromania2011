package ro.ldir.android.util;

import ro.ldir.android.entities.Garbage;
import ro.ldir.android.entities.User;
import android.app.Application;

public class LDIRApplication extends Application
{
	
	private Garbage cachedGarbage;
	private User userDetails;

	public void putCachedGarbage(Garbage cachedGarbage) {
		this.cachedGarbage = cachedGarbage;
	}

	public Garbage popCachedGarbage() {
		Garbage garbage = cachedGarbage;
		cachedGarbage = null;
		return garbage;
	}

	public User getUserDetails()
	{
		return userDetails;
	}

	public void setUserDetails(User userDetails)
	{
		this.userDetails = userDetails;
	}
	
	

}
