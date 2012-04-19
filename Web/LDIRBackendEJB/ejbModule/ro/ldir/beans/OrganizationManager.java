package ro.ldir.beans;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ro.ldir.beans.security.SecurityHelper;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.ldir.exceptions.InvalidTeamOperationException;

/**
 * Session Bean implementation class OrganizationManager
 */
@Stateless
@LocalBean
@DeclareRoles("ADMIN")
public class OrganizationManager implements OrganizationManagerLocal {
	@Resource
	private SessionContext ctx;

	@PersistenceContext(unitName = "ldir")
	private EntityManager em;

	@EJB
	private UserManager userManager;

	public OrganizationManager() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.ldir.beans.OrganizationManagerLocal#addEnrollOrganization(ro.ldir.
	 * dto.Organization)
	 */
	@Override
	public void addEnrollOrganization(Organization organization)
			throws InvalidTeamOperationException {
		User user = userManager.getUser(ctx.getCallerPrincipal().getName());
		if (user.getManagedTeams() == null
				|| user.getManagedTeams().size() == 0)
			throw new InvalidTeamOperationException("User " + user.getEmail()
					+ " does not manage any teams.");
		if (user.getManagedTeams().size() > 1)
			throw new InvalidTeamOperationException("User " + user.getEmail()
					+ " manages " + user.getManagedTeams().size() + " teams");

		Team team = user.getManagedTeams().get(0);

		if (user.getOrganizations() == null)
			user.setOrganizations(new ArrayList<Organization>());
		user.getOrganizations().add(organization);
		organization.setManager(user);

		if (team.getOrganizationMembers() == null)
			team.setOrganizationMembers(new ArrayList<Organization>());
		team.getOrganizationMembers().add(organization);
		organization.setMemberOf(team);

		em.merge(user);
		em.merge(team);
		em.persist(organization);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.OrganizationManagerLocal#addOrganization(ro.ldir.dto.
	 * Organization)
	 */
	@Override
	public void addOrganization(Organization organization) {
		User user = userManager.getUser(ctx.getCallerPrincipal().getName());
		if (user.getOrganizations() == null)
			user.setOrganizations(new ArrayList<Organization>());
		user.getOrganizations().add(organization);
		organization.setManager(user);
		em.merge(user);
		em.persist(organization);
		em.flush();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.OrganizationManagerLocal#deleteOrganization(int)
	 */
	@Override
	public void deleteOrganization(int organizationId) {
		Organization existing = em.find(Organization.class, organizationId);
		User user = existing.getManager();
		SecurityHelper.checkUser(user, ctx);
		user.getOrganizations().remove(existing);
		em.remove(existing);
		em.merge(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.OrganizationManagerLocal#getOrganization(int)
	 */
	@Override
	public Organization getOrganization(int organizationId) {
		Organization org = em.find(Organization.class, organizationId);
		SecurityHelper
				.checkAccessToUser(userManager, org.getManager(), ctx);
		return org;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.OrganizationManagerLocal#getOrganizationByUser(int)
	 */
	@Override
	public List<Organization> getOrganizationByUser(int userId) {
		User user = em.find(User.class, userId);
		return user.getOrganizations();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.OrganizationManagerLocal#updateOrganization(int,
	 * ro.ldir.dto.Organization)
	 */
	@Override
	public void updateOrganization(int organizationId, Organization organization) {
		Organization existing = em.find(Organization.class, organizationId);
		SecurityHelper.checkUser(existing.getManager(), ctx);
		existing.copyFields(organization);
		em.merge(existing);
		em.flush();
	}
}
