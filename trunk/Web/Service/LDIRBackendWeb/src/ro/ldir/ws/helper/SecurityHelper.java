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
 *  Filename: SecurityHelper.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.ws.helper;

import javax.ws.rs.core.SecurityContext;

import ro.ldir.beans.UserManagerLocal;
import ro.ldir.dto.User;

/**
 * Helper class to test security roles.
 */
public class SecurityHelper {
	/**
	 * Check whether the user is an admin.
	 * 
	 * @param sc
	 *            The security context.
	 * @return {@code true} if security check passes, {@code false} otherwise.
	 */
	public static boolean checkAdmin(SecurityContext sc) {
		return sc.isUserInRole(User.SecurityRole.ADMIN.toString());
	}

	/**
	 * Checks whether the authenticated use is the same to the one whose fields
	 * are changed or whether the authenticated user is an admin.
	 * 
	 * @param um
	 *            The UserManager bean.
	 * @param sc
	 *            The security context.
	 * @param userId
	 *            The user being changed.
	 * @return {@code true} if security check passes, {@code false} otherwise.
	 */
	public static boolean checkUserOrAdmin(UserManagerLocal um,
			SecurityContext sc, int userId) {
		if (sc.isUserInRole(User.SecurityRole.ADMIN.toString()))
			return true;
		String email = sc.getUserPrincipal().getName();
		return um.getUser(email).getUserId() == userId;
	}

	/**
	 * Finds the user ID using the security context.
	 * 
	 * @param um
	 *            The UserManager bean.
	 * @param sc
	 *            The security context.
	 * @return The user ID of the logged user.
	 */
	public static int getUserId(UserManagerLocal um, SecurityContext sc) {
		String email = sc.getUserPrincipal().getName();
		return um.getUser(email).getUserId();
	}
}
