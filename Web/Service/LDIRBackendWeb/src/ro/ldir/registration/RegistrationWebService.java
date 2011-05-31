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
 *  Filename: RegistrationWebService.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.registration;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import ro.ldir.beans.UserManagerLocal;
import ro.ldir.dto.User;
import ro.ldir.exceptions.InvalidUserException;

import com.sun.jersey.api.Responses;

/**
 * Handles the registration of new users.
 */
@Path("ws")
public class RegistrationWebService {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	private UserManagerLocal userManager;

	public RegistrationWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		userManager = (UserManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/UserManager!ro.ldir.beans.UserManager");
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	public Response addNewUser(User user) {
		try {
			userManager.addUser(user);
		} catch (InvalidUserException e) {
			return Response.status(Responses.CONFLICT).entity(e.getMessage())
					.type("text/plain").build();
		} catch (EJBException e) {
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}

	@GET
	@Path("{userId:[0-9]+}/{key}")
	public Response activateUser(@PathParam("userId") int userId,
			@PathParam("key") String key) {
		try {
			userManager.activateUser(userId, key);
		} catch (InvalidUserException e) {
			return Response.status(Status.CONFLICT).entity(e.getMessage())
					.type("text/plain").build();
		} catch (EJBException e) {
			throw new WebApplicationException(404);
		}
		return Response.ok().build();
	}
}
