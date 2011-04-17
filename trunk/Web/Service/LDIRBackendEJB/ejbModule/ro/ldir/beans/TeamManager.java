package ro.ldir.beans;

import java.util.HashSet;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.beans.security.SecurityHelper;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Equipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.ldir.exceptions.InvalidTeamOperationException;

/**
 * A session bean managing teams.
 */
@Stateless
@LocalBean
@DeclareRoles("ADMIN")
public class TeamManager implements TeamManagerLocal {
	@Resource
	private SessionContext ctx;

	@PersistenceContext(unitName = "ldir")
	private EntityManager em;

	@EJB
	private UserManager userManager;

	public TeamManager() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#addEquipment(int,
	 * ro.ldir.dto.Equipment)
	 */
	@Override
	public void addEquipment(int teamId, Equipment equipment) {
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, team, ctx);
		team.getEquipments().add(equipment);
		equipment.setTeamOwner(team);
		em.merge(team);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#assignChartArea(int, int)
	 */
	@Override
	public void assignChartArea(int teamId, int chartAreaId) {
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, team, ctx);
		ChartedArea closedArea = em.find(ChartedArea.class, chartAreaId);
		if (team.getChartedAreas() == null)
			team.setChartedAreas(new HashSet<ChartedArea>());
		if (closedArea.getChartedBy() == null)
			closedArea.setChartedBy(new HashSet<Team>());
		team.getChartedAreas().add(closedArea);
		closedArea.getChartedBy().add(team);
		em.merge(team);
		em.merge(closedArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#createTeam(ro.ldir.dto.Team)
	 */
	@Override
	public void createTeam(Team team) {
		User user = SecurityHelper.getMultiUser(userManager, ctx);
		team.setTeamManager(user);
		user.getManagedTeams().add(team);
		em.merge(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#deleteEquipment(int, int)
	 */
	@Override
	public void deleteEquipment(int teamId, int equipmentId) {
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, team, ctx);
		Equipment equipment = em.find(Equipment.class, equipmentId);
		team.getEquipments().remove(equipment);
		em.merge(team);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#deleteTeam(int)
	 */
	@Override
	public void deleteTeam(int teamId) {
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, team, ctx);
		team.getTeamManager().getManagedTeams().remove(team);
		em.remove(team);
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
		SecurityHelper.checkUser(organization.getContactUser(), ctx);
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
		SecurityHelper.checkUser(user, ctx);
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
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamMember(userManager, team, ctx);
		return team;
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
	 * @see ro.ldir.beans.TeamManagerLocal#removeChartAreaAssignment(int, int)
	 */
	@Override
	public void removeChartAreaAssignment(int teamId, int chartAreaId) {
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, team, ctx);
		ChartedArea closedArea = em.find(ChartedArea.class, chartAreaId);
		team.getChartedAreas().remove(closedArea);
		closedArea.getChartedBy().remove(team);
		em.merge(team);
		em.merge(closedArea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#removeEquipment(int, int)
	 */
	@Override
	public void removeEquipment(int teamId, int equipmentId) {
		Team team = em.find(Team.class, teamId);
		Equipment equipment = em.find(Equipment.class, equipmentId);
		team.getEquipments().remove(equipment);
		em.merge(team);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#updateTeam(int, ro.ldir.dto.Team)
	 */
	@Override
	public void updateTeam(int teamId, Team team) {
		Team existing = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, existing, ctx);
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
		SecurityHelper.checkTeamManagerOrOrganization(userManager, team,
				organization, ctx);
		organization.setMemberOf(null);
		team.getOrganizationMembers().remove(organization);
		em.merge(organization);
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
		SecurityHelper.checkTeamManagerOrMember(userManager, team, user, ctx);
		user.setMemberOf(null);
		team.getVolunteerMembers().remove(user);
		em.merge(team);
	}
}
