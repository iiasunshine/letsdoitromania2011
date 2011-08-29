package ro.ldir.android.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ro.ldir.R.string;

public class BaseGarbage{
	
	public static final int DESCRIPTION_LENGTH = 20;
    public static final int DETAILS_LENGTH = 30;

    private int bagCount;
    private String bigComponentsDescription;
    private String description;
    private String details;
    private boolean dispersed;
    private Integer garbageId = -1;
    private int percentageGlass;
    private int percentageMetal;
    private int percentagePlastic;
    private int percentageWaste;
    private ArrayList<String> pictures = new ArrayList<String>();
    private Date recordDate;
    private double xLatitude;
    private double yLongitude;
    
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
	public Integer getGarbageId() {
		return garbageId;
	}
	public void setGarbageId(Integer garbageId) {
		this.garbageId = garbageId;
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
	public double getxLatitude() {
		return xLatitude;
	}
	public void setxLatitude(double xLatitude) {
		this.xLatitude = xLatitude;
	}
	public double getyLongitude() {
		return yLongitude;
	}
	public void setyLongitude(double yLongitude) {
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

}
