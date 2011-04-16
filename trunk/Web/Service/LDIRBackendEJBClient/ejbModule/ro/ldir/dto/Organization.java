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
 *  Filename: Organization.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;
import ro.ldir.dto.helper.FieldAccessBean;
import ro.ldir.dto.helper.NonTransferableField;

/**
 * The entity bean describing an organization.
 */
@Entity
@XmlRootElement
public class Organization extends FieldAccessBean implements Serializable {

	public enum OrganizationType {
		CITY_HALL, COMPANY, LANDFILL, REGISTRATION_POINT, SCHOOL
	}

	private static final long serialVersionUID = 1L;;
	private String address;
	private User contactUser;
	private String county;
	private Team memberOf;
	private Integer membersCount;
	private String name;
	private Integer organizationId;
	private String town;
	private OrganizationType type;

	public Organization() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Organization))
			return false;
		return organizationId.equals(((Organization) obj).organizationId);
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the contactUser
	 */
	@ManyToOne(optional = false)
	@XmlIDREF
	public User getContactUser() {
		return contactUser;
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @return the memberOf
	 */
	@ManyToOne
	@XmlIDREF
	public Team getMemberOf() {
		return memberOf;
	}

	/**
	 * @return the membersCount
	 */
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer getMembersCount() {
		return membersCount;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the organizationId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer getOrganizationId() {
		return organizationId;
	}

	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}

	/**
	 * @return the type
	 */
	public OrganizationType getType() {
		return type;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param contactUser
	 *            the contactUser to set
	 */
	@NonTransferableField
	public void setContactUser(User contactUser) {
		this.contactUser = contactUser;
	}

	/**
	 * @param county
	 *            the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @param memberOf
	 *            the memberOf to set
	 */
	@NonTransferableField
	public void setMemberOf(Team memberOf) {
		this.memberOf = memberOf;
	}

	/**
	 * @param membersCount
	 *            the membersCount to set
	 */
	public void setMembersCount(Integer membersCount) {
		this.membersCount = membersCount;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param organizationId
	 *            the organizationId to set
	 */
	@NonTransferableField
	public void setOrganizationId(Integer organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * @param town
	 *            the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(OrganizationType type) {
		this.type = type;
	}
}
