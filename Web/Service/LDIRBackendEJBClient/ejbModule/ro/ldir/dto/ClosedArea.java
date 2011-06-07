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
 *  Filename: ClosedArea.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto;

import java.awt.geom.Point2D;
import java.util.List;
import java.util.logging.Logger;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import ro.ldir.dto.adapters.IntegerAdapter;
import ro.ldir.dto.helper.FieldAccessBean;

/**
 * A class representing an closed polyline.
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "AREATYPE", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("ClosedArea")
@XmlRootElement
public abstract class ClosedArea extends FieldAccessBean {
	private static Logger log = Logger.getLogger(ClosedArea.class.getName());

	private Integer areaId;

	/** The closed polyline defining this closeding area. */
	private List<Point2D.Double> polyline;

	/** The coordinates of the bounding box, used to speed up queries. */
	private double topLeftX, bottomRightX, topLeftY, bottomRightY;

	public ClosedArea() {
	}

	/**
	 * Checks whether this closed surface encloses a point.
	 * 
	 * @param point
	 *            The point to test against.
	 * @return {@code true} if the area encloses the point, false otherwise.
	 */
	public boolean containsPoint(Point2D.Double point) {
		boolean inside = false;
		Point2D.Double pi, pj;
		for (int i = 0, j = polyline.size() - 1; i < polyline.size(); j = i++) {
			pi = polyline.get(i);
			pj = polyline.get(j);
			if (((pi.y <= point.y && point.y < pj.y) || (pj.y <= point.y && point.y < pi.y))
					&& point.x < (pj.x - pi.x) * (point.y - pi.y)
							/ (pj.y - pi.y) + pi.x)
				inside = !inside;
		}
		return inside;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof ClosedArea))
			return false;
		if (areaId == null)
			return super.equals(obj);
		return areaId.equals(((ClosedArea) obj).areaId);
	}

	/**
	 * @return the areaId
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@XmlID
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	public Integer getAreaId() {
		return areaId;
	}

	/**
	 * @return the bottomRightX
	 */
	public double getBottomRightX() {
		return bottomRightX;
	}

	/**
	 * @return the bottomRightY
	 */
	public double getBottomRightY() {
		return bottomRightY;
	}

	/**
	 * @return the polyline
	 */
	public List<Point2D.Double> getPolyline() {
		return polyline;
	}

	/**
	 * @return the topLeftX
	 */
	public double getTopLeftX() {
		return topLeftX;
	}

	/**
	 * @return the topLeftY
	 */
	public double getTopLeftY() {
		return topLeftY;
	}

	/**
	 * @param areaId
	 *            the areaId to set
	 */
	public void setAreaId(Integer areaId) {
		this.areaId = areaId;
	}

	/**
	 * @param bottomRightX
	 *            the bottomRightX to set
	 */
	public void setBottomRightX(double bottomRightX) {
		this.bottomRightX = bottomRightX;
	}

	/**
	 * @param bottomRightY
	 *            the bottomRightY to set
	 */
	public void setBottomRightY(double bottomRightY) {
		this.bottomRightY = bottomRightY;
	}

	/** Sets the bounding box of this closeding area using the inner polyline. */
	public void setBoundingBox() {
		if (polyline == null)
			return;
		Point2D.Double first = polyline.get(0);
		topLeftX = bottomRightX = first.x;
		topLeftY = bottomRightY = first.y;
		for (Point2D.Double point : polyline) {
			if (topLeftX > point.x)
				topLeftX = point.x;
			if (topLeftY < point.y)
				topLeftY = point.y;
			if (bottomRightX < point.x)
				bottomRightX = point.x;
			if (bottomRightY > point.y)
				bottomRightY = point.y;
		}
	}

	/**
	 * @param polyline
	 *            the polyline to set
	 */
	public void setPolyline(List<Point2D.Double> polyline) {
		this.polyline = polyline;
		log.finer("Set bounding box for a closed area.");
	}

	/**
	 * @param topLeftX
	 *            the topLeftX to set
	 */
	public void setTopLeftX(double topLeftX) {
		this.topLeftX = topLeftX;
	}

	/**
	 * @param topLeftY
	 *            the topLeftY to set
	 */
	public void setTopLeftY(double topLeftY) {
		this.topLeftY = topLeftY;
	}
}
