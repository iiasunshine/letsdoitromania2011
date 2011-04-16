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
 *  Filename: User.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.EncryptAdapter;
import ro.ldir.dto.adapters.IntegerAdapter;
import ro.ldir.dto.helper.FieldAccessBean;
import ro.ldir.dto.helper.NonTransferableField;
import ro.ldir.dto.helper.SHA256Encrypt;

/**
 * The entity bean describing a user. Objects of this type are persisted in the
 * database and send via JSON / XML to clients of the webservices.
 */
@Entity
@XmlRootElement
public class User extends FieldAccessBean implements Serializable {

	public enum Activity {
		CHART, CLEAN
	}

	// public enum CleaningTools {
	// BAGS, GLOVES
	// }

	public enum SecurityRole {
		ADMIN, ORGANIZER, ORGANIZER_MULTI, VOLUNTEER, VOLUNTEER_MULTI;
		private static List<SecurityRole> multiRoles = null;

		public static List<SecurityRole> getMultiRoles() {
			if (multiRoles != null)
				return multiRoles;
			multiRoles = new ArrayList<SecurityRole>();
			multiRoles.add(ORGANIZER_MULTI);
			multiRoles.add(VOLUNTEER_MULTI);
			return multiRoles;
		}
	}

	// public enum Transport {
	// BIKE, CAR, PUBLIC
	// }

	// public enum Type {
	// ORGANIZATION("organization"), PERSON("person"), PUBLIC_INSTITITUION(
	// "public_institution");
	// private String restName;
	//
	// private Type(String restName) {
	// this.restName = restName;
	// }
	//
	// public String getRestName() {
	// return restName;
	// }
	// }

	@XmlType(name = "UserStatus")
	public enum UserStatus {
		PENDING, REGISTERED, SUSPENDED;
	}

	private static final long serialVersionUID = 1L;
	private List<Activity> activities;
	private Date birthday;
	private String county;
	private String email;

	// @OneToMany(mappedBy = "insertBy")
	// @XmlIDREF
	// @NonTransferableField
	// public Collection<Garbage> garbages;

	private String firstName;
	private String lastName;
	private List<Team> managedTeams;
	private Team memberOf;
	private List<Organization> organizations;
	private String passwd;

	// @ManyToMany
	// @JoinTable(name = "USER_TEAM", joinColumns = @JoinColumn(name = "USERID",
	// referencedColumnName = "USERID"), inverseJoinColumns = @JoinColumn(name =
	// "TEAMID", referencedColumnName = "TEAMID"))
	// @XmlIDREF
	// @NonTransferableField
	// public Collection<Team> teams;
	//
	// @OneToMany(mappedBy = "leader")
	// @XmlIDREF
	// @NonTransferableField
	// public Collection<Team> teamsLed;

	private String phone;
	private String registrationToken;
	private String role;
	private UserStatus status;
	private String town;
	private Integer userId;

	public User() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof User))
			return false;
		if (userId == null)
			return super.equals(obj);
		return userId.equals(((User) obj).userId);
	}

	/**
	 * @return the activities
	 */
	public List<Activity> getActivities() {
		return activities;
	}

	/**
	 * @return the birthday
	 */
	@Temporal(TemporalType.TIME)
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @return the county
	 */
	public String getCounty() {
		return county;
	}

	/**
	 * @return the email
	 */
	@Column(unique = true, nullable = false)
	public String getEmail() {
		return email;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the managedTeams
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "teamManager", orphanRemoval = true)
	@XmlIDREF
	public List<Team> getManagedTeams() {
		return managedTeams;
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
	 * @return the organizations
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "contactUser", orphanRemoval = true)
	@XmlIDREF
	public List<Organization> getOrganizations() {
		return organizations;
	}

	/**
	 * @return the passwd
	 */
	@Column(nullable = false, length = 64)
	@XmlJavaTypeAdapter(EncryptAdapter.class)
	public String getPasswd() {
		return passwd;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return the registrationToken
	 */
	@Column(length = 64)
	public String getRegistrationToken() {
		return registrationToken;
	}

	/**
	 * @return the role
	 */
	@Column(nullable = false)
	public String getRole() {
		return role;
	}

	/**
	 * @return the status
	 */
	public UserStatus getStatus() {
		return status;
	}

	/**
	 * @return the town
	 */
	public String getTown() {
		return town;
	}

	/**
	 * @return the userId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer getUserId() {
		return userId;
	}

	/** Checks whether the role of this user allows multiple teams. */
	public boolean isMultiRole() {
		for (SecurityRole sr : SecurityRole.getMultiRoles())
			if (role.equals(sr.toString()))
				return true;
		return false;
	}

	/**
	 * @param activities
	 *            the activities to set
	 */
	@NonTransferableField
	public void setActivities(List<Activity> activities) {
		this.activities = activities;
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @param county
	 *            the county to set
	 */
	public void setCounty(String county) {
		this.county = county;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @param firstName
	 *            the firstName to set
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @param managedTeams
	 *            the managedTeams to set
	 */
	@NonTransferableField
	public void setManagedTeams(List<Team> managedTeams) {
		this.managedTeams = managedTeams;
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
	 * @param organizations
	 *            the organizations to set
	 */
	@NonTransferableField
	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	/**
	 * @param passwd
	 *            the passwd to set
	 */
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @param registrationToken
	 *            the registrationToken to set
	 */
	public void setRegistrationToken(String registrationToken) {
		this.registrationToken = registrationToken;
	}

	/**
	 * @param role
	 *            the role to set
	 */
	@NonTransferableField
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	@NonTransferableField
	public void setStatus(UserStatus status) {
		this.status = status;
	}

	/**
	 * @param town
	 *            the town to set
	 */
	public void setTown(String town) {
		this.town = town;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	@NonTransferableField
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	/**
	 * Tests the given password.
	 * 
	 * @param sha256Password
	 *            The password to test.
	 * @return {@code true} if the password matches.
	 */
	public boolean testPassword(String rawPassword) {
		return this.passwd.equals(SHA256Encrypt.encrypt(rawPassword));
	}
}
