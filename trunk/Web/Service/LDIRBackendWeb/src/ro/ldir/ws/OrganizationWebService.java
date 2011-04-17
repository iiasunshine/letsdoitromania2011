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
 *  Filename: OrganizationWebService.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.ws;

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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import ro.ldir.beans.OrganizationManagerLocal;
import ro.ldir.beans.TeamManagerLocal;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;

/**
 * The organization web service.
 */
@Path("organization")
public class OrganizationWebService {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	private OrganizationManagerLocal orgManager;
	private TeamManagerLocal teamManager;

	public OrganizationWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		orgManager = (OrganizationManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/OrganizationManager!ro.ldir.beans.OrganizationManager");
		teamManager = (TeamManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/TeamManager!ro.ldir.beans.TeamManager");
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	public Response addOrganization(Organization organization) {
		orgManager.addOrganization(organization);
		return Response.ok().build();
	}

	@DELETE
	@Path("{organizationId:[0-9]+}/")
	public Response deleteOrganization(
			@PathParam("organizationId") int organizationId) {
		orgManager.deleteOrganization(organizationId);
		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{organizationId:[0-9]+}/")
	public Organization getOrganization(
			@PathParam("organizationId") int organizationId) {
		return orgManager.getOrganization(organizationId);

	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{organizationId:[0-9]+}/")
	public Response updateOrganization(
			@PathParam("organizationId") int organizationId,
			Organization organization) {
		orgManager.updateOrganization(organizationId, organization);
		return Response.ok().build();
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	@Path("{organizationId:[0-9]+}/team")
	public Response enrollTeam(@PathParam("organizationId") int organizationId,
			Team team) {
		try {
			teamManager.enrollOrganization(organizationId, team.getTeamId());
		} catch (EJBException e) {
			if (e.getCausedByException() instanceof NullPointerException)
				throw new WebApplicationException(404);
			throw new WebApplicationException(500);
		}
		return Response.ok().build();
	}
}
