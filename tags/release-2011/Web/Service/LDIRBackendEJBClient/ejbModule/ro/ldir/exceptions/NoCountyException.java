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
 *  Filename: NoCountyException.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.exceptions;

import ro.ldir.dto.Garbage;

/**
 * Class used to denote that there is no county containing a given pair of
 * geographical coordinates.
 */
public class NoCountyException extends Exception {

	private static final long serialVersionUID = 1L;

	public NoCountyException() {
	}

	public NoCountyException(Garbage garbage) {
		super("No county found for X=" + garbage.getX() + ", Y="
				+ garbage.getY());
	}

	/**
	 * @param arg0
	 */
	public NoCountyException(String arg0) {
		super(arg0);
	}
}
