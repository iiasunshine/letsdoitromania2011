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
 *  Filename: TeamWebService.java
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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import ro.ldir.beans.OrganizationManagerLocal;
import ro.ldir.beans.TeamManagerLocal;
import ro.ldir.beans.UserManagerLocal;
import ro.ldir.dto.CleaningEquipment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;
import ro.ldir.dto.User;
import ro.ldir.ws.helper.SecurityHelper;

/**
 * The team web service.
 */
@Path("team")
public class TeamWebService {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	private OrganizationManagerLocal orgManager;
	private TeamManagerLocal teamManager;
	private UserManagerLocal userManager;

	public TeamWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		userManager = (UserManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/UserManager!ro.ldir.beans.UserManager");
		teamManager = (TeamManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/TeamManager!ro.ldir.beans.TeamManager");
		orgManager = (OrganizationManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/OrganizationManager!ro.ldir.beans.OrganizationManager");
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/cleaning")
	public Response addCleaningEquipment(@PathParam("teamId") int teamId,
			CleaningEquipment equipment, @Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkTeamMemberOrAdmin(userManager, team, sc))
			throw new WebApplicationException(401);
		teamManager.addEquipment(team.getTeamId(), equipment);
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/gps")
	public Response addGpsEquipment(@PathParam("teamId") int teamId,
			GpsEquipment equipment, @Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkTeamMemberOrAdmin(userManager, team, sc))
			throw new WebApplicationException(401);
		teamManager.addEquipment(team.getTeamId(), equipment);
		return Response.ok().build();
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	public Response addTeam(Team team, @Context SecurityContext sc) {

		teamManager.createTeam(SecurityHelper.getUserId(userManager, sc), team);
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/transport")
	public Response addTransportEquipment(@PathParam("teamId") int teamId,
			TransportEquipment equipment, @Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkTeamMemberOrAdmin(userManager, team, sc))
			throw new WebApplicationException(401);
		teamManager.addEquipment(team.getTeamId(), equipment);
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}/equipment/{equipmentId:[0-9]+}")
	public Response deleteEquipment(@PathParam("teamId") int teamId,
			@PathParam("equipmentId") int equipmentId,
			@Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkTeamMemberOrAdmin(userManager, team, sc))
			throw new WebApplicationException(401);
		teamManager.deleteEquipment(team.getTeamId(), equipmentId);
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}")
	public Response deleteTeam(@PathParam("teamId") int teamId,
			@Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkTeamMemberOrAdmin(userManager, team, sc))
			throw new WebApplicationException(401);
		teamManager.deleteTeam(teamId);
		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}")
	public Team getTeam(@PathParam("teamId") int teamId,
			@Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkTeamMemberOrAdmin(userManager, team, sc))
			throw new WebApplicationException(401);
		return team;
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/organizationMembers")
	public List<Organization> getTeamOrganizationMembers(
			@PathParam("teamId") int teamId, @Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkManagerOrAdmin(userManager, team, sc))
			throw new WebApplicationException(401);
		return team.getOrganizationMembers();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/volunteerMembers")
	public List<User> getTeamVolunteerMembers(@PathParam("teamId") int teamId,
			@Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkManagerOrAdmin(userManager, team, sc))
			throw new WebApplicationException(401);
		return team.getVolunteerMembers();
	}

	@GET
	@Path("nameSearch")
	public List<Team> searchTeamByName(@QueryParam("teamName") String teamName) {
		return teamManager.getTeamByName(teamName);
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}")
	public Response updateTeam(@PathParam("teamId") int teamId,
			Team updatedTeam, @Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkTeamMemberOrAdmin(userManager, team, sc))
			throw new WebApplicationException(401);
		teamManager.updateTeam(teamId, updatedTeam);
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}/organization/{organizationId:[0-9]+}")
	public Response withdrawOrganization(@PathParam("teamId") int teamId,
			@PathParam("organizationId") int organizationId,
			@Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		Organization org = orgManager.getOrganization(organizationId);
		if (!SecurityHelper.checkManagerOrAdmin(userManager, team, sc)
				&& !SecurityHelper.checkUserOrAdmin(userManager, sc, org
						.getContactUser().getUserId()))
			throw new WebApplicationException(401);
		teamManager.withdrawOrganization(organizationId, teamId);
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}/volunteer/{userId:[0-9]+}")
	public Response withdrawVolunteer(@PathParam("teamId") int teamId,
			@PathParam("userId") int userId, @Context SecurityContext sc) {
		Team team = teamManager.getTeam(teamId);
		if (!SecurityHelper.checkManagerOrAdmin(userManager, team, sc)
				&& !SecurityHelper.checkUserOrAdmin(userManager, sc, userId))
			throw new WebApplicationException(401);
		teamManager.withdrawUser(userId, teamId);
		return Response.ok().build();
	}
}
