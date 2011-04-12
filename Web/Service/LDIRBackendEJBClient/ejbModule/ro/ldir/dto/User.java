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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;

/**
 * The entity bean describing a user. Objects of this type are persisted in the
 * database and send via JSON / XML to clients of the webservices.
 */
@Entity
@XmlRootElement
public class User implements Serializable {

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

	public enum CleaningTools {
		BAGS, GLOVES
	}

	@XmlType(name = "UserStatus")
	public enum Status {
		REGISTERED, SUSPENDED, UNCONFIRMED
	}

	public enum Transport {
		BIKE, CAR, PUBLIC
	}

	public enum Type {
		ORGANIZATION("organization"), PERSON("person"), PUBLIC_INSTITITUION(
				"public_institution");
		private String restName;

		private Type(String restName) {
			this.restName = restName;
		}

		public String getRestName() {
			return restName;
		}
	}

	private static final long serialVersionUID = 1L;

	private static String sha256Encrypt(String toEnc) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(toEnc.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digest.length; i++) {
			String hex = Integer.toHexString(0xff & digest[i]);
			if (hex.length() == 1)
				sb.append('0');
			sb.append(hex);
		}
		return sb.toString();
	}

	public List<Activity> activities;

	@Temporal(TemporalType.TIME)
	public Date birthday;

	public List<CleaningTools> cleaningTools;

	@Column(unique = true, nullable = false)
	public String email;

	public String firstName;

	@OneToMany(mappedBy = "insertBy")
	@XmlIDREF
	public Collection<Garbage> garbages;

	public boolean hasGPS;

	public String lastName;

	public int membersNumber;

	public String organizationName;

	@Column(nullable = false, length = 64)
	private String passwd;

	public String phone;

	public String role;

	public Status status;

	@ManyToMany
	@JoinTable(name = "USER_TEAM", joinColumns = @JoinColumn(name = "USERID", referencedColumnName = "USERID"), inverseJoinColumns = @JoinColumn(name = "TEAMID", referencedColumnName = "TEAMID"))
	@XmlIDREF
	public Collection<Team> teams;

	@OneToMany(mappedBy = "leader")
	@XmlIDREF
	public Collection<Team> teamsLed;

	public String town;

	public List<Transport> transport;

	public Type type;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer userId;

	public User() {
	}

	public void setPassword(String password) {
		this.passwd = sha256Encrypt(password);
	}

	public boolean testPassword(String md5password) {
		return this.passwd.equals(md5password);
	}

	public void update(User user) {
		birthday = user.birthday;
		cleaningTools = user.cleaningTools;
		firstName = user.firstName;
		hasGPS = user.hasGPS;
		lastName = user.lastName;
		phone = user.phone;
		// TODO complete reinitialization
	}
}
