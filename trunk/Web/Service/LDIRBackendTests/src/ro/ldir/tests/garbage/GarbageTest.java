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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ro.ldir.dto.CountyArea;
import ro.ldir.dto.User;
import ro.ldir.tests.helper.DatabaseHelper;
import ro.ldir.tests.helper.UserSetup;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.core.util.Base64;

/**
 * Abstract class that provides helper methods for the garbage tests.
 */
public abstract class GarbageTest {
	protected static Client client;
	protected static int countyAreaId;
	private static final String geoLocation = "http://localhost:8080/LDIRBackend/ws/geo";
	protected static final String location = "http://localhost:8080/LDIRBackend/ws/garbage";
	protected static final String baseLocation = "http://localhost:8080/LDIRBackend/";
	protected static WebResource resource;
	protected static final String USER = GarbageInsertTest.class.getName();
	protected static int userId;

	@BeforeClass
	public static void insertCountyArea() throws ClassNotFoundException,
			SQLException {
		ArrayList<Point2D.Double> polyline = new ArrayList<Point2D.Double>();
		polyline.add(new Point2D.Double(0, 0));
		polyline.add(new Point2D.Double(0, 10));
		polyline.add(new Point2D.Double(10, 10));
		polyline.add(new Point2D.Double(10, 0));

		CountyArea countyArea = new CountyArea();
		countyArea.setPolyline(polyline);
		countyArea.setName("County");

		WebResource resource = client.resource(geoLocation + "/countyArea");

		ClientResponse cr = resourceBuilder(resource, USER).entity(countyArea,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		Connection c = DatabaseHelper.getDbConnection();
		PreparedStatement s = c
				.prepareStatement("SELECT * FROM CLOSEDAREA WHERE "
						+ "AREATYPE = 'CountyArea' AND BOTTOMRIGHTX = 10 "
						+ "AND BOTTOMRIGHTY = 0 AND TOPLEFTX = 0 "
						+ "AND TOPLEFTY = 10");
		ResultSet rs = s.executeQuery();
		assertTrue(rs.next());
		countyAreaId = rs.getInt("AREAID");
		assertFalse(rs.next());
	}

	protected static void removeAllGarbages() throws ClassNotFoundException,
			SQLException {
		Connection c = DatabaseHelper.getDbConnection();
		PreparedStatement s = c
				.prepareStatement("DELETE FROM GARBAGE WHERE INSERTEDBY=?");
		s.setInt(1, userId);
		s.executeUpdate();
	}

	@AfterClass
	public static void removeCountyArea() {
		WebResource resource = client.resource(geoLocation + "/countyArea/"
				+ countyAreaId);
		ClientResponse r = resourceBuilder(resource, USER).delete(
				ClientResponse.class);
		assertEquals(200, r.getStatus());
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
		ClientConfig cc = new DefaultClientConfig();
		cc.getClasses().add(GarbageContextProvider.class);
		client = Client.create(cc);
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
