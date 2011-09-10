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
 *  Filename: GarbageGroup.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;
import ro.ldir.dto.helper.FieldAccessBean;
import ro.ldir.dto.helper.NonTransferableField;

/** An entity bean representing a group of garbages. */
@Entity
@XmlRootElement
public class GarbageGroup extends FieldAccessBean {
	/**
	 * If two garbage tags are closer than this constant, they should go inside
	 * the same group.
	 */
	public static final double JOIN_RANGE = .02;
	/**
	 * The maximum numbers of garbage tags inside a group. Beyond this value,
	 * neither new garbages are added to the group, nor group merges are
	 * allowed.
	 */
	public static final int MAX_GROUP_SIZE = 1000;
	/**
	 * The {@code garbageCount} field is not stored in the database. Rather,
	 * it's used during serialization to XML/JSON format.
	 */
	private Integer garbageCount;
	private Integer garbageGroupId;
	private List<Garbage> garbages;
	private double radius;
	private double x;
	private double y;

	@PostLoad
	public void computeCount() {
		garbageCount = garbages.size();
	}

	/**
	 * @return the garbageCount
	 */
	@Transient
	public Integer getGarbageCount() {
		return garbageCount;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer getGarbageGroupId() {
		return garbageGroupId;
	}

	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, mappedBy = "garbageGroup")
	@XmlIDREF
	public List<Garbage> getGarbages() {
		return garbages;
	}

	public double getRadius() {
		return radius;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public boolean merge(GarbageGroup other, int max_group_size) {
		if (garbages != null && other.garbages != null
				&& garbages.size() + other.garbages.size() > max_group_size)
			return false;

		if (garbages == null)
			garbages = new ArrayList<Garbage>();
		garbages.addAll(other.garbages);
		for (Garbage garbage : other.garbages)
			garbage.setGarbageGroup(this);
		return true;
	}

	@PrePersist
	@PreUpdate
	public void recomputeProperties() {
		x = 0;
		y = 0;
		for (Garbage garbage : garbages) {
			x += garbage.getX();
			y += garbage.getY();
		}
		x /= garbages.size();
		y /= garbages.size();

		radius = 0;
		for (Garbage garbage : garbages) {
			double tmp = (garbage.getX() - x) * (garbage.getX() - x)
					+ (garbage.getY() - y) * (garbage.getY() - y);
			if (tmp > radius)
				radius = tmp;
		}
	}

	/**
	 * @param garbageCount
	 *            the garbageCount to set
	 */
	@NonTransferableField
	public void setGarbageCount(Integer garbageCount) {
		this.garbageCount = garbageCount;
	}

	public void setGarbageGroupId(Integer garbageGroupId) {
		this.garbageGroupId = garbageGroupId;
	}

	public void setGarbages(List<Garbage> garbages) {
		this.garbages = garbages;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}
}
