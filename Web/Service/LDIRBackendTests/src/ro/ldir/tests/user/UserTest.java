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
 *  Filename: UserTests.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.tests.user;

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

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.User;
import ro.ldir.tests.helper.DatabaseHelper;
import ro.ldir.tests.helper.UserSetup;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.Base64;

/**
 * Test for team operations
 */
public abstract class UserTest {
	protected static final String BASE = "http://localhost:8080/LDIRBackend/ws/";
	protected static int chartAreaId;
	protected static Client client;
	protected static int countyAreaId;
	protected static final String USER = UserTest.class.getName();
	static int userId;

	protected static User getUser() {
		WebResource r = client.resource(BASE + "user/" + userId);
		ClientResponse cr = resourceBuilder(r).get(ClientResponse.class);
		assertEquals(200, cr.getStatus());
		return cr.getEntity(User.class);
	}

	public static void insertChartedArea() throws ClassNotFoundException,
			SQLException {
		ArrayList<Point2D.Double> polyline = new ArrayList<Point2D.Double>();
		polyline.add(new Point2D.Double(0, 0));
		polyline.add(new Point2D.Double(0, 10));
		polyline.add(new Point2D.Double(10, 10));
		polyline.add(new Point2D.Double(10, 0));

		ChartedArea chartedArea = new ChartedArea();
		chartedArea.setPolyline(polyline);
		chartedArea.setScore(-10);

		WebResource r = client.resource(BASE + "geo/chartedArea");

		ClientResponse cr = resourceBuilder(r).entity(chartedArea,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		Connection c = DatabaseHelper.getDbConnection();
		PreparedStatement s = c
				.prepareStatement("SELECT * FROM CLOSEDAREA WHERE "
						+ "AREATYPE = 'ChartedArea' AND BOTTOMRIGHTX = 10 "
						+ "AND BOTTOMRIGHTY = 0 AND TOPLEFTX = 0 "
						+ "AND TOPLEFTY = 10");
		ResultSet rs = s.executeQuery();
		assertTrue(rs.next());
		chartAreaId = rs.getInt("AREAID");
		assertFalse(rs.next());
	}

	public static void removeChartedArea() {
		WebResource r = client
				.resource(BASE + "geo/chartedArea/" + chartAreaId);
		ClientResponse cr = resourceBuilder(r).delete(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	protected static Builder resourceBuilder(WebResource resource) {
		return resource.header(
				HttpHeaders.AUTHORIZATION,
				"Basic "
						+ new String(Base64.encode(USER + ":" + USER), Charset
								.forName("ASCII"))).accept(
				MediaType.APPLICATION_XML);
	}

	@BeforeClass
	public static void setupClient() throws ClassNotFoundException,
			SQLException {
		client = Client.create();
		userId = UserSetup.addTestUser(USER, User.SecurityRole.ADMIN);
		insertChartedArea();
	}

	@AfterClass
	public static void tearDownUser() throws ClassNotFoundException,
			SQLException {
		removeChartedArea();
		UserSetup.removeTestUser(USER);
	}
}
