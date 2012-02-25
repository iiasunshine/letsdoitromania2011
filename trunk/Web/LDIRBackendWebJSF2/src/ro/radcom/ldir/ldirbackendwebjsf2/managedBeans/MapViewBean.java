/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.User;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.MyGarbage;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;

/**
 *
 * @author dan.grigore
 */
public class MapViewBean {

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    private WSInterface wsi = new WSInterface();
    private CountyArea[] countyAreas = null;
    /* variabile afisare */
    private List<MyGarbage> myGarbageList = new ArrayList<MyGarbage>();
    private MyGarbage myGarbage = new MyGarbage(new Garbage());
    private int selectedImgIndex = 0;
    private String debugText = "TEST";

    /** Creates a new instance of MapViewBean */
    public MapViewBean() {
            countyAreas = wsi.getCountyList();
    }

    /*private void init(int garbageId) {
        /* obtinere detalii utilizator 
        User userDetails = null;
        try {
            /* cerere autentificare 
            ClientResponse cr = wsi.login(JsfUtils.getInitParameter("admin.user"),
                    JsfUtils.getInitParameter("admin.password"));
            int statusCode = cr.getStatus();

            /* verificare status code 
            if (statusCode != 200) {
                log4j.warn("err1:  ");
            } else {
                /* obtinere id user 
                int userId = AppUtils.parseToInt(cr.getEntity(String.class));

                /* cerere informatii user 
                cr = wsi.getUserDetails(JsfUtils.getInitParameter("admin.user"),
                        JsfUtils.getInitParameter("admin.password"),
                        userId);
                statusCode = cr.getStatus();
                if (statusCode != 200) {
                    log4j.warn("err2:  ");
                } else {
                    userDetails = cr.getEntity(User.class);
                    userDetails.setPasswd(JsfUtils.getInitParameter("admin.password"));
                }
            }
        } catch (Exception ex) {
            log4j.fatal("Eroare: " + ex);
            JsfUtils.addWarnBundleMessage("internal_err");
        }

        /* parcurgere prelucrare si initializare garbage selectat daca este cazul 
        if (userDetails != null) {
            Iterator<Garbage> iterator = userDetails.getGarbages().iterator();
            while (iterator.hasNext()) {
                Garbage g = iterator.next();
                if (g.getGarbageId() == garbageId) {
                    myGarbage = new MyGarbage(g);
                    latitudine = "" + g.getY();
                    longitudine = "" + g.getX();

                    /* obtinere numar poze 
                    log4j.info("---> pictures: " + g.getPictures().size());
                    for (int i = 0; i < g.getPictures().size(); i++) {
                        try {
                            /**
                             * thumbnail
                             
                            ClientResponse cr = wsi.getPicture(userDetails, g, i, false);

                            if (cr.getStatus() == 200) {
                                File tempFile = cr.getEntity(File.class);
                                log4j.info("---> temp file: " + tempFile.getAbsolutePath());

                                String relativePath = "temp/" + userDetails.getUserId() + "/preview_" + garbageId + "_" + i + "_thumbnail.jpg";
                                String previewFilePath = JsfUtils.makeContextPath(relativePath);
                                File previewFile = new File(previewFilePath);
                                if (!previewFile.getParentFile().isDirectory()) {
                                    if (!previewFile.getParentFile().mkdirs()) {
                                        log4j.warn("nu s-a putut crea drectorul pentru preview: " + previewFile.getParentFile().getAbsolutePath());
                                    }
                                }
                                if (previewFile.isFile()) {
                                    if (!previewFile.delete()) {
                                        log4j.warn("nu s-a putut sterge fisierul existent: " + previewFile.getAbsolutePath());
                                    }
                                }
                                if (!tempFile.renameTo(previewFile)) {
                                    log4j.warn("nu s-a putut redenumi fisierul temporar: " + tempFile.getAbsolutePath());
                                } else {
                                    thumbnails.add(relativePath);
                                }
                            } else {
                                log4j.fatal("Eroare obtinere imagine backend: statusCode=" + cr.getStatus() + " (statusMsg=" + cr.getClientResponseStatus() + ")");
                            }

                            /**
                             * imagine full
                             
                            cr = wsi.getPicture(userDetails, g, i, true);

                            if (cr.getStatus() == 200) {
                                File tempFile = cr.getEntity(File.class);
                                log4j.info("---> temp file: " + tempFile.getAbsolutePath());

                                String relativePath = "temp/" + userDetails.getUserId() + "/preview_" + garbageId + "_" + i + "_full.jpg";
                                String previewFilePath = JsfUtils.makeContextPath(relativePath);
                                File previewFile = new File(previewFilePath);
                                if (!previewFile.getParentFile().isDirectory()) {
                                    if (!previewFile.getParentFile().mkdirs()) {
                                        log4j.warn("nu s-a putut crea drectorul pentru preview: " + previewFile.getParentFile().getAbsolutePath());
                                    }
                                }
                                if (previewFile.isFile()) {
                                    if (!previewFile.delete()) {
                                        log4j.warn("nu s-a putut sterge fisierul existent: " + previewFile.getAbsolutePath());
                                    }
                                }
                                if (!tempFile.renameTo(previewFile)) {
                                    log4j.warn("nu s-a putut redenumi fisierul temporar: " + tempFile.getAbsolutePath());
                                } else {
                                    posters.add(relativePath);
                                }
                            } else {
                                log4j.fatal("Eroare obtinere imagine backend: statusCode=" + cr.getStatus() + " (statusMsg=" + cr.getClientResponseStatus() + ")");
                            }
                        } catch (Exception ex) {
                            log4j.fatal("Eroare obtinere imagine: " + ex);
                        }
                    }
                }
                String infoHtml = "<strong>" + JsfUtils.getBundleMessage("details_morman") + " " + g.getGarbageId() + "</strong><br/>";
                infoHtml += JsfUtils.getBundleMessage("details_area") + " " + (g.getChartedArea() != null ? g.getChartedArea().getAreaId() : "unknown") + "<br/>";
                infoHtml += JsfUtils.getBundleMessage("details_county") + " " + (g.getCounty() != null ? g.getCounty().getName() : "unknown") + "<br/>";
                infoHtml += JsfUtils.getBundleMessage("details_state") + " " + (g.getStatus() != null ? g.getStatus().name() : "unknown") + "<br/>";
                infoHtml += "<br/><a href=\"cartare-mormane-detalii.jsf?garbageId=" + g.getGarbageId() + "\" style=\"color: #4D751F;\">" + JsfUtils.getBundleMessage("details_view_link") + "</a>";
                myGarbageList.add(new MyGarbage(g, infoHtml));
            }
            Collections.sort(myGarbageList, new MyGarbageComparator());
        }
    }*/

    public void actionSetSelectedImage() {
        log4j.info("---> imagine selectata");
        selectedImgIndex = AppUtils.parseToInt(JsfUtils.getRequestParameter("imgIndex"));
    }

    public List<SelectItem> getCountyItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();

        try {
            for (int i = 0; i < countyAreas.length; i++) {
                CountyArea ca = countyAreas[i];
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("name", ca.getName());
                jSONObject.put("bottomRightX", ca.getBottomRightX());
                jSONObject.put("bottomRightY", ca.getBottomRightY());
                jSONObject.put("topLeftX", ca.getTopLeftX());
                jSONObject.put("topLeftY", ca.getTopLeftY());

                JSONArray jSONArray = new JSONArray();
                for (int j = 0; j < ca.getPolyline().size(); j++) {
                    Point2D.Double point = ca.getPolyline().get(j);
                    jSONArray.put(point.getY());
                    jSONArray.put(point.getX());
                }
                jSONObject.put("border", jSONArray);

                items.add(new SelectItem(jSONObject, ca.getName()));
            }
        } catch (Exception ex) {
            log4j.fatal("eroare: " + ex);
        }

        return items;
    }

    /**
     * @return the debugText
     */
    public String getDebugText() {
        return debugText;
    }

    /**
     * @return the myGarbageList
     */
    public List<MyGarbage> getMyGarbageList() {
        return myGarbageList;
    }

    /**
     * @return the myGarbage
     */
    public MyGarbage getMyGarbage() {
        return myGarbage;
    }

    /**
     * @return the selectedImgIndex
     */
    public int getSelectedImgIndex() {
        return selectedImgIndex;
    }

    /**
     * @param selectedImgIndex the selectedImgIndex to set
     */
    public void setSelectedImgIndex(int selectedImgIndex) {
        this.selectedImgIndex = selectedImgIndex;
    }
}
