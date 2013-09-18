/**
 *  This file is part of the LDIRBackend - the backend for the Let's Do It
 *  Romania 2011 Garbage collection campaign.
 *  Copyright (C) 2012 by the LDIR development team, further referred to 
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
 *  Filename: GarbageAnonymousWebService.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.garbage;

import java.io.File;
import javax.activation.MimetypesFileTypeMap;
import com.sun.jersey.api.Responses;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import ro.ldir.beans.GarbageManagerLocal;
import ro.ldir.dto.Garbage;
import ro.ldir.exceptions.InvalidUserOperationException;
import ro.ldir.exceptions.NoCountyException;
//import ro.ldir.ws.GET;
//import ro.ldir.ws.Produces;

/** Serves anonymous calls to handle garbages. */
@Path("ws")
public class GarbageAnonymousWebService {
	private GarbageManagerLocal garbageManager;
	@Context
	HttpServletRequest req;

	public GarbageAnonymousWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		garbageManager = (GarbageManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/GarbageManager!ro.ldir.beans.GarbageManager");
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

	@PUT
	@Path("{garbageId:[0-9]+}/vote")
	public Response voteGarbage(@PathParam("garbageId") int garbageId) {
		try {
			garbageManager.voteGarbage(garbageId, req.getRemoteAddr());
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		} catch (InvalidUserOperationException e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage())
					.type(MediaType.TEXT_PLAIN).build();
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
	
}
