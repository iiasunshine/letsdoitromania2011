package ro.ldir.android.remote;

import java.io.File;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import ro.ldir.android.entities.Garbage;
import ro.ldir.android.entities.GarbageList;
import ro.ldir.android.entities.User;
import ro.ldir.android.util.LLog;
import ro.ldir.android.util.Utils;
import ro.letsdoitromania.android.helpers.Connection;

/**
 * Implementation of the backend using json parsing
 * 
 * @author Coralia Paunoiu
 * 
 */
public class JsonBackend implements IBackend {

	public User signIn(String user, String password) throws RemoteConnError {
		HttpGet request = new HttpGet(
				ConnectionUtils.getWsAddress(ConnectionUtils.ACTION_SIGN_IN));
		HttpResponse response = null;

		try {
			response = ConnectionUtils.getHttpConn(user, password).execute(
					request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != STATUS_CODE_OK) {
				LLog.d("Loging failed. Status = "
						+ response.getStatusLine().toString());
				throw new RemoteConnError(statusCode);
			}

			/* obtinere id user */
			String content = Connection.convertStreamToString(response
					.getEntity().getContent());
			long userId = Long.parseLong(content.toString());

			/* cerere informatii user */
			request = new HttpGet(ConnectionUtils.getWsAddress(
					ConnectionUtils.ACTION_USER_DETAILS, userId));
			response = null;
			response = ConnectionUtils.getHttpConn(user, password).execute(
					request);
			statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				LLog.d("Loging failed. Status = "
						+ response.getStatusLine().toString());
				throw new RemoteConnError(statusCode);
			}
			content = Connection.convertStreamToString(response.getEntity()
					.getContent());
			LLog.d(content);
			User sessionUser = new User();
			JsonSerializer serializer = new JsonSerializer();
			sessionUser = serializer.deserialize(content, sessionUser);
			sessionUser.setPasswd(password);
			sessionUser.setUserId((int) userId);
			return sessionUser;

		} catch (RemoteConnError rce) {
			throw rce;
		} catch (Exception ex) {
			LLog.e(ex);
			throw new RemoteConnError(-1);
		}
	}

	public void signOut() {
		// TODO Auto-generated method stub

	}

	/**
	 * Retrieve all garbages in an area delimited by topLeft, bottomRight
	 * coordinates
	 * 
	 * @param topLeftX
	 * @param topLeftY
	 * @param bottomRightX
	 * @param bottomRightY
	 * @throws RemoteConnError
	 */

	public GarbageList getGarbagesInArea(String topLeftX, String topLeftY,
			String bottomRightX, String bottomRightY) throws RemoteConnError {

		String query = "?topLeftX=" + topLeftX + "&topLeftY=" + topLeftY
				+ "&bottomRightX=" + bottomRightX + "&bottomRightY="
				+ bottomRightY + "&maxResults="+Utils.MAX_RESULTS;
		HttpGet request = new HttpGet(ConnectionUtils.getWsAddress(
				ConnectionUtils.ACTION_GET_GARBAGE_IN_AREA, query));
		HttpResponse response = null;

		try {
			response = ConnectionUtils.getHttpConn(null, null).execute(
					request);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != 200) {
				LLog.d("Get garbages in Area Failed. Status = "
						+ response.getStatusLine().toString());
				throw new RemoteConnError(statusCode);
			}
			String content = Connection.convertStreamToString(response
					.getEntity().getContent());
			LLog.d(content);

			GarbageList garbageList = new GarbageList();

			ObjectMapper mapper = new ObjectMapper();
			mapper.getDeserializationConfig().set(DeserializationConfig.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
			garbageList = mapper.readValue(content, GarbageList.class);

			return garbageList;
		} catch (RemoteConnError rce) {
			throw rce;
		} catch (Exception ex) {
			LLog.e(ex);
			throw new RemoteConnError(-1);
		}
	}

	public int addGarbage(User user, Garbage garbage) throws RemoteConnError {
		HttpPost request = new HttpPost(
				ConnectionUtils
						.getWsAddress(ConnectionUtils.ACTION_ADD_GARBAGE));
		HttpResponse response = null;
		try {
			// set content description and content
			request.setHeader("Content-Type", "application/json");
			// serialize with Jackson
			ObjectMapper mapper = new ObjectMapper();
			String garbageJsonString = mapper.writeValueAsString(garbage);
					
			/*JsonSerializer serializer = new JsonSerializer();
			String garbageJsonString = serializer.serialize(garbage);
			*/
			
			LLog.d("garbageJsonString: " + garbageJsonString);
			request.setEntity(new StringEntity(garbageJsonString, "ASCII"));

			
			response = ConnectionUtils.getHttpConn(user.getEmail(),
					user.getPasswd()).execute(request);

			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			HttpEntity entity = response.getEntity();
			String message = null;
			if (statusCode != STATUS_CODE_OK) {
				// requestul nu a functionat
				String mesaj = status.toString();
				LLog.d("error: " + mesaj);
				throw new RemoteConnError(statusCode);

			} else if (entity != null) {
				InputStream input = entity.getContent();
				message = Connection.convertStreamToString(input);
				input.close();
				LLog.d("Garbage uploaded! The remote DB id of this garbage is: "
						+ message);
				return Integer.parseInt(message);
			}

		} catch (RemoteConnError rce) {
			throw rce;
		} catch (Exception ex) {
			LLog.e(ex);
			throw new RemoteConnError(-1);
		}
		return Garbage.NO_DB_ID;
	}

	public void assignImageToGarbage(User user, long garbageId,
			String picturePath) throws RemoteConnError {

		HttpPost request = new HttpPost(ConnectionUtils.getWsAddress(
				ConnectionUtils.ACTION_ASSIGN_IMAGE, garbageId));
		HttpResponse response = null;

		try {
			File imageFile = new File(picturePath);
			if (!imageFile.exists()) {
				// TODO - find a better way to deal with this exception
				throw new Exception(imageFile.getAbsolutePath()
						+ " does not exist, skipping test!");
			}

			request.setHeader("Content-Type", "multipart/form-data");
			request.setEntity(new FileEntity(imageFile, "multipart/form-data"));

			response = ConnectionUtils.getHttpConn(user.getEmail(),
					user.getPasswd()).execute(request);
			StatusLine status = response.getStatusLine();
			int statusCode = status.getStatusCode();
			if (statusCode != STATUS_CODE_OK) {
				throw new RemoteConnError(statusCode);
			}
		} catch (RemoteConnError rce) {
			throw rce;
		} catch (Exception ex) {
			LLog.e(ex);
			throw new RemoteConnError(-1);
		}
	}

	public void setGarbageStatus(long garbageId, GarbageStatus status)
			throws RemoteConnError {
		// TODO Auto-generated method stub

	}

}
