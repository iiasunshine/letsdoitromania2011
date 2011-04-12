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
 *  Filename: UserManager.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.beans;

import java.util.Collection;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.dto.Garbage;
import ro.ldir.dto.User;
import ro.ldir.dto.User.Activity;
import ro.ldir.dto.User.Status;
import ro.ldir.dto.User.Type;
import ro.ldir.exceptions.InvalidUserException;

/**
 * Session Bean implementation class UserManager
 */
@Stateless
@LocalBean
public class UserManager implements UserManagerLocal {
	@PersistenceContext(unitName = "ldir")
	EntityManager em;

	/**
	 * Default constructor.
	 */
	public UserManager() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void addNewGarbage(int userId, Garbage garbage) {
		User user = em.find(User.class, userId);
		garbage.insertBy = user;
		user.garbages.add(garbage);
		em.merge(user);
	}

	@Override
	public void addUser(User user) throws InvalidUserException {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.email = :emailParam");
		query.setParameter("emailParam", user.email);
		if (query.getResultList().size() > 0)
			throw new InvalidUserException("Email " + user.email
					+ " already in use.");
		em.persist(user);
	}

	@Override
	public Collection<Garbage> getGarbages(int userId) {
		User user = em.find(User.class, userId);
		return user.garbages;
	}

	@Override
	public User getUser(int id) {
		return em.find(User.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers(Activity activity) {
		Query query = em
				.createQuery("SELECT x FROM User x, IN(x.activities) y WHERE y = :activityParam");
		query.setParameter("activityParam", activity);
		return (List<User>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers(String town) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.town = :townParam");
		query.setParameter("townParam", town);
		return (List<User>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers(Type type) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.type = :typeParam");
		query.setParameter("typeParam", type);
		return (List<User>) query.getResultList();
	}

	@Override
	public void setUserActivities(int userId, List<Activity> activities) {
		User user = em.find(User.class, userId);
		user.activities = activities;
		em.merge(user);
	}

	@Override
	public void setUserStatus(int userId, Status status) {
		User user = em.find(User.class, userId);
		user.status = status;
		em.merge(user);
	}

	@Override
	public void updateUser(int userId, User user) {
		User existing = em.find(User.class, userId);
		existing.update(user);
		em.merge(existing);
	}
}
