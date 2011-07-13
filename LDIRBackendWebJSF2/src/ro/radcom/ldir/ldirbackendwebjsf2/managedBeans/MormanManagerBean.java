/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import com.sun.jersey.api.client.ClientResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;
import org.apache.log4j.Logger;
import org.apache.myfaces.custom.fileupload.UploadedFile;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.User;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.MyGarbage;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.MyGarbageComparator;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

/**
 *
 * @author dan.grigore
 */
public class MormanManagerBean {

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    private WSInterface wsi = new WSInterface();

    /* variabile afisare */
    private MyGarbage myGarbage = new MyGarbage(new Garbage());
    private List<MyGarbage> myGarbageList = new ArrayList<MyGarbage>();
    private User userDetails = new User();
    private String latitudine = "0.0";
    private String lat_grd = "0";
    private String lat_min = "0";
    private String lat_sec = "0.0";
    private String longitudine = "0.0";
    private String long_grd = "0";
    private String long_min = "0";
    private String long_sec = "0.0";
    private List<String> thumbnails = new ArrayList<String>();
    private List<String> posters = new ArrayList<String>();
    private boolean coord_zecimale = true;
    private boolean coord_grade = false;
    private UploadedFile uploadedFile1 = null;
    private UploadedFile uploadedFile2 = null;
    private UploadedFile uploadedFile3 = null;
    private int selectedImgIndex = 0;

    /** Creates a new instance of MormanManagerBean */
    public MormanManagerBean() {
        /* obtinere id garbage seletat din request (daca este cazul) */
        int garbageId = AppUtils.parseToInt(JsfUtils.getRequestParameter("garbageId"));

        /* initializare detalii utilizator si lista mormane */
        init(garbageId);

        /* adaugare mesaj info de pe sesiune daca exista */
        String infoMessage = (String) JsfUtils.getHttpSession().getAttribute("INFO_MESSAGE");
        if (infoMessage != null) {
            JsfUtils.addInfoMessage(infoMessage);
            JsfUtils.getHttpSession().removeAttribute("INFO_MESSAGE");
        }

        /* adaugare mesaj warn de pe sesiune daca exista */
        String warnMessage = (String) JsfUtils.getHttpSession().getAttribute("WARN_MESSAGE");
        if (warnMessage != null) {
            JsfUtils.addErrorMessage(warnMessage);
            JsfUtils.getHttpSession().removeAttribute("WARN_MESSAGE");
        }
    }

    private void init(int garbageId) {
        /* obtinere detalii utilizator */
        userDetails = (User) JsfUtils.getHttpSession().getAttribute("USER_DETAILS");

        /* parcurgere prelucrare si initializare garbage selectat daca este cazul */
        if (userDetails.getGarbages() != null) {
            Iterator<Garbage> iterator = userDetails.getGarbages().iterator();
            while (iterator.hasNext()) {
                Garbage g = iterator.next();
                if (g.getGarbageId() == garbageId) {
                    myGarbage = new MyGarbage(g);
                    longitudine = "" + g.getX();
                    latitudine = "" + g.getY();

                    /* obtinere numar poze */
                    for (int i = 0; i < g.getPictures().size(); i++) {
                        try {
                            /**
                             * thumbnail
                             */
                            ClientResponse cr = wsi.getPicture(userDetails, g, i, false);

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
                            cr = wsi.getPicture(userDetails, g, i, true);

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
                                }
                            } else {
                                log4j.fatal("Eroare obtinere imagine backend: statusCode=" + cr.getStatus() + " (statusMsg=" + cr.getClientResponseStatus() + ")");
                            }
                        } catch (Exception ex) {
                            log4j.fatal("Eroare obtinere imagine: " + AppUtils.printStackTrace(ex));
                        }
                    }
                }
                String infoHtml = "<strong>" + JsfUtils.getBundleMessage("details_morman") + " " + g.getGarbageId() + "</strong><br/>";
                infoHtml += JsfUtils.getBundleMessage("details_area") + " " + (g.getChartedArea() != null ? g.getChartedArea().getName() : "unknown") + "<br/>";
                infoHtml += JsfUtils.getBundleMessage("details_county") + " " + (g.getCounty() != null ? g.getCounty().getName() : "unknown") + "<br/>";
                infoHtml += JsfUtils.getBundleMessage("details_state") + " " + (g.getStatus() != null ? g.getStatus().name() : "unknown") + "<br/><br/>";
                infoHtml += (g.getDescription() != null ? g.getDescription() : "") + "<br/>";
                infoHtml += "<br/><a href=\"cartare-mormane-detalii.jsf?garbageId=" + g.getGarbageId() + "\" style=\"color: #4D751F;\">" + JsfUtils.getBundleMessage("details_view_link") + "</a>";
                myGarbageList.add(new MyGarbage(g, infoHtml));
            }
            Collections.sort(myGarbageList, new MyGarbageComparator());
        }
    }

    public void actionSetSelectedImage() {
        log4j.debug("---> imagine selectata");
        selectedImgIndex = AppUtils.parseToInt(JsfUtils.getRequestParameter("imgIndex"));
    }

    public String actionEditMorman() {
        /**
         * validari
         */
        Garbage garbage = myGarbage.getGarbage();
        if (garbage.getDescription() == null || garbage.getDescription().trim().length() == 0) {
            JsfUtils.addWarnBundleMessage("err_mandatory_fields");
            return NavigationValues.MORMAN_ADD_FAIL;
        }
        int totalPercents = garbage.getPercentageGlass() + garbage.getPercentageMetal() + garbage.getPercentagePlastic() + garbage.getPercentageWaste();
        if (totalPercents != 100 && totalPercents != 0) {
            JsfUtils.addWarnBundleMessage("chart_err_overflow_percents");
            return NavigationValues.MORMAN_ADD_FAIL;
        }
        /* latitudine */
        try {
            if (isCoord_zecimale()) {
                garbage.setY(Double.parseDouble(latitudine));
            } else {
                garbage.setY(Double.parseDouble(lat_grd) + (Double.parseDouble(lat_min) / (double) 60) + (Double.parseDouble(lat_sec) / (double) 3600));
            }
            if (garbage.getY() == 0) {
                JsfUtils.addWarnBundleMessage("chart_err_latitude");
                return NavigationValues.MORMAN_ADD_FAIL;
            }
        } catch (Exception ex) {
            JsfUtils.addWarnBundleMessage("chart_err_latitude");
            return NavigationValues.MORMAN_ADD_FAIL;
        }
        /* longitudine */
        try {
            if (isCoord_zecimale()) {
                garbage.setX(Double.parseDouble(longitudine));
            } else {
                garbage.setX(Double.parseDouble(long_grd) + (Double.parseDouble(long_min) / (double) 60) + (Double.parseDouble(long_sec) / (double) 3600));
            }
            if (garbage.getX() == 0) {
                JsfUtils.addWarnBundleMessage("chart_err_longitude");
                return NavigationValues.MORMAN_ADD_FAIL;
            }
        } catch (Exception ex) {
            JsfUtils.addWarnBundleMessage("chart_err_longitude");
            return NavigationValues.MORMAN_ADD_FAIL;
        }
        if (garbage.getGarbageId() == null || garbage.getGarbageId() == 0) {
            for (int i = 0; i < myGarbageList.size(); i++) {
                Garbage garbageItem = myGarbageList.get(i).getGarbage();
                if ((garbage.getX() == garbageItem.getX()) && (garbage.getY() == garbageItem.getY())) {
                    JsfUtils.addWarnBundleMessage("chart_err_exists");
                    return NavigationValues.MORMAN_ADD_FAIL;
                }
            }
        }

        /**
         * procesare
         */
        garbage.setRecordDate(new Date());
        garbage.setStatus(Garbage.GarbageStatus.IDENTIFIED);

        try {
            ClientResponse cr = wsi.addGarbage(userDetails, garbage);
            int statusCode = cr.getStatus();
            log4j.debug("---> Garbage Add statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
            if (statusCode == 200) {
                /* adaugare imagini */
            	
            	try {
					garbage.setGarbageId(new Integer(cr.getEntity(String.class)));
				} catch (NumberFormatException e) {

				}
            	
                String warn_text = "";
                if (uploadedFile1 != null) {
                    if (!uploadImage(uploadedFile1, garbage, 1)) {
                        warn_text = JsfUtils.getBundleMessage("details_add_img_err").replaceAll("\\{0\\}", "1");
                    }
                }
                if (uploadedFile2 != null) {
                    if (!uploadImage(uploadedFile2, garbage, 2)) {
                        warn_text += warn_text.length() > 0 ? "; " : "";
                        warn_text += JsfUtils.getBundleMessage("details_add_img_err").replaceAll("\\{0\\}", "2");
                    }
                }
                if (uploadedFile3 != null) {
                    if (!uploadImage(uploadedFile3, garbage, 3)) {
                        warn_text += warn_text.length() > 0 ? "; " : "";
                        warn_text += JsfUtils.getBundleMessage("details_add_img_err").replaceAll("\\{0\\}", "3");
                    }
                }
                if (warn_text.length() > 0) {
                    JsfUtils.getHttpSession().setAttribute("WARN_MESSAGE", warn_text);
                }

                /* cerere informatii user */
                cr = wsi.reinitUser(userDetails);
                statusCode = cr.getStatus();
                if (statusCode != 200) {
                    log4j.fatal("---> Reinit User Garbage statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
                    return NavigationValues.MORMAN_ADD_SUCCESS;
                } else {
                    /* mesaj info referitor la adaugarea/modificarea mormanului */
                    String infoText = "";
                    if (garbage.getGarbageId() != null && garbage.getGarbageId() > 0) {
                        infoText = JsfUtils.getBundleMessage("details_modify_confirm");
                        infoText = infoText.replaceAll("\\{0\\}", "" + garbage.getGarbageId());
                    } else {
                        init(0);
                        garbage = myGarbageList.get(0).getGarbage();
                        infoText = JsfUtils.getBundleMessage("details_add_confirm");
                        infoText = infoText.replaceAll("\\{0\\}", "" + garbage.getGarbageId());
                    }

                    log4j.debug("---> Reinit User Garbage statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
                    JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE", infoText);
                    return NavigationValues.MORMAN_ADD_SUCCESS;
                }
            } else if (statusCode == 400) {
                JsfUtils.addWarnBundleMessage("chart_err_coords");
                return NavigationValues.MORMAN_ADD_FAIL;
            } else {
                JsfUtils.addWarnBundleMessage("internal_err");
                return NavigationValues.MORMAN_ADD_FAIL;
            }
        } catch (Exception ex) {
            log4j.fatal("Eroare apelare WS: "+AppUtils.printStackTrace(ex));
            JsfUtils.addWarnBundleMessage("internal_err");
            return NavigationValues.MORMAN_ADD_FAIL;
        }
    }

    private boolean uploadImage(UploadedFile uploadedFile, Garbage garbage, int imgNr) {
        try {
            /* salvare imagine in directorul temporar */
            File imageFile = AppUtils.saveToTempFile(uploadedFile, "tmp_" + garbage.getGarbageId() + "_" + imgNr);

            /* timitre imagine la backend */
            ClientResponse cr = wsi.addPicture(userDetails, garbage, imageFile);
            int statusCode = cr.getStatus();
            log4j.debug("---> Add Image[" + imgNr + "] statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
            if (statusCode == 200) {
                imageFile.delete();
                return true;
            } else {
                log4j.fatal("---> Add Image[" + imgNr + "] statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
                return false;
            }
        } catch (Exception ex) {
            log4j.fatal("eroare procesare imagine[" + imgNr + "]: " + AppUtils.printStackTrace(ex));
            return false;
        }
    }

    public String actionDeleteMorman() {
        ClientResponse cr = wsi.deleteGarbage(userDetails, myGarbage.getGarbage());
        int statusCode = cr.getStatus();
        log4j.debug("---> Garbage Delete statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        if (statusCode == 200) {
            /* cerere informatii user */
            cr = wsi.reinitUser(userDetails);
            statusCode = cr.getStatus();
            log4j.debug("---> Reinit User Garbage statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
            if (statusCode != 200) {
                JsfUtils.addWarnBundleMessage("internal_err");
                return NavigationValues.MORMAN_DELETE_FAIL;
            } else {
                return NavigationValues.MORMAN_DELETE_SUCCESS;
            }
        } else {
            JsfUtils.addWarnBundleMessage("internal_err");
            return NavigationValues.MORMAN_DELETE_FAIL;
        }
    }

    public List<SelectItem> getSaciNrItems() {
        List<SelectItem> saciNrItems = new ArrayList<SelectItem>();

        for (int i = 1; i <= 100; i++) {
            saciNrItems.add(new SelectItem(i, "" + i));
        }

        return saciNrItems;
    }

    /**
     * @return the userDetails
     */
    public User getUserDetails() {
        return userDetails;
    }

    /**
     * @param userDetails the userDetails to set
     */
    public void setUserDetails(User userDetails) {
        this.userDetails = userDetails;
    }

    /**
     * @return the latitudine
     */
    public String getLatitudine() {
        return latitudine;
    }

    /**
     * @param latitudine the latitudine to set
     */
    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    /**
     * @return the longitudine
     */
    public String getLongitudine() {
        return longitudine;
    }

    /**
     * @param longitudine the longitudine to set
     */
    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    /**
     * @return the myGarbage
     */
    public MyGarbage getMyGarbage() {
        return myGarbage;
    }

    /**
     * @return the myGarbageList
     */
    public List<MyGarbage> getMyGarbageList() {
        return myGarbageList;
    }

    /**
     * @return the uploadedFile1
     */
    public UploadedFile getUploadedFile1() {
        return uploadedFile1;
    }

    /**
     * @param uploadedFile1 the uploadedFile1 to set
     */
    public void setUploadedFile1(UploadedFile uploadedFile1) {
        this.uploadedFile1 = uploadedFile1;
    }

    /**
     * @return the lat_grd
     */
    public String getLat_grd() {
        return lat_grd;
    }

    /**
     * @param lat_grd the lat_grd to set
     */
    public void setLat_grd(String lat_grd) {
        this.lat_grd = lat_grd;
    }

    /**
     * @return the lat_min
     */
    public String getLat_min() {
        return lat_min;
    }

    /**
     * @param lat_min the lat_min to set
     */
    public void setLat_min(String lat_min) {
        this.lat_min = lat_min;
    }

    /**
     * @return the lat_sec
     */
    public String getLat_sec() {
        return lat_sec;
    }

    /**
     * @param lat_sec the lat_sec to set
     */
    public void setLat_sec(String lat_sec) {
        this.lat_sec = lat_sec;
    }

    /**
     * @return the long_grd
     */
    public String getLong_grd() {
        return long_grd;
    }

    /**
     * @param long_grd the long_grd to set
     */
    public void setLong_grd(String long_grd) {
        this.long_grd = long_grd;
    }

    /**
     * @return the long_min
     */
    public String getLong_min() {
        return long_min;
    }

    /**
     * @param long_min the long_min to set
     */
    public void setLong_min(String long_min) {
        this.long_min = long_min;
    }

    /**
     * @return the long_sec
     */
    public String getLong_sec() {
        return long_sec;
    }

    /**
     * @param long_sec the long_sec to set
     */
    public void setLong_sec(String long_sec) {
        this.long_sec = long_sec;
    }

    /**
     * @return the coord_zecimale
     */
    public boolean isCoord_zecimale() {
        return coord_zecimale;
    }

    /**
     * @param coord_zecimale the coord_zecimale to set
     */
    public void setCoord_zecimale(boolean coord_zecimale) {
        this.coord_zecimale = coord_zecimale;
        this.coord_grade = !coord_zecimale;
    }

    /**
     * @return the coord_grade
     */
    public boolean isCoord_grade() {
        return coord_grade;
    }

    /**
     * @param coord_grade the coord_grade to set
     */
    public void setCoord_grade(boolean coord_grade) {
        this.coord_grade = coord_grade;
        this.coord_zecimale = !coord_grade;
    }

    /**
     * @return the thumbnails
     */
    public List<String> getThumbnails() {
        return thumbnails;
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

    /**
     * @return the posters
     */
    public List<String> getPosters() {
        return posters;
    }

    /**
     * @return the uploadedFile2
     */
    public UploadedFile getUploadedFile2() {
        return uploadedFile2;
    }

    /**
     * @param uploadedFile2 the uploadedFile2 to set
     */
    public void setUploadedFile2(UploadedFile uploadedFile2) {
        this.uploadedFile2 = uploadedFile2;
    }

    /**
     * @return the uploadedFile3
     */
    public UploadedFile getUploadedFile3() {
        return uploadedFile3;
    }

    /**
     * @param uploadedFile3 the uploadedFile3 to set
     */
    public void setUploadedFile3(UploadedFile uploadedFile3) {
        this.uploadedFile3 = uploadedFile3;
    }
}
