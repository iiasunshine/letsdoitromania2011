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

import java.awt.geom.Point2D;
import java.util.List;

import ro.ldir.dto.ChartedArea;

/**
 * Formats a list of charted areas in KML.
 */
public class ChartedAreasKMLFormatter {
	private StringBuffer buf;
	private List<ChartedArea> chartedAreas;
	private String linkPattern = null;

	public ChartedAreasKMLFormatter(List<ChartedArea> chartedAreas,
			String linkPattern) {
		this.chartedAreas = chartedAreas;

		this.linkPattern = linkPattern;
		buf = new StringBuffer();
		appendHeader();
		appendChartedAreas();
		appendFooter();
	}

	private void appendChartedAreas() {
		for (ChartedArea chartedArea : chartedAreas) {
			buf.append("<Placemark>\n");
			buf.append("<styleUrl>#chartedAreaStyle</styleUrl>\n");
			buf.append("<name>Charted Area " + chartedArea.getAreaId()
					+ "</name>\n");
			if (linkPattern != null) {
				buf.append("<description><![CDATA[");
				buf.append(linkPattern.replaceAll("\\{\\{\\{ID\\}\\}\\}",
						chartedArea.getAreaId().toString()));
				buf.append("]]></description>\n");
			}
			buf.append("<Polygon><outerBoundaryIs><LinearRing><coordinates>");
			for (Point2D.Double point : chartedArea.getPolyline())
				buf.append(point.x + "," + point.y + "\n");
			buf.append("</coordinates></LinearRing></outerBoundaryIs></Polygon>\n");
			buf.append("</Placemark>\n");
		}
	}

	private void appendFooter() {
		buf.append("</Document>\n</kml>\n");
	}

	private void appendHeader() {
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		buf.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
		buf.append("<Document>\n");
		buf.append("<Style id=\"chartedAreaStyle\">\n");
		buf.append("  <LineStyle>\n");
		buf.append("    <color>CC000000</color>\n");
		buf.append("    <width>1</width>\n");
		buf.append("  </LineStyle>\n");
		buf.append("  <PolyStyle>\n");
		buf.append("    <color>1AFF0000</color>\n");
		buf.append("    <fill>1</fill>\n");
		buf.append("    <outline>1</outline>\n");
		buf.append("  </PolyStyle>\n");
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
