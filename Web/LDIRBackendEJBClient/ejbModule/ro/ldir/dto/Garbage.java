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

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
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
	public enum AllocatedStatus {
		COMPLETELY("COMPLETELY"), PARTIALLY("PARTIALLY"), UNALLOCATED(
				"UNALLOCATED");
		private String translation;

		AllocatedStatus(String translation) {
			this.translation = translation;
		}

		public String getTranslation() {
			return translation;
		}
	}

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
	
	public enum trashOutGarbageTypes {
		household("Menajer"), 
		automotive("Auto"),
		construction("Constructii"),
		plastic("Plastic"),
		electronic("Sticlă"),
		metal("Metal"),
		liquid("Lichide"),
		dangerous("Periculoase");
		
		private String translation;

		trashOutGarbageTypes(String translation) {
			this.translation = translation;
		}

		public String getTranslation() {
			return translation;
		}
	}

	private static final double CIRCLE_INCREMENT = 0.1;
	public static final int DESCRIPTION_LENGTH = 20;

	public static final int DETAILS_LENGTH = 30;
	private int bagCount;
	private int accuracy;
	private int trashOutSize;
	private String trashOutTypes;
	private String trashOutImageUrls;
	
	private String bigComponentsDescription;
	private ChartedArea chartedArea;
	private CountyArea county;
	private String description;
	private String details;
	private boolean dispersed;
	private List<GarbageEnrollment> garbageEnrollements = new ArrayList<GarbageEnrollment>();
	private GarbageGroup garbageGroup;
	private Integer garbageId;
	private User insertedBy;
	private String name;
	private int percentageGlass;
	private int percentageMetal;
	private int percentagePlastic;
	private int percentageWaste;
	private List<String> pictures = new ArrayList<String>();
	private List<Point2D.Double> polyline;
	private Double radius;
	private Date recordDate;
	private GarbageStatus status;
	private boolean toClean;
	private boolean toVote;
	private TownArea town;
	private Set<GarbageVote> votes = new HashSet<GarbageVote>();
	private double x;

	private double y;

	public Garbage() {
	}

	private void buildRadius() {
		return;
//		if (radius == null)
//			return;
//
//		polyline = new ArrayList<Point2D.Double>();
//
//		double error = -radius;
//		double x = radius, y = 0;
//		while (x > 0) {
//			polyline.add(new Point2D.Double(this.x + x, this.y + y));
//			error += x;
//			x -= CIRCLE_INCREMENT;
//			error += x;
//			if (error >= 0) {
//				error -= y;
//				y += CIRCLE_INCREMENT;
//				error -= y;
//			}
//		}
//
//		error = -radius + CIRCLE_INCREMENT;
//		x = CIRCLE_INCREMENT;
//		y = radius;
//		while (y > 0) {
//			polyline.add(new Point2D.Double(this.x - x, this.y + y));
//			error += y;
//			y -= CIRCLE_INCREMENT;
//			error += y;
//			if (error >= 0) {
//				error -= x;
//				x += CIRCLE_INCREMENT;
//				error -= x;
//			}
//		}
//
//		error = -radius - CIRCLE_INCREMENT;
//		x = -radius;
//		y = -CIRCLE_INCREMENT;
//		while (x < 0) {
//			polyline.add(new Point2D.Double(this.x + x, this.y + y));
//			error -= y;
//			y -= CIRCLE_INCREMENT;
//			error -= y;
//			if (error >= 0) {
//				error += x;
//				x += CIRCLE_INCREMENT;
//				error += x;
//			}
//		}
//
//		error = radius;
//		x = 0;
//		y = -radius;
//		while (y < -CIRCLE_INCREMENT) {
//			polyline.add(new Point2D.Double(this.x + x, this.y + y));
//			error += x;
//			x += CIRCLE_INCREMENT;
//			error += x;
//			if (error >= 0) {
//				error -= y;
//				y += CIRCLE_INCREMENT;
//				error -= y;
//			}
//		}
	}

	@Transient
	@NonComparableField
	public AllocatedStatus getAllocatedStatus() {
		int totalBags = 0;
		for (GarbageEnrollment enrollment : garbageEnrollements)
			totalBags += enrollment.getAllocatedBags();
		if (totalBags == 0)
			return AllocatedStatus.UNALLOCATED;
		if (totalBags == bagCount)
			return AllocatedStatus.COMPLETELY;
		return AllocatedStatus.PARTIALLY;
	}
	
	@Transient
	@NonComparableField
	public int getAllocatedTeams() {
		int totalTeams = 0;
		for (GarbageEnrollment enrollment : garbageEnrollements)
			totalTeams += 1;
	return totalTeams;
	}
	
	@Transient
	@NonComparableField
	public int getAllocatedVolunteers() {
		int totalVolunteers = 0;
		for (GarbageEnrollment enrollment : garbageEnrollements)
			totalVolunteers += enrollment.getTeam().countMembers();
	return totalVolunteers;
	}


	/**
	 * @return the bagCount
	 */
	public int getBagCount() {
		return bagCount;
	}

	
	/**
	 * @return the gps accuracy
	 */
	public int getAccuracy() {
		return accuracy;
	}
	
	/**
	 * @return the size for slovak mobile app.
	 * Possible values: 1,2,3
	 */
	public int getTrashOutSize() {
		return trashOutSize;
	}
	
	/**
	 * @return the types of trash for slovak mobile app
	 * Possible values: 1,2,3
	 */

	public String getTrashOutTypes() {
		return trashOutTypes;
	}	
	
	/**
	 * @return the url of images of trash for slovak mobile app
	 * Possible values: 1,2,3
	 */
	public String getTrashOutImageUrls() {
		return trashOutImageUrls;
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

	@Transient
	public int getCountBagsEnrollments() {

		int suma = 0;
		for (GarbageEnrollment ge : garbageEnrollements) {
			suma = suma + ge.getAllocatedBags();
		}
		return suma;
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
	 * @return the garbageEnrollements
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "garbage")
	@NonComparableField
	@XmlTransient
	public List<GarbageEnrollment> getGarbageEnrollements() {
		return garbageEnrollements;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "GARBAGEGROUPID")
	@NonComparableField
	@XmlIDREF
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

	public String getName() {
		return name;
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

	public List<Point2D.Double> getPolyline() {
		return polyline;
	}

	public Double getRadius() {
		return radius;
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

	@Transient
	@NonComparableField
	public long getVoteCount() {
		return getVotes().size();
	}

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "garbage", orphanRemoval = true)
	@NonComparableField
	@XmlTransient
	public Set<GarbageVote> getVotes() {
		return votes;
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

	public boolean isToClean() {
		return toClean;
	}

	public boolean isToVote() {
		return toVote;
	}

	@PrePersist
	public void onPrePersist() {
		timestampRecord();
		buildRadius();
	}

	// TODO check whether this must be set
	@NonTransferableField
	public void setAllocatedStatus(AllocatedStatus status) {
	}

	/**
	 * @param bagCount
	 *            the bagCount to set
	 */
	public void setBagCount(int bagCount) {
		this.bagCount = bagCount;
	}
	
	/**
	 * @param accuracy
	 *            the gps Accuracy to set
	 */
	public void setAccuracy(int accuracy) {
		this.accuracy = accuracy;
	}
	
	/**
	 * @param the size for slovak mobile app.
	 * Possible values: 1,2,3
	 */
	public void setTrashOutSize(int trashOutSize) {
		this.trashOutSize=trashOutSize;
	}
	
	/**
	 * @param the types of trash for slovak mobile app
	 */
	
	public void setTrashOutTypes(String trashOutTypes) {
		this.trashOutTypes=trashOutTypes;
	}
	
	public void setTrashOutImageUrls(String trashOutImageUrls) {
		this.trashOutImageUrls=trashOutImageUrls;
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
	 * @param garbageEnrollemnts
	 *            the garbageEnrollements to set
	 */
	@NonTransferableField
	public void setGarbageEnrollements(
			List<GarbageEnrollment> garbageEnrollements) {
		this.garbageEnrollements = garbageEnrollements;
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

	public void setName(String name) {
		this.name = name;
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

	public void setPolyline(List<Point2D.Double> polyline) {
		this.polyline = polyline;
	}

	public void setRadius(Double radius) {
		this.radius = radius;
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

	@NonTransferableField
	public void setToClean(boolean toClean) {
		this.toClean = toClean;
	}

	@NonTransferableField
	public void setToVote(boolean toVote) {
		this.toVote = toVote;
	}

	/**
	 * @param town
	 *            the town to set
	 */
	@NonTransferableField
	public void setTown(TownArea town) {
		this.town = town;
	}

	@NonTransferableField
	public void setVoteCount(long voteCount) {

	}

	@NonTransferableField
	public void setVotes(long votes) {
	}

	@NonTransferableField
	public void setVotes(Set<GarbageVote> votes) {
		this.votes = votes;
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

	private void timestampRecord() {
		if (recordDate == null)
			recordDate = new Date();
	}
}
