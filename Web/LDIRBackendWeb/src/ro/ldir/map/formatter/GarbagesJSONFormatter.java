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
 *  Filename: GarbagesJSONFormatter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.map.formatter;

import java.util.List;

import ro.ldir.dto.Garbage;

/**
 * Formats a list of garbages in KML.
 */
public class GarbagesJSONFormatter {
	private StringBuffer buf;
	private List<Garbage> garbages;
	private String linkPattern = null;

	public GarbagesJSONFormatter(List<Garbage> garbages) {
		this.garbages = garbages;
		buf = new StringBuffer();
		appendGarbages();
	}

	private void appendFooter() {
		buf.append("</Document>\n</kml>\n");
	}

	private void appendGarbages() {
		String userRole="";
		buf.append("{\"garbage\":[");
		int flag=0;
		for (Garbage garbage : garbages) {
						
			if(flag!=0)
				buf.append(",");
			buf.append("{");
			buf.append("\"allocatedStatus\""+":"+"\""+garbage.getAllocatedStatus()+"\",");
			buf.append("\"bagCount\":\""+garbage.getBagCount()+"\",");
			buf.append("\"bigComponentsDescription\":\""+""+"\",");
			buf.append("\"chartedArea\":\""+garbage.getChartedArea()+"\",");
			buf.append("\"county\":\""+garbage.getCounty().getName()+"\",");
			buf.append("\"description\":\""+""+"\",");
			buf.append("\"dispersed\":\""+garbage.isDispersed()+"\",");
			buf.append("\"garbageGroup\":\""+garbage.getGarbageGroup()+"\",");
			buf.append("\"garbageId\":\""+garbage.getGarbageId().toString()+"\",");
			buf.append("\"insertedBy\":\""+garbage.getInsertedBy()+"\",");
			buf.append("\"name\":\""+""+"\",");
			buf.append("\"percentageGlass\":\""+garbage.getPercentageGlass()+"\",");
			buf.append("\"percentageMetal\":\""+garbage.getPercentageMetal()+"\",");
			buf.append("\"percentagePlastic\":\""+garbage.getPercentagePlastic()+"\",");
			buf.append("\"percentageWaste\":\""+garbage.getPercentageWaste()+"\",");
			buf.append("\"radius\":\""+garbage.getRadius()+"\",");
			buf.append("\"recordDate\":\""+garbage.getRecordDate().toString()+"\",");
			buf.append("\"status\":\""+garbage.getStatus()+"\",");
			buf.append("\"toClean\":\""+garbage.isToClean()+"\",");
			buf.append("\"toVote\":\""+garbage.isToVote()+"\",");
			buf.append("\"town\":\""+garbage.getTown().getName()+"\",");
			buf.append("\"voteCount\":\""+garbage.getVoteCount()+"\",");
			buf.append("\"x\":\""+garbage.getX()+"\",");
			buf.append("\"y\":\""+garbage.getY()+"\"");
				
			buf.append("}");
			flag=1;
			//String.valueOf(garbage.getCountBagsEnrollments())
		}
		
		buf.append("]}");
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
		
		buf.append("<Style id=\"ZONAVOTARENEVOTAT\">\n");
		buf.append("<IconStyle>\n");
		buf.append("<Icon>\n");
		buf.append("<href>http://localhost:8080/icons/ldirzona64px.png</href>");
		buf.append("</Icon>\n");
		buf.append("</IconStyle>\n");
		buf.append("</Style>\n");
		
		buf.append("<Style id=\"ZONAVOTAREVOTAT\">\n");
		buf.append("<IconStyle>\n");
		buf.append("<Icon>\n");
		buf.append("<href>http://localhost:8080/icons/ldirzona64pxGrey.png</href>");
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
