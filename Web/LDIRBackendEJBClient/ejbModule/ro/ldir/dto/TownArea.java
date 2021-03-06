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
 *  Filename: TownArea.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;

import ro.ldir.dto.helper.NonTransferableField;

/**
 * Defines a town area.
 */
@Entity
@XmlRootElement
@DiscriminatorValue("TownArea")
public class TownArea extends ClosedArea {
	private double area;
	private String county;
	private Set<Garbage> garbages = new HashSet<Garbage>();
	private String name;

	public TownArea() {
	}

	/**
	 * @return the area
	 */
	public double getArea() {
		return area;
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @return the garbages
	 */
	@OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, mappedBy = "town")
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
	 * @param area
	 *            the area to set
	 */
	public void setArea(double area) {
		this.area = area;
	}

	/**
	 * @param county
	 *            the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
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
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
