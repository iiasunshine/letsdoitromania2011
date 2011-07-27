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
import java.text.SimpleDateFormat;
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
import ro.ldir.dto.User;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;

/**
 *
 * @author dan.grigore
 */
public class AdminUsersManagerBean {

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    private WSInterface wsi = new WSInterface();
    /* variabile afisare */
    private User userDetails = new User();
    private User[] usersList;
    private CountyArea[] countyAreas;
    private String selectedCounty;
    private int selectedBirthYear;
    private String selectedRole;
    private String selectedMinGarbages = "";
    private String selectedMaxGarbages = "";
    private boolean noFilter = true;
    /* pentru editare detalii user */
    private User selectedUser = new User();
    private int day;
    private int month;
    private int year;
    private boolean cartare = false;
    private boolean curatenie = false;

    /** Creates a new instance of AdminUsersManagerBean */
    public AdminUsersManagerBean() {
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

        /* obtinere lista utilizatori */
        //initUsersList();

    }

    public void actionEditUser() {
        /**
         * validare campuri
         */
        if (selectedUser.getFirstName() == null || selectedUser.getFirstName().trim().length() == 0
                || selectedUser.getLastName() == null || selectedUser.getLastName().trim().length() == 0
                || selectedUser.getEmail() == null || selectedUser.getEmail().trim().length() == 0
                || selectedUser.getPasswd() == null || selectedUser.getPasswd().trim().length() == 0
                || (!curatenie && !cartare)) {
            JsfUtils.addWarnBundleMessage("err_mandatory_fields");
            return;
        } else {
            selectedUser.setFirstName(selectedUser.getFirstName().trim());
            selectedUser.setLastName(selectedUser.getLastName().trim());
            selectedUser.setEmail(selectedUser.getEmail().trim());
        }

        /**
         * procesare
         */
        List<User.Activity> activities = new ArrayList<User.Activity>();
        if (cartare) {
            activities.add(User.Activity.CHART);
        }
        if (curatenie) {
            activities.add(User.Activity.CLEAN);
        }
        selectedUser.setActivities(activities);

        if (day > 0 && month > 0 && year > 0) {
            Date birthDay = new Date(year - 1900, month - 1, day);
            selectedUser.setBirthday(birthDay);
            log4j.debug("---> BirthDay: " + new SimpleDateFormat("dd/MM/yyyy").format(birthDay));
        }

        /* update date utilizator */
        String location = JsfUtils.getInitParameter("webservice.url") + "/LDIRBackend/ws/user/" + selectedUser.getUserId();
        Client client = Client.create();
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
        ClientResponse cr = builder.entity(selectedUser, MediaType.APPLICATION_XML).put(ClientResponse.class);
        int statusCode = cr.getStatus();
        log4j.debug("---> statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");

        /* verificare statusCode si adaugare mesaje */
        if (statusCode == 200) {
            //JsfUtils.addInfoBundleMessage("register_message");
        } else {
            JsfUtils.addWarnBundleMessage("internal_err");
        }

        initUsersList();
    }

        public void actionGenerateExcel() {
        ClientResponse cr = wsi.getUserListByFilters(userDetails,
                selectedCounty,
                selectedBirthYear,
                selectedRole,
                AppUtils.parseToInt(selectedMinGarbages, -1),
                AppUtils.parseToInt(selectedMaxGarbages, -1),
                "application/vnd.ms-excel");
        log4j.debug("---> cr.getStatus()=" + cr.getStatus());
        if (cr.getStatus() != 200) {
            log4j.fatal("nu s-a reusit generarea (statusCode=" + cr.getStatus() + " responseStatus=" + cr.getResponseStatus() + ")");
            JsfUtils.addWarnBundleMessage("internal_err");
            return;
        } else {
            File tempFile = cr.getEntity(File.class);
            log4j.debug("---> temp file: " + tempFile.getAbsolutePath());

            String relativePath = "temp/statistici/" + userDetails.getUserId() + "/useri.xls";
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

    public List<SelectItem> getYearsItems() {
        List<SelectItem> years = new ArrayList<SelectItem>();

        for (int i = 1993; i >= 1930; i--) {
            years.add(new SelectItem(i, "" + i));
        }

        return years;
    }

    public List<SelectItem> getDaysItems() {
        List<SelectItem> days = new ArrayList<SelectItem>();

        for (int i = 1; i <= 31; i++) {
            days.add(new SelectItem(i, "" + i));
        }

        return days;
    }

    public void actionSelectUser(ActionEvent event) {
        int userId = AppUtils.parseToInt(JsfUtils.getHttpRequest().getParameter("userId"));

        for (int i = 0; i < usersList.length; i++) {
            if (userId == usersList[i].getUserId().intValue()) {
                selectedUser = usersList[i];
                day = selectedUser.getBirthday().getDate();
                month = 1+selectedUser.getBirthday().getMonth();
                year = 1900 + selectedUser.getBirthday().getYear();
                if (selectedUser.getActivities() != null) {
                    for (int j = 0; j < selectedUser.getActivities().size(); j++) {
                        if (selectedUser.getActivities().get(j).equals(User.Activity.CHART)) {
                            cartare = true;
                        }
                        if (selectedUser.getActivities().get(j).equals(User.Activity.CLEAN)) {
                            curatenie = true;
                        }
                    }
                }
                break;
            }
        }
    }

    public void actionApplyFilter() {
        initUsersList();
    }

    private void initUsersList() {
        if ((selectedCounty == null || selectedCounty.length() == 0)
                && (selectedBirthYear <= 0)
                && (selectedRole == null || selectedRole.length() == 0)
                && AppUtils.parseToInt(selectedMinGarbages, -1) == -1
                && AppUtils.parseToInt(selectedMaxGarbages, -1) == -1) {
            usersList = new User[0];
            noFilter = true;
            return;
        } else {
            noFilter = false;
        }

        ClientResponse cr = wsi.getUserListByFilters(userDetails,
                selectedCounty,
                selectedBirthYear,
                selectedRole,
                AppUtils.parseToInt(selectedMinGarbages, -1),
                AppUtils.parseToInt(selectedMaxGarbages, -1),
                null);
        log4j.debug("---> cr.getStatus()=" + cr.getStatus());
        if (cr.getStatus() != 200) {
            log4j.fatal("nu s-a reusit aplicarea filtrului (statusCode=" + cr.getStatus() + " responseStatus=" + cr.getResponseStatus() + ")");
            JsfUtils.addWarnBundleMessage("internal_err");
            return;
        } else {
            usersList = cr.getEntity(User[].class);
        }
    }

    /**
     * @return the userDetails
     */
    public User getUserDetails() {
        return userDetails;
    }

    /**
     * @return the usersList
     */
    public User[] getUsersList() {
        return usersList;
    }

    /**
     * @return the selectedCounty
     */
    public String getSelectedCounty() {
        return selectedCounty;
    }

    /**
     * @param selectedCounty the selectedCounty to set
     */
    public void setSelectedCounty(String selectedCounty) {
        this.selectedCounty = selectedCounty;
    }

    /**
     * @return the selectedBirthYear
     */
    public Integer getSelectedBirthYear() {
        return selectedBirthYear;
    }

    /**
     * @param selectedBirthYear the selectedBirthYear to set
     */
    public void setSelectedBirthYear(Integer selectedBirthYear) {
        this.selectedBirthYear = selectedBirthYear != null ? selectedBirthYear.intValue() : 0;
    }

    /**
     * @return the selectedRole
     */
    public String getSelectedRole() {
        return selectedRole;
    }

    /**
     * @param selectedRole the selectedRole to set
     */
    public void setSelectedRole(String selectedRole) {
        this.selectedRole = selectedRole;
    }

    /**
     * @return the selectedMinGarbages
     */
    public String getSelectedMinGarbages() {
        return selectedMinGarbages;
    }

    /**
     * @param selectedMinGarbages the selectedMinGarbages to set
     */
    public void setSelectedMinGarbages(String selectedMinGarbages) {
        this.selectedMinGarbages = selectedMinGarbages;
    }

    /**
     * @return the selectedMaxGarbages
     */
    public String getSelectedMaxGarbages() {
        return selectedMaxGarbages;
    }

    /**
     * @param selectedMaxGarbages the selectedMaxGarbages to set
     */
    public void setSelectedMaxGarbages(String selectedMaxGarbages) {
        this.selectedMaxGarbages = selectedMaxGarbages;
    }

    /**
     * @return the noFilter
     */
    public boolean isNoFilter() {
        return noFilter;
    }

    /**
     * @return the selectedUser
     */
    public User getSelectedUser() {
        return selectedUser;
    }

    /**
     * @return the day
     */
    public int getDay() {
        return day;
    }

    /**
     * @param day the day to set
     */
    public void setDay(int day) {
        this.day = day;
    }

    /**
     * @return the month
     */
    public int getMonth() {
        return month;
    }

    /**
     * @param month the month to set
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * @return the year
     */
    public int getYear() {
        return year;
    }

    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * @return the cartare
     */
    public boolean isCartare() {
        return cartare;
    }

    /**
     * @param cartare the cartare to set
     */
    public void setCartare(boolean cartare) {
        this.cartare = cartare;
    }

    /**
     * @return the curatenie
     */
    public boolean isCuratenie() {
        return curatenie;
    }

    /**
     * @param curatenie the curatenie to set
     */
    public void setCuratenie(boolean curatenie) {
        this.curatenie = curatenie;
    }
}
