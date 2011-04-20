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
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;
import ro.ldir.dto.helper.FieldAccessBean;
import ro.ldir.dto.helper.NonTransferableField;

/**
 * Entity bean representing a garbage.
 */
@Entity
@XmlRootElement
public class Garbage extends FieldAccessBean {
	public enum GarbageStatus {
		CLEANED, IDENTIFIED
	}

	private ChartedArea chartedArea;
	private CountyArea county;
	private String description;
	private String details;
	private List<Team> enrolledCleaners;
	private Integer garbageId;
	private User insertedBy;
	private List<String> pictures = new ArrayList<String>();
	private GarbageStatus status;
	private TownArea town;
	private int volume;
	private float x;
	private float y;

	public Garbage() {
	}

	/**
	 * @return the chartedArea
	 */
	@ManyToOne
	@JoinColumn(name = "CHARTAREAID")
	@XmlIDREF
	public ChartedArea getChartedArea() {
		return chartedArea;
	}

	/**
	 * @return the county
	 */
	@ManyToOne
	@JoinColumn(name = "COUNTYID")
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
	@ManyToOne
	@JoinColumn(name = "INSERTEDBY")
	@XmlIDREF
	public User getInsertedBy() {
		return insertedBy;
	}

	/**
	 * Returns the number of pictures stored by this garbage.
	 * 
	 * @return The number of pictures.
	 */
	@Transient
	@XmlElement
	public int getPictureCount() {
		return pictures.size();
	}

	/**
	 * @return the pictures
	 */
	@XmlTransient
	public List<String> getPictures() {
		return pictures;
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
	@ManyToOne
	@JoinColumn(name = "TOWNID")
	@XmlIDREF
	public TownArea getTown() {
		return town;
	}

	/**
	 * @return the volume
	 */
	public int getVolume() {
		return volume;
	}

	/**
	 * @return the x
	 */
	public float getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public float getY() {
		return y;
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
	 * @param enrolledCleaners
	 *            the enrolledCleaners to set
	 */
	@NonTransferableField
	public void setEnrolledCleaners(List<Team> enrolledCleaners) {
		this.enrolledCleaners = enrolledCleaners;
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
	 * @param pictures
	 *            the pictures to set
	 */
	@NonTransferableField
	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}

	/**
	 * @param status
	 *            the status to set
	 */
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
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}

	/**
	 * @param x
	 *            the x to set
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * @param y
	 *            the y to set
	 */
	public void setY(float y) {
		this.y = y;
	}
}
