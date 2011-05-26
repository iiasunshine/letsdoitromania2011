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
 *  Filename: GarbagesKMLFormatter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.map.formatter;

import java.util.List;

import ro.ldir.dto.Garbage;

/**
 * Formats a list of garbages in KML.
 */
public class GarbagesKMLFormatter {
	private StringBuffer buf;
	private List<Garbage> garbages;

	public GarbagesKMLFormatter(List<Garbage> garbages) {
		this.garbages = garbages;
		buf = new StringBuffer();
		appendHeader();
		appendGarbages();
		appendFooter();
	}

	private void appendFooter() {
		buf.append("</Document>\n</kml>\n");
	}

	private void appendGarbages() {
		for (Garbage garbage : garbages) {
			buf.append("<Placemark>\n");
			buf.append("<name>Garbage " + garbage.getGarbageId() + "</name>\n");
			buf.append("<description>" + garbage.getDescription()
					+ "</description>\n");
			buf.append("<Point><coordinates>" + garbage.getX() + ","
					+ garbage.getY() + "</coordinates></Point>\n");
			buf.append("</Placemark>\n");
		}
	}

	private void appendHeader() {
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"
				+ "<Document>\n");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return buf.toString();
	}
}
