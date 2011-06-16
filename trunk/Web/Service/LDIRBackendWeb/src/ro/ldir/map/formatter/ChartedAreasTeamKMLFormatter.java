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
 *  Filename: ChartedAreasTeamKMLFormatter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.map.formatter;

import java.awt.geom.Point2D;
import java.util.List;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Team;

/**
 * Formats a list of areas in KML format. Charted areas assigned to a team
 * appear in a different color.
 */
public class ChartedAreasTeamKMLFormatter extends ChartedAreasKMLFormatter {
	private int teamId;

	public ChartedAreasTeamKMLFormatter(int teamId,
			List<ChartedArea> chartedAreas, String linkPattern) {
		super(chartedAreas, linkPattern, Type.GENERIC);
		this.teamId = teamId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.map.formatter.ChartedAreasKMLFormatter#appendChartedAreas()
	 */
	@Override
	protected void appendChartedAreas() {
		for (ChartedArea chartedArea : chartedAreas) {
			buf.append("<Placemark>\n");
			buf.append("<styleUrl>#" + getStyleName(chartedArea)
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

	private String getStyleName(ChartedArea chartedArea) {
		for (Team team : chartedArea.getChartedBy())
			if (team.getTeamId() == teamId)
				return Type.ASSIGNED.getStyleName();
		return Type.GENERIC.getStyleName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.map.formatter.ChartedAreasKMLFormatter#toString()
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
