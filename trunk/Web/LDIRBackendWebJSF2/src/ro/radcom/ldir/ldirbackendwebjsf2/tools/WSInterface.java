package ro.radcom.ldir.ldirbackendwebjsf2.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import ro.ldir.beans.GarbageManagerLocal;
import ro.ldir.beans.GeoManagerLocal;
import ro.ldir.beans.OrganizationManagerLocal;
import ro.ldir.beans.TeamManagerLocal;
import ro.ldir.beans.UserManagerLocal;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CleaningEquipment;
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.Garbage.GarbageStatus;
import ro.ldir.dto.GarbageEnrollment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;
import ro.ldir.dto.User;
import ro.ldir.exceptions.ChartedAreaAssignmentException;
import ro.ldir.exceptions.InvalidTeamOperationException;
import ro.ldir.exceptions.InvalidTokenException;
import ro.ldir.exceptions.InvalidUserException;
import ro.ldir.exceptions.NoCountyException;

/**
 * 
 * @author dan.grigore
 */
public class WSInterface {
	private GeoManagerLocal geoManager;
	private GarbageManagerLocal garbageManager;
	private TeamManagerLocal teamManager;
	private OrganizationManagerLocal orgManager;
	private UserManagerLocal userManager;

	public WSInterface(boolean custom) {

	}

	public WSInterface() {
		this(false);
	}

	public int addGarbage(User user, Garbage garbage) throws NoCountyException {
		if (garbage.getGarbageId() != null && garbage.getGarbageId() > 0) {
			garbageManager.updateGarbage(garbage.getGarbageId(), garbage);
			return garbage.getGarbageId();
		}
	return	garbageManager.insertGarbage(garbage);
	}

	public void addGarbageToTeam(User user, Team team, Garbage garbage) throws InvalidTeamOperationException {
		int maxbags = team.getCleaningPower().intValue() * team.countMembers();
		teamManager.assignGarbage(team.getTeamId(), garbage.getGarbageId(), maxbags);
	}

	public void deleteGarbageFromTeam(User user, int teamId,
			int garbageId) {
		teamManager.removeGarbageAssigment(teamId, garbageId);
	}

	public List<Garbage> getGarbageFromCounty(User user, String county) {
		return garbageManager.getGarbagesByCounty(county);
	}

	public List<Garbage> getGarbageofTeam(User user, int teamId) {
		List<Garbage> garbages = new ArrayList<Garbage>();
		Team team = teamManager.getTeam(teamId);
		for (GarbageEnrollment enrollment : team.getGarbageEnrollements())
			garbages.add(enrollment.getGarbage());
		return garbages;
	}

	public void addChartedArea(User user, int teamId, int areaId) throws ChartedAreaAssignmentException {	
		teamManager.assignChartArea(teamId, areaId);
	}

	/*
     * 
     */
	public void setChartedPercent(int areaId, int percent) {
		geoManager.setPercentageCompleted(areaId, percent);
	}

	public void removeChartedArea(User user, int teamId, int areaId) {
		teamManager.removeChartAreaAssignment(teamId, areaId);
	}

	public void addPicture(User user, Garbage garbage, File picture) throws FileNotFoundException, IOException {
		garbageManager.addNewImage(garbage.getGarbageId(), picture, picture.getName());
	}

	public File getPicture(User user, Garbage garbage, int imgNr,
			boolean full)  {
		if (full)
			return new File(garbageManager.getImageDisplayPath(garbage.getGarbageId(), imgNr));
		return new File(garbageManager.getImageThumbnailPath(garbage.getGarbageId(), imgNr));
	}

	public void deleteGarbage(User user, Garbage garbage) {
		garbageManager.deleteGarbage(garbage.getGarbageId());
	}

	public void setStatusGarbage(User user, Garbage garbage) {
		garbageManager.setGarbageStatus(garbage.getGarbageId(), garbage.getStatus());
	}

	public Garbage getGarbage(User user, int garbageId) {
		return garbageManager.getGarbage(garbageId);
	}

	public List<ChartedArea> getChartedAreasOfTeam(User user, int teamId) {
		return new ArrayList<ChartedArea>(teamManager.getTeam(teamId)
				.getChartedAreas());
	}

	public Team getTeam(User user, int teamId) {
		return teamManager.getTeam(teamId);
	}

	public void addOrganization(User user, Organization org) {
		orgManager.addOrganization(org);

		// String location = JsfUtils.getInitParameter("webservice.url") +
		// "/LDIRBackend/ws/organization";
		// Client client = Client.create();
		// WebResource resource = client.resource(location);
		// Builder builder = resource.header(HttpHeaders.AUTHORIZATION,
		// AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
		// JsfUtils.getInitParameter("admin.password")));
		// ClientResponse cr = builder.entity(organization,
		// MediaType.APPLICATION_XML).post(ClientResponse.class);
	}

	public List<Garbage> getGarbageList(GarbageStatus status) {
		return garbageManager.getGarbages(status);
	}

	public List<Garbage> getGarbageListByFilters(User admin, String countyId,
			int gridId, int userId, Date addDate, String accept) {
		Set<String> counties = new HashSet<String>();
		counties.add(countyId);
		Set<String> grids = new HashSet<String>();
		grids.add(new Integer(gridId).toString());
		Set<Integer> userIds = new HashSet<Integer>();
		userIds.add(userId);
		Set<Date> dates = new HashSet<Date>();
		dates.add(addDate);
		return garbageManager.report(counties, grids, userIds,
				dates);
	}

	public CountyArea[] getCountyList() {
		List<CountyArea> areas = geoManager.getAllCounties();
		CountyArea[] result = new CountyArea[areas.size()];
		int i = 0;
		for (CountyArea area : areas)
			result[i++] = area;
		return result;
	}

	public ChartedArea getChartedArea(int areaId) {
		return geoManager.getChartedArea(areaId);
	}


	public void login(String user, String pass) {
	}

	public void resetPassword(String email) {
		userManager.passwdResetToken(email);
	}

	public void setPassword(String newPassword, String userId,
			String token) throws NumberFormatException, InvalidTokenException {
		userManager.setPassword(Integer.parseInt(userId), token, newPassword);
	}

	public User getUserDetails(String user, String pass, int userId) {
		// TODO
		return null;
	}

	public User[] getUserListByFilters(User admin, String county,
			int birthYear, String role, int minGarbages, int maxGarbages,
			String accept) {
		// TODO
		return null;
	}

	public Team[] getTeamListByFilters(User admin, String county,
			int birthYear, String role, int minGarbages, int maxGarbages,
				String accept) {
		// TODO
		return null;

	}

	public User reinitUser(User userDetails) {
		return getUserDetails(userDetails.getEmail(),
				userDetails.getPasswd(), userDetails.getUserId());
	}

	/**
	 * @param teamId
	 * @param equipment
	 */
	public void addCleaningEquiptment(Integer teamId, CleaningEquipment equipment) {
		teamManager.addCleaningEquipment(teamId, equipment);
	}

	/**
	 * @param teamId
	 * @param transport
	 */
	public void addTransportEquipment(Integer teamId, TransportEquipment trans) {
		teamManager.addTransportEquipment(teamId, trans);
	}

	/**
	 * @param teamId
	 * @param gps
	 */
	public void addGpsEquipment(Integer teamId, GpsEquipment gps) {
		teamManager.addGpsEquipment(teamId, gps);
	}

	/**
	 * @param orgDeleteId
	 */
	public void deleteOrganization(int orgDeleteId) {
		orgManager.deleteOrganization(orgDeleteId);
	}

	/**
	 * @param teamId
	 * @param equipmentId
	 */
	public void deleteEquipment(Integer teamId, Integer equipmentId) {
		teamManager.deleteEquipmentById(teamId, equipmentId);
	}

	/**
	 * @param organization
	 */
	public void updateOrganization(Organization organization) {
		orgManager.updateOrganization(organization.getOrganizationId(), organization);
	}

	/**
	 * @param org
	 * @param userTeam
	 * @throws InvalidTeamOperationException 
	 */
	public void addOrganizationToTeam(Organization org, Team userTeam) throws InvalidTeamOperationException {
		teamManager.enrollOrganization(org.getOrganizationId(), userTeam.getTeamId());
	}

	/**
	 * @param userTeam
	 * @param memDeleteId
	 */
	public void removeVolunteerFromTeam(Team userTeam, int memDeleteId) {
		teamManager.withdrawUser(memDeleteId, userTeam.getTeamId());
		
	}

	/**
	 * @param userTeam
	 * @param userDetails
	 * @throws InvalidTeamOperationException 
	 */
	public void enrollVolunteerToTeam(Team userTeam, User userDetails) throws InvalidTeamOperationException {
		teamManager.enrollUser(userDetails.getUserId(), userTeam.getTeamId());
	}

	/**
	 * @param selectedUser
	 */
	public void updateUser(User selectedUser) {
		userManager.updateUser(selectedUser.getUserId(), selectedUser);
	}

	/**
	 * @param teamTemp
	 */
	public void addTeam(Team teamTemp){
		teamManager.createTeam(teamTemp);
	}

	/**
	 * @param teamId
	 * @param teamTemp
	 */
	public void updateTeam(int teamId, Team teamTemp) {
		teamManager.updateTeam(teamId, teamTemp);
	}

	/**
	 * @param teamId
	 */
	public void deleteTeam(int teamId) {
		teamManager.deleteTeam(teamId);
	}

	/**
	 * @param regiterUser
	 * @throws InvalidUserException 
	 */
	public void registerUser(User regiterUser) throws InvalidUserException {
		userManager.addUser(regiterUser);
	}
}
