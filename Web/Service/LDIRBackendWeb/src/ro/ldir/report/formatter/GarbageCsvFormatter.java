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
 *  Filename: GarbageCsvFormatter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.report.formatter;

import java.util.List;

import ro.ldir.dto.Garbage;

/** Converts a list of garbages to a comma separated text file (CSV). */
public class GarbageCsvFormatter {
	private List<Garbage> garbages;

	public GarbageCsvFormatter(List<Garbage> garbages) {
		this.garbages = garbages;
	}

	protected String convert() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"ID\",");
		buf.append("\"Județ\",");
		buf.append("\"Descriere\",");
		buf.append("\"Stare\",");
		buf.append("\"Dispersat\",");
		buf.append("\"Voluminos\",");
		buf.append("\"Număr saci\",");
		buf.append("\"X\",");
		buf.append("\"Y\"\n");

		for (Garbage garbage : garbages) {
			buf.append(garbage.getGarbageId() + ",");
			buf.append("\"" + garbage.getCounty() + "\",");
			buf.append("\""
					+ garbage.getDescription().substring(0,
							Garbage.DESCRIPTION_LENGTH) + "\",");
			buf.append("\"" + garbage.getStatus().getTranslation() + "\",");
			buf.append("\"" + (garbage.isDispersed() ? "da" : "nu") + "\",");
			buf.append("\""
					+ garbage.getDetails().substring(0, Garbage.DETAILS_LENGTH)
					+ "\",");
			buf.append(garbage.getBagCount() + ",");
			buf.append(garbage.getX() + ",");
			buf.append(garbage.getY() + "\n");
		}
		return buf.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return convert();
	}
}
