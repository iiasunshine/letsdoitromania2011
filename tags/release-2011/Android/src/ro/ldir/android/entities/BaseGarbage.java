package ro.ldir.android.entities;

import java.util.ArrayList;
import java.util.Date;

import ro.ldir.android.remote.GarbageStatus;
import ro.ldir.android.remote.IBackendGarbage;

/**
 * Class with base fields, common with the fields from the backend
 * All fields that are not found on the backend MUST be added in subclasses of this class
 * @author Coralia Paunoiu
 *
 */
public class BaseGarbage implements IBackendGarbage{
	
	/**
	 * the default value that the field {@link Garbage#remoteDbId}} has. If the garbage is not uploaded then this is the id
	 * After the upload, the  {@link Garbage#remoteDbId}} gets a normal id value
	 */
	public static final int NO_DB_ID = -1;
	
	public static final int DESCRIPTION_LENGTH = 20;
    public static final int DETAILS_LENGTH = 30;

    private int bagCount;
    private String bigComponentsDescription;
    private String description;
    private String details;
    private boolean dispersed;
    private int percentageGlass;
    private int percentageMetal;
    private int percentagePlastic;
    private int percentageWaste;
    private ArrayList<String> pictures = new ArrayList<String>();
    private Date recordDate;
    private double xLatitude;
    private double yLongitude;
    /**
     * Remote (backend) database id
     */
    private int garbageId = NO_DB_ID;
    
    private GarbageStatus status =  GarbageStatus.IDENTIFIED;
    
	public int getBagCount() {
		return bagCount;
	}
	public void setBagCount(int bagCount) {
		this.bagCount = bagCount;
	}
	public String getBigComponentsDescription() {
		return bigComponentsDescription;
	}
	public void setBigComponentsDescription(String bigComponentsDescription) {
		this.bigComponentsDescription = bigComponentsDescription;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public boolean isDispersed() {
		return dispersed;
	}
	public void setDispersed(boolean dispersed) {
		this.dispersed = dispersed;
	}
	public int getPercentageGlass() {
		return percentageGlass;
	}
	public void setPercentageGlass(int percentageGlass) {
		this.percentageGlass = percentageGlass;
	}
	public int getPercentageMetal() {
		return percentageMetal;
	}
	public void setPercentageMetal(int percentageMetal) {
		this.percentageMetal = percentageMetal;
	}
	public int getPercentagePlastic() {
		return percentagePlastic;
	}
	public void setPercentagePlastic(int percentagePlastic) {
		this.percentagePlastic = percentagePlastic;
	}
	public int getPercentageWaste() {
		return percentageWaste;
	}
	public void setPercentageWaste(int percentageWaste) {
		this.percentageWaste = percentageWaste;
	}
	public ArrayList<String> getPictures() {
		return pictures;
	}
	public void setPictures(ArrayList<String> pictures) {
		this.pictures = pictures;
	}
	public Date getRecordDate() {
		return recordDate;
	}
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}
	public double getLatitude() {
		return xLatitude;
	}
	public void setLatitude(double xLatitude) {
		this.xLatitude = xLatitude;
	}
	public double getLongitude() {
		return yLongitude;
	}
	public void setLongitude(double yLongitude) {
		this.yLongitude = yLongitude;
	}
	
	public String getCSVPictures()
	{
		StringBuffer buf = new StringBuffer();
		for (String str: pictures)
		{
			buf.append(str).append(",");
		}
		return buf.toString();
	}
	
	public void setCSVPictures(String csv)
	{
		String[] tokens = csv.split(",");
		for(String token: tokens)
		{
			pictures.add(token);
		}
	}

	public int getGarbageId()
	{
		return garbageId;
	}
	public void setGarbageId(int garbageId)
	{
		this.garbageId = garbageId;
	}
	public GarbageStatus getStatus()
	{
		return status;
	}
	public void setStatus(GarbageStatus status)
	{
		this.status = status;
	}
	
	public String getStatusString()
	{
		return status.getTranslation();
	}
	public void setStatus(String status)
	{
		if (GarbageStatus.CLEANED.getTranslation().equals(status))
		{
			this.status = GarbageStatus.CLEANED;
		}
		else
		{
			this.status = GarbageStatus.IDENTIFIED;
		}
	}
	
}
