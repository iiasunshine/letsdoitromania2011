/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ro.ldir.dto.CountyArea;
import ro.ldir.dto.User;
import ro.ldir.report.formatter.ExcelFormatter;
import ro.ldir.report.formatter.GenericXlsxFormatter;
import ro.ldir.report.formatter.UserExcelFormatter;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;

/**
 *
 * @author dan.grigore
 */
public class AdminUsersManagerBean {

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    private WSInterface wsi;
    /* variabile afisare */
    private User userDetails = new User();
    private List<User> usersList;
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

    /** Creates a new instance of AdminUsersManagerBean 
     * @throws NamingException */
    public AdminUsersManagerBean() throws NamingException {
    	wsi = new WSInterface();
    	
        /* obtinere detalii utilizator */
        userDetails = (User) JsfUtils.getHttpSession().getAttribute("USER_DETAILS");
        countyAreas = wsi.getCountyList();
        

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
        wsi.updateUser(selectedUser);
      
        initUsersList();
    }

    
	public void actionGenerateExcel() throws IOException {
		ExcelFormatter fmt = new UserExcelFormatter(usersList);
		byte report[] = new GenericXlsxFormatter(fmt).getBytes();

		FacesContext fc = FacesContext.getCurrentInstance();
		HttpServletResponse response = (HttpServletResponse) fc
				.getExternalContext().getResponse();
		OutputStream out = response.getOutputStream();
		out.write(report);
		out.flush();

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.addHeader("Content-Disposition",
				"attachment; filename=userreport.xlsx");
		out.close();
		FacesContext.getCurrentInstance().responseComplete();
	}

	public void actionGenerateTeamExcel() {

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
		int userId = AppUtils.parseToInt(JsfUtils.getHttpRequest()
				.getParameter("userId"));
		
		for (User user : usersList) {
			if (userId != user.getUserId().intValue())
				continue;

			selectedUser = user;
			day = selectedUser.getBirthday().getDate();
			month = 1 + selectedUser.getBirthday().getMonth();
			year = 1900 + selectedUser.getBirthday().getYear();
			if (selectedUser.getActivities() != null) {
				for (int j = 0; j < selectedUser.getActivities().size(); j++) {
					if (selectedUser.getActivities().get(j)
							.equals(User.Activity.CHART)) {
						cartare = true;
					}
					if (selectedUser.getActivities().get(j)
							.equals(User.Activity.CLEAN)) {
						curatenie = true;
					}
				}
			}
			break;
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
			usersList = new ArrayList<User>();
			noFilter = true;
			return;
		} else {
			noFilter = false;
		}
		usersList = wsi
				.getUserListByFilters(
						(selectedCounty != null && selectedCounty.length() > 0) ? selectedCounty
								: null,
						(selectedBirthYear != 0) ? selectedBirthYear : null,
						(selectedRole != null && selectedRole.length() > 0) ? selectedRole
								: null, AppUtils.parseToInt(
								selectedMinGarbages, null), AppUtils
								.parseToInt(selectedMaxGarbages, null), null);
	}
    
	 public String encodeUrl(String arg){
		  try{
			  arg = URLEncoder.encode(arg,"UTF-8"); 
			  log4j.debug("---> encode: " + arg);
		  }catch(UnsupportedEncodingException uee){
			  log4j.debug("---> encode error: " + uee.getMessage());  
		  }
		  return  arg;
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
    public List<User> getUsersList() {
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
