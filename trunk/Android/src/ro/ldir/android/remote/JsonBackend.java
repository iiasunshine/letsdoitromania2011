package ro.ldir.android.remote;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import ro.ldir.android.entities.Garbage;
import ro.ldir.android.entities.User;
import ro.ldir.android.util.LLog;
import ro.letsdoitromania.android.helpers.Connection;
import android.graphics.Bitmap;

/**
 * Implementation of the backend using json parsing
 * @author Coralia Paunoiu
 *
 */
public class JsonBackend implements IBackend
{

	public User signIn(String user, String password) throws RemoteConnError
	{
		HttpGet request = new HttpGet(ConnectionUtils.getWsAddress(ConnectionUtils.ACTION_SIGN_IN));
		HttpResponse response = null;

		try
		{
			response = ConnectionUtils.getHttpConn(user, password).execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			if(statusCode != STATUS_CODE_OK)
			{
				LLog.d("Loging failed. Status = " + response.getStatusLine().toString());
				throw new RemoteConnError(statusCode);
			}
			
			/* obtinere id user */
			String content = Connection.convertStreamToString(response.getEntity().getContent());
			long userId =  Long.parseLong(content.toString());
			
			/* cerere informatii user */
			request = new HttpGet(ConnectionUtils.getWsAddress(ConnectionUtils.ACTION_USER_DETAILS, userId));
			response = null;
			response = ConnectionUtils.getHttpConn(user, password).execute(request);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				LLog.d("Loging failed. Status = " + response.getStatusLine().toString());
				throw new RemoteConnError(statusCode);
			}
        	content = Connection.convertStreamToString(response.getEntity().getContent());
        	LLog.d(content);
        	User sessionUser = new User();
        	JsonSerializer serializer = new JsonSerializer();
        	sessionUser = serializer.deserialize(content, sessionUser);
        	sessionUser.setPasswd(password);
        	sessionUser.setUserId((int)userId);
        	return sessionUser;

		}catch(RemoteConnError rce)
		{
			throw rce;
		}
		catch (Exception ex)
		{
			LLog.e(ex);
			throw new RemoteConnError(-1);
		}
	}

	public void signOut()
	{
		// TODO Auto-generated method stub
		
	}

	public void addGarbage(Garbage garbage) throws RemoteConnError
	{
		// TODO Auto-generated method stub
		
	}

	public void assignImageToGarbage(long garbageId, Bitmap picture) throws RemoteConnError
	{
		// TODO Auto-generated method stub
		
	}

	public void setGarbageStatus(long garbageId, GarbageStatus status) throws RemoteConnError
	{
		// TODO Auto-generated method stub
		
	}

}
