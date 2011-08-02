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
 *  Filename: GarbageOperations.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.tests.garbage;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ro.ldir.dto.Garbage;
import ro.ldir.tests.helper.DatabaseHelper;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

/**
 * Tester for garbage operations.
 */
public class GarbageOperations extends GarbageTest {
	/** The picture to assign to the test garbage. */
	private static final String GARBAGE_PIC = "P1050120.JPG";
	/** The ID of the inserted garbage at each test. */
	private int garbageId = -1;
	private int imageIndex = 0;

	/** The inserted garbage at each test. */
	private Garbage insertedGarbage = null;

	@Test
	public void getGarbage() {
		System.out.println("get");
		WebResource r = client.resource(location + "/" + garbageId);
		ClientResponse cr = r.accept(MediaType.APPLICATION_XML).get(
				ClientResponse.class);
		assertEquals(200, cr.getStatus());
		assertFalse(cr.getEntity(Garbage.class) == null);
	}

	@Test
	public void getGarbageInsertedBy() {
		System.out.println("get");
		WebResource r = client.resource(location + "/" + garbageId);
		ClientResponse cr = r.accept(MediaType.APPLICATION_XML).get(
				ClientResponse.class);
		Garbage garbage = cr.getEntity(Garbage.class);
		assertEquals(USER, garbage.getInsertedBy().getEmail());
	}

	@Test
	public void getGarbageCountyArea() {
		System.out.println("get");
		WebResource r = client.resource(location + "/" + garbageId);
		ClientResponse cr = r.accept(MediaType.APPLICATION_XML).get(
				ClientResponse.class);
		Garbage garbage = cr.getEntity(Garbage.class);
		assertEquals(countyArea.getName(), garbage.getCounty().getName());
	}

	@Test
	public void imageDisplay() throws IOException {
		File picture = new File(GARBAGE_PIC);
		if (!picture.exists()) {
			System.err.println(GARBAGE_PIC + " does not exist, skipping test!");
			return;
		}
		FormDataMultiPart fdmp = new FormDataMultiPart();
		fdmp.bodyPart(new FileDataBodyPart("file", picture));

		WebResource r = client.resource(location + "/" + garbageId + "/image");

		ClientResponse cr = r.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(
				ClientResponse.class, fdmp);
		assertEquals(200, cr.getStatus());

		r = client.resource(location + "/" + garbageId + "/image/" + imageIndex
				+ "/display");
		imageIndex++;
		cr = r.get(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Test
	public void imageThumb() throws IOException {
		File picture = new File(GARBAGE_PIC);
		if (!picture.exists()) {
			System.err.println(GARBAGE_PIC + " does not exist, skipping test!");
			return;
		}
		FormDataMultiPart fdmp = new FormDataMultiPart();
		fdmp.bodyPart(new FileDataBodyPart("file", picture));

		WebResource r = client.resource(location + "/" + garbageId + "/image");

		ClientResponse cr = r.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(
				ClientResponse.class, fdmp);
		assertEquals(200, cr.getStatus());

		r = client.resource(location + "/" + garbageId + "/image/" + imageIndex
				+ "/thumb");
		imageIndex++;
		cr = r.get(ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Before
	public void insertGarbage() throws ClassNotFoundException, SQLException {
		insertedGarbage = new Garbage();
		insertedGarbage.setX(5);
		insertedGarbage.setY(5);
		ClientResponse cr = resource.entity(insertedGarbage,
				MediaType.APPLICATION_XML).post(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		Connection c = DatabaseHelper.getDbConnection();
		PreparedStatement s = c
				.prepareStatement("SELECT * FROM GARBAGE WHERE INSERTEDBY=?");
		s.setInt(1, userId);
		ResultSet rs = s.executeQuery();
		assertTrue(rs.next());
		garbageId = rs.getInt("GARBAGEID");
		assertFalse(rs.next());

	}

	@Test
	public void pictureInsert() throws IOException {
		File picture = new File(GARBAGE_PIC);
		if (!picture.exists()) {
			System.err.println(GARBAGE_PIC + " does not exist, skipping test!");
			return;
		}
		FormDataMultiPart fdmp = new FormDataMultiPart();
		fdmp.bodyPart(new FileDataBodyPart("file", picture));

		WebResource r = client.resource(location + "/" + garbageId + "/image");

		ClientResponse cr = r.type(MediaType.MULTIPART_FORM_DATA_TYPE).post(
				ClientResponse.class, fdmp);
		assertEquals(200, cr.getStatus());

		r = client
				.resource(location + "/" + garbageId + "/image/" + imageIndex);
		imageIndex++;
		cr = r.get(ClientResponse.class);
		assertEquals(200, cr.getStatus());

		File received = cr.getEntity(File.class);
		received.deleteOnExit();

		assertEquals(picture.length(), received.length());

		FileInputStream orig = new FileInputStream(picture);
		FileInputStream recv = new FileInputStream(received);
		byte bufOrig[] = new byte[4096], bufRecv[] = new byte[4096];

		while (orig.read(bufOrig) > 0 && recv.read(bufRecv) > 0)
			assertArrayEquals(bufOrig, bufRecv);
	}

	@After
	public void removeGarbage() throws ClassNotFoundException, SQLException {
		Connection c = DatabaseHelper.getDbConnection();
		PreparedStatement s = c
				.prepareStatement("DELETE FROM GARBAGE WHERE INSERTEDBY=?");
		s.setInt(1, userId);
		s.executeUpdate();
	}

	@Test
	public void updateCoordinates() {
		WebResource r = client.resource(location + "/" + garbageId);
		System.out.println("updated " + location + "/" + garbageId);
		insertedGarbage.setX(4);
		ClientResponse cr = r
				.entity(insertedGarbage, MediaType.APPLICATION_XML).post(
						ClientResponse.class);
		assertEquals(200, cr.getStatus());
	}

	@Test
	public void updateNoCounty() {
		WebResource r = client.resource(location + "/" + garbageId);
		insertedGarbage.setX(100);
		ClientResponse cr = r
				.entity(insertedGarbage, MediaType.APPLICATION_XML).post(
						ClientResponse.class);
		assertEquals(400, cr.getStatus());
	}
}
