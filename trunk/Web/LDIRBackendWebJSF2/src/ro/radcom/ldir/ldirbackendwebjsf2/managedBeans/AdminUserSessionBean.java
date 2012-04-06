/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import org.apache.log4j.Logger;

import ro.ldir.dto.User;
import ro.ldir.dto.UserSessionRecord;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpSession;

/**
 * 
 * @author iia
 */
public class AdminUserSessionBean {
	private static final Logger log4j = Logger
			.getLogger(AdminUserSessionBean.class.getCanonicalName());
	
	private List<UserSessionRecord> userList = null;

    private User userDetails = new User();
    

	/**
	 * Creates a new instance of AdminUserSessionBean
	 * 
	 */
	public AdminUserSessionBean() {
        userDetails = (User) JsfUtils.getHttpSession().getAttribute("USER_DETAILS");
		initUserSessionRecord();
	}

	private void initUserSessionRecord() {

		Map<UserSessionRecord, HttpSession> maps = UserSessionRecord
				.getLoggedUsers();

		if (maps != null) {
			userList = new ArrayList<UserSessionRecord>(maps.keySet());
		}
	}

	public void actionRefreshList(){
		initUserSessionRecord();
	}
	
	public List<UserSessionRecord> getUserList() {
		return userList;
	}

	public void setUserList(List<UserSessionRecord> userList) {
		this.userList = userList;
	}

	public User getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}
	
	

}