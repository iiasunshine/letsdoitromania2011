/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ro.ldir.dto.User;
import ro.ldir.dto.UserSessionRecord;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

/**
 *
 * @author dan.grigore
 */
public class LoginBean {

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    private WSInterface wsi;
    /* variabile afisare */
    private String loginMail="";
    private String loginPassword="";

    /** Creates a new instance of LoginBean 
     * @throws NamingException */
    public LoginBean() throws NamingException {
    	wsi = new WSInterface();
        /* adaugare mesaj info de pe sesiune daca exista */
        String infoMessage = (String) JsfUtils.getHttpSession().getAttribute("INFO_MESSAGE");
        if (infoMessage != null) {
            JsfUtils.addInfoMessage(infoMessage);
            JsfUtils.getHttpSession().removeAttribute("INFO_MESSAGE");
        }
    }

    public String actionLogin() {
        try {
        	 JsfUtils.getHttpRequest().login(loginMail, loginPassword);
        	 User userDetails = wsi.getUser(loginMail);
        	 userDetails.setPasswd(loginPassword);

        	 UserSessionRecord userSessionRecord=new UserSessionRecord();
        	 userSessionRecord.setUser(userDetails);

        	 JsfUtils.getHttpSession().setAttribute("userSessionRecord", userSessionRecord);

        	 Map <UserSessionRecord, HttpSession>maps=UserSessionRecord.getLoggedUsers();

        	 
             JsfUtils.getHttpSession().setAttribute("USER_DETAILS", userDetails);
             JsfUtils.getHttpSession().setAttribute("COUNTY_SELECTED_VALUE", userDetails.getCounty());
             return NavigationValues.LOGIN_SUCCESS;
        } catch (Exception ex) {
            JsfUtils.addWarnBundleMessage("login_fail");
            return NavigationValues.LOGIN_FAIL;
        }
    }

    /**
     * @return the loginMail
     */
    public String getLoginMail() {
        return loginMail;
    }

    /**
     * @param loginMail the loginMail to set
     */
    public void setLoginMail(String loginMail) {
        this.loginMail = loginMail;
    }

    /**
     * @return the loginPassword
     */
    public String getLoginPassword() {
        return loginPassword;
    }

    /**
     * @param loginPassword the loginPassword to set
     */
    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }
}
