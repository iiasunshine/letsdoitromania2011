package ro.ldir.android.remote;

/**
 * represents the interface of the garbage object common between the mobile application and the backend
 * @author cora
 *
 */
public interface IBackendGarbage
{
	public int getBagCount();
	
	public void setBagCount(int bagCount);
	
	public String getBigComponentsDescription();
	
	public void setBigComponentsDescription(String bigComponentsDescription);
	
	public String getDescription();
	
	public void setDescription(String description);
	
	public String getDetails();
	
	public void setDetails(String details);
	
	public boolean isDispersed();
	
	public void setDispersed(boolean dispersed);
	
	/**
	 * the garbage id in the backend database
	 * @return
	 */
	public int getGarbageId();
	
	/**
	 * the garbage id in the backend database
	 * @param garbageId
	 */
	public void setGarbageId(int garbageId);
	
	public int getPercentageGlass();
	
	public void setPercentageGlass(int percentageGlass);
	
	public int getPercentageMetal();
	
	public void setPercentageMetal(int percentageMetal);
	
	public int getPercentagePlastic();
	
	public void setPercentagePlastic(int percentagePlastic);
	
	public int getPercentageWaste();
	
	public void setPercentageWaste(int percentageWaste);
	
	public GarbageStatus getStatus();
	
	public void setStatus(GarbageStatus status);
	
	public double getLatitude();
	
	public void setLatitude(double latitude);
	
	public double getLongitude();
	
	public void setLongitude(double longitude);
	
}
