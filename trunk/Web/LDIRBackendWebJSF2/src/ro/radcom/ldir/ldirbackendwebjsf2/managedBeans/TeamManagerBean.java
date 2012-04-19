package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import ro.ldir.dto.CleaningEquipment;
import ro.ldir.dto.CleaningEquipment.CleaningType;
import ro.ldir.dto.Equipment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Organization.OrganizationType;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;
import ro.ldir.dto.TransportEquipment.TransportType;
import ro.ldir.dto.User;
import ro.ldir.exceptions.InvalidTeamOperationException;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.CountyNames;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

public class TeamManagerBean {

	private static final Logger log4j = Logger.getLogger(LoginBean.class
			.getCanonicalName());
	WSInterface wsi;

	public final String PAGE_ORGANIZATION = "echipa-org-detalii";
	public final String PAGE_EQIPMENTS = "echipa-equip-detalii";
	private final String ORGANIZER_MULTI = "ORGANIZER_MULTI";

	private boolean managerBool = false;
	private boolean equipmentBool = false;
	private boolean orgBool = false;

	private Team userTeam;

	private User userDetails = new User();
	private User managerDetails;

	private Organization organization = new Organization();
	private Integer teamID;
	private List<User> volunteerMembers;
	private List<Organization> organizationMembers;
	private List<Equipment> equipments;
	private String tipOrganization;
	private String role;

	private String participants;
	private Integer gpsUnits;
	private String transport;
	private Integer bagsUnits;
	private Integer glovesUnits;
	private Integer shovelUnits;
	private String toolsUnits;
	private int teamId = 0;

	public TeamManagerBean() throws NamingException {
		wsi = new WSInterface();
		userDetails = (User) JsfUtils.getHttpSession().getAttribute(
				"USER_DETAILS");

		String sourcePage = FacesContext.getCurrentInstance().getViewRoot()
				.getViewId();
		log4j.info("[TEAM] - operation 1");
		teamId = AppUtils.parseToInt(JsfUtils.getRequestParameter("teamId"));
		role = userDetails.getRole();
		log4j.info("[TEAM] - teamId="+teamId);
		if (teamId > 0 && role.equals(ORGANIZER_MULTI)) {
			try {

				log4j.info("role ->" + role + ", team ->" + teamId);

				Team tmpTeam = wsi.getTeam(userDetails, teamId);
				if (tmpTeam == null) {
					log4j.info("[TEAM] - operation 2");
					userTeam = userDetails.getMemberOf();
					initTeam();
				} else {
					log4j.info("[TEAM] - operation 3");
					userTeam = tmpTeam;
					// TODO nu ar trebui un initteam aici?
					//initTeam();
				}

			} catch (Exception ex) {
				log4j.info("[TEAM] exception ->" + ex + ", team ->" + teamId);
			}
		} else {
			log4j.info("[TEAM] - operation 4");
			userTeam = userDetails.getMemberOf();
			initTeam();
			teamId=userTeam.getTeamId();
		}

		log4j.info("[TEAM] - initliazed team with: user ->" + userDetails
				+ ", team ->" + userTeam);

		if (userTeam != null && userTeam.getEquipments() != null
				&& userTeam.getEquipments().size() > 0) {
			log4j.info("[TEAM] - operation 5");
			equipmentBool = true;
		}
		if (sourcePage.indexOf(PAGE_EQIPMENTS) > -1) {
			log4j.info("[TEAM] - init equipments from begining");
			initEquipments();
		}
		// if(sourcePage.indexOf(PAGE_ORGANIZATION)> -1){
		initOrganization(teamId);
		// }
		initManager();
		initTeamMembers();
		log4j.info("[TEAM] - operation 6");

	}

	private void initEquipments() {
		if (userTeam != null && userTeam.getEquipments() != null
				&& userTeam.getEquipments().size() > 0) {
			equipments = userTeam.getEquipments();
			for (Equipment equi : equipments) {
				if (equi instanceof GpsEquipment) {
					gpsUnits = ((GpsEquipment) equi).getCount();
				}
				if (equi instanceof CleaningEquipment) {
					if (((CleaningEquipment) equi).getCleaningType().equals(
							CleaningType.BAGS)) {
						bagsUnits = ((CleaningEquipment) equi).getCount();
					} else if (((CleaningEquipment) equi).getCleaningType()
							.equals(CleaningType.GLOVES)) {
						glovesUnits = ((CleaningEquipment) equi).getCount();
					} else if (((CleaningEquipment) equi).getCleaningType()
							.equals(CleaningType.SHOVEL)) {
						shovelUnits = ((CleaningEquipment) equi).getCount();
					}
				}
				if (equi instanceof TransportEquipment) {
					transport = ((TransportEquipment) equi).getTransportType()
							.toString();
				}

			}
			equipmentBool = true;
		}
	}

	private void initTeam() {

		try {
			userTeam = wsi.getTeam(userDetails, userTeam.getTeamId());
			setVolunteerMembers(userTeam.getVolunteerMembers());
		} catch (Exception e) {
			log4j.debug("error->" + e.getMessage());
		}

	}

	public void initManager() {

		try {
			managerDetails = userTeam.getTeamManager();
			log4j.debug("managerDetails->" + managerDetails);
		} catch (Exception e) {
			log4j.debug("error->" + e.getMessage());
		}

		managerBool = managerTest();
	}

	public void initTeamMembers() {

		try {
			volunteerMembers = userTeam.getVolunteerMembers();
			if (volunteerMembers != null && managerDetails != null)
				volunteerMembers.remove(managerDetails);
		} catch (Exception e) {
			log4j.debug("error->" + e.getMessage());
		}

	}

	public void initOrganization(Integer teamId) {

		if (teamId > 0) {
			try {
				log4j.info("[TEAM] - organizatile sunt aflate prin echipa!!!");
				organizationMembers = userTeam.getOrganizationMembers();
			} catch (Exception e) {
				log4j.debug("error->" + e.getMessage());
			}
		} else {
			log4j.info("[TEAM] - organizatile sunt aflate prin user!!!");
			organizationMembers = userDetails.getOrganizations();
		}

		if (organizationMembers != null && organizationMembers.size() > 0) {
			log4j.info("[TEAM] - organizationMembers!=null si size>0 ");
			for (Organization org : organizationMembers) {
				if (org.getOrganizationId() != 0 && org.getOrganizationId() > 0) {
					try {
						log4j.info("[TEAM] orgid!=0 and orgID>0");
						
						Team team = org.getMemberOf();
						if (team.getTeamId().equals(userTeam.getTeamId())) {
							log4j.info("[TEAM] - s-a gasit organizatia corecta.");
							organization = org;
							orgBool = true;
							break;
						}

					} catch (Exception ex) {
						orgBool = true;
						organization = organizationMembers.get(0);
						log4j.info("[TEAM] - in exceptie");
					}
				}
			}
			if(organization!=null){
				log4j.info("[TEAM] - organization type is: "+organization.getType().toString());
				tipOrganization=organization.getType().toString();
			}
		}

	}

	public String actionAddToTeam() throws InvalidTeamOperationException {

		if (teamID == null || teamID == 0) {
			JsfUtils.addWarnBundleMessage("err_mandatory_fields");
			return NavigationValues.USER_ADD_TEAM_FAIL;
		}
		userTeam = new Team();
		userTeam.setTeamId(teamID);
		wsi.enrollVolunteerToTeam(userTeam, userDetails);
		JsfUtils.addInfoBundleMessage("success_add_mem_message");
		return NavigationValues.USER_ADD_TEAM_FAIL;
	}

	public String actionDelFromTeam() {

		wsi.removeVolunteerFromTeam(userTeam, userDetails.getUserId());
		JsfUtils.addInfoBundleMessage("success_del_mem_message");
		return NavigationValues.USER_REM_TEAM_FAIL;

	}

	public String actionWithdrawFromTeam() {

		String memDeleteId = JsfUtils.getRequestParameter("memDeleteId");
		if (memDeleteId == null || memDeleteId.length() == 0) {
			JsfUtils.addWarnBundleMessage("err_user_rem");
			return NavigationValues.USER_REM_TEAM_FAIL;
		}
		wsi.removeVolunteerFromTeam(userTeam, Integer.parseInt(memDeleteId));
		User tempUser = new User();
		tempUser.setUserId(new Integer(memDeleteId));
		volunteerMembers.remove(tempUser);
		JsfUtils.addInfoBundleMessage("success_del_mem_message");
		return NavigationValues.USER_REM_TEAM_FAIL;

	}

	public String actionAddOrg() {

		if (organization.getName() == null
				|| organization.getName().length() == 0
				|| organization.getMembersCount() == null
				|| organization.getMembersCount() <= 0) {
			JsfUtils.addWarnBundleMessage("err_mandatory_fields");
			if (role.equals(ORGANIZER_MULTI))
				return NavigationValues.TEAM_ORG_MULTI_FAIL;
			else {
				return NavigationValues.TEAM_ADD_ORG_FAIL;
			}
		}

		if (tipOrganization != null && tipOrganization.length() != 0) {
			if (tipOrganization.equalsIgnoreCase("CITY_HALL")) {
				organization.setType(OrganizationType.CITY_HALL);
			} else if (tipOrganization.equalsIgnoreCase("COMPANY")) {
				organization.setType(OrganizationType.COMPANY);
			} else if (tipOrganization.equalsIgnoreCase("LANDFILL")) {
				organization.setType(OrganizationType.LANDFILL);
			} else if (tipOrganization.equalsIgnoreCase("REGISTRATION_POINT")) {
				organization.setType(OrganizationType.REGISTRATION_POINT);
			} else if (tipOrganization.equalsIgnoreCase("SCHOOL")) {
				organization.setType(OrganizationType.SCHOOL);
			} else if (tipOrganization.equalsIgnoreCase("ONG")) {
				organization.setType(OrganizationType.ONG);
			} else if (tipOrganization.equalsIgnoreCase("INSTITUTIE")) {
				organization.setType(OrganizationType.INSTITUTIE);
			} else if (tipOrganization.equalsIgnoreCase("ALTELE")) {
				organization.setType(OrganizationType.ALTELE);
			} else if (tipOrganization.equalsIgnoreCase("FRIENDS")) {
				organization.setType(OrganizationType.FRIENDS);
			}
		}
		/* create organization */
		wsi.addOrganization(userDetails, organization);

		log4j.info("--->  organization[1]: " + organization.getOrganizationId());

		initOrganization(0);

		for (Organization org : organizationMembers) {
			if (org.getOrganizationId() != 0 && org.getOrganizationId() > 0) {

				Team team = org.getMemberOf();
				if (team == null) {
					try {
						wsi.addOrganizationToTeam(org, userTeam);
					} catch (InvalidTeamOperationException e) {
						return NavigationValues.TEAM_ADD_ORG_FAIL;
					}
					log4j.info("--->  organization:[2] "
							+ organization.getOrganizationId());
					orgBool = true;
					break;
				}
			}
		}

		JsfUtils.addInfoBundleMessage("success_add_group_message");
		if (role.equals(ORGANIZER_MULTI))
			return NavigationValues.TEAM_ORG_MULTI_FAIL;
		else {
			return NavigationValues.TEAM_ADD_ORG_FAIL;
		}
	}

	public String actionEditOrg() {
		if (organization.getName() == null
				|| organization.getName().length() == 0
				|| organization.getMembersCount() == null
				|| organization.getMembersCount() <= 0) {
			JsfUtils.addWarnBundleMessage("err_mandatory_fields");
			if (role.equals(ORGANIZER_MULTI))
				return NavigationValues.TEAM_ORG_MULTI_FAIL;
			else {
				return NavigationValues.TEAM_ADD_ORG_FAIL;
			}
		}

		if (tipOrganization != null && tipOrganization.length() != 0) {
			if (tipOrganization.equalsIgnoreCase("CITY_HALL")) {
				organization.setType(OrganizationType.CITY_HALL);
			} else if (tipOrganization.equalsIgnoreCase("COMPANY")) {
				organization.setType(OrganizationType.COMPANY);
			} else if (tipOrganization.equalsIgnoreCase("LANDFILL")) {
				organization.setType(OrganizationType.LANDFILL);
			} else if (tipOrganization.equalsIgnoreCase("REGISTRATION_POINT")) {
				organization.setType(OrganizationType.REGISTRATION_POINT);
			} else if (tipOrganization.equalsIgnoreCase("SCHOOL")) {
				organization.setType(OrganizationType.SCHOOL);
			} else if (tipOrganization.equalsIgnoreCase("ONG")) {
				organization.setType(OrganizationType.ONG);
			} else if (tipOrganization.equalsIgnoreCase("INSTITUTIE")) {
				organization.setType(OrganizationType.INSTITUTIE);
			} else if (tipOrganization.equalsIgnoreCase("ALTELE")) {
				organization.setType(OrganizationType.ALTELE);

			}
		}
		log4j.info("--->  organization: " + tipOrganization + ","
				+ organization.getType().toString());
		wsi.updateOrganization(organization);
		JsfUtils.addInfoBundleMessage("success_change_group_message");
		if (role.equals(ORGANIZER_MULTI))
			return NavigationValues.TEAM_ORG_MULTI_FAIL;
		else {
			return NavigationValues.TEAM_ADD_ORG_FAIL;
		}
	}

	public String actionDelOrgTeam() {

		String orgDeleteId = JsfUtils.getRequestParameter("orgDeleteId");
		if (orgDeleteId == null || orgDeleteId.length() == 0) {
			JsfUtils.addWarnBundleMessage("err_user_rem");
			if (role.equals(ORGANIZER_MULTI))
				return NavigationValues.TEAM_ORG_MULTI_FAIL;
			else {
				return NavigationValues.TEAM_REM_MEM_FAIL;
			}
		}

		wsi.deleteOrganization(Integer.parseInt(orgDeleteId));

		orgBool = false;
		organizationMembers = null;
		JsfUtils.addInfoBundleMessage("success_del_mem_message");

		if (role.equals(ORGANIZER_MULTI))
			return NavigationValues.TEAM_ORG_MULTI_FAIL;
		else {
			return NavigationValues.TEAM_REM_MEM_FAIL;
		}
	}

	public String actionAddEquipment() {

		// reset equipments
		if (equipments != null) {
			for (Equipment equi : equipments) {
				log4j.warn("[TEAM] - TeamID: " + userTeam.getTeamId()
						+ " with name: " + userTeam.getTeamName()
						+ "  Deleting equipment " + equi.getEquipmentId());
				if (equi.getEquipmentId() != null)
					wsi.deleteEquipment(userTeam.getTeamId(),
							equi.getEquipmentId());
			}
		}
		if (gpsUnits != null && gpsUnits > 0) {
			GpsEquipment gps = new GpsEquipment();
			gps.setCount(gpsUnits);
			wsi.addGpsEquipment(userTeam.getTeamId(), gps);
		}
		if (bagsUnits != null && bagsUnits > 0) {
			CleaningEquipment bags = new CleaningEquipment();
			bags.setCleaningType(CleaningType.BAGS);
			bags.setCount(bagsUnits);
			wsi.addCleaningEquiptment(userTeam.getTeamId(), bags);
		}
		if (glovesUnits != null && glovesUnits > 0) {
			CleaningEquipment gloves = new CleaningEquipment();
			gloves.setCleaningType(CleaningType.GLOVES);
			gloves.setCount(glovesUnits);
			wsi.addCleaningEquiptment(userTeam.getTeamId(), gloves);
		}
		if (shovelUnits != null && shovelUnits > 0) {
			CleaningEquipment shovel = new CleaningEquipment();
			shovel.setCleaningType(CleaningType.SHOVEL);
			shovel.setCount(shovelUnits);
			wsi.addCleaningEquiptment(userTeam.getTeamId(), shovel);
		}
		if (transport != null && transport.length() != 0) {
			TransportEquipment trans = new TransportEquipment();
			if (transport.equals("BICYCLE")) {
				trans.setTransportType(TransportType.BICYCLE);
			} else if (transport.equals("CAR")) {
				trans.setTransportType(TransportType.CAR);
			} else if (transport.equals("ORGANIZATION_CAR")) {
				trans.setTransportType(TransportType.ORGANIZATION_CAR);
			} else if (transport.equals("PUBLIC")) {
				trans.setTransportType(TransportType.PUBLIC);
			}
			wsi.addTransportEquipment(userTeam.getTeamId(), trans);
		}

		if (!equipmentBool)
			JsfUtils.addInfoBundleMessage("success_add_equi_message");
		else
			JsfUtils.addInfoBundleMessage("success_change_equi_message");
		if (role.equals(ORGANIZER_MULTI))
			return NavigationValues.TEAM_ORG_MULTI_FAIL;
		else {
			equipmentBool = true;
			return NavigationValues.TEAM_ADD_EQUI_FAIL;
		}
	}

	public boolean managerTest() {

		if (userDetails != null && managerDetails != null
				&& userDetails.getUserId().equals(managerDetails.getUserId())) {
			return true;
		}
		return false;
	}

	public boolean isManagerBool() {
		return managerBool;
	}

	public void setManagerBool(boolean managerBool) {
		this.managerBool = managerBool;
	}

	public void setUserTeam(Team userTeam) {
		this.userTeam = userTeam;
	}

	public Team getUserTeam() {
		return userTeam;
	}

	public User getUserDetails() {
		return userDetails;
	}

	public User getManagerDetails() {
		return managerDetails;
	}

	public void setManagerDetails(User managerDetails) {
		this.managerDetails = managerDetails;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Organization getOrganization() {
		return organization;
	}

	public Integer getTeamID() {
		return teamID;
	}

	public void setTeamID(Integer teamID) {
		this.teamID = teamID;
	}

	public List<SelectItem> getCountyItems() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		for (int i = 0; i < CountyNames.COUNTIES.size(); i++) {
			String county = CountyNames.COUNTIES.get(i);
			items.add(new SelectItem(county, county));
		}

		return items;
	}

	public List<SelectItem> getNumbers() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		for (int i = 1; i < 50; i++) {
			items.add(new SelectItem(i, "" + i));
		}
		return items;
	}

	public List<SelectItem> getNumSac() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		for (int i = 10; i < 1000; i = i + 10) {
			items.add(new SelectItem(i, "" + i));
		}
		return items;
	}

	public List<SelectItem> getNumbMan() {
		List<SelectItem> items = new ArrayList<SelectItem>();

		for (int i = 5; i < 200; i = i + 5) {
			items.add(new SelectItem(i, "" + i));
		}
		return items;
	}

	public void setVolunteerMembers(List<User> volunteerMembers) {
		this.volunteerMembers = volunteerMembers;
	}

	public List<User> getVolunteerMembers() {
		return volunteerMembers;
	}

	public List<Organization> getOrganizationMembers() {
		return organizationMembers;
	}

	public void setOrganizationMembers(List<Organization> organizationMembers) {
		this.organizationMembers = organizationMembers;
	}

	public void setTipOrganization(String tipOrganization) {
		this.tipOrganization = tipOrganization;
	}

	public String getTipOrganization() {

		return tipOrganization;
	}

	public void setEquipmentBool(boolean equipmentBool) {
		this.equipmentBool = equipmentBool;
	}

	public boolean isEquipmentBool() {
		return equipmentBool;
	}

	public void setOrgBool(boolean orgBool) {
		this.orgBool = orgBool;
	}

	public boolean isOrgBool() {
		return orgBool;
	}

	public void setParticipants(String participants) {
		this.participants = participants;
	}

	public String getParticipants() {
		return participants;
	}

	public Integer getGpsUnits() {
		return gpsUnits;
	}

	public void setGpsUnits(Integer gpsUnits) {
		this.gpsUnits = gpsUnits;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public Integer getBagsUnits() {
		return bagsUnits;
	}

	public void setBagsUnits(Integer bagsUnits) {
		this.bagsUnits = bagsUnits;
	}

	public Integer getGlovesUnits() {
		return glovesUnits;
	}

	public void setGlovesUnits(Integer glovesUnits) {
		this.glovesUnits = glovesUnits;
	}

	public String getToolsUnits() {
		return toolsUnits;
	}

	public void setToolsUnits(String toolsUnits) {
		this.toolsUnits = toolsUnits;
	}

	public void setShovelUnits(Integer shovelUnits) {
		this.shovelUnits = shovelUnits;
	}

	public Integer getShovelUnits() {
		return shovelUnits;
	}

}
