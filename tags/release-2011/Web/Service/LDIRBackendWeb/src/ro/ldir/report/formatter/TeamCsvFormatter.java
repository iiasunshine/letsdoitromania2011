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

import java.util.ArrayList;
import java.util.List;

import ro.ldir.dto.CleaningEquipment;
import ro.ldir.dto.CleaningEquipment.CleaningType;
import ro.ldir.dto.Equipment;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.GarbageEnrollment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;

/** Converts a list of teams to a comma separated text file (CSV). */
public class TeamCsvFormatter {
	private static int getCleaningType(Team team,
			CleaningEquipment.CleaningType cleaningType) {
		int count = 0;
		for (Equipment eq : team.getEquipments()) {
			if (!(eq instanceof CleaningEquipment))
				continue;
			CleaningEquipment ce = (CleaningEquipment) eq;
			if (!ce.getCleaningType().equals(cleaningType))
				continue;
			count += ce.getCount();
		}
		return count;
	}

	private static void organization(StringBuffer buf, Organization org) {
		buf.append("\"" + org.getName() + "\",");
		buf.append("\"" + org.getTown() + "\",");
		buf.append("\"" + org.getCounty() + "\",");
		buf.append("\"" + org.getType() + "\",");
		buf.append("\"" + org.getContactLastname() + "\",");
		buf.append("\"" + org.getContactEmail() + "\",");
		buf.append(org.getMembersCount() + ",");
	}

	private static void organizationVoid(StringBuffer buf) {
		buf.append(",");
		buf.append(",");
		buf.append(",");
		buf.append(",");
		buf.append(",");
		buf.append(",");
		buf.append(",");
	}

	private static void teamFooter(StringBuffer buf, Team team) {
		if (team.getEquipments() == null)
			buf.append("0,,,,,");
		else {
			int gpsCount = 0;
			for (Equipment eq : team.getEquipments()) {
				if (!(eq instanceof GpsEquipment))
					continue;
				gpsCount += ((GpsEquipment) eq).getCount();
			}
			buf.append(gpsCount + ",");

			List<TransportEquipment> transports = new ArrayList<TransportEquipment>();
			for (Equipment eq : team.getEquipments()) {
				if (!(eq instanceof TransportEquipment))
					continue;
				transports.add((TransportEquipment) eq);
			}
			if (transports.size() == 0)
				buf.append(",");
			else {
				buf.append("\"");
				for (int i = 0; i < transports.size() - 1; i++)
					buf.append(transports.get(i).getTransportType() + ", ");
				buf.append(transports.get(transports.size() - 1)
						.getTransportType() + "\",");
			}

			buf.append(getCleaningType(team, CleaningType.BAGS) + ",");
			buf.append(getCleaningType(team, CleaningType.GLOVES) + ",");
			buf.append(getCleaningType(team, CleaningType.SHOVEL) + ",");
		}
		buf.append("\"TBD\",");

		if (team.getGarbageEnrollements() == null
				|| team.getGarbageEnrollements().size() == 0)
			buf.append("0,,0\n");
		else {
			buf.append(team.getGarbageEnrollements().size() + ",");
			buf.append("\"");

			int volume = 0, i = 0;
			Garbage leftover = null;
			for (GarbageEnrollment enrollment : team.getGarbageEnrollements()) {
				if (i == team.getGarbageEnrollements().size() - 1) {
					leftover = enrollment.getGarbage();
					break;
				}
				buf.append(enrollment.getGarbage().getGarbageId() + ", ");
				volume += enrollment.getGarbage().getBagCount();
				i++;
			}
			if (leftover != null) {
				buf.append(leftover.getGarbageId() + "\",");
				volume += leftover.getBagCount();
			}
			buf.append(volume + "\n");
		}
	}

	private static void teamHeader(StringBuffer buf, Team team) {
		buf.append(team.getTeamId() + ",");
		if (team.getTeamManager() != null) {
			buf.append("\"" + team.getTeamManager().getEmail() + "\",");
			buf.append("\"" + team.getTeamManager().getLastName() + "\",");
			buf.append("\"" + team.getTeamManager().getTown() + "\",");
			buf.append("\"" + team.getTeamManager().getCounty() + "\",");
		} else
			buf.append(",,,,");
		buf.append(team.getTeamId() + ",");
		buf.append("\"" + team.getTeamName() + "\",");
		buf.append(team.getVolunteerMembers().size() + ",");
		if (team.getTeamManager() != null)
			if (team.getTeamManager().isMultiRole())
				buf.append("\"DA\",");
			else
				buf.append("\"NU\",");
		else
			buf.append(",");
	}

	private List<Team> teams;

	public TeamCsvFormatter(List<Team> teams) {
		this.teams = teams;
	}

	protected String convert() {
		StringBuffer buf = new StringBuffer();
		buf.append("\"ID\",");
		buf.append("\"Email manager\",");
		buf.append("\"Nume manager\",");
		buf.append("\"Comună manager\",");
		buf.append("\"Județ manager\",");
		buf.append("\"Cod acces\",");
		buf.append("\"Nume\",");
		buf.append("\"Număr membri\",");
		buf.append("\"Multi-manager\",");
		buf.append("\"Nume organizație\",");
		buf.append("\"Comună organizație\",");
		buf.append("\"Județ organizație\",");
		buf.append("\"Tip organizație\",");
		buf.append("\"Nume persoană de contact\",");
		buf.append("\"Email persoană de contact\",");
		buf.append("\"Număr membri organizație\",");
		buf.append("\"Număr GPS\",");
		buf.append("\"Transport\",");
		buf.append("\"Număr saci\",");
		buf.append("\"Număr mănuși\",");
		buf.append("\"Număr lopeți\",");
		buf.append("\"Utilaje\",");
		buf.append("\"Număr mormane alocate\",");
		buf.append("\"Listă mormane alocate\",");
		buf.append("\"Sumă volum mormane alocate\"\n");

		for (Team team : teams) {
			if (team.getOrganizationMembers() == null
					|| team.getOrganizationMembers().size() == 0) {
				teamHeader(buf, team);
				organizationVoid(buf);
				teamFooter(buf, team);
				continue;
			}
			for (Organization org : team.getOrganizationMembers()) {
				teamHeader(buf, team);
				organization(buf, org);
				teamFooter(buf, team);
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
