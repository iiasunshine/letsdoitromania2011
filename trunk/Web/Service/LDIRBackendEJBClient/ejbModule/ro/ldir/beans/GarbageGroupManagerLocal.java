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
 *  Filename: GarbageGroupManagerLocal.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.beans;

import java.util.List;

import ro.ldir.dto.GarbageGroup;

/**
 * The local business interface of the garbage group manager.
 * 
 * @see ro.ldir.beans.GarbageGroupManager
 */
public interface GarbageGroupManagerLocal {
	/**
	 * Gets all the garbage groups that are located inside a bounding box.
	 * 
	 * @param topLeftX
	 *            The top left point X of the bounding box.
	 * @param topLeftY
	 *            The top left point Y of the bounding box.
	 * @param bottomRightX
	 *            The bottom right point X of the bounding box.
	 * @param bottomRightY
	 *            The bottom right point Y of the bounding box.
	 * @return A list of garbages inside the bounding box.
	 */
	public List<GarbageGroup> getGarbageGroups(double topLeftX,
			double topLeftY, double bottomRightX, double bottomRightY);

}
