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
 *  Filename: GeoWebService.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.ws;

import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.ldir.beans.GeoManagerLocal;
import ro.ldir.dto.ChartedArea;

/**
 * The geographical web service.
 */
@Path("geo")
public class GeoWebService {
	// TODO make it a servlet parameter
	private static final int MAX_CHARTAREAS = 100;

	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	private GeoManagerLocal geoManager;;

	public GeoWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		geoManager = (GeoManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/GeoManager!ro.ldir.beans.GeoManager");
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Path("chartedArea")
	public Response addChartedArea(ChartedArea chartedArea) {
		geoManager.newChartedArea(chartedArea);
		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("chartedArea/count")
	public String countChartedAreas(@QueryParam("topLeftX") float topLeftX,
			@QueryParam("topLeftY") float topLeftY,
			@QueryParam("bottomRightY") float bottomRightX,
			@QueryParam("bottomRightY") float bottomRightY) {
		List<ChartedArea> chartedAreas = geoManager.getChartedAreas(topLeftX,
				topLeftY, bottomRightX, bottomRightY);
		return new Integer(chartedAreas.size()).toString();
	}

	@DELETE
	@Path("chartedArea/{chartedAreaId:[0-9]+}")
	public Response deleteChartedArea(
			@PathParam("chartedAreaId") int chartedAreaId) {
		geoManager.deleteChartedArea(chartedAreaId);
		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("chartedArea/{chartedAreaId:[0-9]+}")
	public ChartedArea getChartedArea(
			@PathParam("chartedAreaId") int chartedAreaId) {
		return geoManager.getChartedArea(chartedAreaId);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("chartedArea/all")
	public List<ChartedArea> getChartedAreas(
			@QueryParam("topLeftX") float topLeftX,
			@QueryParam("topLeftY") float topLeftY,
			@QueryParam("bottomRightY") float bottomRightX,
			@QueryParam("bottomRightY") float bottomRightY) {
		List<ChartedArea> chartedAreas = geoManager.getChartedAreas(topLeftX,
				topLeftY, bottomRightX, bottomRightY);
		if (chartedAreas.size() > MAX_CHARTAREAS)
			throw new WebApplicationException(416);
		return chartedAreas;
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("chartedArea/{chartedAreaId:[0-9]+}")
	public Response updateChartedArea(
			@PathParam("chartedAreaId") int chartedAreaId,
			ChartedArea chartedArea) {
		geoManager.updateChartedArea(chartedAreaId, chartedArea);
		return Response.ok().build();
	}
}
