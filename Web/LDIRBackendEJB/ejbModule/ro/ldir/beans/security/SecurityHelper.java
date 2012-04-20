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
package ro.ldir.beans.security;

import java.util.List;

import javax.ejb.SessionContext;
import javax.persistence.EntityManager;

import ro.ldir.beans.UserManager;
import ro.ldir.beans.UserManagerLocal;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;

/**
 * Helper class to test security roles.
 */
public class SecurityHelper {

	/**
	 * Check whether the logged in user has access to the user.
	 * 
	 * @param userManager
	 * @param user
	 * @param ctx
	 */
	public static void checkAccessToUser(UserManager userManager, User user,
			SessionContext ctx) {
		if (ctx.isCallerInRole(User.SecurityRole.ADMIN.toString()))
			return;
		String email = ctx.getCallerPrincipal().getName();
		if (user.getEmail().equals(email))
			return;
		User loggedInUser = userManager.getUser(email);

		if (user.getMemberOf().equals(loggedInUser.getMemberOf()))
			return;
		if (user.getManagedTeams().contains(loggedInUser.getMemberOf()))
			return;
		if (loggedInUser.getManagedTeams().contains(user.getMemberOf()))
			return;
		for (Organization org : user.getOrganizations())
			for (Team team : loggedInUser.getManagedTeams())
				if (team.getOrganizationMembers().contains(org))
					return;
		for (Organization org : loggedInUser.getOrganizations())
			for (Team team : user.getManagedTeams())
				if (team.getOrganizationMembers().contains(org))
					return;
		throw new SecurityException("Access to this user is denied.");
	}

	/**
	 * Checks whether the logged in user charts the given area.
	 * 
	 * @param um
	 * @param ctx
	 * @param chartedArea
	 */
	public static void checkIsCharting(UserManagerLocal um, SessionContext ctx,
			ChartedArea chartedArea) {
		if (ctx.isCallerInRole(User.SecurityRole.ADMIN.toString()))
			return;
		String email = ctx.getCallerPrincipal().getName();
		User user = um.getUser(email);
		for (Team area_team : chartedArea.getChartedBy()) {
			if (user.getMemberOf().equals(area_team))
				return;
			if (user.getManagedTeams().contains(area_team))
				return;
			for (Organization org : user.getOrganizations())
				if (org.getMemberOf().equals(area_team))
					return;
		}
		throw new SecurityException("The user is not charting this area.");
	}

	/**
	 * Checks whether the user is the team manager.
	 * 
	 * @param userManager
	 * @param team
	 * @param ctx
	 */
	public static void checkTeamManager(UserManager userManager, Team team,
			SessionContext ctx) {
		if (ctx.isCallerInRole(User.SecurityRole.ADMIN.toString()))
			return;
		String email = ctx.getCallerPrincipal().getName();
		User user = userManager.getUser(email);
		if (team.getTeamManager().equals(user))
			return;
		throw new SecurityException("The user does not manage the team.");
	}

	/**
	 * Check whether the logged in user is member of the team.
	 * 
	 * @param userManager
	 * @param team
	 * @param user
	 * @param ctx
	 */
	public static void checkTeamManagerOrMember(UserManager userManager,
			Team team, User user, SessionContext ctx) {
		if (ctx.isCallerInRole(User.SecurityRole.ADMIN.toString()))
			return;
		if (team.getTeamManager().equals(user))
			return;
		if (team.getVolunteerMembers().contains(user))
			return;
		throw new SecurityException("The user does not belong to the team.");
	}

	/**
	 * Checks whether the logged in user is either the team manager or an
	 * organization belonging to the team.
	 * 
	 * @param userManager
	 * @param team
	 * @param organization
	 * @param ctx
	 */
	public static void checkTeamManagerOrOrganization(UserManager userManager,
			Team team, Organization organization, SessionContext ctx) {
		if (ctx.isCallerInRole(User.SecurityRole.ADMIN.toString()))
			return;
		String email = ctx.getCallerPrincipal().getName();
		User user = userManager.getUser(email);
		if (team.getTeamManager().equals(user))
			return;
		for (Organization org : user.getOrganizations())
			if (team.getOrganizationMembers().contains(org))
				return;
		throw new SecurityException(
				"The organization does not belong to the team.");
	}

	/**
	 * Checks whether a user can access a team.
	 * 
	 * @param um
	 * @param team
	 * @param ctx
	 */
	public static void checkTeamMember(UserManagerLocal um, Team team,
			SessionContext ctx) {
		if (ctx.isCallerInRole(User.SecurityRole.ADMIN.toString()))
			return;
		String email = ctx.getCallerPrincipal().getName();
		User user = um.getUser(email);
		if (team.getTeamManager().equals(user))
			return;
		if (!team.getVolunteerMembers().contains(user))
			return;
		for (Organization org : team.getOrganizationMembers())
			if (org.getManager().equals(user))
				return;
		throw new SecurityException("The user is not member of this team.");
	}

	/**
	 * Check whether the logged in user is the given user.
	 * 
	 * @param user
	 * @param ctx
	 */
	public static void checkUser(User user, SessionContext ctx) {
		if (ctx.isCallerInRole(User.SecurityRole.ADMIN.toString()))
			return;
		if (ctx.isCallerInRole(User.SecurityRole.ORGANIZER.toString()))
			return;
		if (ctx.isCallerInRole(User.SecurityRole.ORGANIZER_MULTI.toString()))
			return;
		if (user.getEmail().equals(ctx.getCallerPrincipal().getName()))
			return;
		throw new SecurityException("Access to this user is denied.");
	}

	/**
	 * Filters the report on system users according to the security role of the
	 * caller.
	 * 
	 * @param em
	 * @param userManager
	 * @param ctx
	 * @param report
	 *            The original report.
	 * @return A filtered report.
	 */
	public static List<User> filterUserReport(EntityManager em,
			UserManager userManager, SessionContext ctx, List<User> report) {
		String email = ctx.getCallerPrincipal().getName();
		User user = userManager.getUser(email);
		
		if(true)
			return report;
		
		if (user.getRole().equals(User.SecurityRole.ADMIN.toString()))
			return report;
		for (User reportedUser : report) {
			if (reportedUser.isProfileView())
				continue;
			em.detach(reportedUser);
			reportedUser.setEmail("N/A");
			reportedUser.setPhone("N/A");
			if (reportedUser.getLastName() == null
					|| reportedUser.getLastName().length() == 0)
				continue;
			reportedUser
					.setLastName(reportedUser.getLastName().substring(0, 1));
		}
		return report;
	}

	/**
	 * Return the user if the logged in user is multi manager.
	 * 
	 * @param userManager
	 * @param ctx
	 * @return the user entity if the logged in user is a multi manager.
	 */
	public static User getMultiUser(UserManager userManager, SessionContext ctx) {
		String email = ctx.getCallerPrincipal().getName();
		User user = userManager.getUser(email);

		if (user.getManagedTeams() != null && !user.getManagedTeams().isEmpty()
				&& !user.isMultiRole())
			throw new SecurityException("The user cannot manage so many teams.");

		return user;
	}

	/**
	 * Gets the user DTO for the logged in user.
	 * 
	 * @param userManager
	 * @param ctx
	 * @return the logged in user.
	 */
	public static User getUser(UserManager userManager, SessionContext ctx) {
		String email = ctx.getCallerPrincipal().getName();
		return userManager.getUser(email);
	}
}
