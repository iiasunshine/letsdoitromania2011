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

import javax.ws.rs.core.MediaType;

import org.junit.Test;

import ro.ldir.dto.Garbage;

import com.sun.jersey.api.client.ClientResponse;

/**
 * Tester for inserting garbages.
 */
public class GarbageInsertTest extends GarbageTest {
	@Test
	public void insertGarbage() {
		Garbage garbage = new Garbage();
		garbage.setX(5);
		garbage.setY(5);
		ClientResponse cr = resource.entity(garbage, MediaType.APPLICATION_XML)
				.post(ClientResponse.class);
		assertEquals(200, cr.getStatus());
		String id = cr.getEntity(String.class);
		System.out.println("Inserted garbage " + id);
	}

	@Test
	public void insertNoCounty() {
		Garbage garbage = new Garbage();
		garbage.setX(100);
		garbage.setY(100);
		ClientResponse cr = resource.entity(garbage, MediaType.APPLICATION_XML)
				.post(ClientResponse.class);
		assertEquals(400, cr.getStatus());
	}

	@Test
	/* This must be executed after loading all Romanian counties */
	public void insertTimis() {
		Garbage garbage = new Garbage();
		garbage.setX(21.26648);
		garbage.setY(45.80465);
		garbage.setDispersed(true);
		garbage.setDescription("descrierea");
		garbage.setBagCount(2);
		garbage.setPercentagePlastic(80);
		garbage.setPercentageMetal(5);
		garbage.setPercentageGlass(5);
		garbage.setPercentageWaste(10);
		ClientResponse cr = resource.entity(garbage, MediaType.APPLICATION_XML)
				.post(ClientResponse.class);
		assertEquals(200, cr.getStatus());
		String id = cr.getEntity(String.class);
		System.out.println("Inserted Timis garbage " + id);
	}

}
