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
 *  Filename: GarbageManager.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */

package ro.ldir.tests.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import ro.ldir.dto.User;
import ro.ldir.dto.helper.SHA256Encrypt;

/**
 * Sets up a temporary user in the database.
 * 
 */
public class UserSetup {
	public static void addTestUser(String username, User.SecurityRole role)
			throws ClassNotFoundException, SQLException {
		Connection c = DatabaseHelper.getDbConnection();

		PreparedStatement s = c.prepareStatement("INSERT INTO User SET "
				+ " email=?, passwd=?, role=?");
		s.setString(1, username);
		s.setString(2, SHA256Encrypt.encrypt(username));
		s.setString(3, role.toString());
		s.executeUpdate();
	}

	public static void removeTestUser(String username)
			throws ClassNotFoundException, SQLException {
		Connection c = DatabaseHelper.getDbConnection();

		PreparedStatement s = c.prepareStatement("DELETE FROM User WHERE "
				+ " email=?");
		s.setString(1, username);
		s.executeUpdate();
	}
}
