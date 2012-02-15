package ro.ldir.android.remote;

import ro.ldir.android.entities.Garbage;
import ro.ldir.android.entities.User;
import android.graphics.Bitmap;

/**
 * Interface that handles the functions of the backend and connection to the backend
 * @author Coralia Paunoiu
 *
 */
public interface IBackend
{
	public static final int STATUS_CODE_OK = 200;
	
	
	//the following methods MUST be implemented in this version*/

	/**
	 * On success returns the user object filled with information about the user
	 * On failure returns null or throws RemoteConnError
	 */
	public User signIn(String user, String password) throws RemoteConnError;
	
	public void signOut();
	
	/**
	 * 
	 * @param user
	 * @param garbage
	 * @return when successful the id of the garbage in the backend (remote database)
	 * @throws RemoteConnError
	 */
	public int addGarbage(User user, Garbage garbage)  throws RemoteConnError;
	
	public void assignImageToGarbage(User user, long garbageId, String picturePath) throws RemoteConnError;
	
	public void setGarbageStatus(long garbageId, GarbageStatus status) throws RemoteConnError;
	
	/* the following methods SHOULD be implemented in the next versions*/
	
	/*public void updateGarbage(Garbage garbage);
	
	public void deleteGarbage(long garbageId);*/
}
