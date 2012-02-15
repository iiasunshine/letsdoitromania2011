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
 *  Filename: TownInitializer.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.init;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import ro.ldir.dto.TownArea;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.Base64;

/**
 * Loads a list of towns in the backend.
 */
public class TownInitializer {
	public static final String FILENAME = "comune.txt";
	public static final String PASSWD = "dummy";
	public static final String URL = "http://localhost:8080/LDIRBackend/ws/geo/townArea";
	public static final String USER = "dummy@dummy.com";

	public static void main(String argv[]) {
		TownInitializer ci = new TownInitializer(FILENAME, URL, USER, PASSWD);
		try {
			ci.uploadTowns();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String user, passwd, url, filename;

	/**
	 * @param filename
	 * @param url
	 * @param user
	 * @param passwd
	 */
	public TownInitializer(String filename, String url, String user,
			String passwd) {
		this.filename = filename;
		this.url = url;
		this.user = user;
		this.passwd = passwd;
	}

	private void uploadTown(TownArea ca) {
		Client c = Client.create();
		WebResource wr = c.resource(url);
		ClientResponse cr = wr
				.header(HttpHeaders.AUTHORIZATION,
						"Basic "
								+ new String(
										Base64.encode(user + ":" + passwd),
										Charset.forName("ASCII")))
				.entity(ca, MediaType.APPLICATION_XML)
				.post(ClientResponse.class);
		assert (cr.getStatus() == 200);
	}

	public void uploadTowns() throws IOException {
		FileInputStream fis = new FileInputStream(filename);
		DataInputStream dis = new DataInputStream(fis);
		BufferedReader br = new BufferedReader(new InputStreamReader(dis));
		String line;
		TownArea townArea = null;

		while ((line = br.readLine()) != null) {
			if (line.length() == 0) {
				if (townArea != null) {
					uploadTown(townArea);
					System.out.println(townArea.getName() + " done");
					townArea = null;
				}
				continue;
			}

			if (Character.isLetter(line.charAt(0))) {
				StringTokenizer st = new StringTokenizer(line);
				int tokens = st.countTokens();
				int diff = 2;
				townArea = new TownArea();
				String county = st.nextToken();
				// TODO hack for MUNICIPIUL BUCURESTI and SATU MARE
				if (county.equals("MUNICIPIUL") || county.equals("SATU")) {
					townArea.setCounty(county + " " + st.nextToken());
					diff++;
				} else
					townArea.setCounty(county);
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < tokens - diff; i++) {
					if (i > 0)
						buf.append(" ");
					buf.append(st.nextToken());
				}
				townArea.setName(buf.toString());
				townArea.setArea(new Double(st.nextToken()));
				townArea.setPolyline(new ArrayList<Point2D.Double>());
				continue;
			}

			StringTokenizer st = new StringTokenizer(line);
			Double x = new Double(st.nextToken());
			Double y = new Double(st.nextToken());
			townArea.getPolyline().add(new Point2D.Double(x, y));
		}
	}
}
