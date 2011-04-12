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

import java.util.Collection;
import java.util.List;

import javax.ejb.Local;

import ro.ldir.dto.Garbage;
import ro.ldir.dto.User;
import ro.ldir.exceptions.InvalidUserException;

@Local
public interface UserManagerLocal {
	public void addNewGarbage(int userId, Garbage garbage);

	public void addUser(User user) throws InvalidUserException;

	public Collection<Garbage> getGarbages(int userId);

	public User getUser(int userId);

	public List<User> getUsers(String town);

	public List<User> getUsers(User.Activity activity);

	public List<User> getUsers(User.Type type);

	public void setUserActivities(int userId, List<User.Activity> activities);

	public void setUserStatus(int userId, User.Status status);

	public void updateUser(int userId, User user);
}
