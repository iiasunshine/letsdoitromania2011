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
import java.sql.SQLException;

import javax.ws.rs.core.HttpHeaders;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ro.ldir.dto.User;
import ro.ldir.tests.helper.UserSetup;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.Base64;

/**
 * Abstract class that provides helper methods for the garbage tests.
 */
public abstract class GarbageTest {
	protected static final String USER = GarbageInsertTest.class.getName();
	protected static int userId;
	protected static Client client;
	protected static final String location = "http://localhost:8080/LDIRBackend/ws/garbage";
	protected static WebResource resource;

	@BeforeClass
	public static void setupUser() throws ClassNotFoundException, SQLException {
		userId = UserSetup.addTestUser(USER, User.SecurityRole.ADMIN);
	}

	@BeforeClass
	public static void setupClient() throws Exception {
		client = Client.create();
		resource = client.resource(location);
	}

	protected static Builder rootBuilder(String user) {
		return resource.header(
				HttpHeaders.AUTHORIZATION,
				"Basic "
						+ new String(Base64.encode(user + ":" + user), Charset
								.forName("ASCII")));
	}

	@AfterClass
	public static void tearDownUser() throws ClassNotFoundException,
			SQLException {
		UserSetup.removeTestUser(USER);
	}
}
