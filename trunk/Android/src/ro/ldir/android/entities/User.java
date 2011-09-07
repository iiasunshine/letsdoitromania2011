package ro.ldir.android.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Base user. contains only fields common with the backend user
 * If there is the need to add extra fields to the user, then add them in subclasses of this class
 * This class MUST be in sync with the one on the backend
 * @author Coralia Paunoiu
 *
 */
public class User
{
	 public enum Activity {
         CHART("cartare"), CLEAN("salubrizare");
         private String reportName;

         private Activity(String reportName) {
                 this.reportName = reportName;
         }

         public String getReportName() {
                 return reportName;
         }
 }

 public enum SecurityRole {
         ADMIN, ORGANIZER, ORGANIZER_MULTI, PENDING, SUSPENDED, VOLUNTEER, VOLUNTEER_MULTI;
         private static List<SecurityRole> multiRoles = null;

         public static List<SecurityRole> getMultiRoles() {
                 if (multiRoles != null)
                         return multiRoles;
                 multiRoles = new ArrayList<SecurityRole>();
                 multiRoles.add(ORGANIZER_MULTI);
                 multiRoles.add(VOLUNTEER_MULTI);
                 return multiRoles;
         }
 }

 private static final long serialVersionUID = 1L;
 
 private boolean acceptsMoreInfo;
 private List<Activity> activities;
 private Date birthday;
 private String county;
 private String email;
 private String firstName;
 private Set<Garbage> garbages;
 private Integer invalidAccessCount = new Integer(0);
 private Date lastAccess;
 private String lastName;
 private List<Integer> managedTeams;
 private int memberOf;
 private List<Integer> organizations;
 private String passwd;
 private String phone;
 private Date recordDate;
 private String registrationToken;
 private Date resetDate;
 private String resetToken;
 private String role;
 private String town;
 private Integer userId;

 public User() {
 }

public boolean isAcceptsMoreInfo()
{
	return acceptsMoreInfo;
}

public void setAcceptsMoreInfo(boolean acceptsMoreInfo)
{
	this.acceptsMoreInfo = acceptsMoreInfo;
}

public List<Activity> getActivities()
{
	return activities;
}

public void setActivities(List<Activity> activities)
{
	this.activities = activities;
}

public Date getBirthday()
{
	return birthday;
}

public void setBirthday(Date birthday)
{
	this.birthday = birthday;
}

public String getCounty()
{
	return county;
}

public void setCounty(String county)
{
	this.county = county;
}

public String getEmail()
{
	return email;
}

public void setEmail(String email)
{
	this.email = email;
}

public String getFirstName()
{
	return firstName;
}

public void setFirstName(String firstName)
{
	this.firstName = firstName;
}

public Set<Garbage> getGarbages()
{
	return garbages;
}

public void setGarbages(Set<Garbage> garbages)
{
	this.garbages = garbages;
}

public Date getLastAccess()
{
	return lastAccess;
}

public void setLastAccess(Date lastAccess)
{
	this.lastAccess = lastAccess;
}

public String getLastName()
{
	return lastName;
}

public void setLastName(String lastName)
{
	this.lastName = lastName;
}

public List<Integer> getManagedTeams()
{
	return managedTeams;
}

public void setManagedTeams(List<Integer> managedTeams)
{
	this.managedTeams = managedTeams;
}

public int getMemberOf()
{
	return memberOf;
}

public void setMemberOf(int memberOf)
{
	this.memberOf = memberOf;
}

public String getPasswd()
{
	return passwd;
}

public void setPasswd(String passwd)
{
	this.passwd = passwd;
}

public Integer getUserId()
{
	return userId;
}

public void setUserId(Integer userId)
{
	this.userId = userId;
}
 
 

}
