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
 *  Filename: DtoAnnotationTest.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.ws.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ro.ldir.dto.Team;
import ro.ldir.dto.User;

/**
 * Test case for DTO Annotations.
 */
public class DtoAnnotationTest {
	@Test
	public void checkBasicDifferentFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team2.teamName = "Bla";
		team1.teamId = 1;
		team2.teamId = 2;
		assertFalse(team1.fieldsEqual(team2));
	}

	@Test
	public void checkBasicEqualFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team2.teamName = "Bla";
		team1.teamId = 1;
		team2.teamId = 1;
		assertTrue(team1.fieldsEqual(team2));
	}

	@Test
	public void checkNonTransferableFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team1.teamId = 1;
		team1.leader = new User();
		team2.teamId = 2;
		team2.copyFields(team1);
		assertFalse(team1.fieldsEqual(team2));
	}

	@Test
	public void checkSecondUserEqualFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team2.teamName = "Bla";
		team1.teamId = 1;
		team2.teamId = 1;
		team2.leader = new User();
		assertFalse(team1.fieldsEqual(team2));
	}

	@Test
	public void checkStringDifferentFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team2.teamName = "Bla2";
		team1.teamId = 1;
		team2.teamId = 1;
		assertFalse(team1.fieldsEqual(team2));
	}

	@Test
	public void checkStringEqualFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team2.teamName = "Bla";
		team1.teamId = 1;
		team2.teamId = 1;
		assertTrue(team1.fieldsEqual(team2));
	}

	@Test
	public void checkTransferFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team1.teamId = 1;
		team2.teamId = 1;
		team1.leader = team2.leader = new User();
		team2.copyFields(team1);
		assertTrue(team1.fieldsEqual(team2));
	}

	@Test
	public void checkTransferNoUserFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team1.teamId = 1;
		team1.leader = new User();
		team2.copyFields(team1);
		assertFalse(team1.fieldsEqual(team2));
	}

	@Test
	public void checkUserDifferentFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team2.teamName = "Bla";
		team1.teamId = 1;
		team2.teamId = 1;
		team1.leader = new User();
		team2.leader = new User();
		assertFalse(team1.fieldsEqual(team2));
	}

	@Test
	public void checkUserEqualFields() {
		Team team1 = new Team(), team2 = new Team();
		team1.teamName = "Bla";
		team2.teamName = "Bla";
		team1.teamId = 1;
		team2.teamId = 1;
		team1.leader = team2.leader = new User();
		assertTrue(team1.fieldsEqual(team2));
	}
}
