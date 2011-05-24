/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.ldir.view;

import ro.ldir.dto.User;
import java.util.HashMap;
import java.util.Map;
import ro.ldir.beans.UserManagerLocal;
import javax.ejb.EJBException;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.faces.application.FacesMessage;

public class userViewBean extends layoutBean
{

    private User user;
    private UserManagerLocal userManager;
    private String repasswd;
    private Map<String, String> counties = new HashMap<String, String>();
    private Map<String, User.Activity> activitiesList = new HashMap<String, User.Activity>();
    private boolean accept;

    public boolean getAccept()
    {
        return accept;
    }

    public void setAccept(boolean accept)
    {
        this.accept = accept;
    }

    public Map<String, User.Activity> getActivitiesList()
    {
        return activitiesList;
    }

    public userViewBean() throws NamingException
    {
        user = new User();
        counties.put("Bucuresti", "Bucuresti");
        counties.put("Brasov", "Brasov");

        activitiesList.put("Cartare", User.Activity.CHART);
        activitiesList.put("Curatenie", User.Activity.CLEAN);

        InitialContext ic = new InitialContext();
        userManager = (UserManagerLocal) ic.lookup("java:global/LDIRBackendWebJSF/UserManager!ro.ldir.beans.UserManager");

    }

    public Map<String, String> getCounties()
    {
        return counties;
    }

    public String getRepasswd()
    {
        return repasswd;
    }

    public void setRepasswd(String repasswd)
    {
        this.repasswd = repasswd;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String register()
    {
        try
        {
            userManager.addUser(user);
        } catch (EJBException e)
        {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "EJB error:'" + e.getMessage() + "'");
            addMessage(message);

            return "registerpage.faces";
        } 
        return "registrationDone";
    }
}
