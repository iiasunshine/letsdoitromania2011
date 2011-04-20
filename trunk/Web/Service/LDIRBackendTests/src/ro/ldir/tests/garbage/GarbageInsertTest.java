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
 *  Filename: GarbageInsertTest.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.tests.garbage;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.ws.rs.core.MediaType;

import org.junit.AfterClass;
import org.junit.Test;

import ro.ldir.dto.Garbage;
import ro.ldir.tests.helper.DatabaseHelper;

import com.sun.jersey.api.client.ClientResponse;

/**
 * Tester for inserting garbages.
 */
public class GarbageInsertTest extends GarbageTest {
	@Test
	public void unauthorizedInsertGarbage() {
		Garbage garbage = new Garbage();
		ClientResponse cr = rootBuilder("blah").entity(garbage,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(401, cr.getStatus());
	}

	@Test
	public void insertGarbage() {
		Garbage garbage = new Garbage();
		ClientResponse cr = rootBuilder(USER).entity(garbage,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@AfterClass
	public static void dbCleanup() throws ClassNotFoundException, SQLException {
		Connection c = DatabaseHelper.getDbConnection();
		PreparedStatement s = c
				.prepareStatement("DELETE FROM Garbage WHERE INSERTEDBY=?");
		s.setInt(1, userId);
		s.executeUpdate();
	}
}
