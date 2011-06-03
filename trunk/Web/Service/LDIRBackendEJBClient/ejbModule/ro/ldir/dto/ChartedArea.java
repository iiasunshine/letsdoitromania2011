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
 *  Filename: ChartedArea.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import ro.ldir.dto.helper.NonTransferableField;

/**
 * A class representing an area to be charted.
 */
@Entity
@XmlRootElement
@DiscriminatorValue("ChartedArea")
public class ChartedArea extends ClosedArea {
	private double area;
	private Set<Team> chartedBy;
	private String county;
	private String description;
	private Set<Garbage> garbages = new HashSet<Garbage>();
	private String name;
	private double perimeter;
	private int score;

	public ChartedArea() {
	}

	/**
	 * @return the area
	 */
	public double getArea() {
		return area;
	}

	/**
	 * @return the closededBy
	 */
	@ManyToMany
	@JoinTable(name = "CHARTAREA_TEAM", joinColumns = @JoinColumn(name = "CHARTAREAID", referencedColumnName = "AREAID"), inverseJoinColumns = @JoinColumn(name = "TEAMID", referencedColumnName = "TEAMID"))
	@XmlIDREF
	public Set<Team> getChartedBy() {
		return chartedBy;
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the garbages
	 */
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "chartedArea")
	@XmlIDREF
	public Set<Garbage> getGarbages() {
		return garbages;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the perimeter
	 */
	public double getPerimeter() {
		return perimeter;
	}

	/**
	 * @return the score
	 */
	public int getScore() {
		return score;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(double area) {
		this.area = area;
	}

	/**
	 * @param chartedBy
	 *            the chartedBy to set
	 */
	public void setChartedBy(Set<Team> chartedBy) {
		this.chartedBy = chartedBy;
	}

	/**
	 * @param county
	 *            the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param garbages
	 *            the garbages to set
	 */
	@NonTransferableField
	public void setGarbages(Set<Garbage> garbages) {
		this.garbages = garbages;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param perimeter
	 *            the perimeter to set
	 */
	public void setPerimeter(double perimeter) {
		this.perimeter = perimeter;
	}

	/**
	 * @param score
	 *            the score to set
	 */
	public void setScore(int score) {
		this.score = score;
	}
}
