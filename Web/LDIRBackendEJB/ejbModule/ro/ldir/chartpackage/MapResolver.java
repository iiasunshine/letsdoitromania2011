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
 *  Filename: MapResolver.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.chartpackage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.xpath.XPathExpressionException;

/**
 * Builds a map containing the directions from a city to a set of given
 * coordinates.
 */
public class MapResolver {
	private DirectionsResolver directions;
	private String origin;
	private double destX;
	private double destY;
	private static final int WIDTH = 640;
	private static final int HEIGHT = 480;
	private static final String WS_URL = "http://maps.googleapis.com/maps/api/staticmap?sensor=false&size="
			+ WIDTH + "x" + HEIGHT;

	public InputStream getInputStream() throws XPathExpressionException,
			IOException {
		URL url = new URL(WS_URL + "&markers=color:blue|" + origin + "|"
				+ destY + "," + destX + "&path=color:blue|enc:"
				+ directions.getOverview());
		URLConnection conn = url.openConnection();
		return conn.getInputStream();
	}

	public MapResolver(String origin, double destX, double destY)
			throws IOException {
		directions = new DirectionsResolver(origin, destX, destY);
		this.origin = origin;
		this.destX = destX;
		this.destY = destY;
	}
}
