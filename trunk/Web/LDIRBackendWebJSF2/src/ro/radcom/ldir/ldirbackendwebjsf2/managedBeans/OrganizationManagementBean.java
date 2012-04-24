package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

public class OrganizationManagementBean {
	private static final Logger log4j = Logger.getLogger(ResetBean.class
			.getCanonicalName());
	private WSInterface wsi;
	/* variabile afisare */
	private User userDetails = new User();
	private String teamName;
	private List<Team> teamList = new ArrayList<Team>();
	private List<Team> teamAvalable = new ArrayList<Team>();
	private boolean equipmentBool = false;
	private boolean orgBool = false;
	private int teamId;

	public OrganizationManagementBean() throws NamingException {
		wsi = new WSInterface();
		userDetails = (User) JsfUtils.getHttpSession().getAttribute(
				"USER_DETAILS");
		log4j.info("[ORGANIZATION__MANAGEMENT] - (init) userdetails:"
				+ userDetails.getEmail());
		teamId = AppUtils.parseToInt(JsfUtils.getRequestParameter("teamId"));
		log4j.info("[ORGANIZATION__MANAGEMENT] - (init) teamId:" + teamId);
		if (teamId > 0) {

			Team userTeam = wsi.getTeam(userDetails, teamId);
			log4j.info("[ORGANIZATION__MANAGEMENT] - (init) userTeam:"
					+ userTeam.getTeamName());
			teamName = userTeam.getTeamName();
			if (userTeam.getEquipments() != null
					&& userTeam.getEquipments().size() > 0) {
				equipmentBool = true;
			}
			managedOrganization(userTeam);
			// }
		}
		managedTeams();

	}

	private int usedTeamId = 0;

	public void actionSelectTeamToBeUsed() {
		log4j.info("=====================================================START");
		usedTeamId = AppUtils.parseToInt(JsfUtils.getRequestParameter("team"));
		log4j.info("[AREACLEANMANAGER]: trying to set team number "
				+ usedTeamId + " for cleaning");

		/*
		 * if(teamList != null && teamList.size() > 0) for(Team team :
		 * teamList){
		 * 
		 * //first team gets selected if(teamId==team.getTeamId()) {
		 * teamSelected=team;
		 * log4j.info("[AREACLEANMANAGER]: added on session TEAM_SELECTED:"
		 * +teamSelected.getTeamId());
		 * JsfUtils.getHttpSession().removeAttribute("TEAM_SELECTED");
		 * JsfUtils.getHttpSession().setAttribute("TEAM_SELECTED",
		 * teamSelected.getTeamId()); break; }; };
		 */
	}

	public void actionUseTeam() {
		if (teamId < -0) {
			try {
				usedTeamId = AppUtils.parseToInt(JsfUtils
						.getRequestParameter("team"));
				log4j.info("[AREACLEANMANAGER]: trying to set team number "
						+ usedTeamId + " for cleaning");
			} catch (Exception ee) {

			}
		}
		teamId = usedTeamId;
		log4j.info("=====================================================START");
		JsfUtils.getHttpSession().removeAttribute("TEAM_SELECTED");
		JsfUtils.getHttpSession().setAttribute("TEAM_SELECTED", teamId);
		log4j.info("A fost selectata echipa cu id-ul " + teamId
				+ " pentru curatenie");
		JsfUtils.addInfoMessage("A fost selectata echipa cu id-ul " + teamId
				+ " pentru curatenie");
	}

	public void managedOrganization(Team userTeam) {
		List<Organization> organizationMembers = userDetails.getOrganizations();

		log4j.info("[ORGANIZATION__MANAGEMENT] - (managedOrganization) - START");
		if (organizationMembers != null && organizationMembers.size() > 0) {
			log4j.info("[ORGANIZATION__MANAGEMENT] - (managedOrganization) - are grupuri");
			for (Organization org : organizationMembers) {
				if (org == null)
					continue;
				if (org.getOrganizationId() != 0 && org.getOrganizationId() > 0) {

					Team team = org.getMemberOf();
					if (team == null)
						continue;
					if (team.getTeamId() != null
							&& team.getTeamId().equals(teamId)) {
						orgBool = true;
						if (org.getOrganizationId() != null)
							log4j.info("[ORGANIZATION__MANAGEMENT] - (managedOrganization) - a gasit o organizatie/grup:"
									+ org.getOrganizationId());
						break;
					}

				}
			}

		}
		log4j.info("[ORGANIZATION__MANAGEMENT] - (managedOrganization) - END");
	}

	public void managedTeams() {
		log4j.info("[ORGANIZATION__MANAGEMENT] - (managedTeams) - START");
		if (userDetails != null) {
			userDetails = wsi.refreshUser(userDetails.getUserId(), userDetails);
			JsfUtils.getHttpSession().setAttribute("USER_DETAILS", userDetails);
		}
		teamList = userDetails.getManagedTeams();
		if (teamList != null)
			log4j.info("[ORGANIZATION__MANAGEMENT] - (managedTeams) teamList:"
					+ teamList.size());
		log4j.info("[ORGANIZATION__MANAGEMENT] - (managedTeams) - END");
	}

	public String actionTeam() {
		log4j.info("[ORGANIZATION__MANAGEMENT] - (actionTeam) - START");
		/**
		 * validare campuri
		 */
		if (teamName == null || teamName.trim().length() == 0) {
			JsfUtils.addWarnBundleMessage("err_mandatory_fields");
			return NavigationValues.TEAM_ORG_MULTI_FAIL;
		}
		if (teamName.length() < 4) {
			JsfUtils.addWarnBundleMessage("err_team_name");
			return NavigationValues.TEAM_ORG_MULTI_FAIL;
		}

		Team teamTemp = new Team();
		teamTemp.setTeamName(teamName);
		try {
			wsi.addTeam(teamTemp);
			log4j.info("[ORGANIZATION__MANAGEMENT] - (actionTeam) - wsi.added");
			managedTeams();
			String message = "";
			if (teamName != null)
				message = JsfUtils.getBundleMessage("team_add_success_message")
						.replaceAll("\\{0\\}", teamName);
			else
				message = JsfUtils.getBundleMessage("team_add_success_message")
						.replaceAll("\\{0\\}", "");
			JsfUtils.addInfoMessage(message);
		} catch (Exception err) {
			String message = "";
			if (teamName != null)
				message = JsfUtils.getBundleMessage("team_add_fail_message")
						.replaceAll("\\{0\\}", teamName);
			else
				message = JsfUtils.getBundleMessage("team_add_fail_message")
						.replaceAll("\\{0\\}", "");
			JsfUtils.addWarnMessage(message);
		}
		log4j.info("[ORGANIZATION__MANAGEMENT] - (actionTeam) - END");
		return NavigationValues.TEAM_ORG_MULTI_FAIL;

		// String teamNameEncod = encodeUrl(teamName);
		// /* update date utilizator */
		// String location =
		// JsfUtils.getInitParameter("webservice.url")+"/LDIRBackend/ws/team/nameSearch?teamName="+teamNameEncod;
		// Client client = Client.create();
		// WebResource resource = client.resource(location);
		// Builder builder = resource.header(HttpHeaders.AUTHORIZATION,
		// AppUtils.generateCredentials(userDetails.getEmail(),
		// userDetails.getPasswd()));
		// // ClientResponse cr = builder.entity(null,
		// MediaType.APPLICATION_XML).get(ClientResponse.class);
		// ClientResponse cr =
		// builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
		// int statusCode = cr.getStatus();
		// log4j.info("---> statusCode: " + statusCode + " (" +
		// cr.getClientResponseStatus() + ")");
		//
		// teamAvalable = cr.getEntity(new GenericType<List<Team>>(){});
		// if(teamAvalable != null && teamAvalable.size() == 0){
		// JsfUtils.addWarnBundleMessage("fail_add_team_message");
		// return NavigationValues.TEAM_ORG_MULTI_FAIL;
		// }else if(teamAvalable != null && teamAvalable.size() == 1){
		// //add team to user
		// // location =
		// JsfUtils.getInitParameter("webservice.url")+"/LDIRBackend/ws/user/"+userDetails.getUserId()+"/team";
		// location =
		// JsfUtils.getInitParameter("webservice.url")+"/LDIRBackend/ws/team/";
		// client = Client.create();
		// resource = client.resource(location);
		// builder = resource.header(HttpHeaders.AUTHORIZATION,
		// AppUtils.generateCredentials(userDetails.getEmail(),
		// userDetails.getPasswd()));
		// cr = builder.entity(teamAvalable,
		// MediaType.APPLICATION_XML).post(ClientResponse.class);
		// statusCode = cr.getStatus();
		//
		// JsfUtils.addInfoBundleMessage("success_edit_message");
		// return NavigationValues.TEAM_ORG_MULTI_FAIL;
		// }else{
		// for(Team team: teamAvalable){
		// log4j.info("---> teamAvalable: " + team.getTeamName());
		// }
		// // JsfUtils.addInfoBundleMessage("success_edit_message");
		// }

	}

	public String actionChangeNameTeam() {
		log4j.info("[ORGANIZATION__MANAGEMENT] - (actionChangeNameTeam) - START");
		/**
		 * validare campuri
		 */
		if (teamName == null || teamName.trim().length() == 0) {
			JsfUtils.addWarnBundleMessage("err_mandatory_fields");
			return NavigationValues.TEAM_ORG_MULTI_FAIL;
		}
		if (teamName.length() < 4) {
			JsfUtils.addWarnBundleMessage("err_team_name");
			return NavigationValues.TEAM_ORG_MULTI_FAIL;
		}

		Team teamTemp = new Team();
		teamTemp.setTeamName(teamName);
		try {
			wsi.updateTeam(teamId, teamTemp);
			log4j.info("[ORGANIZATION__MANAGEMENT] - (actionChangeNameTeam) - wsi.updated");
			managedTeams();

			JsfUtils.addInfoBundleMessage("team_change_teamname_success_message");
		} catch (Exception err) {
			JsfUtils.addWarnBundleMessage("team_change_teamname_fail_message");
		}
		log4j.info("[ORGANIZATION__MANAGEMENT] - (actionChangeNameTeam) - END");
		return NavigationValues.TEAM_ORG_MULTI_FAIL;
	}

	public String actionDeleteTeam() {
		log4j.info("[ORGANIZATION__MANAGEMENT] - (actionDeleteTeam) - START");
		try {
			wsi.deleteTeam(teamId);
			log4j.info("[ORGANIZATION__MANAGEMENT] - (actionDeleteTeam) - wsi.deleted");
			managedTeams();
			String message = "";
			if (teamName != null)
				message = JsfUtils.getBundleMessage(
						"team_delete_success_message").replaceAll("\\{0\\}",
						teamName);
			else
				message = JsfUtils.getBundleMessage(
						"team_delete_success_message")
						.replaceAll("\\{0\\}", "");
			JsfUtils.addInfoMessage(message);

		} catch (Exception err) {
			String message = "";
			if (teamName != null)
				message = JsfUtils.getBundleMessage("team_delete_fail_message")
						.replaceAll("\\{0\\}", teamName);
			else
				message = JsfUtils.getBundleMessage("team_delete_fail_message")
						.replaceAll("\\{0\\}", "");
			JsfUtils.addWarnMessage(message);
		}
		log4j.info("[ORGANIZATION__MANAGEMENT] - (actionDeleteTeam) - END");
		return NavigationValues.TEAM_ORG_MULTI_FAIL;
	}

	public String encodeUrl(String arg) {

		// String url =
		// "http://search.barnesandnoble.com/booksearch/results.asp?WRD=test 2324";
		try {
			arg = URLEncoder.encode(arg, "UTF-8");
			log4j.debug("---> encode: " + arg);
		} catch (UnsupportedEncodingException uee) {
			log4j.debug("---> encode error: " + uee.getMessage());
		}
		return arg;
	}

	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}

	public User getUserDetails() {
		return userDetails;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<Team> getTeamList() {
		return teamList;
	}

	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}

	public void setTeamAvalable(List<Team> teamAvalable) {
		this.teamAvalable = teamAvalable;
	}

	public List<Team> getTeamAvalable() {
		return teamAvalable;
	}

	public boolean isEquipmentBool() {
		return equipmentBool;
	}

	public void setEquipmentBool(boolean equipmentBool) {
		this.equipmentBool = equipmentBool;
	}

	public boolean isOrgBool() {
		return orgBool;
	}

	public void setOrgBool(boolean orgBool) {
		this.orgBool = orgBool;
	}

	public int getTeamId() {
		return teamId;
	}

	public void setTeamId(int teamId) {
		this.teamId = teamId;
	}

}
