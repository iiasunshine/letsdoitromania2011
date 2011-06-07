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

import ro.ldir.beans.TeamManagerLocal;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CleaningEquipment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;
import ro.ldir.dto.User;

/**
 * The team web service.
 */
@Path("team")
public class TeamWebService {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	private TeamManagerLocal teamManager;

	public TeamWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		teamManager = (TeamManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/TeamManager!ro.ldir.beans.TeamManager");
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/cleaning")
	public Response addCleaningEquipment(@PathParam("teamId") int teamId,
			CleaningEquipment equipment) {
		teamManager.addEquipment(teamId, equipment);
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/gps")
	public Response addGpsEquipment(@PathParam("teamId") int teamId,
			GpsEquipment equipment) {
		teamManager.addEquipment(teamId, equipment);
		return Response.ok().build();
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	public Response addTeam(Team team) {
		teamManager.createTeam(team);
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/transport")
	public Response addTransportEquipment(@PathParam("teamId") int teamId,
			TransportEquipment equipment) {
		teamManager.addEquipment(teamId, equipment);
		return Response.ok().build();
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/chartArea")
	public Response assignChartArea(@PathParam("teamId") int teamId,
			ChartedArea chartedArea) {
		teamManager.assignChartArea(teamId, chartedArea.getAreaId());
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}/equipment/{equipmentId:[0-9]+}")
	public Response deleteEquipment(@PathParam("teamId") int teamId,
			@PathParam("equipmentId") int equipmentId) {
		teamManager.deleteEquipment(teamId, equipmentId);
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}")
	public Response deleteTeam(@PathParam("teamId") int teamId) {
		teamManager.deleteTeam(teamId);
		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}")
	public Team getTeam(@PathParam("teamId") int teamId) {
		return teamManager.getTeam(teamId);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/teamManager")
	public User getTeamManager(@PathParam("teamId") int teamId) {
		try {
			return teamManager.getTeam(teamId).getTeamManager();
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/organizationMembers")
	public List<Organization> getTeamOrganizationMembers(
			@PathParam("teamId") int teamId) {
		try {
			return teamManager.getTeam(teamId).getOrganizationMembers();
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/volunteerMembers")
	public List<User> getTeamVolunteerMembers(@PathParam("teamId") int teamId) {
		try {
			return teamManager.getTeam(teamId).getVolunteerMembers();
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		}
	}

	@DELETE
	@Path("{teamId:[0-9]+}/chartArea/{chartAreaId:[0-9]+}")
	public Response removeChartAreaAssignment(@PathParam("teamId") int teamId,
			@PathParam("chartAreaId") int chartAreaId) {
		teamManager.assignChartArea(teamId, chartAreaId);
		return Response.ok().build();
	}

	@GET
	@Path("nameSearch")
	public List<Team> searchTeamByName(@QueryParam("teamName") String teamName) {
		return teamManager.getTeamByName(teamName);
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}")
	public Response updateTeam(@PathParam("teamId") int teamId, Team updatedTeam) {
		teamManager.updateTeam(teamId, updatedTeam);
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}/organization/{organizationId:[0-9]+}")
	public Response withdrawOrganization(@PathParam("teamId") int teamId,
			@PathParam("organizationId") int organizationId) {
		teamManager.withdrawOrganization(organizationId, teamId);
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}/volunteer/{userId:[0-9]+}")
	public Response withdrawVolunteer(@PathParam("teamId") int teamId,
			@PathParam("userId") int userId) {
		teamManager.withdrawUser(userId, teamId);
		return Response.ok().build();
	}
}
