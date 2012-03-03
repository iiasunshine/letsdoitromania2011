/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import javax.naming.NamingException;

import org.apache.log4j.Logger;

import ro.ldir.dto.User;
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
             JsfUtils.getHttpSession().setAttribute("USER_DETAILS", userDetails);
             return NavigationValues.LOGIN_SUCCESS;
        } catch (Exception ex) {
            log4j.fatal("Eroare: " + ex);
            JsfUtils.addWarnBundleMessage("internal_err");
        }
        return NavigationValues.LOGIN_FAIL;
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
