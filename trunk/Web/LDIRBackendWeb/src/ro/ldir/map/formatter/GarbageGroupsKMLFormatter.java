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

import ro.ldir.dto.GarbageGroup;

/**
 * Formats a list of garbage groups in KML.
 */
public class GarbageGroupsKMLFormatter {
	private StringBuffer buf;
	private List<GarbageGroup> garbageGroups;
	private String linkPattern = null;

	public GarbageGroupsKMLFormatter(List<GarbageGroup> garbageGroups,
			String linkPattern) {
		this.garbageGroups = garbageGroups;
		this.linkPattern = linkPattern;
		buf = new StringBuffer();
		appendHeader();
		appendGarbages();
		appendFooter();
	}

	private void appendFooter() {
		buf.append("</Document>\n</kml>\n");
	}

	private void appendGarbages() {
		for (GarbageGroup garbageGroup : garbageGroups) {
			buf.append("<Placemark>\n");
			buf.append("<styleUrl>#recycle</styleUrl>\n");
			buf.append("<name>Garbage group "
					+ garbageGroup.getGarbageGroupId() + "</name>\n");
			buf.append("<description><![CDATA[");
			buf.append("<p> Grup de " + garbageGroup.getGarbageCount()
					+ " gunoaie</p>\n");
			if (linkPattern != null)
				buf.append("<p>"
						+ linkPattern.replaceAll("\\{\\{\\{ID\\}\\}\\}",
								garbageGroup.getGarbageGroupId().toString())
						+ "</p>");
			buf.append("]]></description>\n");
			buf.append("<Point><coordinates>" + garbageGroup.getX() + ","
					+ garbageGroup.getY() + "</coordinates></Point>\n");
			buf.append("</Placemark>\n");
		}
	}

	private void appendHeader() {
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"
				+ "<Document>\n");
		buf.append("<Style id=\"recycleStyle\">\n");
		buf.append("<IconStyle><Icon>\n");
		buf.append("<href>http://maps.google.com/mapfiles/ms/icons/recycle.png</href>\n");
		buf.append("<hotSpot x=\".5\" y=\".5\" xunits=\"fraction\" yunits=\"fraction\"></hotSpot>\n");
		buf.append("</Icon></IconStyle>\n");
		buf.append("</Style>\n");
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
