package ro.ldir.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ro.radcom.ldir.ldirbackendwebjsf2.managedBeans.LoginBean;

public class UserSessionRecord implements HttpSessionBindingListener {
	private static final Logger log4j = Logger.getLogger(LoginBean.class.getCanonicalName());
	
	private static Map<UserSessionRecord, HttpSession> loggedUsers = new HashMap<UserSessionRecord, HttpSession>();
	private User user;

	private String ip;
	
	private String date;
	
	
	public static Map<UserSessionRecord, HttpSession> getLoggedUsers() {
		return loggedUsers;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}



	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public boolean equals(Object other) {
		return (other instanceof UserSessionRecord) && (user != null) ? user
				.equals(((UserSessionRecord)other).getUser()) : (other == this);
	}
	
	@Override
	public int hashCode(){
		return (user!=null)? (this.getClass().hashCode()+user.hashCode()): super.hashCode();
	}

	@Override
	public void valueBound(HttpSessionBindingEvent event) {
		HttpSession session=loggedUsers.remove(this);
		if(session!=null){
			session.invalidate();
		}
		loggedUsers.put(this, event.getSession());
		log4j.warn("added on session");
	}

	@Override
	public void valueUnbound(HttpSessionBindingEvent event) {
		loggedUsers.remove(this);
		if(user!=null){
			if(user.getEmail()!=null){
				log4j.warn("Removed from session "+user.getEmail());
			}
		}
		log4j.warn("Removed ended");
	}

}
