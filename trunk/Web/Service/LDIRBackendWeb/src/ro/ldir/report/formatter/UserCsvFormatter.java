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
 *  Filename: UserCsvFormatter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.report.formatter;

import java.util.List;

import ro.ldir.dto.User;

/** Converts a list of users to a comma separated text file (CSV). */
public class UserCsvFormatter {
	private List<User> users;

	public UserCsvFormatter(List<User> users) {
		this.users = users;
	}

	protected String convert() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"Prenume\",");
		buf.append("\"Nume\",");
		buf.append("\"Email\",");
		buf.append("\"Telefon\",");
		buf.append("\"Rol\",");
		buf.append("\"Județ\",");
		buf.append("\"Data înregistrării\",");
		buf.append("\"ID\",");
		buf.append("\"Nr. mormane\",");
		buf.append("\"Nr. zone\"");
		buf.append("\"Activitate\"\n");

		for (int i = 0; i < users.size(); i++) {
			User user = users.get(i);
			buf.append("\"" + user.getFirstName() + "\",");
			buf.append("\"" + user.getLastName() + "\",");
			buf.append("\"" + user.getEmail() + "\",");
			buf.append("\"" + user.getPhone() + "\",");
			buf.append("\"" + user.getRole() + "\",");
			buf.append("\"" + user.getCounty() + "\",");
			buf.append("\"" + user.getRecordDate() + "\",");
			buf.append(user.getUserId() + ",");
			if (user.getGarbages() == null)
				buf.append("0,");
			else
				buf.append(user.getGarbages().size() + ",");
			if (user.getMemberOf() == null
					|| user.getMemberOf().getChartedAreas() == null)
				buf.append("0");
			else
				buf.append(user.getMemberOf().getChartedAreas().size());
			buf.append("\"");
			StringBuffer ab = new StringBuffer();
			List<User.Activity> activities = user.getActivities();
			if (activities != null && activities.size() > 0) {
				for (User.Activity activity : activities)
					ab.append(activity.getReportName() + ", ");
				buf.append(ab.substring(0, ab.length() - 2));
			}
			buf.append("\"");
			buf.append("\n");
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
