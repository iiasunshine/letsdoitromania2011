package ro.ldir.beans;

import java.util.List;

import javax.ejb.Local;

import ro.ldir.dto.Equipment;
import ro.ldir.dto.Team;
import ro.ldir.exceptions.InvalidTeamOperationException;

@Local
public interface TeamManagerLocal {
	/**
	 * Add a new equipment to a team.
	 * 
	 * @param teamId
	 *            The team to whom the equipment belongs.
	 * @param equipment
	 *            The equipment.
	 */
	void addEquipment(int teamId, Equipment equipment);

	/**
	 * Adds a charting area assignment for the specified team.
	 * 
	 * @param teamId
	 *            the team ID to assignment to.
	 * @param chartAreaId
	 *            the chart area ID to assign to the team.
	 */
	public void assignChartArea(int teamId, int chartAreaId);

	/**
	 * Create a new team managed by the user {@code userId}. The manager does
	 * not actively participate in the team until it registers to the team.
	 * 
	 * @param team
	 *            The new team;
	 * @throws InvalidTeamOperationException
	 *             if the user cannot handle that team.
	 */
	public void createTeam(Team team);

	/**
	 * Delete an equipment from a team.
	 * 
	 * @param teamId
	 *            The team to delete the equipment.
	 * @param equipmentId
	 *            The equipment ID to delete.
	 */
	public void deleteEquipment(int teamId, int equipmentId);

	/**
	 * Delete a team.
	 * 
	 * @param teamId
	 *            The team ID to delete.
	 */
	public void deleteTeam(int teamId);

	/**
	 * Enroll a organization in a team.
	 * 
	 * @param organizationId
	 *            The organization ID to enroll.
	 * @param teamId
	 *            The team ID to enroll in.
	 * @throws InvalidTeamOperationException
	 *             if the user cannot join that team.
	 */
	public void enrollOrganization(int organizationId, int teamId)
			throws InvalidTeamOperationException;

	/**
	 * Enroll a user in a team.
	 * 
	 * @param userId
	 *            The user ID to enroll.
	 * @param teamId
	 *            The team ID to enroll in.
	 * @throws InvalidTeamOperationException
	 *             if the organization cannot join that team.
	 */
	public void enrollUser(int userId, int teamId)
			throws InvalidTeamOperationException;

	/**
	 * Finds a team using the team ID.
	 * 
	 * @param teamId
	 *            The team ID to search.
	 * @return The team corresponding to the ID.
	 */
	public Team getTeam(int teamId);

	/**
	 * Finds a team using the team's name.
	 * 
	 * @param name
	 *            The team to search for.
	 * @return The teams matching the name.
	 */
	public List<Team> getTeamByName(String name);

	/**
	 * Removes an existing charting area assignment for the specified team.
	 * 
	 * @param teamId
	 *            the team ID to remove the assignment.
	 * @param chartAreaId
	 *            the chart area ID to remove from the team.
	 */
	public void removeChartAreaAssignment(int teamId, int chartAreaId);

	/**
	 * Remove an equipment from a team.
	 * 
	 * @param teamId
	 *            The team ID to whom the equipment belonged.
	 * @param equipmentId
	 *            The equipment ID to remove.
	 */
	public void removeEquipment(int teamId, int equipmentId);

	/**
	 * Updates a team with existing values.
	 * 
	 * @param teamId
	 *            The team to update.
	 * @param team
	 *            The new properties.
	 */
	public void updateTeam(int teamId, Team team);

	/**
	 * Withdraw a organization from a team.
	 * 
	 * @param organizationId
	 *            The organization ID to withdraw.
	 * @param teamId
	 *            The team ID to withdraw from.
	 */
	public void withdrawOrganization(int organizationId, int teamId);

	/**
	 * Withdraw a user from a team.
	 * 
	 * @param userId
	 *            The user ID to withdraw.
	 * @param teamId
	 *            The team ID to withdraw from.
	 */
	public void withdrawUser(int userId, int teamId);
}
