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
 *  Filename: GarbageContextProvider.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.tests.garbage;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import ro.ldir.idresolver.LdirContext;

/**
 * A custom context provider to use the LDIR ID resolver. This context provider
 * supports only XML serializing.
 * 
 * @see LdirResolver
 */
@Provider
public class GarbageContextProvider implements ContextResolver<JAXBContext> {
	private JAXBContext context;

	public GarbageContextProvider() throws JAXBException {
		context = new LdirContext(GarbageTest.baseLocation, GarbageTest.USER,
				GarbageTest.USER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.ws.rs.ext.ContextResolver#getContext(java.lang.Class)
	 */
	public JAXBContext getContext(Class<?> type) {
		if (LdirContext.serves(type))
			return context;
		return null;
	}
}
