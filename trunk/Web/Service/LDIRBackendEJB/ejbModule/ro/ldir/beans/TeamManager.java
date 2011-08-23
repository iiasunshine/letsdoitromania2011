package ro.ldir.beans;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import ro.ldir.beans.security.SecurityHelper;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CleaningEquipment;
import ro.ldir.dto.Equipment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;
import ro.ldir.dto.User;
import ro.ldir.exceptions.ChartedAreaAssignmentException;
import ro.ldir.exceptions.InvalidTeamOperationException;

/**
 * A session bean managing teams.
 */
@Stateless
@LocalBean
@DeclareRoles({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
public class TeamManager implements TeamManagerLocal {
	private static Logger log = Logger.getLogger(TeamManager.class.getName());

	/**
	 * Return the name of the default team for the specified user.
	 * 
	 * @param user
	 *            The user to build the default team name.
	 * @return The default team name.
	 */
	public static String defaultTeamName(User user) {
		return user.getLastName() + " "
				+ Integer.toHexString(user.getEmail().hashCode());
	}

	@Resource
	private SessionContext ctx;

	@PersistenceContext(unitName = "ldir")
	private EntityManager em;

	@Resource
	private Integer maxChartedAreaPerPerson;

	@Resource
	private Integer maxPersonsPerChartedArea;

	@EJB
	private UserManager userManager;

	public TeamManager() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#addCleaningEquipment(int,
	 * ro.ldir.dto.CleaningEquipment)
	 */
	@Override
	public void addCleaningEquipment(int teamId, CleaningEquipment equipment) {
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, team, ctx);
		team.getEquipments().add(equipment);
		equipment.setTeamOwner(team);
		em.merge(team);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#addGpsEquipment(int,
	 * ro.ldir.dto.GpsEquipment)
	 */
	@Override
	public void addGpsEquipment(int teamId, GpsEquipment equipment) {
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, team, ctx);
		team.getEquipments().add(equipment);
		equipment.setTeamOwner(team);
		em.merge(team);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#addTransportEquipment(int,
	 * ro.ldir.dto.TransportEquipment)
	 */
	@Override
	public void addTransportEquipment(int teamId, TransportEquipment equipment) {
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
	public void assignChartArea(int teamId, int chartAreaId)
			throws ChartedAreaAssignmentException {
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, team, ctx);
		ChartedArea closedArea = em.find(ChartedArea.class, chartAreaId);
		if (team.getChartedAreas() == null)
			team.setChartedAreas(new HashSet<ChartedArea>());
		if (closedArea.getChartedBy() == null)
			closedArea.setChartedBy(new HashSet<Team>());

		if (closedArea.countChartingPeople() > maxPersonsPerChartedArea)
			throw new ChartedAreaAssignmentException(
					"There are too many people charting this area already!");

		if (team.getChartedAreas().size() > team.countMembers()
				* maxChartedAreaPerPerson)
			throw new ChartedAreaAssignmentException(
					"There are too many charted areas for this team!");

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
		em.persist(team);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#deleteEquipment(int, int)
	 */
	@Override
	public void deleteEquipment(int teamId, int equipmentIdx) {
		Team team = em.find(Team.class, teamId);
		SecurityHelper.checkTeamManager(userManager, team, ctx);
		team.getEquipments().remove(equipmentIdx);
		em.merge(team);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.TeamManagerLocal#deleteEquipment(int, int)
	 */
	@Override
	public void deleteEquipmentById(int teamId, int equipmentId) {
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

		Query query = em
				.createQuery("DELETE FROM Equipment e WHERE e.teamOwner = :team");
		query.setParameter("team", team);
		query.executeUpdate();

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
		SecurityHelper.checkUser(organization.getManager(), ctx);
		if (organization.getMemberOf() != null)
			throw new InvalidTeamOperationException(
					"The organization cannot participate in several teams.");
		Team team = em.find(Team.class, teamId);
		organization.setMemberOf(team);
		team.getOrganizationMembers().add(organization);
		em.merge(team);
		em.merge(organization);
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
		Team existingTeam = user.getMemberOf();
		if (existingTeam != null) {
			if (!existingTeam.getTeamName().equals(defaultTeamName(user)))
				throw new InvalidTeamOperationException(
						"The user cannot participate in several teams.");
			log.info("Removing " + user.getEmail() + " from default team "
					+ existingTeam.getTeamName());
			existingTeam.getVolunteerMembers().remove(user);
			user.setMemberOf(null);
			em.merge(existingTeam);
		}
		Team team = em.find(Team.class, teamId);
		user.setMemberOf(team);
		team.getVolunteerMembers().add(user);
		em.merge(team);
		em.merge(user);
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

	private Team getTeamByExactName(String nameParam) {
		Query query = em
				.createQuery("SELECT x FROM Team x WHERE x.teamName LIKE :nameParam");
		query.setParameter("nameParam", nameParam);
		return (Team) query.getSingleResult();
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
	 * @see
	 * ro.ldir.beans.TeamManagerLocal#reportAssignedChartedAreas(java.util.Set,
	 * java.util.Set, java.util.Set)
	 */
	@SuppressWarnings("unchecked")
	@Override
	@RolesAllowed({ "ADMIN", "ORGANIZER", "ORGANIZER_MULTI" })
	public List<Team> reportAssignedChartedAreas(Set<String> counties,
			Set<String> chartedAreaNames, Set<Integer> userIds) {
		StringBuffer buf = new StringBuffer();

		buf.append("SELECT t FROM Team t INNER JOIN t.chartedAreas ca ");
		if (userIds != null && userIds.size() > 0)
			buf.append("INNER JOIN t.teamManager u ");
		if (counties != null && counties.size() > 0)
			buf.append("INNER JOIN ca.county c ");
		if ((userIds != null && userIds.size() > 0)
				|| (counties != null && counties.size() > 0)
				|| (chartedAreaNames != null && chartedAreaNames.size() > 0))
			buf.append("WHERE ");
		if (chartedAreaNames != null && chartedAreaNames.size() > 0)
			buf.append("ca.name IN :chartedAreaNames");
		if (userIds != null && userIds.size() > 0)
			buf.append("u.userId IN :userIds");
		if (counties != null && counties.size() > 0)
			buf.append("ca.county IN :counties");

		Query query = em.createQuery(buf.toString());
		if (chartedAreaNames != null && chartedAreaNames.size() > 0)
			query.setParameter("chartedAreaNames", chartedAreaNames);
		if (userIds != null && userIds.size() > 0)
			query.setParameter("userIds", userIds);
		if (counties != null && counties.size() > 0)
			query.setParameter("counties", counties);

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
		em.merge(team);
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

		Team defaultTeam = null;
		try {
			defaultTeam = getTeamByExactName(defaultTeamName(user));
		} catch (NoResultException e) {
			log.info("The default team (" + defaultTeamName(user) + ") for "
					+ user.getEmail() + "was not found");
		}

		user.setMemberOf(defaultTeam);
		team.getVolunteerMembers().remove(user);
		if (defaultTeam != null) {
			log.info("Moving " + user.getEmail()
					+ " back to the default team (" + defaultTeam.getTeamName()
					+ ")");
			defaultTeam.getVolunteerMembers().add(user);
			em.merge(defaultTeam);
		}
		em.merge(team);
		em.merge(user);
	}
}
