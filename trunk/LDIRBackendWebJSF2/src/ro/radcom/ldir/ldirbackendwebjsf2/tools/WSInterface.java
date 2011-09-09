/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ro.radcom.ldir.ldirbackendwebjsf2.tools;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import org.apache.log4j.Logger;
import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.Organization;
import ro.ldir.dto.User;
import ro.radcom.ldir.ldirbackendwebjsf2.tools.customObjects.GarbageContextProvider;

/**
 *
 * @author dan.grigore
 */
public class WSInterface {

    private static final Logger log4j = Logger.getLogger(WSInterface.class.getCanonicalName());
    private Client client = null;
    private String WS_URL = "";

    public WSInterface(boolean custom) {
        WS_URL = JsfUtils.getInitParameter("webservice.url");
        if (custom) {
            ClientConfig cc = new DefaultClientConfig();
            cc.getClasses().add(GarbageContextProvider.class);
            client = Client.create(cc);
       // 	client = LdirClientResolverFactory.getResolverClient("http://app.letsdoitromania.ro/LDIRBackend/", username introdus de utilizator, parola introdusa de utilizator);
        } else {
            client = Client.create();
        }
    }

    public WSInterface() {
        this(false);
    }

    public ClientResponse addGarbage(User user, Garbage garbage) {
        String location = WS_URL + "/LDIRBackend/ws/garbage";
        if (garbage.getGarbageId() != null && garbage.getGarbageId() > 0) {
            location += "/" + garbage.getGarbageId();
        }
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.entity(garbage, MediaType.APPLICATION_XML).post(ClientResponse.class);
        return cr;
    }

    public ClientResponse addChartedArea(User user, int teamId, int areaId) {
        String location = WS_URL + "/LDIRBackend/ws/team/" + teamId + "/chartArea";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
                JsfUtils.getInitParameter("admin.password")));

        ChartedArea chartedArea = new ChartedArea();
        chartedArea.setAreaId(areaId);

        ClientResponse cr = builder.entity(chartedArea, MediaType.APPLICATION_XML).post(ClientResponse.class);
        return cr;
    }

    /*
     * 
     */
    public ClientResponse setChartedPercent(int areaId, int percent) {
        String location = WS_URL + "/LDIRBackend/ws/geo/chartedArea/" + areaId + "/percentageCompleted";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
                JsfUtils.getInitParameter("admin.password")));

        Integer percentInteger = new Integer(percent);

        ClientResponse cr = builder.entity(percentInteger, MediaType.APPLICATION_XML).post(ClientResponse.class);
        return cr;
    }

    public ClientResponse removeChartedArea(User user, int teamId, int areaId) {
        String location = WS_URL + "/LDIRBackend/ws/team/" + teamId + "/chartArea/" + areaId;
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));

        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).delete(ClientResponse.class);
        return cr;
    }

    public ClientResponse addPicture(User user, Garbage garbage, File picture) throws Exception {
        if (!picture.exists()) {
            throw new Exception(picture.getAbsolutePath() + " does not exist, skipping test!");
        }

        String location = WS_URL + "/LDIRBackend/ws/garbage/" + garbage.getGarbageId() + "/image";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));

        FormDataMultiPart fdmp = new FormDataMultiPart();
        fdmp.bodyPart(new FileDataBodyPart("file", picture));
        ClientResponse cr = builder.entity(garbage, MediaType.MULTIPART_FORM_DATA_TYPE).post(ClientResponse.class, fdmp);

        return cr;
    }

    public ClientResponse getPicture(User user, Garbage garbage, int imgNr, boolean full) throws Exception {
        String location = WS_URL + "/LDIRBackend/ws/garbage/" + garbage.getGarbageId() + "/image/" + imgNr + (full ? "/display" : "/thumb");
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));

        ClientResponse cr = builder.get(ClientResponse.class);
        return cr;
    }

    public ClientResponse countPictures(User user, Garbage garbage) throws Exception {
        String location = WS_URL + "/LDIRBackend/ws/garbage/" + garbage.getGarbageId() + "/imageCount";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));

        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).get(ClientResponse.class);

        return cr;
    }

    public ClientResponse deleteGarbage(User user, Garbage garbage) {
        String location = WS_URL + "/LDIRBackend/ws/garbage/" + garbage.getGarbageId();
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).delete(ClientResponse.class);
        return cr;
    }

    public ClientResponse getGarbage(User user, int garbageId) {
        String location = WS_URL + "/LDIRBackend/ws/garbage/" + garbageId;
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }
    public ClientResponse getChartedAreasOfTeam(User user, int teamId) {
        String location = WS_URL + "/LDIRBackend/ws/team/" + teamId + "/chartArea";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }
    public ClientResponse getTeam(User user,int teamId) {  
        String location = WS_URL + "/LDIRBackend/ws/team/" + teamId;
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
//        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }
    public ClientResponse getTeamManager(User user,int teamId) {
        String location = WS_URL + "/LDIRBackend/ws/team/" + teamId + "/teamManager";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }
    public ClientResponse getTeamMembers(User user,int teamId) {
        String location = WS_URL + "/LDIRBackend/ws/team/" + teamId + "/volunteerMembers";
        WebResource resource = client.resource(location); 
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }
    public ClientResponse getTeamOrganization(User user,int teamId) {
        String location = WS_URL + "/LDIRBackend/ws/team/" + teamId + "/organizationMembers";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }
    
    public ClientResponse addOrganization(User user,Organization org) {
        String location = WS_URL + "/LDIRBackend/ws/organization";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.entity(org, MediaType.APPLICATION_XML).post(ClientResponse.class);
        return cr;
        
//		String location = JsfUtils.getInitParameter("webservice.url") + "/LDIRBackend/ws/organization";
//		Client client = Client.create();
//	    WebResource resource = client.resource(location);
//	    Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"), JsfUtils.getInitParameter("admin.password")));
//	    ClientResponse cr = builder.entity(organization, MediaType.APPLICATION_XML).post(ClientResponse.class);
    }
    public ClientResponse getOrganizationTeam(User user,int orgId) {
        String location = WS_URL + "/LDIRBackend/ws/organization/" + orgId + "/memberOf";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
        ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse getTeamsOfChartedArea(User user, int areaId) {
        String location = WS_URL + "/LDIRBackend/ws/geo/chartedArea/" + areaId + "/chartedBy";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
//        ClientResponse cr = builder.entity(null, MediaType.APPLICATION_XML).get(ClientResponse.class);
        ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse getGarbagesOfChartedArea(User user, int areaId) {
        String location = WS_URL + "/LDIRBackend/ws/geo/chartedArea/" + areaId + "/garbages";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user.getEmail(), user.getPasswd()));
//      ClientResponse cr = builder.entity(null, MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
        ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse getGarbageList(String status) {
        String location = WS_URL + "/LDIRBackend/ws/garbage/statusSearch/?status=" + status;
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
                JsfUtils.getInitParameter("admin.password")));
        ClientResponse cr = builder.entity(null, MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse getGarbageListByFilters(User admin, String countyId, int gridId, int userId, Date addDate, String accept) {
        String location = WS_URL + "/LDIRBackend/ws/garbage/report?";
        if (countyId != null && countyId.length() > 0) {
            location += "county=" + countyId + "&";
        }
        if (gridId > 0) {
            location += "chartedArea=" + gridId + "&";
        }
        if (userId > 0) {
            location += "userId=" + userId + "&";
        }
        if (addDate != null) {
            location += "insertDate=" + new SimpleDateFormat("yyyy-MM-dd").format(addDate);
        }
        log4j.debug("---> URL: " + location);

        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(admin.getEmail(), admin.getPasswd()));
        if (accept != null && accept.length() > 0) {
            builder.accept(accept);
        }
        ClientResponse cr = builder.entity(null, MediaType.TEXT_XML_TYPE).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse getCountyList() {
        String location = WS_URL + "/LDIRBackend/ws/geo/countyArea/all";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
                JsfUtils.getInitParameter("admin.password")));
        ClientResponse cr = builder.entity(null, MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse getChartedArea(int areaId) {
        String location = WS_URL + "/LDIRBackend/ws/geo/chartedArea/" + areaId;
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
                JsfUtils.getInitParameter("admin.password")));
//        ClientResponse cr = builder.entity(null, MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
        ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse getMemberOfTeam(int userId) {
        String location = WS_URL + "/LDIRBackend/ws/user/" + userId + "/memberOf";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(JsfUtils.getInitParameter("admin.user"),
                JsfUtils.getInitParameter("admin.password")));
//        ClientResponse cr = builder.entity(null, MediaType.TEXT_PLAIN_TYPE).get(ClientResponse.class);
        ClientResponse cr = builder.accept(MediaType.APPLICATION_XML).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse login(String user, String pass) {
        String location = WS_URL + "/LDIRBackend/ws/user";
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user, pass));
        ClientResponse cr = builder.entity(null, MediaType.TEXT_PLAIN).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse resetPassword(String email) {
        String location = WS_URL + "/LDIRBackend/reg/ws/reset?email=" + email;
        WebResource resource = client.resource(location);
        Builder builder = resource.getRequestBuilder();
        ClientResponse cr = builder.entity(null, MediaType.TEXT_PLAIN).get(ClientResponse.class);
        return cr;
    }

    public ClientResponse setPassword(String newPassword, String userId, String token) {
        String location = WS_URL + "/LDIRBackend/reg/ws/reset/" + userId + "/" + token;
        WebResource resource = client.resource(location);
        Builder builder = resource.getRequestBuilder();
        ClientResponse cr = builder.entity(newPassword, MediaType.APPLICATION_XML).post(ClientResponse.class);
        return cr;
    }

    public ClientResponse getUserDetails(String user, String pass, int userId) {
        String location = WS_URL + "/LDIRBackend/ws/user/" + userId;
        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(user, pass));
        ClientResponse cr = builder.entity(null, MediaType.TEXT_PLAIN).get(ClientResponse.class);

        return cr;
    }

    public ClientResponse getUserListByFilters(User admin, String county, int birthYear, String role, int minGarbages, int maxGarbages, String accept) {
        String location = WS_URL + "/LDIRBackend/ws/user/report?";
        if (county != null && county.length() > 0) {
            location += "county=" + county + "&";
        }
        if (birthYear > 0) {
            location += "birthyear=" + birthYear + "&";
        }
        if (role != null && role.length() > 0) {
            location += "role=" + role + "&";
        }
        if (minGarbages >= 0) {
            location += "minGarbages=" + minGarbages + "&";
        }
        if (maxGarbages >= 0) {
            location += "maxGarbages=" + maxGarbages + "&";
        }

        log4j.debug("---> URL: " + location);

        WebResource resource = client.resource(location);
        Builder builder = resource.header(HttpHeaders.AUTHORIZATION, AppUtils.generateCredentials(admin.getEmail(), admin.getPasswd()));
        if (accept != null && accept.length() > 0) {
            builder.accept(accept);
        }
        ClientResponse cr = builder.entity(null, MediaType.TEXT_XML_TYPE).get(ClientResponse.class);

        return cr;

    }

    public ClientResponse reinitUser(User userDetails) {
        String pass = userDetails.getPasswd();
        ClientResponse cr = getUserDetails(userDetails.getEmail(), userDetails.getPasswd(), userDetails.getUserId());
        int statusCode = cr.getStatus();
        if (statusCode == 200) {
            userDetails = cr.getEntity(User.class);
            userDetails.setPasswd(pass);
            JsfUtils.getHttpSession().setAttribute("USER_DETAILS", userDetails);
        }
        return cr;
    }
}
