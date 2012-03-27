package ro.ldir.android.remote;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import ro.ldir.android.entities.Garbage;
import ro.ldir.android.entities.GarbageList;
import ro.ldir.android.entities.User;
import ro.ldir.android.util.LLog;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

/**
 *  This file is part of the LDIRAndroid - the Android client for the Let's Do It
 *  Romania 2011 Garbage collection campaign.
 *  Copyright (C) 2011 by the LDIR development team, further referred to 
 *  as "authors".
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 *  Filename: JerseyBackend.java
 *  Author(s): Catalin Mincu, cata.mincu@gmail.com
 *
 */

public class JerseyBackend implements IBackend {

	public User signIn(String user, String password) throws RemoteConnError {
		// TODO Auto-generated method stub
		return null;
	}

	public void signOut() {
		// TODO Auto-generated method stub

	}

	public GarbageList getGarbagesInArea(String topLeftX, String topLeftY,
			String bottomRightX, String bottomRightY, String username,
			String password) throws RemoteConnError {

		List<Garbage> garbageList = new LinkedList<Garbage>();

		String query = "?topLeftX=" + topLeftX + "&topLeftY=" + topLeftY
				+ "&bottomRightX=" + bottomRightX + "&bottomRightY="
				+ bottomRightY;
		String location = ConnectionUtils.getWsAddress(
				ConnectionUtils.ACTION_GET_GARBAGE_IN_AREA, query);

		Client client = Client.create();
		
		WebResource resource = client.resource(location);
		
		ClientResponse cr = resource.entity(garbageList,
				MediaType.APPLICATION_JSON).get(ClientResponse.class);

		int statusCode = cr.getStatus();
		if (statusCode != 200) {
			LLog.d("Loging failed. Status = "
					+ cr.getClientResponseStatus().toString());
			throw new RemoteConnError(statusCode);
		}
		return null;
	}

	public int addGarbage(User user, Garbage garbage) throws RemoteConnError {
		// TODO Auto-generated method stub
		return 0;
	}

	public void assignImageToGarbage(User user, long garbageId,
			String picturePath) throws RemoteConnError {
		// TODO Auto-generated method stub

	}

	public void setGarbageStatus(long garbageId, GarbageStatus status)
			throws RemoteConnError {
		// TODO Auto-generated method stub

	}

}
