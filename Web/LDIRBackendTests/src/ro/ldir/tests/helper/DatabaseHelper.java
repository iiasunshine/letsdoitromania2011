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
 *  Filename: DatabaseHelper.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.tests.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Configuration settings for the database.
 */
public class DatabaseHelper {
	/** The host where the database is located. */
	public static final String DB_HOST = "localhost";
	/** The database name. */
	public static final String DB_NAME = "ldir";
	/** The password to access the database. */
	public static final String DB_PASSWORD = "ldir";
	/** The username to access the database. */
	public static final String DB_USER = "ldir";

	/**
	 * Gets a connection to the database.
	 * 
	 * @return The connection to the database.
	 * @throws ClassNotFoundException
	 *             If the MySQL connector cannot be found.
	 * @throws SQLException
	 *             If the connection cannot be established.
	 */
	public static Connection getDbConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connect = DriverManager.getConnection("jdbc:mysql://"
				+ DB_HOST + "/" + DB_NAME + "?" + "user=" + DB_USER
				+ "&password=" + DB_PASSWORD);
		return connect;
	}
}
