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
 *  Filename: Team.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;
import ro.ldir.dto.helper.FieldAccessBean;
import ro.ldir.dto.helper.NonComparableField;
import ro.ldir.dto.helper.NonTransferableField;

/**
 * The entity bean  representing a team.
 */
@Entity
@XmlRootElement
public class Team extends FieldAccessBean {
	private static final Integer DEFAULT_CLEANINGPOWER = 4;
	private Set<ChartedArea> chartedAreas;
	private Integer cleaningPower = DEFAULT_CLEANINGPOWER;
	private List<Equipment> equipments;
	private List<GarbageEnrollment> garbageEnrollements = new ArrayList<GarbageEnrollment>();
	private List<Organization> organizationMembers;
	private Integer teamId;
	private User teamManager;
	private String teamName;
	private List<User> volunteerMembers;

	public Team() {
	}

	/** Count the number of members belonging to the team. */
	public int countMembers() {
		int n = 0;
		if (volunteerMembers != null)
			n += volunteerMembers.size();
		if (organizationMembers != null)
			for (Organization org : organizationMembers)
				n += org.getMembersCount();
		return n;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Team))
			return false;
		if (teamId == null)
			return super.equals(obj);
		return teamId.equals(((Team) obj).teamId);
	}

	/**
	 * @return the chartedAreas
	 */
	@ManyToMany(mappedBy = "chartedBy")
	@XmlIDREF
	public Set<ChartedArea> getChartedAreas() {
		return chartedAreas;
	}

	/**
	 * @return the cleaningPower
	 */
	@Column(nullable = false, columnDefinition = "DEFAULT '4' NOT NULL")
	public Integer getCleaningPower() {
		return cleaningPower;
	}

	/**
	 * @return the equipments
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "teamOwner")
	public List<Equipment> getEquipments() {
		return equipments;
	}

	/**
	 * @return the garbageEnrollements
	 */
	@OneToMany(cascade = { CascadeType.ALL }, mappedBy = "team")
	@NonComparableField
	public List<GarbageEnrollment> getGarbageEnrollements() {
		return garbageEnrollements;
	}

	/**
	 * 
	 * @return A list of garbages inserted by all team members.
	 */
	@Transient
	@XmlIDREF
	public List<Garbage> getInsertedGarbages() {
		ArrayList<Garbage> result = new ArrayList<Garbage>();
		if (volunteerMembers == null)
			return result;
		for (User user : volunteerMembers) {
			Set<Garbage> userGarbages = user.getGarbages();
			if (userGarbages == null)
				continue;
			result.addAll(userGarbages);
		}
		return result;
	}

	/**
	 * @return the organizationMembers
	 */
	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "memberOf")
	public List<Organization> getOrganizationMembers() {
		return organizationMembers;
	}

	/**
	 * @return the teamId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer getTeamId() {
		return teamId;
	}

	/**
	 * @return the teamManager
	 */
	@ManyToOne(optional = false)
	@XmlIDREF
	public User getTeamManager() {
		return teamManager;
	}

	/**
	 * @return the teamName
	 */
	@Column(unique = true, nullable = false)
	public String getTeamName() {
		return teamName;
	}

	/**
	 * @return the volunteerMembers
	 */
	@OneToMany(cascade = CascadeType.MERGE, mappedBy = "memberOf")
	@XmlIDREF
	public List<User> getVolunteerMembers() {
		return volunteerMembers;
	}

	/**
	 * @param chartedAreas
	 *            the chartedAreas to set
	 */
	@NonTransferableField
	public void setChartedAreas(Set<ChartedArea> chartedAreas) {
		this.chartedAreas = chartedAreas;
	}

	/**
	 * @param cleaningPower
	 *            the cleaningPower to set
	 */
	public void setCleaningPower(Integer cleaningPower) {
		this.cleaningPower = cleaningPower;
	}

	/**
	 * @param equipments
	 *            the equipments to set
	 */
	@NonTransferableField
	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
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

	/**
	 * @param organizationMembers
	 *            the organizationMembers to set
	 */
	@NonTransferableField
	public void setOrganizationMembers(List<Organization> organizationMembers) {
		this.organizationMembers = organizationMembers;
	}

	/**
	 * @param teamId
	 *            the teamId to set
	 */
	@NonTransferableField
	public void setTeamId(Integer teamId) {
		this.teamId = teamId;
	}

	/**
	 * @param teamManager
	 *            the teamManager to set
	 */
	@NonTransferableField
	public void setTeamManager(User teamManager) {
		this.teamManager = teamManager;
	}

	/**
	 * @param teamName
	 *            the teamName to set
	 */
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	/**
	 * @param volunteerMembers
	 *            the volunteerMembers to set
	 */
	@NonTransferableField
	public void setVolunteerMembers(List<User> volunteerMembers) {
		this.volunteerMembers = volunteerMembers;
	}
}
