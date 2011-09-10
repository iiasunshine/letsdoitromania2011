/**
 *  This file is part of the LDIRBackend - the backend for the Let's Do It
 *  Romania 2011 Garbage collection campaign.
 *  Copyright (C) 2011 by the LDIR development team, further referred to 
 *  as "authors".
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Filename: Garbage.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;
import ro.ldir.dto.helper.FieldAccessBean;
import ro.ldir.dto.helper.NonComparableField;
import ro.ldir.dto.helper.NonTransferableField;

/**
 * Entity bean representing a garbage.
 */
@Entity
@XmlRootElement
public class Garbage extends FieldAccessBean {
	public enum GarbageStatus {
		CLEANED("Curățat"), IDENTIFIED("Identificat");
		private String translation;

		GarbageStatus(String translation) {
			this.translation = translation;
		}

		public String getTranslation() {
			return translation;
		}
	}

	public static final int DESCRIPTION_LENGTH = 20;
	public static final int DETAILS_LENGTH = 30;

	private int bagCount;
	private String bigComponentsDescription;
	private ChartedArea chartedArea;
	private CountyArea county;
	private String description;
	private String details;
	private boolean dispersed;
	private List<Team> enrolledCleaners;
	private GarbageGroup garbageGroup;
	private Integer garbageId;
	private User insertedBy;
	private int percentageGlass;
	private int percentageMetal;
	private int percentagePlastic;
	private int percentageWaste;
	private List<String> pictures = new ArrayList<String>();
	private Date recordDate;
	private GarbageStatus status;
	private TownArea town;
	private double x;
	private double y;

	public Garbage() {
	}

	/**
	 * @return the bagCount
	 */
	public int getBagCount() {
		return bagCount;
	}

	/**
	 * @return the bigComponentsDescription
	 */
	public String getBigComponentsDescription() {
		return bigComponentsDescription;
	}

	/**
	 * @return the chartedArea
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "CHARTAREAID")
	@XmlIDREF
	public ChartedArea getChartedArea() {
		return chartedArea;
	}

	/**
	 * @return the county
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "COUNTYID")
	@NotNull
	@XmlIDREF
	public CountyArea getCounty() {
		return county;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the details
	 */
	public String getDetails() {
		return details;
	}

	/**
	 * @return the enrolledCleaners
	 */
	@ManyToMany
	@JoinTable(name = "GARBAGE_TEAM", joinColumns = @JoinColumn(name = "GARBAGEID", referencedColumnName = "GARBAGEID"), inverseJoinColumns = @JoinColumn(name = "TEAMID", referencedColumnName = "TEAMID"))
	@XmlIDREF
	public List<Team> getEnrolledCleaners() {
		return enrolledCleaners;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "GARBAGEGROUPID")
	@NonComparableField
	public GarbageGroup getGarbageGroup() {
		return garbageGroup;
	}

	/**
	 * @return the garbageId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer getGarbageId() {
		return garbageId;
	}

	/**
	 * @return the insertedBy
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE,
			CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "INSERTEDBY")
	@XmlIDREF
	public User getInsertedBy() {
		return insertedBy;
	}

	/**
	 * @return the percentageGlass
	 */
	public int getPercentageGlass() {
		return percentageGlass;
	}

	/**
	 * @return the percentageMetal
	 */
	public int getPercentageMetal() {
		return percentageMetal;
	}

	/**
	 * @return the percentagePlastic
	 */
	public int getPercentagePlastic() {
		return percentagePlastic;
	}

	/**
	 * @return the percentageWaste
	 */
	public int getPercentageWaste() {
		return percentageWaste;
	}

	/**
	 * @return the pictures
	 */
	public List<String> getPictures() {
		return pictures;
	}

	/**
	 * @return the recordDate
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@NonComparableField
	public Date getRecordDate() {
		return recordDate;
	}

	/**
	 * @return the status
	 */
	public GarbageStatus getStatus() {
		return status;
	}

	/**
	 * @return the town
	 */
	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "TOWNID")
	@XmlIDREF
	public TownArea getTown() {
		return town;
	}

	/**
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * @return the dispersed
	 */
	public boolean isDispersed() {
		return dispersed;
	}

	/**
	 * @param bagCount
	 *            the bagCount to set
	 */
	public void setBagCount(int bagCount) {
		this.bagCount = bagCount;
	}

	/**
	 * @param bigComponentsDescription
	 *            the bigComponentsDescription to set
	 */
	public void setBigComponentsDescription(String bigComponentsDescription) {
		this.bigComponentsDescription = bigComponentsDescription;
	}

	/**
	 * @param chartedArea
	 *            the chartedArea to set
	 */
	@NonTransferableField
	public void setChartedArea(ChartedArea chartedArea) {
		this.chartedArea = chartedArea;
	}

	/**
	 * @param county
	 *            the county to set
	 */
	@NonTransferableField
	public void setCounty(CountyArea county) {
		this.county = county;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param details
	 *            the details to set
	 */
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * @param dispersed
	 *            the dispersed to set
	 */
	public void setDispersed(boolean dispersed) {
		this.dispersed = dispersed;
	}

	/**
	 * @param enrolledCleaners
	 *            the enrolledCleaners to set
	 */
	@NonTransferableField
	public void setEnrolledCleaners(List<Team> enrolledCleaners) {
		this.enrolledCleaners = enrolledCleaners;
	}

	@NonTransferableField
	public void setGarbageGroup(GarbageGroup garbageGroup) {
		this.garbageGroup = garbageGroup;
	}

	/**
	 * @param garbageId
	 *            the garbageId to set
	 */
	@NonTransferableField
	public void setGarbageId(Integer garbageId) {
		this.garbageId = garbageId;
	}

	/**
	 * @param insertedBy
	 *            the insertedBy to set
	 */
	@NonTransferableField
	public void setInsertedBy(User insertedBy) {
		this.insertedBy = insertedBy;
	}

	/**
	 * @param percentageGlass
	 *            the percentageGlass to set
	 */
	public void setPercentageGlass(int percentageGlass) {
		this.percentageGlass = percentageGlass;
	}

	/**
	 * @param percentageMetal
	 *            the percentageMetal to set
	 */
	public void setPercentageMetal(int percentageMetal) {
		this.percentageMetal = percentageMetal;
	}

	/**
	 * @param percentagePlastic
	 *            the percentagePlastic to set
	 */
	public void setPercentagePlastic(int percentagePlastic) {
		this.percentagePlastic = percentagePlastic;
	}

	/**
	 * @param percentageWaste
	 *            the percentageWaste to set
	 */
	public void setPercentageWaste(int percentageWaste) {
		this.percentageWaste = percentageWaste;
	}

	/**
	 * @param pictures
	 *            the pictures to set
	 */
	@NonTransferableField
	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	/**
	 * @param recordDate
	 *            the recordDate to set
	 */
	@NonTransferableField
	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	@NonTransferableField
	public void setStatus(GarbageStatus status) {
		this.status = status;
	}

	/**
	 * @param town
	 *            the town to set
	 */
	@NonTransferableField
	public void setTown(TownArea town) {
		this.town = town;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(double y) {
		this.y = y;
	}

	/** Set up the recordDate timestamp. */
	@PrePersist
	public void timestampRecord() {
		if (recordDate == null)
			recordDate = new Date();
	}
}
