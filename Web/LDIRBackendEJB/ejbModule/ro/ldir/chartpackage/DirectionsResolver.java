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
 *  Filename: DirectionsResolver.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.chartpackage;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.xml.sax.InputSource;

/**
 * Resolves the directions from a city to given coordinates.
 */
public class DirectionsResolver {
	private static final String BOTTOMRIGHTX_PATH = "/DirectionsResponse/route/bounds/southwest/lat/text()";
	private static final String BOTTOMRIGHTY_PATH = "/DirectionsResponse/route/bounds/northeast/lng/text()";
	private static final String OVERVIEW_PATH = "/DirectionsResponse/route/overview_polyline/points/text()";
	private static final String TOPLEFTX_PATH = "/DirectionsResponse/route/bounds/southwest/lng/text()";
	private static final String TOPLEFTY_PATH = "/DirectionsResponse/route/bounds/northeast/lat/text()";
	private static final String WS_URL = "http://maps.googleapis.com/maps/api/directions/xml?sensor=false";

	private InputSource xmlInput;
	private XPath xpath;

	public DirectionsResolver(String origin, double destX, double destY)
			throws IOException {
		URL url = new URL(WS_URL + "&origin=" + origin + "&destination="
				+ destY + "," + destX);
		URLConnection conn = url.openConnection();
		xmlInput = new InputSource(conn.getInputStream());
		xpath = XPathFactory.newInstance().newXPath();
	}

	public double getBottomRightX() throws NumberFormatException,
			XPathExpressionException {
		return new Double(xpath.evaluate(BOTTOMRIGHTX_PATH, xmlInput))
				.doubleValue();
	}

	public double getBottomRightY() throws NumberFormatException,
			XPathExpressionException {
		return new Double(xpath.evaluate(BOTTOMRIGHTY_PATH, xmlInput))
				.doubleValue();
	}

	public String getOverview() throws XPathExpressionException {
		return xpath.evaluate(OVERVIEW_PATH, xmlInput);
	}

	public double getTopLeftX() throws NumberFormatException,
			XPathExpressionException {
		return new Double(xpath.evaluate(TOPLEFTX_PATH, xmlInput))
				.doubleValue();
	}

	public double getTopLeftY() throws NumberFormatException,
			XPathExpressionException {
		return new Double(xpath.evaluate(TOPLEFTY_PATH, xmlInput))
				.doubleValue();
	}
}
