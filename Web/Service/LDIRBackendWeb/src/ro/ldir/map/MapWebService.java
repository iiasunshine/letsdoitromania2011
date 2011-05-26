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
 *  Filename: MapWebService.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.map;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import ro.ldir.beans.GarbageManagerLocal;
import ro.ldir.beans.GeoManagerLocal;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Garbage;
import ro.ldir.map.formatter.ChartedAreasKMLFormatter;
import ro.ldir.map.formatter.GarbagesKMLFormatter;

/**
 * The map web service for serving KML.
 */
@Path("ws")
public class MapWebService {
	private GarbageManagerLocal garbageManager;
	private GeoManagerLocal geoManager;;

	public MapWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		garbageManager = (GarbageManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/GarbageManager!ro.ldir.beans.GarbageManager");
		geoManager = (GeoManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/GeoManager!ro.ldir.beans.GeoManager");
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("chartedAreas")
	public String getChartedAreas(@QueryParam("topLeftX") float topLeftX,
			@QueryParam("topLeftY") float topLeftY,
			@QueryParam("bottomRightX") float bottomRightX,
			@QueryParam("bottomRightY") float bottomRightY) {
		List<ChartedArea> chartedAreas = geoManager.getChartedAreas(topLeftX,
				topLeftY, bottomRightX, bottomRightY);
		return new ChartedAreasKMLFormatter(chartedAreas).toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("garbages")
	public String getGarbages(@QueryParam("topLeftX") float topLeftX,
			@QueryParam("topLeftY") float topLeftY,
			@QueryParam("bottomRightX") float bottomRightX,
			@QueryParam("bottomRightY") float bottomRightY) {
		List<Garbage> garbages = garbageManager.getGarbages(topLeftX, topLeftY,
				bottomRightX, bottomRightY);
		return new GarbagesKMLFormatter(garbages).toString();
	}
}
