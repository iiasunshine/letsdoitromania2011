/**
 *  This file is part of the LDIRBackend - the backend for the Let's Do It
 *  Romania 2011 Garbage collection campaign.
 *  Copyright (C) 2012 by the LDIR development team, further referred to 
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
 *  Filename: GarbageVote.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import ro.ldir.dto.helper.FieldAccessBean;

@Entity
public class GarbageVote extends FieldAccessBean {
	private Garbage garbage;
	private Integer garbageVoteId;
	private String ip;
	private Date timestamp;
	private User user;

	public GarbageVote() {
	}

	public GarbageVote(Garbage garbage, String ip) {
		this.garbage = garbage;
		this.ip = ip;
	}

	public GarbageVote(Garbage garbage, User user) {
		this.garbage = garbage;
		this.user = user;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "GARBAGEID")
	@NotNull
	public Garbage getGarbage() {
		return garbage;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getGarbageVoteId() {
		return garbageVoteId;
	}

	public String getIp() {
		return ip;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getTimestamp() {
		return timestamp;
	}

	@ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "USERID")
	public User getUser() {
		return user;
	}

	public void setGarbage(Garbage garbage) {
		this.garbage = garbage;
	}

	public void setGarbageVoteId(Integer garbageVoteId) {
		this.garbageVoteId = garbageVoteId;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@PrePersist
	public void timestampRecord() {
		if (timestamp == null)
			timestamp = new Date();
	}
}
