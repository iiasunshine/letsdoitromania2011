package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.GarbageEnrollment;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.MyGarbage;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.MyGarbageComparator;


public class AreaCleanManagerBean {
	
    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    WSInterface wsi;
    private CountyArea[] countyAreas = null;

    /* variabile afisare */
    private List<MyGarbage> myGarbageList = new ArrayList<MyGarbage>();
    private List<MyGarbage> garbageList = new ArrayList<MyGarbage>();
    private User userDetails = new User();
    private Team userTeam = null;    
    private User managerDetails;
    private JSONObject areaJsonBouns = null;
    private List<Garbage> areaGarbages = new ArrayList<Garbage>();
    private List<Team> areaTeams = new ArrayList<Team>();
    private String info = "Info: <br/>";
    private JSONObject selectedCounty = null;
    private boolean managerBool = false;
    private List<Team> teamList = new ArrayList<Team>();
    private String country;
    private Team teamSelected=null;
    private List<GarbageEnrollment> userGarbageEnr= new ArrayList<GarbageEnrollment>();
    
    private String currentLat="44.4317879";
    private String currentLng="26.1015844";
	
    private int teamSelectedId=-1;
    
	public AreaCleanManagerBean() throws NamingException{
		log4j.info("---------[AREACLEANMANAGER] - fresh start");
		wsi = new WSInterface();
	  
	/**
     * obtinere detalii utilizator
     */
    userDetails = (User) JsfUtils.getHttpSession().getAttribute("USER_DETAILS");
    String lastPosition = (String) JsfUtils.getHttpSession().getAttribute("LASTPOSITION");
    if(lastPosition!=null)
    {
    	currentLat=lastPosition.split(",")[0].toString();
    	currentLng=lastPosition.split(",")[1].toString();
    	JsfUtils.getHttpSession().removeAttribute("LASTPOSITION");
    };
    
    if(JsfUtils.getHttpSession().getAttribute("TEAM_SELECTED")!=null){
    	teamSelectedId = (Integer)  JsfUtils.getHttpSession().getAttribute("TEAM_SELECTED");
    };
    log4j.info("---------[AREACLEANMANAGER] - teamSelectedId: "+teamSelectedId);
    /* adaugare mesaj info de pe sesiune daca exista */
    String infoMessage = (String) JsfUtils.getHttpSession().getAttribute("INFO_MESSAGE");
    if (infoMessage != null) {
        JsfUtils.addInfoMessage(infoMessage);
        JsfUtils.getHttpSession().removeAttribute("INFO_MESSAGE");
    }
    
    teamSelected=null;
    

    /* adaugare mesaj warn de pe sesiune daca exista */
    String warnMessage = (String) JsfUtils.getHttpSession().getAttribute("WARN_MESSAGE");
    if (warnMessage != null) {
        JsfUtils.addErrorMessage(warnMessage);
        JsfUtils.getHttpSession().removeAttribute("WARN_MESSAGE");
    }
    /**
     * echipa utilizatorului curent (=> lista zone atribuite)
     */
    if (true) {
            userTeam = userDetails.getMemberOf();
            initManager();
            managedTeams();
            
            //load garbages on every team
            if(teamList != null && teamList.size() > 0){
            	for(Team team : teamList){
            		
            		if(teamSelected==null&&teamSelectedId==-1)
                    {
            			teamSelected=team;
            			JsfUtils.getHttpSession().setAttribute("TEAM_SELECTED", teamSelected.getTeamId());            			
                    }
            		if(teamSelected==null&&teamSelectedId==team.getTeamId())
                    {
            			teamSelected=team;
            			JsfUtils.getHttpSession().setAttribute("TEAM_SELECTED", teamSelected.getTeamId());            			
                    }


            	areaGarbages =wsi.getGarbageofTeam(userDetails, team.getTeamId());
                    	
                    	                    	
                    	if(areaGarbages.isEmpty()!=true&&areaGarbages.size()!=0){
                    		//garbageEnrollements.addAll(Arrays.asList(cr.getEntity(Garbage[].class)));                    		
                    		//Set<Garbage> foo = new HashSet<Garbage>(areaGarbages);
                    		//List<Garbage> foo = new List<Garbage>(areaGarbages);
                    				
                        	team.setGarbages(areaGarbages);
                        		 
                    		};
                    		if(teamSelected!=null)
                    			if(teamSelected.getTeamId()==team.getTeamId())
                    				teamSelected=team;
                    
            		

            	}
            }
        
            log4j.info("---------[AREACLEANMANAGER] - finish start");

    }
    

        countyAreas = wsi.getCountyList();
        info += "CountyAreas = " + countyAreas.length + "<br/>";
    //init garbages
    	init();
    	
	}
	
	public void init(){
		
        areaGarbages = wsi.getGarbageFromCounty(userDetails, country, null, null);
        
//

		if(country != null){
			  for (int i = 0; i < countyAreas.length; i++) {
	                CountyArea ca = countyAreas[i];
	                if(ca.getName().equals(country)){
	                    /* obiect JSON cu coordonatele pentru zoom si focus */
	                    try {
	                        areaJsonBouns = new JSONObject();
	                        areaJsonBouns.put("bottomRightX", "" + ca.getBottomRightX());
	                        areaJsonBouns.put("bottomRightY", "" + ca.getBottomRightY());
	                        areaJsonBouns.put("topLeftX", "" + ca.getTopLeftX());
	                        areaJsonBouns.put("topLeftY", "" + ca.getTopLeftY());
//	    	                JSONArray jSONArray = new JSONArray();
//	    	                for (int j = 0; j < ca.getPolyline().size(); j++) {
//	    	                    Point2D.Double point = ca.getPolyline().get(j);
//	    	                    jSONArray.put(point.getY());
//	    	                    jSONArray.put(point.getX());
//	    	                }
//	    	                areaJsonBouns.put("border", jSONArray);
	                    } catch (Exception ex) {
	                        log4j.fatal("Eroare construire areaJsonBouns: " + AppUtils.printStackTrace(ex));
	                    }
	                    break;
	                }
	            }
		}
		
		
//        Iterator<Garbage> iterator = garbage.iterator();
        for (Garbage g: areaGarbages) {

        	
//            Garbage g = iterator.next();       
            String infoHtml = "<strong>" + JsfUtils.getBundleMessage("details_morman") + " " + g.getGarbageId() + "</strong><br/>";
//            infoHtml += JsfUtils.getBundleMessage("details_area") + " " + (g.getChartedArea() != null ? g.getChartedArea().getName() : "unknown") + "<br/>";
//            infoHtml += JsfUtils.getBundleMessage("details_county") + " " + (g.getCounty() != null ? g.getCounty().getName() : "unknown") + "<br/>";
            infoHtml += JsfUtils.getBundleMessage("details_state") + " " + (g.getStatus() != null ? g.getStatus().name() : "unknown") + "<br/><br/>";
            infoHtml += (g.getDescription() != null ? g.getDescription() : "") + "<br/>";
            infoHtml += "<br/><a href=\"curatenie-morman-detalii.jsf?garbageId=" + g.getGarbageId() + "\" style=\"color: #4D751F;\">" + "&raquo; Detalii / Aloca mormanul pentru echipa ta" + "</a>";
            garbageList.add(new MyGarbage(g, infoHtml));
        }
        Collections.sort(garbageList, new MyGarbageComparator());
		
		
		
	}
	
	
	public void actionSelectTeam()
	{
		
		int teamId=AppUtils.parseToInt(JsfUtils.getRequestParameter("team"));
		log4j.info("[AREACLEANMANAGER]: trying to set team number "+teamId+" for cleaning");
		JsfUtils.getHttpSession().removeAttribute("TEAM_SELECTED");
		//JsfUtils.getHttpSession().setAttribute("TEAM_SELECTED", teamSelected.getTeamId());
		JsfUtils.getHttpSession().setAttribute("TEAM_SELECTED", teamId);
		reloadTeam(teamId);
		/*
	    if(teamList != null && teamList.size() > 0)
        	for(Team team : teamList){

        		//first team gets selected
        		if(teamId==team.getTeamId())
                {
        			teamSelected=team;
        			log4j.info("[AREACLEANMANAGER]: added on session TEAM_SELECTED:"+teamSelected.getTeamId());
        			JsfUtils.getHttpSession().removeAttribute("TEAM_SELECTED");
        			JsfUtils.getHttpSession().setAttribute("TEAM_SELECTED", teamSelected.getTeamId());
        			break;
                };
        	};*/
	}
	
	public void reloadTeam(int id){
    	teamList = userDetails.getManagedTeams();
    	log4j.info("user teams:" + teamList);

	    if(teamList != null && teamList.size() > 0)
        	for(Team team : teamList){

        		//first team gets selected
        		if(id==team.getTeamId())
                {
        			teamSelected=team;
        			break;
                };
        	};

		
	}
	
    public void managedTeams(){
    	teamList = userDetails.getManagedTeams();
    	log4j.info("user teams:" + teamList);
    }
    
	public void initManager(){
		
		try {
			managerDetails = userTeam.getTeamManager();
			log4j.info("managerDetails->" + managerDetails);
		} catch (Exception e) {
			log4j.debug("error->" + e.getMessage());
		}
		
		setManagerBool(managerTest());	
	}
	
	public boolean managerTest(){
		
		if(userDetails != null && managerDetails != null
				&& userDetails.getUserId().equals(managerDetails.getUserId())){
			return true;
		}	
		return false;
	}
	public String garbageFromCountry(){
		init();
		return "";
	}
    public List<SelectItem> getCountyItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();

        try {
            for (int i = 0; i < countyAreas.length; i++) {
                CountyArea ca = countyAreas[i];
                JSONObject jSONObject = new JSONObject();
//                jSONObject.put("name", ca.getName());
//                jSONObject.put("bottomRightX", ca.getBottomRightX());
//                jSONObject.put("bottomRightY", ca.getBottomRightY());
//                jSONObject.put("topLeftX", ca.getTopLeftX());
//                jSONObject.put("topLeftY", ca.getTopLeftY());

//                JSONArray jSONArray = new JSONArray();
//                for (int j = 0; j < ca.getPolyline().size(); j++) {
//                    Point2D.Double point = ca.getPolyline().get(j);
//                    jSONArray.put(point.getY());
//                    jSONArray.put(point.getX());
//                }
//                jSONObject.put("border", jSONArray);

//                items.add(new SelectItem(jSONObject, ca.getName()));
                items.add(new SelectItem(ca.getName(), ca.getName()));
            }
        } catch (Exception ex) {
            log4j.fatal("eroare: " + ex);
        }

        return items;
    }
    
    public Team getTeamSelected(){
    	return this.teamSelected;
    }
    
    public void setTeamSelected(Team team){
    	this.teamSelected=team;
    }
    
	public void setManagerBool(boolean managerBool) {
		this.managerBool = managerBool;
	}
	public boolean isManagerBool() {
		return managerBool;
	}
	public List<Team> getTeamList() {
		return teamList;
	}
	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}

	public List<MyGarbage> getMyGarbageList() {
		return myGarbageList;
	}

	public void setMyGarbageList(List<MyGarbage> myGarbageList) {
		this.myGarbageList = myGarbageList;
	}

	public User getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}

	public List<Garbage> getAreaGarbages() {
		return areaGarbages;
	}

	public void setAreaGarbages(List<Garbage> areaGarbages) {
		this.areaGarbages = areaGarbages;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountry() {
		return country;
	}

	public JSONObject getAreaJsonBouns() {
		return areaJsonBouns;
	}

	public void setAreaJsonBouns(JSONObject areaJsonBouns) {
		this.areaJsonBouns = areaJsonBouns;
	}

	public List<MyGarbage> getGarbageList() {
		return garbageList;
	}

	public void setGarbageList(List<MyGarbage> garbageList) {
		this.garbageList = garbageList;
	}
	
	public String getCurrentLat() {
		return currentLat;
	}
	
	public String getCurrentLng() {
		return currentLng;
	}

	public String getCountySelectedValue(){
		return wsi.getCountySelectedValue();
	}
	
	public void setCountySelectedValue(String value){
		wsi.setCountySelectedValue(value);
	}
	
	
}
