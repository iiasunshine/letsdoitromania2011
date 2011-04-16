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

import java.util.Date;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.dto.User;
import ro.ldir.dto.User.Activity;
import ro.ldir.dto.User.SecurityRole;
import ro.ldir.dto.helper.SHA256Encrypt;
import ro.ldir.exceptions.InvalidUserException;

/**
 * Session bean managing users.
 * 
 * A user is defined by the {@link ro.ldir.dto.User} entity bean.
 * 
 * The exported methods are defined by the
 * {@link ro.ldir.beans.UserManagerLocal} interface. Access to this bean is
 * exported by the webservice defined by the {@link ro.ldir.ws.UserWebService}.
 * 
 * @see ro.ldir.dto.User
 * @see ro.ldir.beans.UserManagerLocal
 * @see ro.ldir.ws.UserWebService
 */
@Stateless
@LocalBean
public class UserManager implements UserManagerLocal {
	@PersistenceContext(unitName = "ldir")
	private EntityManager em;

	/** Default constructor. */
	public UserManager() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#activateUser(int, java.lang.String)
	 */
	@Override
	public void activateUser(int userId, String key)
			throws InvalidUserException {
		User existing = em.find(User.class, userId);
		if (existing.getStatus() != User.UserStatus.PENDING)
			throw new InvalidUserException("The user is not pending.");
		existing.setStatus(User.UserStatus.REGISTERED);
		em.merge(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#addUser(ro.ldir.dto.User)
	 */
	@Override
	public void addUser(User user) throws InvalidUserException {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.email = :emailParam");
		query.setParameter("emailParam", user.getEmail());
		if (query.getResultList().size() > 0)
			throw new InvalidUserException("Email " + user.getEmail()
					+ " already in use.");
		user.setStatus(User.UserStatus.PENDING);
		user.setRole(User.SecurityRole.VOLUNTEER.toString());
		user.setRegistrationToken(SHA256Encrypt.encrypt(new Date() + user.getEmail()));
		em.persist(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#getUser(int)
	 */
	@Override
	public User getUser(int id) {
		return em.find(User.class, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#getUser(java.lang.String)
	 */
	@Override
	public User getUser(String email) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.email = :emailParam");
		query.setParameter("emailParam", email);
		return (User) query.getSingleResult();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#getUsers(ro.ldir.dto.User.Activity)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers(Activity activity) {
		Query query = em
				.createQuery("SELECT x FROM User x, IN(x.activities) y WHERE y = :activityParam");
		query.setParameter("activityParam", activity);
		return (List<User>) query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#getUsers(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers(String town) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.town = :townParam");
		query.setParameter("townParam", town);
		return (List<User>) query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#getUsers(ro.ldir.dto.User.Type)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<User> getUsers(User.SecurityRole role) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.role = :roleParam");
		query.setParameter("roleParam", role);
		return (List<User>) query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#setUserActivities(int,
	 * java.util.List)
	 */
	@Override
	public void setUserActivities(int userId, List<Activity> activities) {
		User user = em.find(User.class, userId);
		user.setActivities(activities);
		em.merge(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#setUserRole(int,
	 * ro.ldir.dto.User.SecurityRole)
	 */
	@Override
	public void setUserRole(int userId, SecurityRole role) {
		User existing = em.find(User.class, userId);
		existing.setRole(role.toString());
		em.merge(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#setUserStatus(int,
	 * ro.ldir.dto.User.Status)
	 */
	@Override
	public void setUserStatus(int userId, User.UserStatus status) {
		User user = em.find(User.class, userId);
		user.setStatus(status);
		em.merge(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#updateUser(int, ro.ldir.dto.User)
	 */
	@Override
	public void updateUser(int userId, User user) {
		User existing = em.find(User.class, userId);
		existing.copyFields(user);
		em.merge(existing);
	}
}
