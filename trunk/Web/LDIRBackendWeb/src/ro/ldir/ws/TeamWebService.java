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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import ro.ldir.dto.Equipment;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.GarbageEnrollment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;
import ro.ldir.dto.User;
import ro.ldir.dto.helper.AssignedChartedAreaFilter;
import ro.ldir.exceptions.ChartedAreaAssignmentException;
import ro.ldir.exceptions.InvalidTeamOperationException;
import ro.ldir.report.formatter.AssignedChartedAreasCsvFormatter;
import ro.ldir.report.formatter.AssignedChartedAreasExcelFormatter;
import ro.ldir.report.formatter.ExcelFormatter;
import ro.ldir.report.formatter.GenericXlsFormatter;
import ro.ldir.report.formatter.GenericXlsxFormatter;
import ro.ldir.report.formatter.TeamCsvFormatter;
import ro.ldir.report.formatter.TeamExcelFormatter;

import com.sun.jersey.api.client.ClientResponse.Status;

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
		teamManager.addCleaningEquipment(teamId, equipment);
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/cleaningGarbages/{garbageId:[0-9]+}")
	public Response addCleaningGarbage(@PathParam("teamId") int teamId,
			@PathParam("garbageId") int garbageId, String abs) {
		Integer allocatedBags;
		try {
			allocatedBags = new Integer(abs);
		} catch (NumberFormatException e) {
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity("Cannot convert input " + e.getMessage()).build();
		}
		try {

			teamManager.assignGarbage(teamId, garbageId, allocatedBags);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		} catch (InvalidTeamOperationException e) {
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/gps")
	public Response addGpsEquipment(@PathParam("teamId") int teamId,
			GpsEquipment equipment) {
		teamManager.addGpsEquipment(teamId, equipment);
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
		teamManager.addTransportEquipment(teamId, equipment);
		return Response.ok().build();
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/chartArea")
	public Response assignChartArea(@PathParam("teamId") int teamId,
			ChartedArea chartedArea) {
		try {
			teamManager.assignChartArea(teamId, chartedArea.getAreaId());
		} catch (ChartedAreaAssignmentException e) {
			return Response.status(Status.NOT_ACCEPTABLE)
					.entity(e.getMessage()).build();
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}/equipment/{equipmentIdx:[0-9]+}")
	public Response deleteEquipment(@PathParam("teamId") int teamId,
			@PathParam("equipmentIdx") int equipmentIdx) {
		try {
			teamManager.deleteEquipment(teamId, equipmentIdx);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException
					|| e.getCausedByException() instanceof IndexOutOfBoundsException)
				return Response.status(Status.NOT_FOUND).build();
			return Response.status(Status.INTERNAL_SERVER_ERROR)
					.entity(e.getCausedByException().getMessage()).build();
		}
		return Response.ok().build();
	}

	@DELETE
	@Path("{teamId:[0-9]+}/equipmentId/{equipmentId:[0-9]+}")
	public Response deleteEquipmentById(@PathParam("teamId") int teamId,
			@PathParam("equipmentId") int equipmentId) {
		teamManager.deleteEquipmentById(teamId, equipmentId);
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
	@Path("{teamId:[0-9]+}/chartArea")
	public List<ChartedArea> getChartedAreas(@PathParam("teamId") int teamId) {
		try {
			return new ArrayList<ChartedArea>(teamManager.getTeam(teamId)
					.getChartedAreas());
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		}
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/cleaning")
	public List<CleaningEquipment> getCleaningEquipment(
			@PathParam("teamId") int teamId) {
		List<CleaningEquipment> result = new ArrayList<CleaningEquipment>();
		Team team = teamManager.getTeam(teamId);
		if (team == null)
			throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
		for (Equipment e : team.getEquipments())
			if (e instanceof CleaningEquipment)
				result.add((CleaningEquipment) e);
		return result;
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/cleaningGarbages")
	public List<Garbage> getCleaningGarbages(@PathParam("teamId") int teamId) {
		List<Garbage> garbages = new ArrayList<Garbage>();
		Team team;
		try {
			team = teamManager.getTeam(teamId);
		} catch (NullPointerException e) {
			throw new WebApplicationException(404);
		}
		for (GarbageEnrollment enrollment : team.getGarbageEnrollements())
			garbages.add(enrollment.getGarbage());
		return garbages;
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/equipment/{equipmentIdx:[0-9]+}")
	public Equipment getEquipment(@PathParam("teamId") int teamId,
			@PathParam("equipmentIdx") int equipmentIdx) {
		Team team = teamManager.getTeam(teamId);
		if (team == null)
			throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
		Equipment result;
		try {
			result = team.getEquipments().get(equipmentIdx);
		} catch (IndexOutOfBoundsException e) {
			throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
		}
		return result;
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/equipmentCount")
	public String getEquipmentCount(@PathParam("teamId") int teamId) {
		Team team = teamManager.getTeam(teamId);
		if (team == null)
			throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
		if (team.getEquipments() == null)
			return "0";
		return new Integer(team.getEquipments().size()).toString();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/gps")
	public List<GpsEquipment> getGpsEquipment(@PathParam("teamId") int teamId) {
		List<GpsEquipment> result = new ArrayList<GpsEquipment>();
		Team team = teamManager.getTeam(teamId);
		if (team == null)
			throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
		for (Equipment e : team.getEquipments())
			if (e instanceof GpsEquipment)
				result.add((GpsEquipment) e);
		return result;
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

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/transport")
	public List<TransportEquipment> getTransportEquipment(
			@PathParam("teamId") int teamId) {
		List<TransportEquipment> result = new ArrayList<TransportEquipment>();
		Team team = teamManager.getTeam(teamId);
		if (team == null)
			throw new WebApplicationException(Status.NOT_FOUND.getStatusCode());
		for (Equipment e : team.getEquipments())
			if (e instanceof TransportEquipment)
				result.add((TransportEquipment) e);
		return result;
	}

	@DELETE
	@Path("{teamId:[0-9]+}/chartArea/{chartAreaId:[0-9]+}")
	public Response removeChartAreaAssignment(@PathParam("teamId") int teamId,
			@PathParam("chartAreaId") int chartAreaId) {
		try {
			teamManager.removeChartAreaAssignment(teamId, chartAreaId);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}

	@DELETE
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/cleaningGarbages")
	public Response removeCleaningGarbage(@PathParam("teamId") int teamId,
			Garbage garbage) {
		try {
			teamManager.removeGarbageAssigment(teamId, garbage.getGarbageId());
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}
	
	@DELETE
	@Consumes({ "application/json", "application/xml" })
	@Path("{teamId:[0-9]+}/cleaningGarbages/delete/{garbageId:[0-9]+}")
	public Response removeCleaningGarbage(@PathParam("teamId") int teamId,
			@PathParam("garbageId") int garbageId) {
		try {
			teamManager.removeGarbageAssigment(teamId, garbageId);
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}


	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("report")
	public List<Team> report(@QueryParam("county") Set<String> counties) {
		return teamManager.report(counties);
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("reportChartedArea")
	public List<Team> reportChartedArea(
			@QueryParam("county") Set<String> counties,
			@QueryParam("chartedArea") Set<String> chartedAreaNames,
			@QueryParam("managerId") Set<Integer> userIds) {
		return teamManager.reportAssignedChartedAreas(counties,
				chartedAreaNames, userIds);
	}

	@GET
	@Produces({ "text/csv" })
	@Path("reportChartedArea")
	public String reportChartedAreaCsv(
			@QueryParam("county") Set<String> counties,
			@QueryParam("chartedArea") Set<String> chartedAreaNames,
			@QueryParam("managerId") Set<Integer> userIds) {
		return new AssignedChartedAreasCsvFormatter(
				teamManager.reportAssignedChartedAreas(counties,
						chartedAreaNames, userIds),
				new AssignedChartedAreaFilter(counties, chartedAreaNames,
						userIds)).toString();
	}

	@GET
	@Produces({ "application/vnd.ms-excel" })
	@Path("reportChartedArea")
	public Response reportChartedAreaXls(
			@QueryParam("county") Set<String> counties,
			@QueryParam("chartedArea") Set<String> chartedAreaNames,
			@QueryParam("managerId") Set<Integer> userIds) {
		ExcelFormatter fmt = new AssignedChartedAreasExcelFormatter(
				teamManager.reportAssignedChartedAreas(counties,
						chartedAreaNames, userIds),
				new AssignedChartedAreaFilter(counties, chartedAreaNames,
						userIds));
		byte report[] = new GenericXlsFormatter(fmt).getBytes();
		return Response
				.ok(report)
				.header("Content-Disposition",
						"attachment; filename=assignedreport.xls").build();
	}

	@GET
	@Produces({ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
	@Path("reportChartedArea")
	public Response reportChartedAreaXlsx(
			@QueryParam("county") Set<String> counties,
			@QueryParam("chartedArea") Set<String> chartedAreaNames,
			@QueryParam("managerId") Set<Integer> userIds) {
		ExcelFormatter fmt = new AssignedChartedAreasExcelFormatter(
				teamManager.reportAssignedChartedAreas(counties,
						chartedAreaNames, userIds),
				new AssignedChartedAreaFilter(counties, chartedAreaNames,
						userIds));
		byte report[] = new GenericXlsxFormatter(fmt).getBytes();
		return Response
				.ok(report)
				.header("Content-Disposition",
						"attachment; filename=assignedreport.xlsx").build();
	}

	@GET
	@Produces({ "text/csv" })
	@Path("report")
	public String reportCsv(@QueryParam("county") Set<String> counties) {
		return new TeamCsvFormatter(teamManager.report(counties)).toString();
	}

	@GET
	@Produces({ "application/vnd.ms-excel" })
	@Path("report")
	public Response reportXls(@QueryParam("county") Set<String> counties) {
		ExcelFormatter fmt = new TeamExcelFormatter(
				teamManager.report(counties));
		byte report[] = new GenericXlsFormatter(fmt).getBytes();
		return Response
				.ok(report)
				.header("Content-Disposition",
						"attachment; filename=assignedreport.xls").build();
	}

	@GET
	@Produces({ "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
	@Path("report")
	public Response reportXlsx(@QueryParam("county") Set<String> counties) {
		ExcelFormatter fmt = new TeamExcelFormatter(
				teamManager.report(counties));
		byte report[] = new GenericXlsxFormatter(fmt).getBytes();
		return Response
				.ok(report)
				.header("Content-Disposition",
						"attachment; filename=assignedreport.xlsx").build();
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
