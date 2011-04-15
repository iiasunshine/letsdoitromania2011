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
 *  Filename: UserWebService.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.ws;

import java.util.Collection;
import java.util.List;

import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.ldir.beans.UserManagerLocal;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.User;
import ro.ldir.exceptions.InvalidUserException;

import com.sun.jersey.api.Responses;

/**
 * The garbage user. Implements queries for users, updates of users information,
 * user authentication, etc.
 */
@Path("user")
public class UserWebService {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	private UserManagerLocal userManager;

	public UserWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		userManager = (UserManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/UserManager!ro.ldir.beans.UserManager");
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/garbage")
	public Response addNewGarbage(@PathParam("userId") int userId,
			Garbage garbage) {
		try {
			userManager.addNewGarbage(userId, garbage);
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		}
		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/garbage")
	public Collection<Garbage> getGarbagesByUser(@PathParam("userId") int userId) {
		try {
			return userManager.getGarbages(userId);
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}")
	public User getUser(@PathParam("userId") Integer userId) {
		User user = userManager.getUser(userId);
		if (user == null)
			throw new WebApplicationException(404);
		return user;
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/activities")
	public List<User.Activity> getUserActivities(@PathParam("userId") int userId) {
		User user = userManager.getUser(userId);
		return user.activities;
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("byActivity/{activity}")
	public Collection<User> getUsersByActivity(
			@PathParam("activity") String activity) {
		for (User.Activity a : User.Activity.values())
			if (a.getRestName().equals(activity))
				return userManager.getUsers(a);

		throw new WebApplicationException(404);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("byTown/{town}")
	public Collection<User> getUsersByTown(@PathParam("town") String town) {
		return userManager.getUsers(town);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("byType/{type}")
	public Collection<User> getUsersByType(@PathParam("type") String type) {
		for (User.Type a : User.Type.values())
			if (a.getRestName().equals(type))
				return userManager.getUsers(a);

		throw new WebApplicationException(404);
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	public Response newUser(User user) {
		try {
			userManager.addUser(user);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof InvalidUserException)
				return Response.status(Responses.CONFLICT)
						.entity(e.getCausedByException().getMessage())
						.type("text/plain").build();
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/activities")
	public Response setUserActivities(@PathParam("userId") int userId,
			List<User.Activity> activities) {
		try {
			userManager.setUserActivities(userId, activities);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/status")
	public Response setUserStatus(@PathParam("userId") int userId,
			User.Status status) {
		try {
			userManager.setUserStatus(userId, status);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/")
	public Response updateUser(@PathParam("userId") int userId, User user) {
		try {
			userManager.updateUser(userId, user);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}
}