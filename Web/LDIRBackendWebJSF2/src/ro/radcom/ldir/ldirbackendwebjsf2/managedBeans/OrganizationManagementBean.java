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
	    private static final Logger log4j = Logger.getLogger(ResetBean.class.getCanonicalName());
	    private WSInterface wsi;
	    /* variabile afisare */
	    private User userDetails = new User();
	    private String teamName;
	    private List<Team> teamList = new ArrayList<Team>();
	    private List<Team> teamAvalable = new ArrayList<Team>();
		private boolean equipmentBool = false;
		private boolean orgBool =false;
		private int teamId;


	    public OrganizationManagementBean() throws NamingException{
	    	wsi = new WSInterface();
	    	userDetails = (User)JsfUtils.getHttpSession().getAttribute("USER_DETAILS");
	    	
	    	teamId = AppUtils.parseToInt(JsfUtils.getRequestParameter("teamId"));
	    	if(teamId >0){

				Team userTeam = userDetails.getMemberOf();
				
				 teamName = userTeam.getTeamName();
				 if(userTeam.getEquipments() != null && userTeam.getEquipments().size() >0 ){
					 equipmentBool = true;
				 }
				 managedOrganization(userTeam);
//				 }
				 log4j.info("user teams:" + teamName);
	    	}
	    	managedTeams();
	    	
	    }
	    

	    public void managedOrganization(Team userTeam){
	    	List<Organization> organizationMembers = userDetails.getOrganizations();
			
		   
				if(organizationMembers != null && organizationMembers.size() > 0){
				    for(Organization org: organizationMembers){
				    	if(org.getOrganizationId() != 0 && org.getOrganizationId() > 0){		    		
				    			Team  team = org.getMemberOf();
				    			if(team.getTeamId().equals(teamId)){
				    				orgBool = true;
				    				break;
				    			}
				    			log4j.debug("--->  organization: " + team);
				    }
				}
			
		    
	    	}
	    
	    }
	    public void managedTeams(){
	    	teamList = userDetails.getManagedTeams();
	    	log4j.info("user teams:" + teamList);
	    }

		public String actionTeam(){
	    	   	
	        /**
	         * validare campuri
	         */
	        if(teamName == null || teamName.trim().length() == 0) {
	            JsfUtils.addWarnBundleMessage("err_mandatory_fields");
	            return NavigationValues.TEAM_ORG_MULTI_FAIL;
	        }
	        if(teamName.length() < 4){
	            JsfUtils.addWarnBundleMessage("err_team_name");
	            return NavigationValues.TEAM_ORG_MULTI_FAIL;
	        }
	        
	        Team teamTemp = new Team();
	        teamTemp.setTeamName(teamName);
	        
	        wsi.addTeam(teamTemp);
	    	managedTeams();
	        JsfUtils.addInfoBundleMessage("success_edit_message");
	          return NavigationValues.TEAM_ORG_MULTI_FAIL;
	   
//	        String teamNameEncod = encodeUrl(teamName);
//	    	 /* update date utilizator */
//	        String location = JsfUtils.getInitParameter("webservice.url")+"/LDIRBackend/ws/team/nameSearch?teamName="+teamNameEncod;
//	        Client client = Client.create();
//	        WebResource resource = client.resource(location);
//	        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
////	        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).get(ClientResponse.class);
//	    	ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
//	        int statusCode = cr.getStatus();
//	        log4j.info("---> statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
//
//	        teamAvalable = cr.getEntity(new GenericType<List<Team>>(){});
//	        if(teamAvalable != null && teamAvalable.size() == 0){
//	        	  JsfUtils.addWarnBundleMessage("fail_add_team_message");
//		          return NavigationValues.TEAM_ORG_MULTI_FAIL;
//	        }else if(teamAvalable != null && teamAvalable.size() == 1){
//	        	//add team to user
////	             location = JsfUtils.getInitParameter("webservice.url")+"/LDIRBackend/ws/user/"+userDetails.getUserId()+"/team";
//	             location = JsfUtils.getInitParameter("webservice.url")+"/LDIRBackend/ws/team/";
//	             client = Client.create();
//	 	         resource = client.resource(location);
//	 	         builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
//	 	         cr = builder.entity(teamAvalable, MediaType.APPLICATION_XML).post(ClientResponse.class);
//	 	         statusCode = cr.getStatus();
//
//	        	  JsfUtils.addInfoBundleMessage("success_edit_message");
//		          return NavigationValues.TEAM_ORG_MULTI_FAIL;
//	        }else{
//	        	for(Team team: teamAvalable){
//	        		log4j.info("---> teamAvalable: " + team.getTeamName());
//	        	}
////	       	 JsfUtils.addInfoBundleMessage("success_edit_message");
//	        }


	    }
		
		public String actionChangeNameTeam(){
	        /**
	         * validare campuri
	         */
	        if(teamName == null || teamName.trim().length() == 0) {
	            JsfUtils.addWarnBundleMessage("err_mandatory_fields");
	            return NavigationValues.TEAM_ORG_MULTI_FAIL;
	        }
	        if(teamName.length() < 4){
	            JsfUtils.addWarnBundleMessage("err_team_name");
	            return NavigationValues.TEAM_ORG_MULTI_FAIL;
	        }
	        
	        Team teamTemp = new Team();
	        teamTemp.setTeamName(teamName);
	        wsi.updateTeam(teamId, teamTemp);
	        managedTeams();
	        JsfUtils.addInfoBundleMessage("success_edit_message");
	          return NavigationValues.TEAM_ORG_MULTI_FAIL;
		}
		public String actionDeleteTeam(){
			

            wsi.deleteTeam(teamId);
	        managedTeams();
	        JsfUtils.addInfoBundleMessage("success_edit_message");
	          return NavigationValues.TEAM_ORG_MULTI_FAIL;
		}
		
		 public String encodeUrl(String arg){

//			 String   url = "http://search.barnesandnoble.com/booksearch/results.asp?WRD=test 2324";		
			  try{
				  arg = URLEncoder.encode(arg,"UTF-8"); 
				  log4j.debug("---> encode: " + arg);
			  }catch(UnsupportedEncodingException uee){
				  log4j.debug("---> encode error: " + uee.getMessage());  
			  }
			  return  arg;
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

	
