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

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJBException;
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
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.Team;
import ro.ldir.dto.TownArea;

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

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Path("countyArea")
	public Response addCountyArea(CountyArea countyArea) {
		geoManager.newCountyArea(countyArea);
		return Response.ok().build();
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Path("townArea")
	public Response addTownArea(TownArea townArea) {
		geoManager.newTownArea(townArea);
		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("chartedArea/count")
	public String countChartedAreas(@QueryParam("topLeftX") double topLeftX,
			@QueryParam("topLeftY") double topLeftY,
			@QueryParam("bottomRightX") double bottomRightX,
			@QueryParam("bottomRightY") double bottomRightY) {
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

	@DELETE
	@Path("countyArea/{countyAreaId:[0-9]+}")
	public Response deleteCountyArea(@PathParam("countyAreaId") int countyAreaId) {
		geoManager.deleteCountyArea(countyAreaId);
		return Response.ok().build();
	}

	@DELETE
	@Path("townArea/{townAreaId:[0-9]+}")
	public Response deleteTownArea(@PathParam("townAreaId") int townAreaId) {
		geoManager.deleteTownArea(townAreaId);
		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("countyArea/all")
	public List<CountyArea> getAllCounties() {
		return geoManager.getAllCounties();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("chartedArea/{chartedAreaId:[0-9]+}")
	public ChartedArea getChartedArea(
			@PathParam("chartedAreaId") int chartedAreaId) {
		try {
			return geoManager.getChartedArea(chartedAreaId);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("chartedArea/{chartedAreaId:[0-9]+}/chartedBy")
	public List<Team> getChartedAreaChartedBy(
			@PathParam("chartedAreaId") int chartedAreaId) {
		try {
			return new ArrayList<Team>(geoManager.getChartedArea(chartedAreaId)
					.getChartedBy());
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		} catch (EJBException e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("chartedArea/{chartedAreaId:[0-9]+}/garbages")
	public List<Garbage> getChartedAreaGarbages(
			@PathParam("chartedAreaId") int chartedAreaId) {
		try {
			return new ArrayList<Garbage>(geoManager.getChartedArea(
					chartedAreaId).getGarbages());
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		} catch (EJBException e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("chartedArea/all")
	public List<ChartedArea> getChartedAreas(
			@QueryParam("topLeftX") double topLeftX,
			@QueryParam("topLeftY") double topLeftY,
			@QueryParam("bottomRightX") double bottomRightX,
			@QueryParam("bottomRightY") double bottomRightY) {
		List<ChartedArea> chartedAreas = geoManager.getChartedAreas(topLeftX,
				topLeftY, bottomRightX, bottomRightY);
		if (chartedAreas.size() > MAX_CHARTAREAS)
			throw new WebApplicationException(416);
		return chartedAreas;
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("countyArea/{countyAreaId:[0-9]+}")
	public CountyArea getCountyArea(@PathParam("countyAreaId") int countyAreaId) {
		return geoManager.getCountyArea(countyAreaId);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("countyArea/{countyAreaId:[0-9]+}/garbages")
	public ArrayList<Garbage> getCountyAreaGarbages(
			@PathParam("countyAreaId") int countyAreaId) {
		try {
			return new ArrayList<Garbage>(geoManager
					.getCountyArea(countyAreaId).getGarbages());
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		} catch (EJBException e) {
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("townArea/{townAreaId:[0-9]+}")
	public TownArea getTownArea(@PathParam("townAreaId") int townAreaId) {
		return geoManager.getTownArea(townAreaId);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("townArea/{townAreaId:[0-9]+}/garbages")
	public ArrayList<Garbage> getTownAreaGarbages(
			@PathParam("townAreaId") int townAreaId) {
		try {
			return new ArrayList<Garbage>(geoManager.getTownArea(townAreaId)
					.getGarbages());
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		} catch (EJBException e) {
			throw new WebApplicationException(500);
		}
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

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("countyArea/{countyAreaId:[0-9]+}")
	public Response updateCountyArea(
			@PathParam("countyAreaId") int countyAreaId, CountyArea countyArea) {
		geoManager.updateCountyArea(countyAreaId, countyArea);
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("townArea/{townAreaId:[0-9]+}")
	public Response updateTownArea(@PathParam("townAreaId") int townAreaId,
			TownArea townArea) {
		geoManager.updateTownArea(townAreaId, townArea);
		return Response.ok().build();
	}
}
