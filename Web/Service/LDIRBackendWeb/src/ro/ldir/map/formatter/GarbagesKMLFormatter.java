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
	private String linkPattern = null;

	public GarbagesKMLFormatter(List<Garbage> garbages, String linkPattern) {
		this.garbages = garbages;
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
		for (Garbage garbage : garbages) {
			
			buf.append("<Placemark>\n");
			buf.append("<styleUrl>#" + garbage.getAllocatedStatus()
					+ "</styleUrl>\n");
			buf.append("<name>Garbage " + garbage.getGarbageId() + "</name>\n");
			buf.append("<description><![CDATA[");
			buf.append("<p>" + garbage.getDescription() + "</p>\n");
			
			if(garbage.getCounty().getName()!="TIMIS")
			{if (linkPattern != null) {
				buf.append("<p>"
						+ linkPattern.replaceAll("\\{\\{\\{ID\\}\\}\\}",
								garbage.getGarbageId().toString()) + "</p>\n");
			}};
			if(garbage.getCounty().getName()=="TIMIS")
			{if (linkPattern != null) {
				buf.append("<p>Alocarea pentru judetul Timis se face offline.  <a href=\"www.letsdoitromania.ro/contact\">Contactati</a> va rog echipa locala</p>\n");
			}};
			buf.append("<p>Saci alocati " + String.valueOf(garbage.getCountBagsEnrollments()) + " / "+String.valueOf(garbage.getBagCount())+"</p>\n");
			buf.append("<p>Judet: "+garbage.getCounty().getName()+"</p>\n");
			buf.append("]]></description>\n");
			buf.append("<Point><coordinates>" + garbage.getX() + ","
					+ garbage.getY() + "</coordinates></Point>\n");
			buf.append("</Placemark>\n");
		}
	}

	private void appendHeader() {
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
				+ "<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n"
				+ "<Document>\n");
		buf.append("<Style id=\"UNALLOCATED\">\n");
		buf.append("<IconStyle>\n");
		buf.append("<Icon>\n");
		buf.append("<href>http:////maps.gstatic.com/mapfiles/ms2/micons/red-dot.png</href>\n");
		buf.append("</Icon>\n");
		buf.append("</IconStyle>\n");
		buf.append("</Style>\n");
		buf.append("<Style id=\"PARTIALLY\">\n");
		buf.append("<IconStyle>\n");
		buf.append("<Icon>\n");
		buf.append("<href>http:////maps.gstatic.com/mapfiles/ms2/micons/purple.png</href>");
		buf.append("</Icon>\n");
		buf.append("</IconStyle>\n");
		buf.append("</Style>\n");
		buf.append("<Style id=\"COMPLETELY\">\n");
		buf.append("<IconStyle>\n");
		buf.append("<Icon>\n");
		buf.append("<href>http:////maps.gstatic.com/mapfiles/ms2/micons/yellow.png</href>");
		buf.append("</Icon>\n");
		buf.append("</IconStyle>\n");
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
