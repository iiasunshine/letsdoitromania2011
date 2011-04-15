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
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.ldir.dto.User;

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
	private static final String ws = "http://localhost:8080/LDIRBackend/ws/user";
	private static WebResource wsResource;
	private static final String reg = "http://localhost:8080/LDIRBackend/reg/ws";
	private static WebResource regResource;
	private static final String password1 = "password";
	User user1;
	private static final String password2 = "password2";
	User user2;
	int user1id, user2id;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		client = Client.create();
		wsResource = client.resource(ws);
		regResource = client.resource(reg);
	}

	private static Builder build(WebResource resource, User user) {
		return resource.header(HttpHeaders.AUTHORIZATION, "Basic "
				+ new String(Base64.encode(user.getEmail() + ":" + user.getPasswd()),
						Charset.forName("ASCII")));
	}

	private static Builder build(WebResource resource, User user, String path) {
		return resource.path(path).header(
				HttpHeaders.AUTHORIZATION,
				"Basic "
						+ new String(Base64.encode(user.getEmail() + ":"
								+ user.getPasswd()), Charset.forName("ASCII")));
	}

	// @Test
	// public void unauthorizedAddUser() {
	// User user = new User();
	// user.email = "user1@ldir.ro";
	// user.passwd = "user1";
	//
	// ClientResponse cr = rootBuilder("admin@admin", "pfffassword111")
	// .entity(user, MediaType.APPLICATION_XML).post(
	// ClientResponse.class);
	// assertEquals(401, cr.getStatus()); // unauthorized
	// }

	// @Test
	// public void addEmptyUser() {
	// User user = new User();
	//
	// ClientResponse cr = rootBuilder("admin@admin.com", "password").entity(
	// user, MediaType.APPLICATION_XML).post(ClientResponse.class);
	// assertEquals(500, cr.getStatus());
	// }

	@Before
	public void addUser1() {
		user1 = new User();
		user1.setEmail("user1@ldir.ro");
		user1.setPasswd(password1);

		build(regResource, user1).entity(user1, MediaType.APPLICATION_XML)
				.post(ClientResponse.class);

		String userId = build(wsResource, user1, "").get(String.class);
		System.out.println("got user ID " + userId);
		user1id = new Integer(userId).intValue();
		System.out.println("user 1: " + user1id);
		assertTrue(user1id != -1);
	}

	@Test
	public void getAgainstMe() {
		String userId = build(wsResource, user1, "").get(String.class);
		System.out.println("got user ID " + userId);
		assertTrue(!userId.equals("-1"));
		ClientResponse cr = build(wsResource, user1, "/" + user1id).get(
				ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Test
	public void getAgainstOther() {
		String userId = build(wsResource, user1, "").get(String.class);
		System.out.println("got user ID " + userId);
		assertTrue(!userId.equals("-1"));
		ClientResponse cr = build(wsResource, user1, "/" + user1id).get(
				ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Before
	public void addUser2() {
		user2 = new User();
		user2.setEmail("user2@ldir.ro");
		user2.setPasswd(password2);

		build(regResource, user2).entity(user2, MediaType.APPLICATION_XML)
				.post(ClientResponse.class);

		String userId = build(wsResource, user2, "").get(String.class);
		System.out.println("got user ID " + userId);
		user2id = new Integer(userId).intValue();
		System.out.println("user 2: " + user2id);
		assertTrue(user2id != -1);
	}
}
