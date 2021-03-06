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

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Schedule;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import ro.ldir.beans.security.SecurityHelper;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.ldir.dto.User.Activity;
import ro.ldir.dto.User.SecurityRole;
import ro.ldir.dto.helper.SHA256Encrypt;
import ro.ldir.exceptions.InvalidTokenException;
import ro.ldir.exceptions.InvalidUserOperationException;

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
@DeclareRoles({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
public class UserManager implements UserManagerLocal {
	private static Logger log = Logger.getLogger(UserManager.class.getName());
	/**
	 * After this timeout, users that have not activated their account are
	 * deleted.
	 */
	@Resource
	private Integer activateTimeout;
	@Resource
	private SessionContext ctx;
	@PersistenceContext(unitName = "ldir")
	private EntityManager em;
	@Resource
	private Integer maxInvalidAccesses;
	@Resource
	private Integer resetTokenTimeout;

	@EJB
	private UserMailer userMailer;

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
			throws InvalidUserOperationException {
		User user = em.find(User.class, userId);
		if (!user.getRole().equals(SecurityRole.PENDING.toString()))
			throw new InvalidUserOperationException("The user is not pending.");
		user.setRole(SecurityRole.VOLUNTEER.toString());

		try {
			createDefaultTeam(user);
		} catch (Exception e) {
			log.warning("Unable to create a default team for user " + userId
					+ " (" + user.getEmail() + ")\n" + "Exception: "
					+ e.getClass().getName() + "\nMessage: " + e.getMessage());
			overwriteTeam(user);
		}
		em.merge(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#addUser(ro.ldir.dto.User)
	 */
	@Override
	public int addUser(User user) throws InvalidUserOperationException {
		String email = new String(user.getEmail());

		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.email = :emailParam");
		query.setParameter("emailParam", email);
		if (query.getResultList().size() > 0)
			throw new InvalidUserOperationException("Email " + email
					+ " already in use.");

		user.setRole(User.SecurityRole.PENDING.toString());
		user.setRegistrationToken(SHA256Encrypt.encrypt(new Date() + email));

		em.persist(user);
		em.flush();

		query = em
				.createQuery("SELECT x FROM User x WHERE x.email = :emailParam");
		query.setParameter("emailParam", email);
		user = (User) query.getSingleResult();

		activateUser(user.getUserId(), user.getRegistrationToken());

		return user.getUserId();
		// userMailer.sendWelcomeMessage(user.getEmail());
	}

	/**
	 * Create a default team for the specified user.
	 * 
	 * @param user
	 */
	private void createDefaultTeam(User user) {
		Team userTeam = new Team();
		userTeam.setTeamName(TeamManager.defaultTeamName(user));
		userTeam.setTeamManager(user);
		userTeam.setVolunteerMembers(new ArrayList<User>());
		userTeam.getVolunteerMembers().add(user);

		user.getManagedTeams().add(userTeam);
		user.setMemberOf(userTeam);
		em.persist(userTeam);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#getUser(int)
	 */
	@Override
	public User getUser(int id) {
		User user = em.find(User.class, id);
		SecurityHelper.checkAccessToUser(this, user, ctx);
		return user;
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

		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) query.getResultList();
		if (users == null || users.size() == 0)
			return null;
		User user = users.get(0);
		SecurityHelper.checkAccessToUser(this, user, ctx);
		return user;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#adminGetUser(java.lang.String)
	 */
	@Override
	public User adminGetUser(String email) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.email = :emailParam");
		query.setParameter("emailParam", email);

		@SuppressWarnings("unchecked")
		List<User> users = (List<User>) query.getResultList();
		if (users == null || users.size() == 0)
			return null;
		User user = users.get(0);
		return user;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#getUsers(ro.ldir.dto.User.Activity)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed("ADMIN")
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
	@RolesAllowed("ADMIN")
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
	@RolesAllowed("ADMIN")
	public List<User> getUsers(User.SecurityRole role) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.role = :roleParam");
		query.setParameter("roleParam", role);
		return (List<User>) query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#invalidAccess(java.lang.String)
	 */
	@Override
	public void invalidAccess(String email) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.email = :emailParam");
		query.setParameter("emailParam", email);
		User user;
		try {
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			return;
		}
		user.setInvalidAccessCount(user.getInvalidAccessCount() + 1);
		if (user.getInvalidAccessCount().equals(maxInvalidAccesses)) {
			log.info("Resseting account \"" + user.getEmail() + "\".");
			user.setPasswd(new Date().toString());
			user.setInvalidAccessCount(0);
			userMailer.sendAccountResetMessage(user.getEmail());
		}
		em.merge(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#mailGarbagePackage(int,
	 * java.lang.String, java.util.Set)
	 */
	@Override
	public void mailGarbagePackage(int userId, String origin,
			Set<Integer> garbageIds) {
		User user = getUser(userId);
		Set<Garbage> garbages = new HashSet<Garbage>();
		for (Integer garbageId : garbageIds)
			garbages.add(em.find(Garbage.class, garbageId));
		userMailer.sendGarbagePackage(user, origin, garbages);
	}

	/**
	 * Allocate an existing team to the specified user.
	 * 
	 * @param user
	 */
	private void overwriteTeam(User user) {
		// TODO this is a hack to prevent orphan teams.
		Query query = em.createQuery("SELECT t FROM TEAM t WHERE "
				+ "t.teamName = :teamName");
		query.setParameter("teamName", TeamManager.defaultTeamName(user));
		Team userTeam = (Team) query.getSingleResult();

		if (userTeam == null) {
			log.warning("Default team (" + TeamManager.defaultTeamName(user)
					+ ") not found, user " + user.getUserId() + "("
					+ user.getEmail() + ") will have no team.");
			return;
		}

		userTeam.setTeamManager(user);
		userTeam.setVolunteerMembers(new ArrayList<User>());
		userTeam.getVolunteerMembers().add(user);

		user.getManagedTeams().add(userTeam);
		user.setMemberOf(userTeam);

		log.info("Allocated existing team " + TeamManager.defaultTeamName(user)
				+ ") to user " + user.getUserId() + "(" + user.getEmail()
				+ ").");

		em.merge(userTeam);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#passwdResetToken(int)
	 */
	@Override
	public void passwdResetToken(String email) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.email = :emailParam");
		query.setParameter("emailParam", email);
		User user;
		try {
			user = (User) query.getSingleResult();
		} catch (NoResultException e) {
			throw new NullPointerException("User not found.");
		}
		user.setResetToken(SHA256Encrypt.encrypt(new Date() + user.getEmail()
				+ user.getUserId()));
		user.setResetDate(new Date());
		em.merge(user);
		em.flush();

		userMailer.sendResetToken(user);
	}

	/**
	 * Runs every hour to delete users that have not activated their account.
	 */
	@Schedule(hour = "*/6")
	public void prunePendingUsers() {
		Date threshold = new Date(System.currentTimeMillis() - activateTimeout
				* 3600 * 1000);
		Query query = em.createQuery("DELETE FROM User x WHERE "
				+ "x.role = 'PENDING' AND x.recordDate < :threshold");
		query.setParameter("threshold", threshold, TemporalType.TIMESTAMP);
		int affected = query.executeUpdate();
		log.info("Pruned " + affected + " pending users");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#report(java.util.Set, java.util.Set,
	 * java.util.Set, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
	public List<User> report(Set<String> counties, Set<Integer> birthYears,
			Set<String> roles, Integer minGarbages, Integer maxGarbages) {
		log.info("Requesting user report for counties=" + counties
				+ ", birthYears=" + birthYears + ", roles=" + roles + ", "
				+ minGarbages + " < garbages < " + maxGarbages);

		StringBuffer buf = new StringBuffer();

		if (minGarbages != null || maxGarbages != null)
			buf.append("SELECT u FROM Garbage g INNER JOIN g.insertedBy u ");
		else
			buf.append("SELECT u FROM User u ");

		if ((counties != null && counties.size() > 0)
				|| (birthYears != null && birthYears.size() > 0)
				|| (roles != null && roles.size() > 0))
			buf.append("WHERE ");
		if (counties != null && counties.size() > 0)
			buf.append("u.county IN :counties ");
		if (birthYears != null && birthYears.size() > 0) {
			if (counties != null && counties.size() > 0)
				buf.append("AND ");
			buf.append("(");
			for (int i = 0; i < birthYears.size(); i++) {
				if (birthYears.size() > 1 && i > 0)
					buf.append("OR ");
				buf.append("u.birthday BETWEEN :start" + i + " AND :stop" + i);
			}
			buf.append(") ");
		}
		if (roles != null && roles.size() > 0) {
			if ((counties != null && counties.size() > 0)
					|| (birthYears != null && birthYears.size() > 0))
				buf.append("AND ");
			buf.append("u.role in :roles ");
		}

		if (minGarbages != null || maxGarbages != null)
			buf.append("GROUP BY u.userId HAVING ");

		if (minGarbages != null) {
			buf.append("COUNT(g.garbageId) > :minGarbages ");
			if (maxGarbages != null)
				buf.append("AND ");
		}
		if (maxGarbages != null) {
			buf.append("COUNT(g.garbageId) < :maxGarbages ");
		}

		Query query = em.createQuery(buf.toString());

		if (counties != null && counties.size() > 0)
			query.setParameter("counties", counties);
		if (birthYears != null && birthYears.size() > 0) {
			int i = 0;
			for (Integer year : birthYears) {
				Date start = new GregorianCalendar(year, 1, 1, 0, 0).getTime();
				Date stop = new GregorianCalendar(year, 12, 31, 23, 59)
						.getTime();
				query.setParameter("start" + i, start);
				query.setParameter("stop" + i, stop);
				i++;
			}
		}
		if (roles != null && roles.size() > 0)
			query.setParameter("roles", roles);
		if (minGarbages != null)
			query.setParameter("minGarbages", minGarbages);
		if (maxGarbages != null)
			query.setParameter("maxGarbages", maxGarbages);

		List<User> report = query.getResultList();
		return SecurityHelper.filterUserReport(em, this, ctx, report);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#searchByEmail(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed("ADMIN")
	public List<User> searchByEmail(String email) {
		Query query = em
				.createQuery("SELECT x FROM User x WHERE x.email LIKE :emailParam");
		query.setParameter("emailParam", "%" + email + "%");
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#setPassword(int, java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void setPassword(int userId, String token, String password)
			throws InvalidTokenException {
		User user = em.find(User.class, userId);
		if (user.getResetToken() == null)
			throw new InvalidTokenException(
					"No password reset was requested for this user.");
		if (!user.getResetToken().equals(token))
			throw new InvalidTokenException("Reset tokens do not match.");

		Date threshold = new Date(System.currentTimeMillis()
				- resetTokenTimeout * 3600 * 1000);
		if (user.getResetDate().before(threshold))
			throw new InvalidTokenException("Reset token is too old.");

		user.setResetDate(null);
		user.setResetToken(null);
		user.setPasswd(SHA256Encrypt.encrypt(password));
		log.fine("Reset password for " + user.getEmail());
		em.merge(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#setPassword(int, java.lang.String)
	 */
	@Override
	public void setPassword(int userId, String newPassword) {
		User user = em.find(User.class, userId);
		if (user != null) {
			user.setPasswd(SHA256Encrypt.encrypt(newPassword));
			log.fine("set password for " + user.getEmail());
			em.merge(user);
		} else {
			throw new SecurityException(
					"The user cannot change password for id:" + user);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#setUserActivities(int,
	 * java.util.List)
	 */
	@Override
	public void setUserActivities(int userId, List<User.Activity> activities) {
		User user = em.find(User.class, userId);
		SecurityHelper.checkUser(user, ctx);
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
	@RolesAllowed({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
	public void setUserRole(int userId, User.SecurityRole role) {
		User existing = em.find(User.class, userId);
		existing.setRole(role.toString());
		em.merge(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.UserManagerLocal#timestampLastAccess(ro.ldir.dto.User)
	 */
	@Override
	public void timestampLastAccess(User user) {
		user.setLastAccess(new Date());
		user.setInvalidAccessCount(0);
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
		SecurityHelper.checkUser(existing, ctx);
		existing.copyFields(user);
		em.merge(existing);
	}
}
