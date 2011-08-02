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
 *  Filename: LdirContextResolver.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.idresolver;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * A custom context resolver to use the LDIR ID resolver. This context resolver
 * supports only XML serializing.
 * 
 * @see LdirResolver
 */
@Provider
class LdirContextResolver implements ContextResolver<JAXBContext> {
	private JAXBContext ldirContext = null;

	/**
	 * Configures this ldirContext provider.
	 * 
	 * @param url
	 *            The URL where the web service is executing.
	 * @param user
	 *            The user to connect to the web service.
	 * @param pass
	 *            The password to connect to the web service.
	 * @throws JAXBException
	 */
	public LdirContextResolver(String url, String user, String pass)
			throws JAXBException {
		ldirContext = new LdirContext(url, user, pass);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ws.rs.ext.ContextResolver#getContext(java.lang.Class)
	 */
	public JAXBContext getContext(Class<?> type) {
		if (LdirContext.serves(type))
			return ldirContext;
		return null;
	}
}
