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
 *  Filename: GarbageTest.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.tests.garbage;

import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.ws.rs.core.HttpHeaders;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ro.ldir.dto.User;
import ro.ldir.tests.helper.DatabaseHelper;
import ro.ldir.tests.helper.UserSetup;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.Base64;

/**
 * Abstract class that provides helper methods for the garbage tests.
 */
public abstract class GarbageTest {
	protected static Client client;
	protected static final String location = "http://localhost:8080/LDIRBackend/ws/garbage";
	protected static WebResource resource;
	protected static final String USER = GarbageInsertTest.class.getName();
	protected static int userId;

	protected static void removeAllGarbages() throws ClassNotFoundException,
			SQLException {
		Connection c = DatabaseHelper.getDbConnection();
		PreparedStatement s = c
				.prepareStatement("DELETE FROM GARBAGE WHERE INSERTEDBY=?");
		s.setInt(1, userId);
		s.executeUpdate();
	}

	protected static Builder resourceBuilder(WebResource resource, String user) {
		return resource.header(
				HttpHeaders.AUTHORIZATION,
				"Basic "
						+ new String(Base64.encode(user + ":" + user), Charset
								.forName("ASCII")));
	}

	protected static Builder rootBuilder(String user) {
		return resource.header(
				HttpHeaders.AUTHORIZATION,
				"Basic "
						+ new String(Base64.encode(user + ":" + user), Charset
								.forName("ASCII")));
	}

	@BeforeClass
	public static void setupClient() throws Exception {
		client = Client.create();
		resource = client.resource(location);
	}

	@BeforeClass
	public static void setupUser() throws ClassNotFoundException, SQLException {
		userId = UserSetup.addTestUser(USER, User.SecurityRole.ADMIN);
	}

	@AfterClass
	public static void tearDownUser() throws ClassNotFoundException,
			SQLException {
		removeAllGarbages();
		UserSetup.removeTestUser(USER);
	}

	/** The web resource different for each test. */
	protected WebResource instanceResource;

	protected Builder instanceBuilder(String user) {
		return instanceResource.header(
				HttpHeaders.AUTHORIZATION,
				"Basic "
						+ new String(Base64.encode(user + ":" + user), Charset
								.forName("ASCII")));
	}
}
