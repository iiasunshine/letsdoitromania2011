/**
 *  This file is part of the LDIRBackend - the backend for the Let's Do It
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
 *  Filename: LdirResolver.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.idresolver;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.xml.sax.SAXException;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.CountyArea;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.TownArea;
import ro.ldir.dto.User;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.WebResource.Builder;
import com.sun.jersey.core.util.Base64;
import com.sun.xml.bind.IDResolver;

/**
 * A resolver to fetch entities from the backend WS during the marshalling
 * process.
 */
class LdirResolver extends IDResolver {

	/**
	 * A collection mapping the objects this resolver fetches from the web
	 * service with the URL suffix used to fetch objects.
	 */
	@SuppressWarnings("serial")
	private static final Map<String, String> MANAGED_ENTITIES = Collections
			.unmodifiableMap(new HashMap<String, String>() {
				{
					put(ChartedArea.class.getName(), "ws/geo/chartedArea/");
					put(CountyArea.class.getName(), "ws/geo/countyArea/");
					put(Garbage.class.getName(), "ws/garbage/");
					put(Organization.class.getName(), "ws/organization/");
					put(Team.class.getName(), "ws/team/");
					put(TownArea.class.getName(), "ws/geo/townArea/");
					put(User.class.getName(), "ws/user/");
				}
			});

	/** A list of served classes. */
	@SuppressWarnings("rawtypes")
	protected static final Class[] SERVED_CLASSES = { ChartedArea.class,
			CountyArea.class, Garbage.class, Organization.class, Team.class,
			TownArea.class, User.class };

	private Map<String, Map<String, Object>> objects = new HashMap<String, Map<String, Object>>();
	private String pass;
	private String URL;

	private String user;

	/**
	 * Constructs a new ID resolver.
	 * 
	 * @param URL
	 *            The URL where the web service is located.
	 * @param user
	 *            The user to connect to the WS.
	 * @param pass
	 *            The password.
	 */
	public LdirResolver(String URL, String username, String password) {
		this.URL = URL;
		this.user = username;
		this.pass = password;

		for (String type : MANAGED_ENTITIES.keySet())
			objects.put(type, new HashMap<String, Object>());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.xml.internal.bind.IDResolver#bind(java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void bind(String id, Object object) throws SAXException {
		objects.get(object.getClass().getName()).put(id, object);
	}

	/**
	 * Fetches an object from the web service.
	 * 
	 * @param id
	 * @param type
	 * @return
	 */
	private Object fetch(String id, Class<?> type) {
		Client c = Client.create();
		WebResource r = c.resource(URL + MANAGED_ENTITIES.get(type.getName())
				+ id);
		Builder b = r.header(
				HttpHeaders.AUTHORIZATION,
				"Basic "
						+ new String(Base64.encode(user + ":" + pass), Charset
								.forName("ASCII"))).accept(
				MediaType.APPLICATION_XML);
		ClientResponse cr = b.get(ClientResponse.class);
		if (cr.getStatus() != 200)
			throw new RuntimeException("Unable to fetch object type: " + type
					+ ", ID:" + id + " from URL:" + r + ". Code is "
					+ cr.getStatus());

		Object result = cr.getEntity(type);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sun.xml.internal.bind.IDResolver#resolve(java.lang.String,
	 * java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Callable<?> resolve(String id, Class type) throws SAXException {
		if (!serves(type))
			return null;
		Object existing = objects.get(type.getName()).get(id);

		if (existing == null)
			existing = fetch(id, type);

		final Object result = existing;
		return new Callable() {
			public Object call() {
				return result;
			}
		};
	}

	/**
	 * @param type
	 * @return
	 */
	public static boolean serves(Class<?> type) {
		for (Class<?> c : LdirResolver.SERVED_CLASSES)
			if (c.equals(type))
				return true;

		return false;
	}
}
