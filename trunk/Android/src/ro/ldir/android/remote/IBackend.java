package ro.ldir.android.remote;

import android.graphics.Bitmap;
import ro.ldir.android.entities.Garbage;

public interface IBackend
{
	/* the following methods MUST be implemented in this version*/
	
	public void signIn(String user, String password);
	
	public void signOut();
	
	public void addGarbage(Garbage garbage);
	
	public void assignImageToGarbage(long garbageId, Bitmap picture);
	
	public void setGarbageStatus(long garbageId, GarbageStatus status);
	
	/* the following methods SHOULD be implemented in the next versions*/
	
	/*public void updateGarbage(Garbage garbage);
	
	public void deleteGarbage(long garbageId);*/
}
