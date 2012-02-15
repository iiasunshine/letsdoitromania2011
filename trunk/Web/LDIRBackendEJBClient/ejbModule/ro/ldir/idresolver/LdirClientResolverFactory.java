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
 *  Filename: LdirClientResolverFactory.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.idresolver;

import javax.xml.bind.JAXBException;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

/**
 * A factory class to creates Jersey clients which solves one-level
 * indirections.
 * 
 * When a client fetches an object references another object by ID, it
 * automatically places calls to fetch the references objects. Level two
 * references (i.e., other objects referenced by the referenced object) are not
 * fetched.
 */
public class LdirClientResolverFactory {
	/**
	 * Creates a client instance to make calls on the web service.
	 * 
	 * All calls made through this client:
	 * <ul>
	 * <li>are automatically authenticated.
	 * <li>resolve one-level indirections, i.e., they fetch objects directly
	 * referenced by all returned objects.
	 * </ul>
	 * 
	 * @param url
	 *            The url were the LDIR web service is located.
	 * @param user
	 *            The username to conenct to the web service.
	 * @param pass
	 *            The password to connect to the web service.
	 * @return A client instance to make calls on the web service.
	 * @throws JAXBException
	 */
	public static Client getResolverClient(String url, String user, String pass)
			throws JAXBException {
		ClientConfig cc = new DefaultClientConfig();
		cc.getSingletons().add(new LdirContextResolver(url, user, pass));
		Client client = Client.create(cc);
		client.addFilter(new HTTPBasicAuthFilter(user, pass));
		return client;
	}
}
