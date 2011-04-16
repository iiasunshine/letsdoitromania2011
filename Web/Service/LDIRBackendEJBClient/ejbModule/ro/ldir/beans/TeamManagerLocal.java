package ro.ldir.beans;

import java.util.List;

import javax.ejb.Local;

import ro.ldir.dto.Team;
import ro.ldir.exceptions.InvalidTeamOperationException;

@Local
public interface TeamManagerLocal {
	/**
	 * Create a new team managed by the user {@code userId}. The manager does
	 * not actively participate in the team until it registers to the team.
	 * 
	 * @param userId
	 *            The user managing the team.
	 * @param team
	 *            The new team;
	 * @throws InvalidTeamOperationException
	 *             if the user cannot handle that team.
	 */
	public void createTeam(int userId, Team team)
			throws InvalidTeamOperationException;

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
