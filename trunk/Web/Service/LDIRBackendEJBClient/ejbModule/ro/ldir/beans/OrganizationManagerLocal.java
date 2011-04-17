package ro.ldir.beans;

import java.util.List;

import javax.ejb.Local;

import ro.ldir.dto.Organization;

@Local
public interface OrganizationManagerLocal {
	/**
	 * Add a new organization in the database.
	 * 
	 * @param organization
	 *            The organization to enter.
	 */
	public void addOrganization(Organization organization);

	/**
	 * Delete an existing organization.
	 * 
	 * @param organizationId
	 *            The organization to delete.
	 */
	public void deleteOrganization(int organizationId);

	/**
	 * Get an organization by ID.
	 * 
	 * @param organizationId
	 *            The ID of the organization.
	 * @return The organization matching the ID.
	 */
	public Organization getOrganization(int organizationId);

	/**
	 * Get an organization by its contact user.
	 * 
	 * @param User
	 *            The user of the organization.
	 * @return The organization matching the user.
	 */
	public List<Organization> getOrganizationByUser(int userId);

	/**
	 * Update an existing organization.
	 * 
	 * @param organizationId
	 *            The id of the organization to update.
	 * @param organization
	 *            The new properties of the organization.
	 */
	public void updateOrganization(int organizationId, Organization organization);
}
