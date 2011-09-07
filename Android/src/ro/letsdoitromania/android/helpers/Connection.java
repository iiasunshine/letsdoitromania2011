package ro.letsdoitromania.android.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import ro.letsdoitromania.android.structuri.Morman;


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

	//>addresses & actions
	final static String _ldir_host    = "app.letsdoitromania.ro";
	final static int    _ldir_port    = 8080;
	final static String _ldir_proto   = "http";
	final static String _ldir_ws_auth = "LDIRBackend/ws";
	//<
	
	String getWsAddress(String action){
		//"http://app.letsdoitromania.ro:8080/LDIRBackend/ws/garbage");
		return _ldir_proto + "://" + _ldir_host + ":" + _ldir_port + "/" + _ldir_ws_auth + "/" + action;
	}
	
	DefaultHttpClient getHttpConn(String user, String password){
	
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
	    
	    HttpHost targetHost          = new HttpHost(_ldir_host, _ldir_port, _ldir_proto);
	    DefaultHttpClient httpClient = new DefaultHttpClient();
	    
	    httpClient.addRequestInterceptor(preemptiveAuth, 0);

	    CredentialsProvider cp  = ((AbstractHttpClient) httpClient).getCredentialsProvider();
	    
	    cp.setCredentials(  new AuthScope(targetHost.getHostName(), targetHost.getPort()),
	    					new UsernamePasswordCredentials(user, password));

	    return httpClient;		    
		
	}
	
	public long authenticate(String user, String password){
		
		HttpGet request 		= new HttpGet(getWsAddress("user"));
		HttpResponse response 	= null;
		
		try{
			response = getHttpConn(user,password).execute(request);
			
			if (response.getStatusLine().getStatusCode() != 200)
				return -1;
			
			String  content	= convertStreamToString(response.getEntity().getContent());
			return Long.parseLong(content.toString());
						
		}
		catch(Exception ex){
			
		}
		return -1;		
	}
	
	public boolean addGarbage(String user, String password, long usrId, Morman morman){
	
	    HttpPost httppost = new HttpPost(getWsAddress("garbage"));
	    
	    HttpResponse response = null;
	    
	        
	    // Execute HTTP Post Request
	    try{
	    	
	    	httppost.setHeader("Content-Type", "application/json");
	     
	    	httppost.setEntity(new StringEntity(morman.toJson(),"ASCII"));
	       
	    	response          = getHttpConn(user,password).execute(httppost);
	        	
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
	        
	    if (response != null)
	    	return true;
	    else
	    	return false;
	    
	};
	

	 public static String convertStreamToString(InputStream is) {

	        BufferedReader reader = new BufferedReader(new InputStreamReader(is), 8192);

	        StringBuilder sb = new StringBuilder();

	        String line = null;

	        try {

	            while ((line = reader.readLine()) != null) {

	                sb.append(line);//+ "/n" );

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
