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
 *  Filename: UserManagerLocal.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.beans;

import java.util.List;
import java.util.Set;

import javax.ejb.Local;

import ro.ldir.dto.User;
import ro.ldir.dto.User.Activity;
import ro.ldir.dto.User.SecurityRole;
import ro.ldir.exceptions.InvalidTokenException;
import ro.ldir.exceptions.InvalidUserException;

/**
 * The local business interface of the bean managing users.
 * 
 * @see ro.ldir.beans.UserManager
 */
@Local
public interface UserManagerLocal {

	/**
	 * Marks a user as registered.
	 * 
	 * @param userId
	 *            The user id to change.
	 * @param key
	 *            The key to mark the user as registered.
	 * @throws InvalidUserException
	 *             If the user is in an invalid state.
	 */
	public void activateUser(int userId, String key)
			throws InvalidUserException;

	/**
	 * Inserts a new user in the system in the system.
	 * 
	 * @param user
	 *            The new insert to be inserted in the system.
	 * @throws InvalidUserException
	 *             When the insertion cannot be completed successfully. This can
	 *             be triggered for instance by a duplicate email address.
	 */
	public void addUser(User user) throws InvalidUserException;

	/**
	 * Returns the User object corresponding to a given user ID.
	 * 
	 * @param userId
	 *            The user to search against.
	 * @return The user object if found, {@code null} if none found.
	 */
	public User getUser(int userId);

	/**
	 * Get a user by email.
	 * 
	 * @param email
	 *            The email of the user.
	 * @return The user matching the email, {@code null} if none.
	 */
	public User getUser(String email);

	/**
	 * A list of users that have a given role
	 * 
	 * @param role
	 *            The role to search against.
	 * @return The list of users of the given type.
	 */
	public List<User> getUsers(SecurityRole role);

	/**
	 * Returns a list of users in a town.
	 * 
	 * @param town
	 *            The town of interest.
	 * @return A list of users in the given town.
	 */
	public List<User> getUsers(String town);

	/**
	 * Returns a list of users enrolled to a certain activity.
	 * 
	 * @param activity
	 *            The activity to search against.
	 * @return The list of users in the given activity.
	 */
	public List<User> getUsers(User.Activity activity);

	/**
	 * An invalid access has been detected for the specified user.
	 * 
	 * @param email
	 *            The email of the user with invalid access.
	 */
	public void invalidAccess(String email);

	/**
	 * Generates a password reset token. The token is sent via email to the
	 * user.
	 * 
	 * @param email
	 *            The email of the user for which the token should be generated.
	 */
	public void passwdResetToken(String email);

	/**
	 * Lists all users that match the given parameters. Use {@code null} if you
	 * do not want to use any of the query parameters.
	 * 
	 * @param counties
	 *            Returns all users that live in one of the specified counties.
	 * @param birthYears
	 *            Returns all users that were born in one of the specified
	 *            years.
	 * @param roles
	 *            Returns all users that are in the specified role.
	 * @param minGarbages
	 *            Returns all users that inserted a minimum amount of garbages.
	 * @param maxGarbages
	 *            Returns all users that inserted a maximal amount of garbagee.
	 * @return The list of users matching the provided criteria.
	 */
	public List<User> report(Set<String> counties, Set<Integer> birthYears,
			Set<String> roles, Integer minGarbages, Integer maxGarbages);

	/**
	 * Search for all users whose email match.
	 * 
	 * @param email
	 *            The email to search for.
	 * @return A list of users matching the email
	 */
	public List<User> searchByEmail(String email);

	/**
	 * Updates the password of a user. The token must match the one set in the
	 * database. It must be set with {@link #passwdResetToken(int)}.
	 * 
	 * @param userId
	 *            The user ID to set the password for.
	 * @param token
	 *            The token registered in the database.
	 * @param password
	 *            The new password.
	 * @throws InvalidTokenException
	 *             if the token doesn't match the DB entry.
	 */
	public void setPassword(int userId, String token, String password)
			throws InvalidTokenException;

	/**
	 * Configures an user for a given set of activities. The current activities
	 * will overwrite all previous activities for which the user was enrolled.
	 * 
	 * @param userId
	 *            The user ID to configure.
	 * @param activities
	 *            The new activities to set.
	 */
	public void setUserActivities(int userId, List<Activity> activities);

	/**
	 * Sets the user's security role.
	 * 
	 * @param user
	 *            Id The user to change
	 * @param role
	 *            The new security role.
	 */
	public void setUserRole(int userId, SecurityRole role);

	/**
	 * Timestamps the last access property of the provided user.
	 * 
	 * @param user
	 */
	public void timestampLastAccess(User user);

	/**
	 * Updates a user with a new object. All previous fields of the user are
	 * overwritten.
	 * 
	 * @param userId
	 *            The user ID to update.
	 * @param user
	 *            The new configuration for the user.
	 */
	public void updateUser(int userId, User user);
}
