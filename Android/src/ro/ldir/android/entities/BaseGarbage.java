package ro.ldir.android.entities;

import java.util.ArrayList;
import java.util.Date;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

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
    private double yLatitude;
    private double xLongitude;
    private int county;
    private int town;
	private int garbageGroup;    
    private int insertedBy;
    private String name;
    private boolean toClean;
    private boolean toVote;
    private int voteCount;
    private int chartedArea;
    
    
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
		return getyLatitude();
	}
	public void setLatitude(double yLatitude) {
		this.setyLatitude(yLatitude);
	}
	public double getLongitude() {
		return getxLongitude();
	}
	public void setLongitude(double xLongitude) {
		this.setxLongitude(xLongitude);
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
	
	@JsonIgnore
	public void setStatus(GarbageStatus status)
	{
		this.status = status;
	}
	
	public String getStatusString()
	{
		return status.getTranslation();
	}
	@JsonProperty("allocatedStatus")
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
	public int getCounty() {
		return county;
	}
	public void setCounty(int county) {
		this.county = county;
	}

    
	public int getTown() {
		return town;
	}
	public void setTown(int town) {
		this.town = town;
	}
	public int getGarbageGroup() {
		return garbageGroup;
	}
	public void setGarbageGroup(int garbageGroup) {
		this.garbageGroup = garbageGroup;
	}
	public int getInsertedBy() {
		return insertedBy;
	}
	public void setInsertedBy(int insertedBy) {
		this.insertedBy = insertedBy;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isToClean() {
		return toClean;
	}
	public void setToClean(boolean toClean) {
		this.toClean = toClean;
	}
	public boolean isToVote() {
		return toVote;
	}
	public void setToVote(boolean toVote) {
		this.toVote = toVote;
	}
	public int getVoteCount() {
		return voteCount;
	}
	public void setVoteCount(int voteCount) {
		this.voteCount = voteCount;
	}
	public int getChartedArea() {
		return chartedArea;
	}
	public void setChartedArea(int chartedArea) {
		this.chartedArea = chartedArea;
	}
	@JsonProperty("y")
	public double getyLatitude() {
		return yLatitude;
	}
	public void setyLatitude(double yLatitude) {
		this.yLatitude = yLatitude;
	}
	@JsonProperty("x")
	public double getxLongitude() {
		return xLongitude;
	}
	public void setxLongitude(double xLongitude) {
		this.xLongitude = xLongitude;
	}	
}
