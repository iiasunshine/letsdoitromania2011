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
 *  Filename: UserManagementTest.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.ws.tests;

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.ldir.dto.User;

import com.sun.jersey.api.Responses;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.Base64;

/**
 * Tests for the user management web service.
 */
public class UserManagementTest {
	private static Client client;
	private static final String location = "http://localhost:8080/LDIRBackend/ws/user";
	private static WebResource resource;
	User user1;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		client = Client.create();
		resource = client.resource(location);
	}

	private static Builder rootBuilder(String user, String password) {
		return resource.header(HttpHeaders.AUTHORIZATION,
				"Basic "
						+ new String(Base64.encode(user + ":" + password),
								Charset.forName("ASCII")));
	}

	@Test
	public void unauthorizedAddUser() {
		User user = new User();
		user.email = "user1@ldir.ro";
		user.passwd = "user1";

		ClientResponse cr = rootBuilder("admin@admin", "pfffassword111")
				.entity(user, MediaType.APPLICATION_XML).post(
						ClientResponse.class);
		assertEquals(401, cr.getStatus()); // unauthorized
	}

	@Test
	public void addEmptyUser() {
		User user = new User();

		ClientResponse cr = rootBuilder("admin@admin.com", "password").entity(
				user, MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(500, cr.getStatus());
	}

	@Before
	public void authorizedAddUser() {
		user1 = new User();
		user1.email = "user1@ldir.ro";
		user1.passwd = "admin1";

		rootBuilder("admin@admin.com", "password").entity(user1,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
	}

	@Test
	public void addDuplicateUser() {
		ClientResponse cr = rootBuilder("admin@admin.com", "password").entity(
				user1, MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(Responses.CONFLICT, cr.getStatus());
	}
}
