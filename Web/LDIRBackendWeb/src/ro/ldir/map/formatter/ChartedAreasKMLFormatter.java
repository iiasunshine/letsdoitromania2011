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
	public enum Type {
		ASSIGNED("assignedStyle"), GENERIC("genericStyle"), HIGH_ASSIGNED(
				"highAssigned"), HIGH_UNASSIGNED("highUnassigned"), LOW_ASSIGNED(
				"lowAssigned"), LOW_UNASSIGNED("lowUnassigned");
		public static String getStyleName(ChartedArea chartedArea) {
			switch (chartedArea.getScore()) {
			case 3:
			case 2:
				if (chartedArea.getChartedBy() != null
						&& chartedArea.getChartedBy().size() > 0)
					return HIGH_ASSIGNED.styleName;
				return HIGH_UNASSIGNED.styleName;
			case 1:
			case 0:
				if (chartedArea.getChartedBy() != null
						&& chartedArea.getChartedBy().size() > 0)
					return LOW_ASSIGNED.styleName;
				return LOW_UNASSIGNED.styleName;
			}
			return GENERIC.styleName;
		}

		private String styleName;

		Type(String styleName) {
			this.styleName = styleName;
		}

		public String getStyleName() {
			return this.styleName;
		}
	}

	protected StringBuffer buf;
	protected List<ChartedArea> chartedAreas;
	protected String linkPattern = null;

	public ChartedAreasKMLFormatter(List<ChartedArea> chartedAreas,
			String linkPattern) {
		this.chartedAreas = chartedAreas;
		this.linkPattern = linkPattern;
	}

	protected void appendChartedAreas() {
		for (ChartedArea chartedArea : chartedAreas) {
			buf.append("<Placemark>\n");
			buf.append("<styleUrl>#" + Type.getStyleName(chartedArea)
					+ "</styleUrl>\n");
			buf.append("<name>Zona de cartare " + chartedArea.getName()
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

	protected void appendFooter() {
		buf.append("</Document>\n</kml>\n");
	}

	protected void appendHeader() {
		buf.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		buf.append("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
		buf.append("<Document>\n");

		buf.append("<Style id=\"genericStyle\">\n");
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

		buf.append("<Style id=\"assignedStyle\">\n");
		buf.append("  <LineStyle>\n");
		buf.append("    <color>CCFFB4FF</color>\n");
		buf.append("    <width>2</width>\n");
		buf.append("  </LineStyle>\n");
		buf.append("  <PolyStyle>\n");
		buf.append("    <color>A0FFB4FF</color>\n");
		buf.append("    <fill>1</fill>\n");
		buf.append("    <outline>1</outline>\n");
		buf.append("  </PolyStyle>\n");
		buf.append("</Style>\n");

		buf.append("<Style id=\"highUnassigned\">\n");
		buf.append("  <LineStyle>\n");
		buf.append("    <color>CC000000</color>\n");
		buf.append("    <width>1</width>\n");
		buf.append("  </LineStyle>\n");
		buf.append("  <PolyStyle>\n");
		buf.append("    <color>50000040</color>\n");
		buf.append("    <fill>1</fill>\n");
		buf.append("    <outline>1</outline>\n");
		buf.append("  </PolyStyle>\n");
		buf.append("</Style>\n");

		buf.append("<Style id=\"highAssigned\">\n");
		buf.append("  <LineStyle>\n");
		buf.append("    <color>CC000000</color>\n");
		buf.append("    <width>1</width>\n");
		buf.append("  </LineStyle>\n");
		buf.append("  <PolyStyle>\n");
		buf.append("    <color>1A0000FF</color>\n");
		buf.append("    <fill>1</fill>\n");
		buf.append("    <outline>1</outline>\n");
		buf.append("  </PolyStyle>\n");
		buf.append("</Style>\n");

		buf.append("<Style id=\"lowUnassigned\">\n");
		buf.append("  <LineStyle>\n");
		buf.append("    <color>CC000000</color>\n");
		buf.append("    <width>1</width>\n");
		buf.append("  </LineStyle>\n");
		buf.append("  <PolyStyle>\n");
		buf.append("    <color>50004000</color>\n");
		buf.append("    <fill>1</fill>\n");
		buf.append("    <outline>1</outline>\n");
		buf.append("  </PolyStyle>\n");
		buf.append("</Style>\n");

		buf.append("<Style id=\"lowAssigned\">\n");
		buf.append("  <LineStyle>\n");
		buf.append("    <color>CC000000</color>\n");
		buf.append("    <width>1</width>\n");
		buf.append("  </LineStyle>\n");
		buf.append("  <PolyStyle>\n");
		buf.append("    <color>1A00FF00</color>\n");
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
		buf = new StringBuffer();
		appendHeader();
		appendChartedAreas();
		appendFooter();
		return buf.toString();
	}
}
