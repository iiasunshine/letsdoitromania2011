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
 *  Filename: AssignedChartedAreasCsvFormatter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.report.formatter;

import java.util.List;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.ldir.dto.helper.AssignedChartedAreaFilter;

/** Converts a list of teams to a comma separated text file (CSV). */
public class AssignedChartedAreasCsvFormatter {
	private AssignedChartedAreaFilter filter;
	private List<Team> teams;

	public AssignedChartedAreasCsvFormatter(List<Team> teams,
			AssignedChartedAreaFilter filter) {
		this.teams = teams;
		this.filter = filter;
	}

	protected String convert() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"Zonă cartare\",");
		buf.append("\"Scor\",");
		buf.append("\"Județ\",");
		buf.append("\"ID manager\",");
		buf.append("\"Email manager\",");
		buf.append("\"Rol manager\",");
		buf.append("\"Comună manager\",");
		buf.append("\"Județ manager\"\n");

		for (Team team : teams) {
			if (!filter.pass(team))
				continue;
			for (ChartedArea ca : team.getChartedAreas()) {
				if (!filter.pass(ca))
					continue;
				User manager = team.getTeamManager();
				buf.append("\"" + ca.getName() + "\",");
				buf.append(ca.getScore() + ",");
				buf.append("\"" + ca.getCounty() + "\",");
				buf.append(manager.getUserId() + ",");
				buf.append("\"" + manager.getEmail() + "\",");
				buf.append("\"" + manager.getRole() + "\",");
				buf.append("\"" + manager.getTown() + "\",");
				buf.append("\"" + manager.getCounty() + "\"\n");
			}
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
