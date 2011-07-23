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
 *  Filename: GarbageWebService.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.ws;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
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
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import ro.ldir.beans.GarbageManagerLocal;
import ro.ldir.dto.Garbage;
import ro.ldir.exceptions.NoCountyException;
import ro.ldir.report.formatter.GarbageCsvFormatter;
import ro.ldir.report.formatter.GarbageXlsFormatter;
import ro.ldir.report.formatter.GarbageXlsxFormatter;

import com.sun.jersey.api.Responses;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;

/**
 * The garbage webservice. Implements queries for the garbage, updates of
 * garbage information, uploads of images, etc.
 */
@Path("garbage")
public class GarbageWebService {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	private GarbageManagerLocal garbageManager;

	public GarbageWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		garbageManager = (GarbageManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/GarbageManager!ro.ldir.beans.GarbageManager");
	}

	@POST
	@Consumes("multipart/form-data")
	@Path("{garbageId:[0-9]+}/image")
	public Response addNewImage(@PathParam("garbageId") int garbageId,
			@FormDataParam("file") File file,
			@FormDataParam("file") FormDataContentDisposition fcdsFile) {
		Response response = Response.ok().build();

		String mimeType = new MimetypesFileTypeMap().getContentType(fcdsFile
				.getFileName());
		if (mimeType.indexOf("image/") != 0)
			return Response
					.status(Responses.NOT_ACCEPTABLE)
					.entity("File " + fcdsFile.getFileName()
							+ " is of uknown type.").build();

		try {
			garbageManager.addNewImage(garbageId, file, fcdsFile.getFileName());
		} catch (NullPointerException e) {
			response = Response.status(404).build();
		} catch (FileNotFoundException e) {
			response = Response.status(500).entity(e.getMessage()).build();
		} catch (IOException e) {
			response = Response.status(500).entity(e.getMessage()).build();
		} finally {
			file.delete();
		}
		return response;
	}

	@DELETE
	@Path("{garbageId:[0-9]+}")
	public Response deleteGarbage(@PathParam("garbageId") int garbageId) {
		try {
			garbageManager.deleteGarbage(garbageId);
		} catch (EJBException e) {
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("{garbageId:[0-9]+}/image/{imageId:[0-9]+}")
	public Response deleteImage(@PathParam("garbageId") int garbageId,
			@PathParam("imageId") int imageId) {
		try {
			garbageManager.deleteImage(garbageId, imageId);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException
					|| e.getCausedByException() instanceof ArrayIndexOutOfBoundsException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}

		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{garbageId:[0-9]+}")
	public Garbage getGarbage(@PathParam("garbageId") Integer garbageId) {
		Garbage garbage = garbageManager.getGarbage(garbageId);
		if (garbage == null)
			throw new WebApplicationException(404);
		return garbage;
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("countySearch")
	public List<Garbage> getGarbageByCounty(@QueryParam("county") String county) {
		return garbageManager.getGarbagesByCounty(county);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("statusSearch")
	public List<Garbage> getGarbageByStatus(
			@QueryParam("status") Garbage.GarbageStatus status) {
		return garbageManager.getGarbages(status);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("townSearch")
	public List<Garbage> getGarbageByTown(@QueryParam("town") String town) {
		return garbageManager.getGarbagesByTown(town);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("bbox")
	public List<Garbage> getGarbages(@QueryParam("topLeftX") double topLeftX,
			@QueryParam("topLeftY") double topLeftY,
			@QueryParam("bottomRightX") double bottomRightX,
			@QueryParam("bottomRightY") double bottomRightY) {
		return garbageManager.getGarbages(topLeftX, topLeftY, bottomRightX,
				bottomRightY);
	}

	@GET
	@Path("{garbageId:[0-9]+}/image/{imageId:[0-9]+}")
	public Response getImage(@PathParam("garbageId") int garbageId,
			@PathParam("imageId") int imageId) {
		File f;
		try {
			f = new File(garbageManager.getImagePath(garbageId, imageId));
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException
					|| e.getCausedByException() instanceof ArrayIndexOutOfBoundsException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		if (!f.exists())
			throw new WebApplicationException(404);
		String mt = new MimetypesFileTypeMap().getContentType(f);
		return Response.ok(f, mt).build();
	}

	@GET
	@Path("{garbageId:[0-9]+}/imageCount")
	public String getImageCount(@PathParam("garbageId") int garbageId) {
		try {
			return new Integer(garbageManager.getGarbage(garbageId)
					.getPictures().size()).toString();
		} catch (NullPointerException e) {
			throw new WebApplicationException(Status.NOT_FOUND);
		}
	}

	@GET
	@Path("{garbageId:[0-9]+}/image/{imageId:[0-9]+}/display")
	public Response getImageDisplay(@PathParam("garbageId") int garbageId,
			@PathParam("imageId") int imageId) {
		File f;
		try {
			f = new File(garbageManager.getImageDisplayPath(garbageId, imageId));
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException
					|| e.getCausedByException() instanceof ArrayIndexOutOfBoundsException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		if (!f.exists())
			throw new WebApplicationException(404);
		String mt = new MimetypesFileTypeMap().getContentType(f);
		return Response.ok(f, mt).build();
	}

	@GET
	@Path("{garbageId:[0-9]+}/image/{imageId:[0-9]+}/thumb")
	public Response getImageThumbnail(@PathParam("garbageId") int garbageId,
			@PathParam("imageId") int imageId) {
		File f;
		try {
			f = new File(garbageManager.getImageThumbnailPath(garbageId,
					imageId));
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException
					|| e.getCausedByException() instanceof ArrayIndexOutOfBoundsException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		if (!f.exists())
			throw new WebApplicationException(404);
		String mt = new MimetypesFileTypeMap().getContentType(f);
		return Response.ok(f, mt).build();
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	public String insertGarbage(Garbage garbage) {
		int insertedId;
		try {
			insertedId = garbageManager.insertGarbage(garbage);
		} catch (NoCountyException e) {
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		return new Integer(insertedId).toString();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("report")
	public List<Garbage> report(@QueryParam("county") Set<String> counties,
			@QueryParam("chartedArea") Set<String> chartedAreaNames,
			@QueryParam("userId") Set<Integer> userIds,
			@QueryParam("insertDate") Set<Date> insertDates) {
		return garbageManager.report(counties, chartedAreaNames, userIds,
				insertDates);
	}

	@GET
	@Produces({ "text/csv" })
	@Path("report")
	public String reportCsv(@QueryParam("county") Set<String> counties,
			@QueryParam("chartedArea") Set<String> chartedAreaNames,
			@QueryParam("userId") Set<Integer> userIds,
			@QueryParam("insertDate") Set<Date> insertDates) {
		return new GarbageCsvFormatter(garbageManager.report(counties,
				chartedAreaNames, userIds, insertDates)).toString();
	}

	@GET
	@Produces({ "application/vnd.ms-excel" })
	@Path("report")
	public Response reportXls(@QueryParam("county") Set<String> counties,
			@QueryParam("chartedArea") Set<String> chartedAreaNames,
			@QueryParam("userId") Set<Integer> userIds,
			@QueryParam("insertDate") Set<Date> insertDates) {
		byte report[] = new GarbageXlsFormatter(garbageManager.report(counties,
				chartedAreaNames, userIds, insertDates)).getBytes();
		return Response
				.ok(report)
				.header("Content-Disposition",
						"attachment; filename=userreport.xls").build();
	}

	@GET
	@Produces({ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
	@Path("report")
	public Response reportXlsx(@QueryParam("county") Set<String> counties,
			@QueryParam("chartedArea") Set<String> chartedAreaNames,
			@QueryParam("userId") Set<Integer> userIds,
			@QueryParam("insertDate") Set<Date> insertDates) {
		byte report[] = new GarbageXlsxFormatter(garbageManager.report(
				counties, chartedAreaNames, userIds, insertDates)).getBytes();
		return Response
				.ok(report)
				.header("Content-Disposition",
						"attachment; filename=userreport.xls").build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{garbageId:[0-9]+}/status")
	public Response setGarbageStatus(@PathParam("garbageId") int garbageId,
			Garbage.GarbageStatus status) {
		try {
			garbageManager.setGarbageStatus(garbageId, status);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Path("{garbageId:[0-9]+}")
	public Response updateGarbage(@PathParam("garbageId") Integer garbageId,
			Garbage garbage) {
		try {
			garbageManager.updateGarbage(garbageId, garbage);
		} catch (NoCountyException e) {
			return Response.status(Status.BAD_REQUEST)
					.entity("No county was found to contain this garbage.")
					.type("text/plain").build();
		}
		return Response.ok().build();
	}

}
