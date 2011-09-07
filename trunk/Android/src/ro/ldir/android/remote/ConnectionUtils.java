package ro.ldir.android.remote;

import java.io.IOException;

import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import ro.ldir.android.util.LLog;

public class ConnectionUtils
{
	public final static String _ldir_host_prod    = "app.letsdoitromania.ro";
	public final static String _ldir_host_test    = "app-test.letsdoitromania.ro";
	public final static String _ldir_host_test_ip    = "85.204.235.19";
	public final static String _ldir_host    = _ldir_host_prod;
	public final static int    _ldir_port    = 8080;
	public final static String _ldir_proto   = "http";
	public final static String _ldir_ws_auth = "LDIRBackend";
	
	/*  Actions  */
	public static final String ACTION_SIGN_IN = "ws/user";
	public static final String ACTION_USER_DETAILS = "ws/user/%d";
	public static final String ACTION_SIGN_OUT = ""; //TODO
	public static final String ACTION_ADD_GARBAGE = "ws/garbage";
	public static final String ACTION_ASSIGN_IMAGE = "ws/garbage/%d/image";
	public static final String ACTION_SET_STATUS = "ws/garbage/%d/status";
	
	/**
	 * Creates an URL like: http://app.letsdoitromania.ro:8080/LDIRBackend/<b>action</b>
	 * @param action
	 * @return 
	 */
	public static final String getWsAddress(String action, Object... params){
		String url =  _ldir_proto + "://" + _ldir_host + ":" + _ldir_port + "/" + _ldir_ws_auth + "/" + String.format(action, params);
		LLog.d("wsAddress: " + url);
		return url;
	}
	
	public static DefaultHttpClient getHttpConn(String user, String password)
	{

		HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
			public void process(final HttpRequest request, final HttpContext context)
					throws HttpException, IOException
			{
				AuthState authState = (AuthState) context
						.getAttribute(ClientContext.TARGET_AUTH_STATE);
				CredentialsProvider credsProvider = (CredentialsProvider) context
						.getAttribute(ClientContext.CREDS_PROVIDER);
				HttpHost targetHost = (HttpHost) context
						.getAttribute(ExecutionContext.HTTP_TARGET_HOST);

				if (authState.getAuthScheme() == null)
				{
					AuthScope authScope = new AuthScope(targetHost.getHostName(),
							targetHost.getPort());
					Credentials creds = credsProvider.getCredentials(authScope);
					if (creds != null)
					{
						authState.setAuthScheme(new BasicScheme());
						authState.setCredentials(creds);
					}
				}
			}
		};

		HttpHost targetHost = new HttpHost(_ldir_host, _ldir_port, _ldir_proto);
		DefaultHttpClient httpClient = new DefaultHttpClient();

		httpClient.addRequestInterceptor(preemptiveAuth, 0);

		CredentialsProvider cp = ((AbstractHttpClient) httpClient).getCredentialsProvider();

		cp.setCredentials(new AuthScope(targetHost.getHostName(), targetHost.getPort()),
				new UsernamePasswordCredentials(user, password));

		return httpClient;

	}
}
