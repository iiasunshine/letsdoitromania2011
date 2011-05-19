package ro.letsdoitromania.android.helpers;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse; 
import org.apache.http.HttpRequest;
import org.apache.http.HttpException;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.NameValuePair; 
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.auth.AuthState;
//import org.apache.http.client.;
import org.apache.http.client.CredentialsProvider;
//import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient; 
import org.apache.http.client.entity.UrlEncodedFormEntity; 
import org.apache.http.client.methods.HttpPost; 
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.*;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
//import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.DefaultHttpClient; 
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class Connection {
	
	class Garbage{
		public String description;
		public String status;
	    public int    volume;
	    public float  x;
	    public float  y;
	    
	    public String serializeJSON(){
	    	return "{" + "description:" + "\"" + description  + "\"" + "," + "status:"  + "\"" +  status  + "\"" +  
	    				"," + "volume:" + volume + "," + "x:" + x + "," + "y:" + y + "}";
	    	
	    }
	    public String serializeXML(){
	    	return "<description>" + description + "," + "status:" + status + 
	    				"," + "volume:" + volume + "," + "x:" + x + "," + "y:" + y + "}";
	    }
	}
	
	public Connection(){};
	
	public boolean authenticate(){
	
	    HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
	        public void process(final HttpRequest request, final HttpContext context) throws HttpException, IOException {
	            AuthState authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
	            CredentialsProvider credsProvider = (CredentialsProvider) context.getAttribute(
	                    ClientContext.CREDS_PROVIDER);
	            HttpHost targetHost = (HttpHost) context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
	            
	            if (authState.getAuthScheme() == null) {
	                AuthScope authScope = new AuthScope(targetHost.getHostName(), targetHost.getPort());
	                Credentials creds = credsProvider.getCredentials(authScope);
	                if (creds != null) {
	                    authState.setAuthScheme(new BasicScheme());
	                    authState.setCredentials(creds);
	                }
	            }
	        }    
	    };
	    
	    DefaultHttpClient httpclient = new DefaultHttpClient();
	    httpclient.addRequestInterceptor(preemptiveAuth, 0);

	    HttpHost targetHost   = new HttpHost("app.letsdoitromania.ro", 8080, "http"); 

	    CredentialsProvider cp  = ((AbstractHttpClient) httpclient).getCredentialsProvider();
	    
	    cp.setCredentials(  new AuthScope(targetHost.getHostName(), targetHost.getPort()),
	    					new UsernamePasswordCredentials("dummy@dummy.com", "dummy"));

	    // Create AuthCache instance
	    //AuthCache authCache   = new BasicAuthCache();
	    // Generate BASIC scheme object and add it to the local auth cache
	    //BasicScheme basicAuth = new BasicScheme();
	    //authCache.put(targetHost, basicAuth);

	    // Add AuthCache to the execution context
	    //BasicHttpContext localcontext = new BasicHttpContext();
	    //localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache); 
	    //localcontext.setAttribute(ClientContext.CREDS_PROVIDER, cp);
	    
	    HttpPost httppost = new HttpPost("http://app.letsdoitromania.ro:8080/LDIRBackend/ws/garbage");
	    
	   // String user = "dummy@dummy.com";
	   // String pwd  = "dummy";
	    Garbage morman = new Garbage();
	    morman.description = "dummy_garbage";
	    morman.status      = "IDENTIFIED";
	    morman.volume      = 100;
	    morman.x           = 47.3f;
	    morman.y           = 44.2f;
	    
	    HttpResponse response = null;
	    
	    try {
	        
	        // Execute HTTP Post Request
	        try{
	
	        	httppost.setHeader("Content-Type", "application/json");
	     
	        	String morman_json = morman.serializeJSON();
	        	httppost.setEntity(new StringEntity(morman_json,"ASCII"));
	       
	        	response          = httpclient.execute(httppost);
	        	
	        	StatusLine stat   = response.getStatusLine();
	        	int status        = stat.getStatusCode();
	        	HttpEntity entity = response.getEntity();
	        	String message    = null;
	        	
	        	if (entity != null){
        		   InputStream input = entity.getContent();	
        		   message = convertStreamToString(input);
        		   System.out.println(message);
        		   input.close();
        		}
	        	if ( status != 200){
	        		//requestul nu a funcționat
	        		System.out.println("cannot insert");
	        		String mesaj = stat.toString();
	        		System.out.println(mesaj);
	        		
	        		
	        	}
	        	
	        }
	        catch (Exception e){
	        	return false;
	        }
	        
	    //} catch (ClientProtocolException e) {
	        // TODO Auto-generated catch block
	    } catch (Exception e) {
	        // TODO Auto-generated catch block
	    }
	    
	    if (response != null)
	    	return true;
	    else
	    	return false;
	    
	};
	

	 private static String convertStreamToString(InputStream is) {

	        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8192);

	        StringBuilder sb = new StringBuilder();

	        String line = null;

	        try {

	            while ((line = reader.readLine()) != null) {

	                sb.append(line + "\n");

	            }

	        } catch (IOException e) {

	            e.printStackTrace();

	        } finally {

	            try {

	                is.close();

	            } catch (IOException e) {

	                e.printStackTrace();

	            }

	        }

	        return sb.toString();


	    }

}
