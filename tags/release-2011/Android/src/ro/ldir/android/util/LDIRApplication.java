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
	
	/**
	 * Checks if the user is logged in in the application
	 * @return true if the user is logged in - there are user details and false otherwise
	 */
	public boolean isLoggedIn()
	{
		return getUserDetails() != null;
	}

}
