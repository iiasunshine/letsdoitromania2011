/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.ldir.exceptions.ChartedAreaAssignmentException;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.MyAreaComparator;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

/**
 *
 * @author dan.grigore
 */
public class AreaManagerBean {

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    WSInterface wsi = new WSInterface();
    private CountyArea[] countyAreas = null;

    /* variabile afisare */
    private User userDetails = new User();
    private List<ChartedArea> chartedAreasList = new ArrayList<ChartedArea>();
    private Team userTeam = null;
    private ChartedArea seletedArea = null;

    private JSONObject areaJsonBouns = null;
    private List<Garbage> areaGarbages = new ArrayList<Garbage>();
    private List<Team> areaTeams = new ArrayList<Team>();
    private boolean assigned = false;
    private String info = "Info: <br/>";
    private JSONObject selectedCounty = null;
    private String dummyCounty = "";


    /** Creates a new instance of AreaManagerBean */
    public AreaManagerBean() {
        /**
         * obtinere detalii utilizator
         */
    	
        userDetails = (User) JsfUtils.getHttpSession().getAttribute("USER_DETAILS");

        /**
         * echipa utilizatorului curent (=> lista zone atribuite)
         */
        if (true) {
                userTeam = userDetails.getMemberOf();
        }

        /**
         * obtinere detalii grid selectat daca este cazul
         */
        int areaId = AppUtils.parseToInt(JsfUtils.getRequestParameter("areaId"));
        if (areaId > 0) {
        	seletedArea = wsi.getChartedArea(areaId);
                /* obtinere nr echipe care au mai cartat aceasta zona*/
                areaTeams.addAll(seletedArea.getChartedBy());
                

                /* obtinere lista gunoaie */
                areaGarbages.addAll(seletedArea.getGarbages());
                

                /* obiect JSON cu coordonatele pentru zoom si focus */
                try {
                    areaJsonBouns = new JSONObject();
                    areaJsonBouns.put("bottomRightX", "" + seletedArea.getBottomRightX());
                    areaJsonBouns.put("bottomRightY", "" + seletedArea.getBottomRightY());
                    areaJsonBouns.put("topLeftX", "" + seletedArea.getTopLeftX());
                    areaJsonBouns.put("topLeftY", "" + seletedArea.getTopLeftY());
                } catch (Exception ex) {
                    log4j.fatal("Eroare construire areaJsonBouns: " + AppUtils.printStackTrace(ex));
                }
            }
        

        /**
         * zone de cartare atribuite echipei curente
         */
        if (userTeam != null) {

                chartedAreasList.addAll(wsi.getChartedAreasOfTeam(userDetails, userTeam.getTeamId()));
                Collections.sort(chartedAreasList, new MyAreaComparator());

                /* verificare daca gridul curent se afla in lista celor cartate */
                if (seletedArea != null) {
                    for (int i = 0; i < chartedAreasList.size(); i++) {
                        if (chartedAreasList.get(i).getAreaId().equals(seletedArea.getAreaId())) {
                            assigned = true;
                            break;
                        }
                    }
                }
            
        }

        /**
         * obtinere lista judete
         */
        if (seletedArea == null) {
                countyAreas =  wsi.getCountyList();
                info += "CountyAreas = " + countyAreas.length + "<br/>";
        }

        /**
         * adaugare mesaj info de pe sesiune daca exista
         */
        String infoMessage = (String) JsfUtils.getHttpSession().getAttribute("INFO_MESSAGE");
        if (infoMessage != null) {
            JsfUtils.addInfoMessage(infoMessage);
            JsfUtils.getHttpSession().removeAttribute("INFO_MESSAGE");
        }

        /**
         * adaugare JSON judet selectat daca exista
         */
        String cc = (String) JsfUtils.getHttpSession().getAttribute("SELECTED_COUNTY");
        if (cc != null) {
            JsfUtils.getHttpSession().removeAttribute("SELECTED_COUNTY");

            try {
                for (int i = 0; i < countyAreas.length; i++) {
                    CountyArea ca = countyAreas[i];
                    if (ca.getName().equalsIgnoreCase(cc)) {
                        selectedCounty = new JSONObject();
                        selectedCounty.put("name", ca.getName());
                        selectedCounty.put("bottomRightX", ca.getBottomRightX());
                        selectedCounty.put("bottomRightY", ca.getBottomRightY());
                        selectedCounty.put("topLeftX", ca.getTopLeftX());
                        selectedCounty.put("topLeftY", ca.getTopLeftY());
                        dummyCounty = selectedCounty.toString();
                        log4j.info("--> dummy: "+dummyCounty);
                        break;
                    }
                }
            } catch (Exception ex) {
                log4j.warn("Eroare constructie JSON judet selectat : " + ex);
            }
        }
    }

    public JSONArray getChartedAreasJson() {
        //JSONObject jSONObject = new JSONObject();

        JSONArray jSONArray = new JSONArray();
        try {
            for (int i = 0; i < chartedAreasList.size(); i++) {
                ChartedArea ca = chartedAreasList.get(i);
                JSONObject jSONObject2 = new JSONObject();

                jSONObject2.put("id", "" + ca.getAreaId());
                jSONObject2.put("bottomRightX", ca.getBottomRightX());
                jSONObject2.put("bottomRightY", ca.getBottomRightY());
                jSONObject2.put("topLeftX", ca.getTopLeftX());
                jSONObject2.put("topLeftY", ca.getTopLeftY());
                JSONArray jSONArray2 = new JSONArray();
                for (int j = 0; j < ca.getPolyline().size(); j++) {
                    Point2D.Double point = ca.getPolyline().get(j);
                    jSONArray2.put(point.getY());
                    jSONArray2.put(point.getX());
                }
                jSONObject2.put("border", jSONArray2);
                jSONArray.put(jSONObject2);
            }
            //jSONObject.put("chartedAreas", jSONArray);
        } catch (Exception ex) {
            log4j.fatal("Eroare constructie JSON Array coordonate zone cartate: " + AppUtils.printStackTrace(ex));
        }

        return jSONArray;
    }

    public String actionAssignArea() {
        /* atribuire zona noua daca este cazul */
        int addAreaId = AppUtils.parseToInt(JsfUtils.getRequestParameter("addAreaId"));
        String addAreaName = JsfUtils.getRequestParameter("addAreaName");
        String addAreaCounty = JsfUtils.getRequestParameter("addAreaCounty");
        if (addAreaId > 0 && userTeam != null) {
            try {
            	wsi.addChartedArea(userDetails, userTeam.getTeamId(), addAreaId);
            }  catch (ChartedAreaAssignmentException e) {
                log4j.warn("prea multe echipe pe zona sau prea multe zone la echipa " + addAreaId);
                    JsfUtils.addWarnBundleMessage("internal_err");
                return NavigationValues.AREA_ASSIGN_FAIL;
            } 
                log4j.debug("zona atribuita " + addAreaId);

                String infoText = JsfUtils.getBundleMessage("area_add_confirm").replaceAll("\\{0\\}", "" + addAreaName);
                JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE", infoText);
                JsfUtils.getHttpSession().setAttribute("SELECTED_COUNTY", addAreaCounty);
                return NavigationValues.AREA_ASSIGN_SUCCESS;
            
        }

        return NavigationValues.AREA_ASSIGN_FAIL;
    }

    public String actionRemoveArea() {
        log4j.debug("---> remove area");
        /* stergere zona noua daca este cazul */
        int removeAreaId = AppUtils.parseToInt(JsfUtils.getRequestParameter("removeAreaId"));
        String removeAreaName = JsfUtils.getRequestParameter("removeAreaName");
        String removeAreaCounty = JsfUtils.getRequestParameter("removeAreaCounty");

        log4j.debug("---> removeAreaId: " + removeAreaId + "  userTeam: " + userTeam);
        if (removeAreaId > 0 && userTeam != null) {
            wsi.removeChartedArea(userDetails, userTeam.getTeamId(), removeAreaId);
            
                log4j.debug("zona stearsa " + removeAreaId );

                String infoText = JsfUtils.getBundleMessage("area_remove_confirm").replaceAll("\\{0\\}", "" + removeAreaName);
                JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE", infoText);
                JsfUtils.getHttpSession().setAttribute("SELECTED_COUNTY", removeAreaCounty);
                return NavigationValues.AREA_ASSIGN_SUCCESS;
            
        }

        return NavigationValues.AREA_ASSIGN_FAIL;
    }
    
    /**
     * @return
     
    public String actionSetChartedArea(){
       int cpAreaId = AppUtils.parseToInt(JsfUtils.getRequestParameter("cpAreaId"));
        int cpPercent= AppUtils.parseToInt(JsfUtils.getRequestParameter("cpPercentageCompleted"));
        if (cpAreaId > 0 && userTeam != null) {
            ClientResponse cr = wsi.setChartedPercent(cpAreaId, cpPercent);
            if (cr.getStatus() == 403) {
//
                }
            else if (cr.getStatus() != 200) {
                log4j.fatal("nu s-a reusit selectarea procentului");
                JsfUtils.addWarnBundleMessage("internal_err");
            } else {
                log4j.debug("s-a reusit selectarea procentului" + cpPercent);

                String infoText = JsfUtils.getBundleMessage("area_add_confirm").replaceAll("\\{0\\}", "");
                JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE", infoText);
                return NavigationValues.AREA_ASSIGN_SUCCESS;
            }
        }

    	return NavigationValues.AREA_SET_CHARTED_PERCENT_FAIL;
    }
*/
    /*public List<SelectItem> getCountyItems(){
    List<SelectItem> items = new ArrayList<SelectItem>();

    for (int i=0; i<CountyNames.COUNTIES.size(); i++){
    String county = CountyNames.COUNTIES.get(i);
    items.add(new SelectItem(county, county));
    }

    return items;
    }*/
    public List<SelectItem> getCountyItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();

        try {
            for (int i = 0; i < countyAreas.length; i++) {
                CountyArea ca = countyAreas[i];
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("name", ca.getName().replaceAll(" ", "%20"));
                jSONObject.put("bottomRightX", ca.getBottomRightX());
                jSONObject.put("bottomRightY", ca.getBottomRightY());
                jSONObject.put("topLeftX", ca.getTopLeftX());
                jSONObject.put("topLeftY", ca.getTopLeftY());
                items.add(new SelectItem(jSONObject, ca.getName()));
            }
        } catch (Exception ex) {
            log4j.fatal("eroare: " + AppUtils.printStackTrace(ex));
        }

        return items;
    }

    /**
     * @return the info
     */
    public String getInfo() {
        return info;
    }

    /**
     * @return the seletedArea
     */
    public ChartedArea getSeletedArea() {
        return seletedArea;
    }

    /**
     * @return the userTeam
     */
    public Team getUserTeam() {
        return userTeam;
    }

    /**
     * @return the assigned
     */
    public boolean isAssigned() {
        return assigned;
    }

    /**
     * @return the chartedAreas
     */
    public List<ChartedArea> getChartedAreasList() {
        return chartedAreasList;
    }

    /**
     * @return the areaJsonBouns
     */
    public JSONObject getAreaJsonBouns() {
        return areaJsonBouns;
    }

    /**
     * @return the areaGarbages
     */
    public List<Garbage> getAreaGarbages() {
        return areaGarbages;
    }

    /**
     * @return the areaTeams
     */
    public List<Team> getAreaTeams() {
        return areaTeams;
    }

    /**
     * @return the selectedCounty
     */
    public JSONObject getSelectedCounty() {
        return selectedCounty;
    }

    /**
     * @return the dummyCounty
     */
    public String getDummyCounty() {
        return dummyCounty;
    }

    /**
     * @param dummyCounty the dummyCounty to set
     */
    public void setDummyCounty(String dummyCounty) {
        this.dummyCounty = dummyCounty;
    }

    /**
     * @return the userDetails
     */
    public User getUserDetails() {
        return userDetails;
    }


}
