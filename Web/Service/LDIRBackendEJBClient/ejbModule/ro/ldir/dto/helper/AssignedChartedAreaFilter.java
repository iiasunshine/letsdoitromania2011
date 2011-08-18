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
 *  Filename: AssignedChartedAreaFilter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.dto.helper;

import java.util.Set;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Team;

/** Filters output of a report containing assigned charted areas. */
public class AssignedChartedAreaFilter {
	private Set<String> chartedAreaNames;
	private Set<String> counties;
	private Set<Integer> userIds;

	/**
	 * Consturcts a new filter. Use {@code null} if you do not want one of the
	 * parameters to be enforced.
	 * 
	 * @param counties
	 *            A list of counties to test against.
	 * @param chartedAreaNames
	 *            A list of charted area names to test against.
	 * @param userIds
	 *            A list of user IDs to test the team managers against.
	 */
	public AssignedChartedAreaFilter(Set<String> counties,
			Set<String> chartedAreaNames, Set<Integer> userIds) {
		this.counties = counties;
		this.chartedAreaNames = chartedAreaNames;
		this.userIds = userIds;
	}

	public boolean pass(ChartedArea chartedArea) {
		if (counties != null && counties.size() > 0
				&& !counties.contains(chartedArea.getCounty()))
			return false;
		if (chartedAreaNames != null && chartedAreaNames.size() > 0
				&& !chartedAreaNames.contains(chartedArea.getName()))
			return false;
		return true;
	}

	public boolean pass(Team team) {
		if (userIds == null || userIds.size() == 0)
			return true;
		return userIds.contains(team.getTeamManager().getUserId());
	}
}
