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

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response.Status;

import ro.ldir.beans.GarbageManagerLocal;
import ro.ldir.dto.Garbage;
import ro.ldir.exceptions.NoCountyException;

/** Serves anonymous calls to handle garbages. */
@Path("ws")
public class GarbageAnonymousWebService {
	private GarbageManagerLocal garbageManager;

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
}
