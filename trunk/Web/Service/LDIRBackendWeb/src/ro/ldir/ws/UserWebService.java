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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import ro.ldir.beans.TeamManagerLocal;
import ro.ldir.beans.UserManagerLocal;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.ldir.dto.User.SecurityRole;

/**
 * The garbage user. Implements queries for users, updates of users information,
 * user authentication, etc.
 */
@Path("user")
public class UserWebService {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	private TeamManagerLocal teamManager;
	private UserManagerLocal userManager;

	public UserWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		userManager = (UserManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/UserManager!ro.ldir.beans.UserManager");
		teamManager = (TeamManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/TeamManager!ro.ldir.beans.TeamManager");
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/team")
	public Response enrollTeam(@PathParam("userId") int userId, Team team) {
		try {
			teamManager.enrollUser(userId, team.getTeamId());
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}

	@GET
	public String getId(@Context SecurityContext sc) {
		return new Integer(userManager.getUser(sc.getUserPrincipal().getName())
				.getUserId()).toString();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/managedTeams")
	public Collection<Team> getManagedTeams(@PathParam("userId") int userId) {
		try {
			return userManager.getUser(userId).getManagedTeams();
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/organizations")
	public Collection<Organization> getOrganizations(
			@PathParam("userId") int userId) {
		try {
			return userManager.getUser(userId).getOrganizations();
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}")
	public User getUser(@PathParam("userId") Integer userId) {
		return userManager.getUser(userId);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{userId:[0-9]+}/activities")
	public List<User.Activity> getUserActivities(@PathParam("userId") int userId) {
		return userManager.getUser(userId).getActivities();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("byActivity/{activity}")
	public Collection<User> getUsersByActivity(
			@PathParam("activity") String activity) {
		for (User.Activity a : User.Activity.values())
			if (a.toString().equals(activity))
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
	@Path("byRole/{type}")
	public Collection<User> getUsersByType(@PathParam("type") String type) {
		for (User.SecurityRole a : User.SecurityRole.values())
			if (a.toString().equals(type))
				return userManager.getUsers(a);
		throw new WebApplicationException(404);
	}

	@GET
	@Path("emailSearch")
	public List<User> searchUserByEmail(@QueryParam("email") String email) {
		return userManager.searchByEmail(email);
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
	@Path("{userId:[0-9]+}/role")
	public Response setUserRole(@PathParam("userId") int userId,
			String roleString) {
		SecurityRole role = null;
		for (SecurityRole r : SecurityRole.values())
			if (roleString.equals(r.toString())) {
				role = r;
				break;
			}
		if (role == null)
			throw new WebApplicationException(500);

		try {
			userManager.setUserRole(userId, role);
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