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
 *  Filename: GarbageGeoTest.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.tests.garbage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Garbage;
import ro.ldir.tests.helper.DatabaseHelper;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

/**
 * Tests association between garbage tags and chart areas.
 */
public class GarbageGeoTest extends GarbageTest {

	private static int chartAreaId;
	private static final String geoLocation = "http://localhost:8080/LDIRBackend/ws/geo";

	@BeforeClass
	public static void insertChartedArea() throws ClassNotFoundException,
			SQLException {
		ArrayList<Point2D.Float> polyline = new ArrayList<Point2D.Float>();
		polyline.add(new Point2D.Float(0, 0));
		polyline.add(new Point2D.Float(0, 10));
		polyline.add(new Point2D.Float(10, 10));
		polyline.add(new Point2D.Float(10, 0));

		ChartedArea chartedArea = new ChartedArea();
		chartedArea.setPolyline(polyline);
		chartedArea.setScore(-10);

		WebResource resource = client.resource(geoLocation + "/chartedArea");

		ClientResponse cr = resourceBuilder(resource, USER).entity(chartedArea,
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

	@AfterClass
	public static void removeChartedArea() {
		WebResource resource = client.resource(geoLocation + "/chartedArea/"
				+ chartAreaId);
		ClientResponse r = resourceBuilder(resource, USER).delete(
				ClientResponse.class);
		assertEquals(200, r.getStatus());
	}

	@Before
	public void insertGarbage() {
		Garbage garbage = new Garbage();
		garbage.setX(5);
		garbage.setY(5);

		ClientResponse cr = rootBuilder(USER).entity(garbage,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Test
	public void testChartedAreaMembership() {
		instanceResource = client.resource(geoLocation + "/chartedArea/"
				+ chartAreaId);
		ChartedArea area = instanceBuilder(USER).get(ChartedArea.class);
		assertTrue(area.containsPoint(new Point2D.Float(5, 5)));
		// TODO: this this returns ID, thus cannot access the field like this
		// assertEquals(1, area.getGarbages().size());
	}

	@Test
	public void testBoundingBox() {
		String p = location + "/bbox";
		instanceResource = client.resource(p);

		MultivaluedMap<String, String> params = new MultivaluedMapImpl();
		params.add("topLeftX", "0");
		params.add("topLeftY", "10");
		params.add("bottomRightX", "10");
		params.add("bottomRightY", "0");

		ClientResponse cr = resourceBuilder(
				instanceResource.queryParams(params), USER).get(
				ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}
}
