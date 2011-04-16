package ro.ldir.beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.ldir.exceptions.InvalidTeamOperationException;

/**
 * A session bean managing teams.
 */
@Stateless
@LocalBean
public class TeamManager implements TeamManagerLocal {
	@PersistenceContext(unitName = "ldir")
	private EntityManager em;

	public TeamManager() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#createTeam(int, ro.ldir.dto.Team)
	 */
	@Override
	public void createTeam(int userId, Team team)
			throws InvalidTeamOperationException {
		User user = em.find(User.class, userId);
		if (user.getManagedTeams() != null && !user.getManagedTeams().isEmpty()
				&& !user.isMultiRole())
			throw new InvalidTeamOperationException(
					"The user cannot manage so many teams.");
		team.setTeamManager(user);
		user.getManagedTeams().add(team);
		em.merge(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#deleteTeam(int)
	 */
	@Override
	public void deleteTeam(int teamId) {
		Team existing = em.find(Team.class, teamId);
		existing.getTeamManager().getManagedTeams().remove(existing);
		em.remove(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#enrollOrganization(int, int)
	 */
	@Override
	public void enrollOrganization(int organizationId, int teamId)
			throws InvalidTeamOperationException {
		Organization organization = em.find(Organization.class, organizationId);
		if (organization.getMemberOf() != null)
			throw new InvalidTeamOperationException(
					"The organization cannot participate in several teams.");
		Team team = em.find(Team.class, teamId);
		organization.setMemberOf(team);
		team.getOrganizationMembers().add(organization);
		em.merge(team);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#enrollUser(int, int)
	 */
	@Override
	public void enrollUser(int userId, int teamId)
			throws InvalidTeamOperationException {
		User user = em.find(User.class, userId);
		if (user.getMemberOf() != null)
			throw new InvalidTeamOperationException(
					"The user cannot participate in several teams.");
		Team team = em.find(Team.class, teamId);
		user.setMemberOf(team);
		team.getVolunteerMembers().add(user);
		em.merge(team);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#getTeam(int)
	 */
	@Override
	public Team getTeam(int teamId) {
		return em.find(Team.class, teamId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#getTeamByName(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Team> getTeamByName(String nameParam) {
		Query query = em
				.createQuery("SELECT x FROM Team x WHERE x.teamName LIKE :nameParam");
		query.setParameter("nameParam", "%" + nameParam + "%");
		return query.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#updateTeam(int, ro.ldir.dto.Team)
	 */
	@Override
	public void updateTeam(int teamId, Team team) {
		Team existing = em.find(Team.class, teamId);
		existing.copyFields(team);
		em.merge(existing);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#withdrawOrganization(int, int)
	 */
	@Override
	public void withdrawOrganization(int organizationId, int teamId) {
		Organization organization = em.find(Organization.class, organizationId);
		Team team = em.find(Team.class, teamId);
		organization.setMemberOf(null);
		team.getOrganizationMembers().remove(organization);
		em.merge(organization);
		// TODO chack whether the organization is affected
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#withdrawUser(int, int)
	 */
	@Override
	public void withdrawUser(int userId, int teamId) {
		User user = em.find(User.class, userId);
		Team team = em.find(Team.class, teamId);
		user.setMemberOf(null);
		team.getVolunteerMembers().remove(user);
		em.merge(team);
		// TODO chack whether the user is affected
	}
}
