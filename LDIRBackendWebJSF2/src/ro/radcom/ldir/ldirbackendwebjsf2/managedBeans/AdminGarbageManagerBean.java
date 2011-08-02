/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.User;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.ImageInfo;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;

/**
 *
 * @author dan.grigore
 */
public class AdminGarbageManagerBean {

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    private WSInterface wsi = new WSInterface();
    /* variabile afisare */
    private User userDetails = new User();
    private Garbage[] garbageList;
    private CountyArea[] countyAreas;
    private String userId;
    private String gridId;
    private String countyId;
    private Date addDate;
    private boolean noFilter = true;
    private List<String> thumbnails = new ArrayList<String>();
    private List<String> posters = new ArrayList<String>();
    private List<Integer> posterHeights = new ArrayList<Integer>();
    /* pentru editare detalii morman */
    private Garbage selectedGarbage = new Garbage();
    private int selectedImgIndex = 0;

    /** Creates a new instance of AdminGarbageManagerBean */
    public AdminGarbageManagerBean() {
        /* obtinere detalii utilizator */
        userDetails = (User) JsfUtils.getHttpSession().getAttribute("USER_DETAILS");

        /* obtinere lista judete */
        ClientResponse cr = wsi.getCountyList();
        if (cr.getStatus() != 200) {
            log4j.fatal("nu s-a reusit obtinerea listei de judete(statusCode=" + cr.getStatus() + " responseStatus=" + cr.getResponseStatus() + ")");
            JsfUtils.addWarnBundleMessage("internal_err");
            return;
        } else {
            countyAreas = cr.getEntity(CountyArea[].class);
        }
    }

    public void actionApplyFilter() {
        initGarbageList();
    }

    public void actionSelectGarbage(ActionEvent event) {
        int garbageId = AppUtils.parseToInt(JsfUtils.getHttpRequest().getParameter("garbageId"));

        /* identificare morman */
        for (int i = 0; i < garbageList.length; i++) {
            if (garbageId == garbageList[i].getGarbageId().intValue()) {
                selectedGarbage = garbageList[i];
                break;
            }
        }

        if(event.getComponent().getClientId().indexOf("delete") >= 0){
            return;
        }

        /* resetare parametri folositi */
        posters = new ArrayList<String>();
        thumbnails = new ArrayList<String>();
        posterHeights = new ArrayList<Integer>();
        selectedImgIndex = 0;

        /* obtinere poze */
        for (int i = 0; i < selectedGarbage.getPictures().size(); i++) {
            int height = 0;

            try {
                /**
                 * thumbnail
                 */
                ClientResponse cr = wsi.getPicture(userDetails, selectedGarbage, i, false);

                if (cr.getStatus() == 200) {
                    File tempFile = cr.getEntity(File.class);
                    log4j.debug("---> temp file: " + tempFile.getAbsolutePath());

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
                 */
                cr = wsi.getPicture(userDetails, selectedGarbage, i, true);

                if (cr.getStatus() == 200) {
                    File tempFile = cr.getEntity(File.class);
                    log4j.debug("---> temp file: " + tempFile.getAbsolutePath());

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
                        ImageInfo imageInfo = ImageInfo.getImageInfo(previewFile.getAbsolutePath(), false);
                        if (imageInfo.getHeight() > 550) {
                            height = 550;
                        }
                        posterHeights.add(height);
                    }
                } else {
                    log4j.fatal("Eroare obtinere imagine backend: statusCode=" + cr.getStatus() + " (statusMsg=" + cr.getClientResponseStatus() + ")");
                }
            } catch (Exception ex) {
                log4j.fatal("Eroare obtinere imagine: " + AppUtils.printStackTrace(ex));
            }
        }
    }

    public void actionSetSelectedImage() {
        log4j.debug("---> imagine selectata");
        selectedImgIndex = AppUtils.parseToInt(JsfUtils.getRequestParameter("imgIndex"));
    }

    public void actionDeleteGarbage() {
        /*int garbageId = AppUtils.parseToInt(JsfUtils.getHttpRequest().getParameter("garbageId"));*/

        /* identificare morman */
        Garbage garbage = selectedGarbage;
        /*for (int i = 0; i < garbageList.length; i++) {
            if (garbageId == garbageList[i].getGarbageId().intValue()) {
                garbage = garbageList[i];
                break;
            }
        }*/
        if (garbage == null) {
            return;
        }

        /* stergere */
        String location = JsfUtils.getInitParameter("webservice.url") + "/LDIRBackend/ws/garbage/" + selectedGarbage.getGarbageId();
        Client client = Client.create();
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).delete(ClientResponse.class);
        int statusCode = cr.getStatus();
        log4j.debug("---> statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");

        /* verificare statusCode si adaugare mesaje */
        if (statusCode == 200) {
            //JsfUtils.addInfoBundleMessage("register_message");
        } else {
            JsfUtils.addWarnBundleMessage("internal_err");
        }

        initGarbageList();
    }

    private void initGarbageList() {
        if ((countyId == null || countyId.length() == 0)
                && (gridId == null || gridId.length() == 0)
                && (userId == null || userId.length() == 0)
                && (addDate == null)) {
            garbageList = new Garbage[0];
            noFilter = true;
            return;
        } else {
            noFilter = false;
        }

        ClientResponse cr = wsi.getGarbageListByFilters(userDetails,
                countyId,
                AppUtils.parseToInt(gridId),
                AppUtils.parseToInt(userId),
                addDate,
                null);
        log4j.debug("---> cr.getStatus()=" + cr.getStatus());
        if (cr.getStatus() != 200) {
            log4j.fatal("nu s-a reusit aplicarea filtrului (statusCode=" + cr.getStatus() + " responseStatus=" + cr.getResponseStatus() + ")");
            JsfUtils.addWarnBundleMessage("internal_err");
            return;
        } else {
            garbageList = cr.getEntity(Garbage[].class);
        }
    }

    public void actionGenerateExcel() {
        ClientResponse cr = wsi.getGarbageListByFilters(userDetails,
                countyId,
                AppUtils.parseToInt(gridId),
                AppUtils.parseToInt(userId),
                addDate,
                "application/vnd.ms-excel");
        log4j.debug("---> cr.getStatus()=" + cr.getStatus());
        if (cr.getStatus() != 200) {
            log4j.fatal("nu s-a reusit generarea (statusCode=" + cr.getStatus() + " responseStatus=" + cr.getResponseStatus() + ")");
            JsfUtils.addWarnBundleMessage("internal_err");
            return;
        } else {
            File tempFile = cr.getEntity(File.class);
            log4j.debug("---> temp file: " + tempFile.getAbsolutePath());

            String relativePath = "temp/statistici/" + userDetails.getUserId() + "/mormane.xls";
            String previewFilePath = JsfUtils.makeContextPath(relativePath);
            File file = new File(previewFilePath);
            if (!file.getParentFile().isDirectory()) {
                if (!file.getParentFile().mkdirs()) {
                    log4j.warn("nu s-a putut crea drectorul pentru preview: " + file.getParentFile().getAbsolutePath());
                }
            }

            if (file.isFile()) {
                if (!file.delete()) {
                    log4j.warn("nu s-a putut sterge fisierul existent: " + file.getAbsolutePath());
                }
            }

            log4j.debug("---> file: " + file.getAbsolutePath());
            if (!tempFile.renameTo(file)) {
                log4j.warn("nu s-a putut redenumi fisierul temporar: " + tempFile.getAbsolutePath());
            } else {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().redirect(JsfUtils.getHttpRequest().getContextPath()+"/"+relativePath);
                    return;
                } catch (Exception ex) {
                    log4j.warn("Eroare RequestForward: " + ex);
                }
            }
        }
    }

    public List<SelectItem> getCountyItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();

        try {
            for (int i = 0; i < countyAreas.length; i++) {
                CountyArea ca = countyAreas[i];

                items.add(new SelectItem(ca.getName(), ca.getName()));
            }
        } catch (Exception ex) {
            log4j.fatal("eroare: " + ex);
        }

        return items;
    }

    /**
     * @return the userDetails
     */
    public User getUserDetails() {
        return userDetails;
    }

    /**
     * @return the garbageList
     */
    public Garbage[] getGarbageList() {
        return garbageList;
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return the gridId
     */
    public String getGridId() {
        return gridId;
    }

    /**
     * @param gridId the gridId to set
     */
    public void setGridId(String gridId) {
        this.gridId = gridId;
    }

    /**
     * @return the countyId
     */
    public String getCountyId() {
        return countyId;
    }

    /**
     * @param countyId the countyId to set
     */
    public void setCountyId(String countyId) {
        this.countyId = countyId;
    }

    /**
     * @return the addDate
     */
    public Date getAddDate() {
        return addDate;
    }

    /**
     * @param addDate the addDate to set
     */
    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    /**
     * @return the noFilter
     */
    public boolean isNoFilter() {
        return noFilter;
    }

    /**
     * @return the selectedGarbage
     */
    public Garbage getSelectedGarbage() {
        return selectedGarbage;
    }

    /**
     * @param selectedGarbage the selectedGarbage to set
     */
    public void setSelectedGarbage(Garbage selectedGarbage) {
        this.selectedGarbage = selectedGarbage;
    }

    /**
     * @return the thumbnails
     */
    public List<String> getThumbnails() {
        return thumbnails;
    }

    /**
     * @return the posters
     */
    public List<String> getPosters() {
        return posters;
    }

    /**
     * @return the posterHeights
     */
    public List<Integer> getPosterHeights() {
        return posterHeights;
    }

    /**
     * @return the selectedImgIndex
     */
    public int getSelectedImgIndex() {
        return selectedImgIndex;
    }
}
