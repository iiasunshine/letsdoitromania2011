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
 *  Filename: Equipment.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;
import ro.ldir.dto.helper.FieldAccessBean;
import ro.ldir.dto.helper.NonTransferableField;

/**
 * Denotes the equipments that a team may have.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "EQUIPMENTTYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("Equipment")
@XmlRootElement
@XmlSeeAlso({ CleaningEquipment.class, GpsEquipment.class,
		TransportEquipment.class })
public abstract class Equipment extends FieldAccessBean {
	private Integer equipmentId;
	private Team teamOwner;

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Equipment))
			return false;
		if (equipmentId == null)
			return super.equals(obj);
		return equipmentId.equals(((Equipment) obj).equipmentId);
	}

	/**
	 * @return the equipmentId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer getEquipmentId() {
		return equipmentId;
	}

	/**
	 * @return the teamOwner
	 */
	@ManyToOne(optional = false)
	@XmlIDREF
	@JoinColumn(name = "TEAMOWNER")
	public Team getTeamOwner() {
		return teamOwner;
	}

	/**
	 * @param equipmentId
	 *            the equipmentId to set
	 */
	@NonTransferableField
	public void setEquipmentId(Integer equipmentId) {
		this.equipmentId = equipmentId;
	}

	/**
	 * @param teamOwner
	 *            the teamOwner to set
	 */
	@NonTransferableField
	public void setTeamOwner(Team teamOwner) {
		this.teamOwner = teamOwner;
	}
}
