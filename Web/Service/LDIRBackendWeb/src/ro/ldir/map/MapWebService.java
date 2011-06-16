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

import java.util.Arrays;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import ro.ldir.beans.GarbageManagerLocal;
import ro.ldir.beans.GeoManagerLocal;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Garbage;
import ro.ldir.map.formatter.ChartedAreasKMLFormatter;
import ro.ldir.map.formatter.ChartedAreasTeamKMLFormatter;
import ro.ldir.map.formatter.GarbagesKMLFormatter;
import ro.ldir.map.formatter.ChartedAreasKMLFormatter.Type;

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
	@Path("chartedArea/{chartedAreaId:[0-9]+}")
	public String getChartedArea(@PathParam("chartedAreaId") int chartedAreaId) {
		ChartedArea ca = geoManager.getChartedArea(chartedAreaId);
		if (ca == null)
			throw new WebApplicationException(Status.NOT_FOUND);
		List<ChartedArea> wrapper = Arrays.asList(ca);
		return new ChartedAreasKMLFormatter(wrapper, null, Type.GENERIC)
				.toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("chartedArea/find")
	public String getChartedArea(@QueryParam("name") String name) {
		ChartedArea ca = geoManager.getChartedArea(name);
		if (ca == null)
			throw new WebApplicationException(Status.NOT_FOUND);
		return new ChartedAreasKMLFormatter(Arrays.asList(ca), null,
				Type.GENERIC).toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("chartedAreas")
	public String getChartedAreas(@QueryParam("topLeftX") double topLeftX,
			@QueryParam("topLeftY") double topLeftY,
			@QueryParam("bottomRightX") double bottomRightX,
			@QueryParam("bottomRightY") double bottomRightY,
			@QueryParam("cb") String callbackPattern) {
		List<ChartedArea> chartedAreas = geoManager.getChartedAreas(topLeftX,
				topLeftY, bottomRightX, bottomRightY);
		return new ChartedAreasKMLFormatter(chartedAreas, callbackPattern,
				ChartedAreasKMLFormatter.Type.GENERIC).toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("chartedAreas/search")
	public String getChartedAreas(@QueryParam("name") String name) {
		List<ChartedArea> cas = geoManager.getChartedAreas(name);
		if (cas.size() == 0)
			throw new WebApplicationException(Status.NOT_FOUND);
		return new ChartedAreasKMLFormatter(cas, null, Type.GENERIC).toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("chartedAreas/team/{teamId:[0-9]+}")
	public String getChartedAreasAndTeam(@PathParam("teamId") int teamId,
			@QueryParam("topLeftX") double topLeftX,
			@QueryParam("topLeftY") double topLeftY,
			@QueryParam("bottomRightX") double bottomRightX,
			@QueryParam("bottomRightY") double bottomRightY,
			@QueryParam("cb") String callbackPattern) {
		List<ChartedArea> chartedAreas = geoManager.getChartedAreas(topLeftX,
				topLeftY, bottomRightX, bottomRightY);
		return new ChartedAreasTeamKMLFormatter(teamId, chartedAreas,
				callbackPattern).toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("team/{teamId:[0-9]+}/chartedAreas")
	public String getChartedAreasByChartedBy(@PathParam("teamId") int teamId,
			@QueryParam("cb") String callbackPattern) {
		List<ChartedArea> chartedAreas;
		try {
			chartedAreas = geoManager.getChartedAreasByChartedBy(teamId);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return new ChartedAreasKMLFormatter(chartedAreas, callbackPattern,
				ChartedAreasKMLFormatter.Type.ASSIGNED).toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("countySearch/chartedAreas")
	public String getChartedAreasByCounty(@QueryParam("county") String county,
			@QueryParam("cb") String callbackPattern) {
		List<ChartedArea> chartedAreas = geoManager
				.getChartedAreasByCounty(county);
		return new ChartedAreasKMLFormatter(chartedAreas, callbackPattern,
				ChartedAreasKMLFormatter.Type.GENERIC).toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("countySearch/team/{teamId:[0-9]+}/chartedAreas")
	public String getChartedAreasByCountyAndTeam(
			@PathParam("teamId") int teamId,
			@QueryParam("county") String county,
			@QueryParam("cb") String callbackPattern) {
		List<ChartedArea> chartedAreas = geoManager
				.getChartedAreasByCounty(county);
		return new ChartedAreasTeamKMLFormatter(teamId, chartedAreas,
				callbackPattern).toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("countySearch/garbages")
	public String getGarbageByCounty(@QueryParam("county") String county,
			@QueryParam("cb") String callbackPattern) {
		List<Garbage> garbages = garbageManager.getGarbagesByCounty(county);
		return new GarbagesKMLFormatter(garbages, callbackPattern).toString();
	}

	@GET
	@Produces({ "application/vnd.google-earth.kml+xml" })
	@Path("garbages")
	public String getGarbages(@QueryParam("topLeftX") double topLeftX,
			@QueryParam("topLeftY") double topLeftY,
			@QueryParam("bottomRightX") double bottomRightX,
			@QueryParam("bottomRightY") double bottomRightY,
			@QueryParam("cb") String callbackPattern) {
		List<Garbage> garbages = garbageManager.getGarbages(topLeftX, topLeftY,
				bottomRightX, bottomRightY);
		return new GarbagesKMLFormatter(garbages, callbackPattern).toString();
	}
}
