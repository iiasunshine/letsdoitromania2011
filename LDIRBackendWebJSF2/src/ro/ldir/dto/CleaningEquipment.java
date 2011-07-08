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
 *  Filename: CleaningEquipment.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The cleaning equipment of a team.
 */
@Entity
@XmlRootElement
@DiscriminatorValue("CleaningEquipment")
public class CleaningEquipment extends Equipment {
	public enum CleaningType {
		BAGS, GLOVES
	}

	private CleaningType cleaningType;

	/**
	 * @return the cleaningType
	 */
	public CleaningType getCleaningType() {
		return cleaningType;
	}

	/**
	 * @param cleaningType
	 *            the cleaningType to set
	 */
	public void setCleaningType(CleaningType cleaningType) {
		this.cleaningType = cleaningType;
	}
}
