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
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlID;
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
		CHART("chart"), CLEAN("clean");

		private String restName;

		private Activity(String restName) {
			this.restName = restName;
		}

		public String getRestName() {
			return restName;
		}
	}

	// public enum CleaningTools {
	// BAGS, GLOVES
	// }

	@XmlType(name = "UserStatus")
	public enum UserStatus {
		REGISTERED("registered"), PENDING("pending"), SUSPENDED("suspended");

		private String restName;

		private UserStatus(String restName) {
			this.restName = restName;
		}

		public String getRestName() {
			return restName;
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

	public enum SecurityRole {
		ADMIN("admin"), ORGANIZER("organizer"), ORGANIZER_MULTI(
				"organizer_multi"), VOLUNTEER("volunteer"), VOLUNTEER_MULTI(
				"volunteer_multi");

		private String restName;

		private SecurityRole(String restName) {
			this.restName = restName;
		}

		public String getRestName() {
			return restName;
		}
	}

	private static final long serialVersionUID = 1L;

	@NonTransferableField
	public List<Activity> activities;

	@Temporal(TemporalType.TIME)
	public Date birthday;

	@Column(unique = true, nullable = false)
	public String email;

	public String firstName;

	// @OneToMany(mappedBy = "insertBy")
	// @XmlIDREF
	// @NonTransferableField
	// public Collection<Garbage> garbages;

	public String lastName;

	@Column(nullable = false, length = 64)
	@XmlJavaTypeAdapter(EncryptAdapter.class)
	public String passwd;

	@Column(length = 64)
	public String registrationToken;

	public String phone;

	@NonTransferableField
	public UserStatus status;

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

	public String town;
	
	public String county;

	@Column(nullable = false)
	@NonTransferableField
	public String role;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@NonTransferableField
	public Integer userId;

	public User() {
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
