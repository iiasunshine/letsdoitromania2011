package ro.ldir.beans;

import java.util.List;

import javax.ejb.Local;

import ro.ldir.dto.Organization;
import ro.ldir.exceptions.InvalidTeamOperationException;

@Local
public interface OrganizationManagerLocal {
	/**
	 * Adds a new organization in the database and enrolls it in the user's
	 * <b>only</b> team.
	 * 
	 * @param organization
	 *            The organization to add in the database.
	 * @throws InvalidTeamOperationException
	 *             If the user manages 0 or more than one team.
	 */
	public void addEnrollOrganization(Organization organization)
			throws InvalidTeamOperationException;

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
