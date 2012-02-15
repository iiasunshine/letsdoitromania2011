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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

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
import com.sun.xml.bind.IDResolver;

/**
 * A resolver to fetch one level referenced entities from the back-end web
 * service during while marshalling.
 */
class LdirResolver extends IDResolver {

	/**
	 * A collection mapping the objects this resolver fetches from the web
	 * service with the url suffix used to fetch objects.
	 */
	@SuppressWarnings("serial")
	private static final Map<Class<?>, String> ENTITIES_URL = Collections
			.unmodifiableMap(new HashMap<Class<?>, String>() {
				{
					put(ChartedArea.class, "ws/geo/chartedArea/");
					put(CountyArea.class, "ws/geo/countyArea/");
					put(Garbage.class, "ws/garbage/");
					put(Organization.class, "ws/organization/");
					put(Team.class, "ws/team/");
					put(TownArea.class, "ws/geo/townArea/");
					put(User.class, "ws/user/");
				}
			});

	/**
	 * Returns the set of classes managed by this resolver.
	 * 
	 * @return
	 */
	public static Set<Class<?>> managedTypes() {
		return ENTITIES_URL.keySet();
	}

	private Client client;
	private Map<Class<?>, Map<String, Object>> objects = new HashMap<Class<?>, Map<String, Object>>();
	private String url;

	/**
	 * Constructs a new ID resolver.
	 * 
	 * @param url
	 *            The url where the web service is located.
	 * @param client
	 *            The Jersey client instance to use during ID resolving.
	 */
	public LdirResolver(String url, Client client) {
		this.url = url;
		this.client = client;

		for (Class<?> type : ENTITIES_URL.keySet())
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
		objects.get(object.getClass()).put(id, object);
	}

	/**
	 * Fetches an object from the web service.
	 * 
	 * @param id
	 *            The ID of the object.
	 * @param type
	 *            The type of the object.
	 * @return An object instanced fetched from the web service.
	 */
	private Object fetch(String id, Class<?> type) {
		WebResource r = client.resource(url + ENTITIES_URL.get(type) + id);
		Builder b = r.accept(MediaType.APPLICATION_XML);
		ClientResponse cr = b.get(ClientResponse.class);
		if (cr.getStatus() != 200)
			throw new RuntimeException("Unable to fetch object type: " + type
					+ ", ID:" + id + " from url:" + r + ". Code is "
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

		if (!ENTITIES_URL.containsKey(type))
			return null;

		Object object = objects.get(type).get(id);
		if (object == null)
			object = fetch(id, type);

		final Object result = object;
		return new Callable() {
			public Object call() {
				return result;
			}
		};
	}
}
