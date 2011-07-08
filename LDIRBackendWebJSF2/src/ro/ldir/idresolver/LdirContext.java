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
 *  Filename: LdirContext.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.idresolver;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.Validator;

import com.sun.xml.bind.IDResolver;

/**
 * A custom context to use the LDIR ID resolver.
 */
@SuppressWarnings("deprecation")
public class LdirContext extends JAXBContext {
	/**
	 * Tests whether a given class is served by this context.
	 * 
	 * @param type
	 *            The class to test
	 * @return {@code true} if the class is served by this context.
	 */
	public static boolean serves(Class<?> type) {
		return LdirResolver.serves(type);
	}

	private JAXBContext context;
	private String pass;
	private String URL;

	private String user;

	/**
	 * Constructor of the context.
	 * 
	 * @param URL
	 *            The URL where the web service is located.
	 * @param user
	 *            The username to connect to the web service.
	 * @param pass
	 *            The password to connect to the web service.
	 * 
	 * @throws JAXBException
	 */
	public LdirContext(String URL, String user, String pass)
			throws JAXBException {
		context = JAXBContext.newInstance(LdirResolver.SERVED_CLASSES);
		this.URL = URL;
		this.user = user;
		this.pass = pass;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.bind.JAXBContext#createMarshaller()
	 */
	@Override
	public Marshaller createMarshaller() throws JAXBException {
		return context.createMarshaller();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.bind.JAXBContext#createUnmarshaller()
	 */
	@Override
	public Unmarshaller createUnmarshaller() throws JAXBException {
		Unmarshaller unmarshaller = context.createUnmarshaller();
		unmarshaller.setProperty(IDResolver.class.getName(), new LdirResolver(
				URL, user, pass));
		return unmarshaller;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.xml.bind.JAXBContext#createValidator()
	 */
	@Override
	public Validator createValidator() throws JAXBException {
		return context.createValidator();
	}

}
