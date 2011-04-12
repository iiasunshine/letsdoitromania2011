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

import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;

/**
 * @author Stefan Guna
 * 
 */
@Entity
@XmlRootElement
public class Garbage implements Serializable {
	@XmlType(name = "GarbageStatus")
	public enum Status {
		CLEANED("cleaned"), IDENTIFIED("identified");

		private String restName;

		private Status(String restName) {
			this.restName = restName;
		}

		public String getRestName() {
			return restName;
		}
	}

	private static final long serialVersionUID = 1L;

	public String county;

	public String description;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer garbageId;

	@ManyToOne
	@JoinColumn(name = "INSERTBY", nullable = false)
	@XmlIDREF
	public User insertBy;

	public float latitude;

	public float longitude;

	// TODO change it to an arraylist
	public Collection<String> pictures;

	public Status status;

	public String town;

	public int volume;

	public Garbage() {
	}
}
