package ro.ldir.beans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import ro.ldir.dto.Organization;
import ro.ldir.dto.User;

/**
 * Session Bean implementation class OrganizationManager
 */
@Stateless
@LocalBean
public class OrganizationManager implements OrganizationManagerLocal {
	@PersistenceContext(unitName = "ldir")
	private EntityManager em;

	public OrganizationManager() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ro.ldir.beans.OrganizationManagerLocal#addOrganization(int,
	 * ro.ldir.dto.Organization)
	 */
	@Override
	public void addOrganization(int userId, Organization organization) {
		User user = em.find(User.class, userId);
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
		return em.find(Organization.class, organizationId);
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
		existing.copyFields(organization);
		System.out.println(existing.getName());
		em.merge(existing);
		em.flush();
	}
}
