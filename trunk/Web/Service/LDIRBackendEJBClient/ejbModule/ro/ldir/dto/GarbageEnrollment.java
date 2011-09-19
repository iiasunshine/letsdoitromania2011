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
 *  Filename: GarbageEnrollment.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;
import ro.ldir.dto.helper.FieldAccessBean;
import ro.ldir.dto.helper.NonTransferableField;

/** An entity denoting the enrollment of a team to clean a garbage. */
@Entity
@XmlRootElement
@Table(name = "GARBAGE_TEAM")
public class GarbageEnrollment extends FieldAccessBean {
	private int allocatedBags;
	private Garbage garbage;
	private Integer garbageEnrollmentId;
	private Team team;

	public GarbageEnrollment() {

	}

	public GarbageEnrollment(int allocatedBags, Garbage garbage, Team team) {
		this.allocatedBags = allocatedBags;
		this.garbage = garbage;
		this.team = team;
	}

	/**
	 * @return the allocatedBags
	 */
	public int getAllocatedBags() {
		return allocatedBags;
	}

	/**
	 * @return the garbage
	 */
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, optional = false)
	@XmlIDREF
	@JoinColumn(name = "GARBAGEID")
	public Garbage getGarbage() {
		return garbage;
	}

	/**
	 * @return the garbageEnrollmentId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer getGarbageEnrollmentId() {
		return garbageEnrollmentId;
	}

	/**
	 * @return the team
	 */
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE,
			CascadeType.PERSIST, CascadeType.REFRESH }, optional = false)
	@XmlIDREF
	@JoinColumn(name = "TEAMID")
	public Team getTeam() {
		return team;
	}

	/**
	 * @param allocatedBags
	 *            the allocatedBags to set
	 */
	public void setAllocatedBags(int allocatedBags) {
		this.allocatedBags = allocatedBags;
	}

	/**
	 * @param garbage
	 *            the garbage to set
	 */
	@NonTransferableField
	public void setGarbage(Garbage garbage) {
		this.garbage = garbage;
	}

	/**
	 * @param garbageEnrollmentId
	 *            the garbageEnrollmentId to set
	 */
	@NonTransferableField
	public void setGarbageEnrollmentId(Integer garbageEnrollmentId) {
		this.garbageEnrollmentId = garbageEnrollmentId;
	}

	/**
	 * @param team
	 *            the team to set
	 */
	@NonTransferableField
	public void setTeam(Team team) {
		this.team = team;
	}
}
