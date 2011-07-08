/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import com.sun.jersey.api.client.ClientResponse;
import javax.validation.ValidationException;
import org.apache.log4j.Logger;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.AppUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

/**
 *
 * @author dan.grigore
 */
public class ResetBean {

    private static final Logger log4j = Logger.getLogger(ResetBean.class.getCanonicalName());
    private WSInterface wsi = new WSInterface(true);
    /* variabile afisare */
    private String loginMail = "";
    private String password = "";
    private String passwordConfirm = "";
    private String userId = "";
    private String token = "";

    /** Creates a new instance of ResetBean */
    public ResetBean() {
        log4j.info("---> init");
        /* adaugare mesaj info de pe sesiune daca exista */
        String infoMessage = (String) JsfUtils.getHttpSession().getAttribute("INFO_MESSAGE");
        if (infoMessage != null) {
            log4j.info("---> reset info message: " + infoMessage);
            JsfUtils.addInfoMessage(infoMessage);
            JsfUtils.getHttpSession().removeAttribute("INFO_MESSAGE");
        }

        /* citire parametri request daca exista */
        if(JsfUtils.getHttpSession().getAttribute("userId") != null){
            userId = JsfUtils.getHttpSession().getAttribute("userId").toString();
            JsfUtils.getHttpSession().removeAttribute("userId");
        }else{
            userId = JsfUtils.getRequestParameter("userId");
        }
        if(JsfUtils.getHttpSession().getAttribute("token") != null){
            token = JsfUtils.getHttpSession().getAttribute("token").toString();
            JsfUtils.getHttpSession().removeAttribute("token");
        }else{
            token = JsfUtils.getRequestParameter("token");
        }
    }

    /**
     *
     * @return
     */
    public String actionReset() {
        try {
            /* cerere resetare parola */
            ClientResponse cr = wsi.resetPassword(loginMail);
            int statusCode = cr.getStatus();
            log4j.debug("---> statusCode login: " + statusCode + " (" + cr.getClientResponseStatus() + ")");

            /* verificare status code */
            if (statusCode == 404) {
                JsfUtils.addWarnBundleMessage("reset_message_not_found");
            } else if (statusCode != 200) {
                JsfUtils.addWarnBundleMessage("internal_err");
            } else {
                JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE", JsfUtils.getBundleMessage("reset_message_confirm"));
                return NavigationValues.RESET_SUCCESS;
            }
            log4j.warn("---> statusCode login: " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        } catch (Exception ex) {
            log4j.fatal("Eroare resetare parola: " + AppUtils.printStackTrace(ex));
            JsfUtils.addWarnBundleMessage("internal_err");
        }

        return NavigationValues.RESET_FAIL;
    }

    /**
     * 
     * @return
     */
    public String actionNewPassword() {
        /**
         * validari
         */
        if (password == null || password.length() == 0
                || passwordConfirm == null || passwordConfirm.length() == 0) {
            JsfUtils.addWarnBundleMessage("err_mandatory_fields");
            JsfUtils.getHttpSession().setAttribute("userId", userId);
            JsfUtils.getHttpSession().setAttribute("token", token);
            return NavigationValues.NEW_PASS_FAIL;
        } else {
            password = password.trim();
        }
        if (!passwordConfirm.equals(password)) {
            JsfUtils.addWarnBundleMessage("register_err_pass");
            JsfUtils.getHttpSession().setAttribute("userId", userId);
            JsfUtils.getHttpSession().setAttribute("token", token);
            return NavigationValues.NEW_PASS_FAIL;
        }

        /**
         * setare parola noua
         */
        try {
            /* cerere resetare parola */
            ClientResponse cr = wsi.setPassword(password, getUserId(), getToken());
            int statusCode = cr.getStatus();
            log4j.debug("---> statusCode login(userId=" + getUserId() + ", token=" + getToken() + ", password=" + password + "): " + statusCode + " (" + cr.getClientResponseStatus() + ")");

            /* verificare status code */
            if (statusCode == 404) {
                JsfUtils.addWarnBundleMessage("newpass_message_invalid_user");
            } else if (statusCode == 406) {
                JsfUtils.addWarnBundleMessage("newpass_message_expired");
            } else if (statusCode != 200) {
                JsfUtils.addWarnBundleMessage("internal_err");
            } else {
                JsfUtils.getHttpSession().setAttribute("INFO_MESSAGE", JsfUtils.getBundleMessage("newpass_message_confirm"));
                JsfUtils.addInfoBundleMessage("newpass_message_confirm");
                return NavigationValues.NEW_PASS_SUCCESS;
            }
            log4j.warn("---> statusCode login(userId=" + getUserId() + ", token=" + getToken() + ", password=" + password + "): " + statusCode + " (" + cr.getClientResponseStatus() + ")");
        } catch (Exception ex) {
            log4j.fatal("Eroare resetare parola: " + AppUtils.printStackTrace(ex));
            JsfUtils.addWarnBundleMessage("internal_err");
        }

        JsfUtils.getHttpSession().setAttribute("userId", userId);
        JsfUtils.getHttpSession().setAttribute("token", token);
        return NavigationValues.NEW_PASS_FAIL;
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the passwordConfirm
     */
    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    /**
     * @param passwordConfirm the passwordConfirm to set
     */
    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
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
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }
}
