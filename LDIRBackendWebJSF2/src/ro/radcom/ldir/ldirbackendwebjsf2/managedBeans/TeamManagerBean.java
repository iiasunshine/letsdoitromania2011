package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import ro.ldir.dto.CleaningEquipment;
import ro.ldir.dto.Equipment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;
import ro.ldir.dto.TransportEquipment.TransportType;
import ro.ldir.dto.User;
import ro.ldir.dto.CleaningEquipment.CleaningType;
import ro.ldir.dto.Organization.OrganizationType;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.DataValidation;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.CountyNames;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;

import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.server.spi.monitoring.glassfish.GlobalStatsProvider;

public class TeamManagerBean {

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    WSInterface wsi = new WSInterface();
	
	
	
	private boolean managerBool = false;
	private boolean equipmentBool = false;
	private boolean orgBool =false;
	private Team userTeam;
    private User userDetails = new User();
    private User managerDetails;
    private Organization organization = new Organization();
    private Integer teamID;
    private List<User> volunteerMembers;
    private List<Organization> organizationMembers;
    private List<Equipment> equipments;
    private String tipOrganization;

    private String participants;
    private Integer gpsUnits;
    private String transport;
    private Integer bagsUnits;
    private Integer glovesUnits;
    private Integer shovelUnits;
    private String toolsUnits;

	public TeamManagerBean() {
		userDetails = (User) JsfUtils.getHttpSession().getAttribute("USER_DETAILS");
		log4j.debug("user ->"+userDetails+", team ->"+userTeam);

		ClientResponse cr = wsi.getMemberOfTeam(userDetails.getUserId());

		if (cr.getStatus() != 200) {
			log4j.debug("nu s-a reusit obtinerea echipei utlizatorului curent (statusCode="
					+ cr.getStatus()+ " responseStatus="+ cr.getResponseStatus() + ")");
//			JsfUtils.addWarnBundleMessage("warrning_no_team");
		} else {
//			String team = cr1.getEntity(String.class);

			userTeam = cr.getEntity(Team.class);
			initEquipments();
			initTeam();
			initManager();
			initTeamMembers();	
			initOrganization();
		}

	}
	private void initEquipments(){
		if(userTeam != null && userTeam.getEquipments()!= null &&
				userTeam.getEquipments().size() > 0){
			equipments= userTeam.getEquipments();
			for(Equipment equi:equipments){
				if(equi instanceof GpsEquipment){
					gpsUnits = ((GpsEquipment) equi).getCount();
				}
				if(equi instanceof CleaningEquipment){
					if(((CleaningEquipment) equi).getCleaningType().equals(CleaningType.BAGS)){
						bagsUnits = ((CleaningEquipment) equi).getCount();
					}else if(((CleaningEquipment) equi).getCleaningType().equals(CleaningType.GLOVES)){
						glovesUnits = ((CleaningEquipment) equi).getCount();
					}else if(((CleaningEquipment) equi).getCleaningType().equals(CleaningType.SHOVEL)){
						shovelUnits =((CleaningEquipment) equi).getCount();
					}
				}
				if(equi instanceof TransportEquipment){
					transport = ((TransportEquipment) equi).getTransportType().toString();
				}

			}
			equipmentBool = true;
		}
	}

	private void initTeam() {
		
		try {
			String location = JsfUtils.getInitParameter("webservice.url")
					+ "/LDIRBackend/ws/team/" + userTeam.getTeamId();
			Client client = Client.create();
			WebResource resource = client.resource(location);
			Builder builder = resource.header(HttpHeaders.AUTHORIZATION,
					AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
			                JsfUtils.getInitParameter("admin.password")));
			ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML)
					.get(ClientResponse.class);
			userTeam = cr.getEntity(Team.class);
			int statusCode = cr.getStatus();
			log4j.debug("---> statusCode: " + statusCode + " ("
					+ cr.getClientResponseStatus() + ")");
			setVolunteerMembers(userTeam.getVolunteerMembers());
		} catch (Exception e) {
			log4j.debug("error->" + e.getMessage());
		}

		
	}
	public void initManager(){
		
		try {
			String location = JsfUtils.getInitParameter("webservice.url")
					+ "/LDIRBackend/ws/team/" + userTeam.getTeamId()
					+ "/teamManager";
			Client client = Client.create();
			WebResource resource = client.resource(location);
			Builder builder = resource.header(HttpHeaders.AUTHORIZATION,
					AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
			                JsfUtils.getInitParameter("admin.password")));
			ClientResponse cr = builder.entity(managerDetails,
					MediaType.APPLICATION_XML).get(ClientResponse.class);
			managerDetails = cr.getEntity(User.class);
			log4j.debug("managerDetails->" + managerDetails);
		} catch (Exception e) {
			log4j.debug("error->" + e.getMessage());
		}
		
		managerBool = managerTest();	
	}
	public void initTeamMembers(){
		
		try {
			String location = JsfUtils.getInitParameter("webservice.url")
					+ "/LDIRBackend/ws/team/" + userTeam.getTeamId()+"/volunteerMembers";
			Client client = Client.create();
			WebResource resource = client.resource(location);
			Builder builder = resource.header(HttpHeaders.AUTHORIZATION,
					AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
			                JsfUtils.getInitParameter("admin.password")));
			
			ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).get(ClientResponse.class);
			volunteerMembers = cr.getEntity(new GenericType<List<User>>() {});
			int statusCode = cr.getStatus();
			log4j.debug("---> statusCode: " + statusCode + " ("+ cr.getClientResponseStatus() + ")");
			if(statusCode == 200){
				if(volunteerMembers != null && managerDetails != null)
				volunteerMembers.remove(managerDetails);
			}
		} catch (Exception e) {
			log4j.debug("error->" + e.getMessage());
		}
		
	}
	public void initOrganization(){
		
		try {
			String location = JsfUtils.getInitParameter("webservice.url")
			+ "/LDIRBackend/ws/user/" + userDetails.getUserId()+"/organizations";
			Client client = Client.create();
			WebResource resource = client.resource(location);
			Builder builder = resource.header(HttpHeaders.AUTHORIZATION,
					AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"), JsfUtils.getInitParameter("admin.password")));
			
			ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).get(ClientResponse.class);
			organizationMembers = cr.getEntity(new GenericType<List<Organization>>() {});
			
			int statusCode = cr.getStatus();
			log4j.debug("---> statusCode: " + statusCode + " ("+ cr.getClientResponseStatus() + ")");
			if(statusCode == 200){
				organization = organizationMembers.get(0);
				orgBool = true;
			}
		} catch (Exception e) {
			log4j.debug("error->" + e.getMessage());
		}
		
	}

	public String actionAddToTeam(){
		
		if(teamID == null || teamID == 0){
		       JsfUtils.addWarnBundleMessage("err_mandatory_fields");
		       return NavigationValues.USER_ADD_TEAM_FAIL;
		}
		userTeam = new Team();
		userTeam.setTeamId(teamID);
		
		/* add user to team */
		String location = JsfUtils.getInitParameter("webservice.url") +"/LDIRBackend/ws/user/"+ userDetails.getUserId()+"/team";
		Client client = Client.create();
	    WebResource resource = client.resource(location);
	    Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
	    ClientResponse cr = builder.entity(userTeam, MediaType.APPLICATION_XML).post(ClientResponse.class);
	        
	    int statusCode = cr.getStatus();
        log4j.debug("---> statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        
        /* verificare statusCode si adaugare mesaje */
        if(statusCode == 200){
        	  JsfUtils.addInfoBundleMessage("success_add_mem_message");
        	  return NavigationValues.USER_ADD_TEAM_FAIL;
        }else if(statusCode == 404){
        		JsfUtils.addWarnBundleMessage("user_not_exist_message");
              return NavigationValues.USER_ADD_TEAM_FAIL;
        }else if(statusCode == 409){
        		JsfUtils.addWarnBundleMessage("err_user_enrolled");
        	  return NavigationValues.USER_ADD_TEAM_FAIL;
        }else{
        	 return NavigationValues.USER_ADD_TEAM_FAIL;
        }
		
	}
	public String actionDelFromTeam(){
		
	
		String location = JsfUtils.getInitParameter("webservice.url") +"/LDIRBackend/ws/team/"+ userTeam.getTeamId()+"/volunteer/"+userDetails.getUserId();
		Client client = Client.create();
	    WebResource resource = client.resource(location);
	    Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
	    ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).delete(ClientResponse.class);
	        
	    int statusCode = cr.getStatus();
        log4j.debug("---> statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        
        /* verificare statusCode si adaugare mesaje */
        if(statusCode == 200){
        	  JsfUtils.addInfoBundleMessage("success_del_mem_message");
        	  return NavigationValues.USER_REM_TEAM_FAIL;
        }else{
        	JsfUtils.addWarnBundleMessage("err_user_rem");
        	 return NavigationValues.USER_REM_TEAM_FAIL;
        }
	}
	public String actionWithdrawFromTeam(){
		
		String memDeleteId = JsfUtils.getRequestParameter("memDeleteId");
		if(memDeleteId == null || memDeleteId.length()==0){
        	JsfUtils.addWarnBundleMessage("err_user_rem");
       	    return NavigationValues.TEAM_REM_MEM_FAIL;
		}
		String location = JsfUtils.getInitParameter("webservice.url") +"/LDIRBackend/ws/team/"+ userTeam.getTeamId()+"/volunteer/"+memDeleteId;
		Client client = Client.create();	
	    WebResource resource = client.resource(location);
	    Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
	    ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).delete(ClientResponse.class);
	        
	    int statusCode = cr.getStatus();
        log4j.debug("---> statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        
        /* verificare statusCode si adaugare mesaje */
        if(statusCode == 200){
        	  User tempUser = new User();
        	  tempUser.setUserId(new Integer(memDeleteId));
        	  volunteerMembers.remove(tempUser);
        	  JsfUtils.addInfoBundleMessage("success_del_mem_message");
        	  return NavigationValues.TEAM_REM_MEM_FAIL;
        }else{
        	JsfUtils.addWarnBundleMessage("err_user_rem");
        	 return NavigationValues.TEAM_REM_MEM_FAIL;
        }
		
	}
	
	public String actionAddOrg(){

		if(organization.getName() == null || organization.getName().length()==0
				|| organization.getAddress() == null || organization.getAddress().length()==0
				|| organization.getTown() == null || organization.getTown().length()==0
				||organization.getCounty() == null || organization.getCounty().length()==0
				||organization.getContactFirstname() == null || organization.getContactFirstname().length()==0
				||organization.getContactLastname() == null || organization.getContactLastname().length()==0
				||organization.getContactPhone() == null || organization.getContactPhone().length()==0
				||organization.getContactEmail() == null || organization.getContactEmail().length()==0
				||organization.getMembersCount() == null){
		       JsfUtils.addWarnBundleMessage("err_mandatory_fields");
		       return NavigationValues.TEAM_ADD_ORG_FAIL;
		}
		
//		if(DataValidation.validateNumber(organization.getMembersCount()))
		
		if(tipOrganization != null && tipOrganization.length() != 0){
			if(tipOrganization.equalsIgnoreCase("CITY_HALL")){
				organization.setType(OrganizationType.CITY_HALL);
			}else if(tipOrganization.equalsIgnoreCase("COMPANY")){
				organization.setType(OrganizationType.COMPANY);
			}else if(tipOrganization.equalsIgnoreCase("LANDFILL")){
				organization.setType(OrganizationType.LANDFILL);
			}else if(tipOrganization.equalsIgnoreCase("REGISTRATION_POINT")){
				organization.setType(OrganizationType.REGISTRATION_POINT);
			}else if(tipOrganization.equalsIgnoreCase("SCHOOL")){
				organization.setType(OrganizationType.SCHOOL);
			}		
		}
		 log4j.info("--->  organization: " + tipOrganization );
		organization.setMemberOf(userTeam);
		organization.setManager(managerDetails);
		/* create organization */
		String location = JsfUtils.getInitParameter("webservice.url") + "/LDIRBackend/ws/organization";
		Client client = Client.create();
	    WebResource resource = client.resource(location);
	    Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
	    ClientResponse cr = builder.entity(organization, MediaType.APPLICATION_XML).post(ClientResponse.class);
	    log4j.debug("--->  organization: " + organization.getOrganizationId() );
 
	    int statusCode = cr.getStatus();
        log4j.debug("---> statusCode organization: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        if(statusCode == 200){  
        	 orgBool = true;
		     JsfUtils.addInfoBundleMessage("success_edit_message");
        }else{
        	 JsfUtils.addWarnBundleMessage("err_mandatory_fields");		      
        }
        return NavigationValues.TEAM_ADD_ORG_FAIL;

	}
	public String actionEditOrg(){
		if(organization.getName() == null || organization.getName().length()==0
				|| organization.getAddress() == null || organization.getAddress().length()==0
				|| organization.getTown() == null || organization.getTown().length()==0
				||organization.getCounty() == null || organization.getCounty().length()==0
				||organization.getContactFirstname() == null || organization.getContactFirstname().length()==0
				||organization.getContactLastname() == null || organization.getContactLastname().length()==0
				||organization.getContactPhone() == null || organization.getContactPhone().length()==0
				||organization.getContactEmail() == null || organization.getContactEmail().length()==0
				||organization.getMembersCount() == null || organization.getMembersCount() <= 0){
		       JsfUtils.addWarnBundleMessage("err_mandatory_fields");
		       return NavigationValues.TEAM_ADD_ORG_FAIL;
		}
		
		if(tipOrganization != null && tipOrganization.length() != 0){
			if(tipOrganization.equalsIgnoreCase("CITY_HALL")){
				organization.setType(OrganizationType.CITY_HALL);
			}else if(tipOrganization.equalsIgnoreCase("COMPANY")){
				organization.setType(OrganizationType.COMPANY);
			}else if(tipOrganization.equalsIgnoreCase("LANDFILL")){
				organization.setType(OrganizationType.LANDFILL);
			}else if(tipOrganization.equalsIgnoreCase("REGISTRATION_POINT")){
				organization.setType(OrganizationType.REGISTRATION_POINT);
			}else if(tipOrganization.equalsIgnoreCase("SCHOOL")){
				organization.setType(OrganizationType.SCHOOL);
			}		
		}
		 log4j.info("--->  organization: " + tipOrganization +","+ organization.getType().toString());
		/* edit organization */
		String location = JsfUtils.getInitParameter("webservice.url") + "/LDIRBackend/ws/organization/"+organization.getOrganizationId();
		Client client = Client.create();
	    WebResource resource = client.resource(location);
	    Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
	    ClientResponse cr = builder.entity(organization, MediaType.APPLICATION_XML).put(ClientResponse.class);
 
	    int statusCode = cr.getStatus();
        log4j.debug("---> statusCode organization: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        if(statusCode == 200){  
        	 orgBool = true;
		     JsfUtils.addInfoBundleMessage("success_edit_message");
        }else{
        	 JsfUtils.addWarnBundleMessage("err_mandatory_fields");		      
        }
        return NavigationValues.TEAM_ADD_ORG_FAIL;
	}
	public String actionDelOrgTeam(){
		
		String orgDeleteId = JsfUtils.getRequestParameter("orgDeleteId");
		if(orgDeleteId == null || orgDeleteId.length()==0){
        	JsfUtils.addWarnBundleMessage("err_user_rem");
       	    return NavigationValues.TEAM_REM_MEM_FAIL;
		}
	
		String location = JsfUtils.getInitParameter("webservice.url") +"/LDIRBackend/ws/organization/"+orgDeleteId;		
		Client client = Client.create();	
	    WebResource resource = client.resource(location);
	    Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
	    ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).delete(ClientResponse.class);
	        
	    int statusCode = cr.getStatus();
       log4j.debug("---> statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
       
       /* verificare statusCode si adaugare mesaje */
       if(statusCode == 200){
    	  orgBool = false;
    	  organizationMembers =null;
       	  JsfUtils.addInfoBundleMessage("success_del_mem_message");
       	  return NavigationValues.TEAM_REM_MEM_FAIL;
       }else{
        	JsfUtils.addWarnBundleMessage("err_user_rem");
       	 return NavigationValues.TEAM_REM_MEM_FAIL;
       }
		
	}
	public String actionAddEquipment(){
    	
        //reset equipments
		if(equipments!=null){
			for(Equipment equi: equipments){
			String location = JsfUtils.getInitParameter("webservice.url")
				+ "/LDIRBackend/ws/team/" + userTeam.getTeamId() + "/equipmentId/"+equi.getEquipmentId();
			Client client = Client.create();
		    WebResource resource = client.resource(location);
		    Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
		    ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).delete(ClientResponse.class);
		    
		    int statusCode = cr.getStatus();
		    log4j.debug("---> statusCode equipment del from team: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
			}
		} 

		
		
		
		if (gpsUnits != null && gpsUnits > 0) {			
			GpsEquipment gps = new GpsEquipment();
			gps.setCount(gpsUnits);
			String location = JsfUtils.getInitParameter("webservice.url")
					+ "/LDIRBackend/ws/team/" + userTeam.getTeamId() + "/gps";
			Client client = Client.create();
			WebResource resource = client.resource(location);
			Builder builder = resource.header(
					HttpHeaders.AUTHORIZATION,AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
			ClientResponse cr = builder.entity(gps, MediaType.APPLICATION_XML).put(ClientResponse.class);
			int statusCode = cr.getStatus();
			
			log4j.debug("---> statusCode gps add team: " + statusCode + " ("
					+ cr.getClientResponseStatus() + ")");
		}
		if (bagsUnits != null && bagsUnits > 0) {
			CleaningEquipment bags = new CleaningEquipment();
			bags.setCleaningType(CleaningType.BAGS);
			bags.setCount(bagsUnits);
			String location = JsfUtils.getInitParameter("webservice.url")
					+ "/LDIRBackend/ws/team/" + userTeam.getTeamId() + "/cleaning";
			Client client = Client.create();
			WebResource resource = client.resource(location);
			Builder builder = resource.header(
					HttpHeaders.AUTHORIZATION,AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
			ClientResponse cr = builder.entity(bags, MediaType.APPLICATION_XML)
					.put(ClientResponse.class);
			int statusCode = cr.getStatus();
			
			log4j.debug("---> statusCode bags add team: " + statusCode + " ("
					+ cr.getClientResponseStatus() + ")");
		}
		if (glovesUnits != null && glovesUnits > 0) {
			CleaningEquipment gloves = new CleaningEquipment();
			gloves.setCleaningType(CleaningType.GLOVES);
			gloves.setCount(glovesUnits);
			String location = JsfUtils.getInitParameter("webservice.url")
					+ "/LDIRBackend/ws/team/" + userTeam.getTeamId() + "/cleaning";
			Client client = Client.create();
			WebResource resource = client.resource(location);
			Builder builder = resource.header(
					HttpHeaders.AUTHORIZATION,AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
			ClientResponse cr = builder.entity(gloves, MediaType.APPLICATION_XML)
					.put(ClientResponse.class);
			int statusCode = cr.getStatus();
			
			log4j.debug("---> statusCode bags add team: " + statusCode + " ("
					+ cr.getClientResponseStatus() + ")");
		}
		if (shovelUnits != null && shovelUnits > 0) {
			CleaningEquipment shovel = new CleaningEquipment();
			shovel.setCleaningType(CleaningType.SHOVEL);
			shovel.setCount(shovelUnits);
			String location = JsfUtils.getInitParameter("webservice.url")
					+ "/LDIRBackend/ws/team/" + userTeam.getTeamId() + "/cleaning";
			Client client = Client.create();
			WebResource resource = client.resource(location);
			Builder builder = resource.header(
					HttpHeaders.AUTHORIZATION,AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
			ClientResponse cr = builder.entity(shovel, MediaType.APPLICATION_XML)
					.put(ClientResponse.class);
			int statusCode = cr.getStatus();
			
			log4j.debug("---> statusCode shovel add team: " + statusCode + " ("
					+ cr.getClientResponseStatus() + ")");
		}	
		if (transport != null && transport.length() != 0) {
			TransportEquipment trans = new TransportEquipment();
			if(transport.equals("BICYCLE")){
				trans.setTransportType(TransportType.BICYCLE);
			}else if(transport.equals("CAR")){
				trans.setTransportType(TransportType.CAR);
			}else if(transport.equals("ORGANIZATION_CAR")){
				trans.setTransportType(TransportType.ORGANIZATION_CAR);
			}else if(transport.equals("PUBLIC")){
				trans.setTransportType(TransportType.PUBLIC);
			}
			String location = JsfUtils.getInitParameter("webservice.url")
					+ "/LDIRBackend/ws/team/" + userTeam.getTeamId() + "/transport"; 
			Client client = Client.create();
			WebResource resource = client.resource(location);
			Builder builder = resource.header(
					HttpHeaders.AUTHORIZATION,AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
			ClientResponse cr = builder.entity(trans, MediaType.APPLICATION_XML)
					.put(ClientResponse.class);
			int statusCode = cr.getStatus();
			
			log4j.debug("---> statusCode shovel add team: " + statusCode + " ("
					+ cr.getClientResponseStatus() + ")");
		}
		
		JsfUtils.addInfoBundleMessage("success_add_equi_message");
		return NavigationValues.TEAM_ADD_EQUI_FAIL;
	}
	
	public String actionDelEquipment(){
		
//		http://localhost:8080/LDIRBackend/ws/team/teamId /equipment/equipmentId
		String location = JsfUtils.getInitParameter("webservice.url")
			+ "/LDIRBackend/ws/team/" + userTeam.getTeamId() + "/equipmentId/1";
		Client client = Client.create();
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).delete(ClientResponse.class);
        
        int statusCode = cr.getStatus();
        log4j.debug("---> statusCode equipment del from team: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        
		return NavigationValues.USER_ADD_TEAM_FAIL;
	}

	
	public boolean managerTest(){
		
		if(userDetails != null && managerDetails != null
				&& userDetails.getUserId().equals(managerDetails.getUserId())){
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
	public List<SelectItem> getNumbers(){
		 List<SelectItem> items = new ArrayList<SelectItem>();
		
		 for(int i = 1; i < 50 ; i++){
			 items.add(new SelectItem(i,""+i));
		 }
		return items;
	}
	public List<SelectItem> getNumSac(){
		 List<SelectItem> items = new ArrayList<SelectItem>();
		
		 for(int i = 10; i < 1000 ; i = i+10){
			 items.add(new SelectItem(i,""+i));
		 }
		return items;
	}
	public List<SelectItem> getNumbMan(){
		 List<SelectItem> items = new ArrayList<SelectItem>();
		
		 for(int i = 5; i < 200 ; i=i+5){
			 items.add(new SelectItem(i,""+i));
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
