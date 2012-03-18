/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.managedBeans;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.naming.NamingException;

import nl.captcha.Captcha;
import nl.captcha.backgrounds.GradiatedBackgroundProducer;
import nl.captcha.gimpy.DropShadowGimpyRenderer;
import nl.captcha.text.producer.DefaultTextProducer;

import org.apache.log4j.Logger;

import ro.ldir.dto.User;
import ro.ldir.exceptions.InvalidUserOperationException;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.JsfUtils;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.WSInterface;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.CountyNames;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.NavigationValues;

/**
 *
 * @author dan.grigore
 */
public class RegisterBean {
	private WSInterface wsi;

    private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());

    /* variabile afisare */
    private User regiterUser = new User();
    private String passwordConfirm;
    private int day;
    private int month;
    private int year;
    private boolean cartare = false;
    private boolean curatenie = false;
    private boolean acceptTerms = false;
    private boolean profileView = true;
    private boolean acceptReceiveNotifications = true;
    private String antispam;

    /** Creates a new instance of RegisterBean 
     * @throws NamingException */
    public RegisterBean() throws NamingException {
    	wsi = new WSInterface();
        initCaptcha();
    }

    private void initCaptcha() {
        if (JsfUtils.getHttpSession().getAttribute(Captcha.NAME) == null) {
            Captcha captcha = new Captcha.Builder(120, 40).addBackground(new GradiatedBackgroundProducer()).addText(new DefaultTextProducer()).gimp(new DropShadowGimpyRenderer()).addNoise().build();
            JsfUtils.getHttpSession().setAttribute(Captcha.NAME, captcha);
        }
    }

    public String actionRegister() {
        Captcha captcha = (Captcha) JsfUtils.getHttpSession().getAttribute(Captcha.NAME);
        JsfUtils.getHttpSession().removeAttribute(Captcha.NAME);
        initCaptcha();

        /**
         * validare campuri
         */
        if (regiterUser.getFirstName() == null || regiterUser.getFirstName().trim().length() == 0
                || regiterUser.getLastName() == null || regiterUser.getLastName().trim().length() == 0
                || regiterUser.getEmail() == null || regiterUser.getEmail().trim().length() == 0
                || regiterUser.getPasswd() == null || regiterUser.getPasswd().trim().length() == 0
                || passwordConfirm == null || passwordConfirm.length() == 0
                || (!curatenie && !cartare)) {
            JsfUtils.addWarnBundleMessage("err_mandatory_fields");
            return NavigationValues.REGISTER_FAIL;
        } else {
            regiterUser.setFirstName(regiterUser.getFirstName().trim());
            regiterUser.setLastName(regiterUser.getLastName().trim());
            regiterUser.setEmail(regiterUser.getEmail().trim());
            regiterUser.setPasswd(regiterUser.getPasswd().trim());
        }
        if (!acceptTerms) {
            JsfUtils.addWarnBundleMessage("register_err_terms");
            return NavigationValues.REGISTER_FAIL;
        }
        if (!passwordConfirm.equals(regiterUser.getPasswd())) {
            JsfUtils.addWarnBundleMessage("register_err_pass");
            return NavigationValues.REGISTER_FAIL;
        }
        if (antispam == null || captcha == null || !captcha.isCorrect(antispam)) {
            JsfUtils.addWarnBundleMessage("register_err_antispam");
            antispam = "";
            return NavigationValues.REGISTER_FAIL;
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
        regiterUser.setActivities(activities);

        if (day > 0 && month > 0 && year > 0) {
            Date birthDay = new Date(year - 1900, month - 1, day);
            regiterUser.setBirthday(birthDay);
            log4j.debug("---> BirthDay: " + new SimpleDateFormat("dd/MM/yyyy").format(birthDay));
        }
        
        regiterUser.setAcceptsMoreInfo(acceptReceiveNotifications);
        regiterUser.setProfileView(profileView);

        try {
			wsi.registerUser(regiterUser);
		} catch (InvalidUserOperationException e) {   
            JsfUtils.addWarnBundleMessage("register_err_duplicate_mail");
            return NavigationValues.REGISTER_FAIL;
		}
            JsfUtils.addInfoBundleMessage("register_message2");
            return NavigationValues.REGISTER_FAIL;
       
    }

    public List<SelectItem> getCountyItems() {
        List<SelectItem> items = new ArrayList<SelectItem>();

        for (int i = 0; i < CountyNames.COUNTIES.size(); i++) {
            String county = CountyNames.COUNTIES.get(i);
            items.add(new SelectItem(county, county));
        }

        return items;
    }

    /**
     * @return the regiterUser
     */
    public User getRegiterUser() {
        return regiterUser;
    }

    /**
     * @param regiterUser the regiterUser to set
     */
    public void setRegiterUser(User regiterUser) {
        this.regiterUser = regiterUser;
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
            years.add(new SelectItem(i, "" + i));
        }

        return years;
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

    /**
     * @return the antispam
     */
    public String getAntispam() {
        return antispam;
    }

    /**
     * @param antispam the antispam to set
     */
    public void setAntispam(String antispam) {
        this.antispam = antispam;
    }

    /**
     * @return the acceptTerms
     */
    public boolean isAcceptTerms() {
        return acceptTerms;
    }

    /**
     * @param acceptTerms the acceptTerms to set
     */
    public void setAcceptTerms(boolean acceptTerms) {
        this.acceptTerms = acceptTerms;
    }

	/**
	 * @param acceptReceiveNotifications the acceptReceiveNotifications to set
	 */
	public void setAcceptReceiveNotifications(boolean acceptReceiveNotifications) {
		this.acceptReceiveNotifications = acceptReceiveNotifications;
	}

	/**
	 * @return the acceptReceiveNotifications
	 */
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
