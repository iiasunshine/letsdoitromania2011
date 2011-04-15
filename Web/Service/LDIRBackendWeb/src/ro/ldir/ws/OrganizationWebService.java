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
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;

import ro.ldir.beans.OrganizationManagerLocal;
import ro.ldir.beans.UserManagerLocal;
import ro.ldir.dto.Organization;
import ro.ldir.ws.helper.SecurityHelper;

/**
 * The organization web service.
 */
@Path("organization")
public class OrganizationWebService {
	@SuppressWarnings("unused")
	@Context
	private UriInfo context;

	private OrganizationManagerLocal orgManager;
	private UserManagerLocal userManager;

	public OrganizationWebService() throws NamingException {
		InitialContext ic = new InitialContext();
		userManager = (UserManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/UserManager!ro.ldir.beans.UserManager");
		orgManager = (OrganizationManagerLocal) ic
				.lookup("java:global/LDIRBackend/LDIRBackendEJB/OrganizationManager!ro.ldir.beans.OrganizationManager");
	}

	@POST
	@Consumes({ "application/json", "application/xml" })
	public Response addOrganization(Organization organization,
			@Context SecurityContext sc) {
		orgManager.addOrganization(SecurityHelper.getUserId(userManager, sc),
				organization);
		return Response.ok().build();
	}

	@DELETE
	@Path("{organizationId:[0-9]+}/")
	public Response deleteOrganization(
			@PathParam("organizationId") int organizationId,
			@Context SecurityContext sc) {
		Organization existing = orgManager.getOrganization(organizationId);
		if (!SecurityHelper.checkUserOrAdmin(userManager, sc, existing
				.getContactUser().getUserId()))
			throw new WebApplicationException(401);
		orgManager.deleteOrganization(organizationId);
		return Response.ok().build();
	}

	@GET
	@Produces({ "application/json", "application/xml" })
	@Path("{organizationId:[0-9]+}/")
	public Organization getOrganization(
			@PathParam("organizationId") int organizationId,
			@Context SecurityContext sc) {
		Organization organization = orgManager.getOrganization(organizationId);
		if (!SecurityHelper.checkUserOrAdmin(userManager, sc, organization
				.getContactUser().getUserId()))
			throw new WebApplicationException(401);
		return organization;
	}

	@PUT
	@Consumes({ "application/json", "application/xml" })
	@Path("{organizationId:[0-9]+}/")
	public Response updateOrganization(
			@PathParam("organizationId") int organizationId,
			Organization organization, @Context SecurityContext sc) {
		Organization existing = orgManager.getOrganization(organizationId);
		if (!SecurityHelper.checkUserOrAdmin(userManager, sc, existing
				.getContactUser().getUserId()))
			throw new WebApplicationException(401);
		orgManager.updateOrganization(organizationId, organization);
		return Response.ok().build();
	}
}
