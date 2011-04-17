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
import ro.ldir.dto.User;

/**
 * Session Bean implementation class OrganizationManager
 */
@Stateless
@LocalBean
@DeclareRoles("ADMIN")
public class OrganizationManager implements OrganizationManagerLocal {
	@PersistenceContext(unitName = "ldir")
	private EntityManager em;

	@EJB
	private UserManager userManager;

	@Resource
	private SessionContext ctx;

	public OrganizationManager() {
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
		organization.setContactUser(user);
		em.merge(user);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.OrganizationManagerLocal#deleteOrganization(int)
	 */
	@Override
	public void deleteOrganization(int organizationId) {
		Organization existing = em.find(Organization.class, organizationId);
		User user = existing.getContactUser();
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
				.checkAccessToUser(userManager, org.getContactUser(), ctx);
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
		SecurityHelper.checkUser(existing.getContactUser(), ctx);
		existing.copyFields(organization);
		System.out.println(existing.getName());
		em.merge(existing);
		em.flush();
	}
}
