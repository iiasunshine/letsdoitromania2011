package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;

import ro.ldir.dto.User;
import ro.ldir.dto.User.Activity;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.DataValidation;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.CountyNames;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

/**
 * 
 * @author Iurie Potorac
 *
 */
public class UserManagerBean {

    private static final Logger log4j = Logger.getLogger(ResetBean.class.getCanonicalName());
    private WSInterface wsi = new WSInterface(true);
    /* variabile afisare */
    private User userDetails = new User();
    private int day;
    private int month;
    private int year;
    private String password;
    private String passwordCurent;
    private String passwordConfirm;
    private boolean cartare = false;
    private boolean curatenie = false;
    private boolean acceptReceiveNotifications = true;
    private boolean profileView;
    
    public UserManagerBean(){
    	userDetails = (User)JsfUtils.getHttpSession().getAttribute("USER_DETAILS");
    	populateData();
    }
    
    private void populateData() {
		Date date = userDetails.getBirthday();
			
		if(date != null){
			//set hours to get correct day and not the day before 
			date.setHours(10);
			log4j.debug("date:"+date+","+date.getDate()+","+date.getMonth()+","+date.getYear()+","+userDetails.getFirstName()+","+userDetails.getActivities());
		
			setDay(date.getDate());
			setMonth(date.getMonth());
			setYear(date.getYear());
		}
		
		//acceptReceiveNotifications = userDetails.getAcceptsMoreInfo();
		
		List<Activity> activities = userDetails.getActivities();
		cartare = false;
	    curatenie = false;
		for(Activity activity:activities){
			if(activity == Activity.CHART){
				this.cartare = true;
			}
			if(activity == Activity.CLEAN){
				this.curatenie = true;
			}
		}
		this.profileView=userDetails.getProfileView();
		
	}

	public String actionEdit(){
    	   	
        /**
         * validare campuri
         */
        if (userDetails.getFirstName() == null || userDetails.getFirstName().trim().length() == 0
                || userDetails.getLastName() == null || userDetails.getLastName().trim().length() == 0
                || (!curatenie && !cartare)) {
            JsfUtils.addWarnBundleMessage("err_mandatory_fields");
            return NavigationValues.USER_EDIT_FAIL;
        }
        
        if(DataValidation.validateName(userDetails.getFirstName())
        		&& DataValidation.validateName(userDetails.getLastName())){
        	userDetails.setFirstName(userDetails.getFirstName().trim());
        	userDetails.setLastName(userDetails.getLastName().trim());
        }else{
            JsfUtils.addWarnBundleMessage("wrong_user_name");
            return NavigationValues.USER_EDIT_FAIL;
        }	

    	List<User.Activity> activities = new ArrayList<User.Activity>();
    	if(cartare){
    		activities.add(User.Activity.CHART);
    	}
    	if(curatenie){
    		activities.add(User.Activity.CLEAN);
    	}
    	userDetails.setActivities(activities);
    	
        if (day > 0 && month >= 0 && year > 0) {
            Date birthDay = new Date(year, month, day,1,1);
            userDetails.setBirthday(birthDay);
    		log4j.debug("Date persistance:"+userDetails.getBirthday());	
        }
        
    	//userDetails.setAcceptsMoreInfo(acceptReceiveNotifications);
    	userDetails.setProfileView(profileView);
	
    	 /* update date utilizator */
        String location = JsfUtils.getInitParameter("webservice.url") + "/LDIRBackend/ws/user/" + userDetails.getUserId();
        Client client = Client.create();
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), userDetails.getPasswd()));
        ClientResponse cr = builder.entity(userDetails, MediaType.APPLICATION_XML).put(ClientResponse.class);
        
        
        int statusCode = cr.getStatus();
        log4j.debug("---> statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        
        /* verificare statusCode si adaugare mesaje */
        if(statusCode == 200){
        	  JsfUtils.addInfoBundleMessage("success_edit_message");
        	  return NavigationValues.USER_EDIT_FAIL;
        }else if(statusCode == 403){
        	JsfUtils.addWarnBundleMessage("access_policy_violated");
              return NavigationValues.USER_EDIT_FAIL;
        }else if(statusCode == 404){
        		JsfUtils.addWarnBundleMessage("user_not_exist_message");
        	  return NavigationValues.USER_EDIT_FAIL;
        }else{
//        	 return NavigationValues.USER_EDIT_FAIL;
        }

          return NavigationValues.USER_EDIT_SUCCESS;

    }
	public String actionPassword(){
		
		if(password == null || password.trim().length()==0
			|| passwordConfirm == null || passwordConfirm.trim().length()==0
			|| passwordCurent == null || passwordCurent.trim().length() == 0){
			JsfUtils.addWarnBundleMessage("err_mandatory_fields");
			return NavigationValues.USER_EDIT_PASS_FAIL;
		}
		if( !passwordCurent.equals(userDetails.getPasswd()) ){
			JsfUtils.addWarnBundleMessage("wrong_pass_message");
			return NavigationValues.USER_EDIT_PASS_FAIL;
		}
		if( !password.equals(passwordConfirm)){
			JsfUtils.addWarnBundleMessage("register_err_pass");
			return NavigationValues.USER_EDIT_PASS_FAIL;
		}
		userDetails.setPasswd(password);
		
   	 /* update date utilizator */
        String location = JsfUtils.getInitParameter("webservice.url") + "/LDIRBackend/ws/user/" + userDetails.getUserId();
        Client client = Client.create();
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(userDetails.getEmail(), passwordCurent));
        ClientResponse cr = builder.entity(userDetails, MediaType.APPLICATION_XML).put(ClientResponse.class);
        
        
        int statusCode = cr.getStatus();
        log4j.debug("---> statusCode: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        
        /* verificare statusCode si adaugare mesaje */
        if(statusCode == 200){
        	  JsfUtils.addInfoBundleMessage("success_edit_message");
        	  return NavigationValues.USER_EDIT_PASS_FAIL;
        }else if(statusCode == 403){
        	  JsfUtils.addWarnBundleMessage("access_policy_violated");
              return NavigationValues.USER_EDIT_PASS_FAIL;
        }else if(statusCode == 404){
        	  JsfUtils.addWarnBundleMessage("user_not_exist_message");
        	  return NavigationValues.USER_EDIT_PASS_FAIL;
        }else{
        	
        }
		return NavigationValues.USER_EDIT_SUCCESS;
	}
    
    public List<SelectItem> getDaysItems() {
        List<SelectItem> days = new ArrayList<SelectItem>();

        for (int i = 1; i <= 31; i++) {
            days.add(new SelectItem(i, "" + i));
        }

        return days;
    }

    public List<SelectItem> getYearsItems() {
        List<SelectItem> years = new ArrayList<SelectItem>();

        for (int i = 1993; i >= 1930; i--) {
            years.add(new SelectItem(i-1900, "" + i));
        }

        return years;
    }
    
    public List<SelectItem> getCountyItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();

        for (int i = 0; i < CountyNames.COUNTIES.size(); i++) {
            String county = CountyNames.COUNTIES.get(i);
            items.add(new SelectItem(county, county));
        }

        return items;
    }
    
	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}

	public User getUserDetails() {
		return userDetails;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getMonth() {
		return month;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getYear() {
		return year;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPasswordCurent() {
		return passwordCurent;
	}

	public void setPasswordCurent(String passwordCurent) {
		this.passwordCurent = passwordCurent;
	}

	public String getPasswordConfirm() {
		return passwordConfirm;
	}

	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}

	public boolean isCartare() {
		return cartare;
	}

	public void setCartare(boolean cartare) {
		this.cartare = cartare;
	}

	public boolean isCuratenie() {
		return curatenie;
	}

	public void setCuratenie(boolean curatenie) {
		this.curatenie = curatenie;
	}

	public void setAcceptReceiveNotifications(boolean acceptReceiveNotifications) {
		this.acceptReceiveNotifications = acceptReceiveNotifications;
	}

	public boolean isAcceptReceiveNotifications() {
		return acceptReceiveNotifications;
	}

	public void setProfileView(boolean profileView) {
		this.profileView = profileView;
	}

	public boolean isProfileView() {
		return profileView;
	}
}
