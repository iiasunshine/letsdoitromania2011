/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import com.sun.jersey.api.client.ClientResponse;
import org.apache.log4j.Logger;
import ro.ldir.dto.User;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

/**
 *
 * @author dan.grigore
 */
public class LoginBean {

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
    private WSInterface wsi = new WSInterface(true);
    /* variabile afisare */
    private String loginMail="";
    private String loginPassword="";

    /** Creates a new instance of LoginBean */
    public LoginBean() {
        /* adaugare mesaj info de pe sesiune daca exista */
        String infoMessage = (String) JsfUtils.getHttpSession().getAttribute("INFO_MESSAGE");
        if (infoMessage != null) {
            JsfUtils.addInfoMessage(infoMessage);
            JsfUtils.getHttpSession().removeAttribute("INFO_MESSAGE");
        }
    }

    public String actionLogin() {
        try {
            /* cerere autentificare */
            ClientResponse cr = wsi.login(loginMail, loginPassword);
            int statusCode = cr.getStatus();
            log4j.debug("---> statusCode login: " + statusCode + " (" + cr.getClientResponseStatus() + ")");

            /* verificare status code */
            if (statusCode == 403 || statusCode == 401) {
                JsfUtils.addWarnBundleMessage("login_fail");
            } else if (statusCode != 200) {
                JsfUtils.addWarnBundleMessage("internal_err");
            } else {
                /* obtinere id user */
                int userId = AppUtils.parseToInt(cr.getEntity(String.class));

                /* cerere informatii user */
                cr = wsi.getUserDetails(loginMail, loginPassword, userId);
                statusCode = cr.getStatus();
                if (statusCode != 200) {
                    JsfUtils.addWarnBundleMessage("internal_err");
                } else {
                    User userDetails = cr.getEntity(User.class);
                    userDetails.setPasswd(loginPassword);
                    JsfUtils.getHttpSession().setAttribute("USER_DETAILS", userDetails);
                    return NavigationValues.LOGIN_SUCCESS;
                }
            }
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
