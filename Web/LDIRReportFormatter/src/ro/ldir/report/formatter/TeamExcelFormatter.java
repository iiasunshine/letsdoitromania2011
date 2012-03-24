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
 *  Filename: UserExcelFormatter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.report.formatter;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import ro.ldir.dto.CleaningEquipment;
import ro.ldir.dto.CleaningEquipment.CleaningType;
import ro.ldir.dto.Equipment;
import ro.ldir.dto.Garbage;
import ro.ldir.dto.GarbageEnrollment;
import ro.ldir.dto.GpsEquipment;
import ro.ldir.dto.Organization;
import ro.ldir.dto.Team;
import ro.ldir.dto.TransportEquipment;

/** Converts a list of teams to an Excel workbook. */
public class TeamExcelFormatter implements ExcelFormatter {

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

	private static void organization(Row row, Organization org) {
		row.createCell(9).setCellValue(org.getName());
		row.createCell(10).setCellValue(org.getTown());
		row.createCell(11).setCellValue(org.getCounty());
		if (org.getType() != null)
			row.createCell(12).setCellValue(org.getType().toString());
		row.createCell(13).setCellValue(org.getContactLastname());
		row.createCell(14).setCellValue(org.getContactEmail());
		row.createCell(15, Cell.CELL_TYPE_NUMERIC).setCellValue(
				org.getMembersCount());
	}

	private static void teamFooter(Row row, Team team) {

		if (team.getEquipments() != null) {
			int gpsCount = 0;
			for (Equipment eq : team.getEquipments()) {
				if (!(eq instanceof GpsEquipment))
					continue;
				gpsCount += ((GpsEquipment) eq).getCount();
			}
			row.createCell(16, Cell.CELL_TYPE_NUMERIC).setCellValue(gpsCount);

			List<TransportEquipment> transports = new ArrayList<TransportEquipment>();
			for (Equipment eq : team.getEquipments()) {
				if (!(eq instanceof TransportEquipment))
					continue;
				transports.add((TransportEquipment) eq);
			}
			if (transports.size() != 0) {
				StringBuffer buf = new StringBuffer();
				for (int i = 0; i < transports.size() - 1; i++)
					buf.append(transports.get(i).getTransportType() + ", ");
				buf.append(transports.get(transports.size() - 1)
						.getTransportType());
				row.createCell(17).setCellValue(buf.toString());
			}

			row.createCell(18, Cell.CELL_TYPE_NUMERIC).setCellValue(
					getCleaningType(team, CleaningType.BAGS));
			row.createCell(19, Cell.CELL_TYPE_NUMERIC).setCellValue(
					getCleaningType(team, CleaningType.GLOVES));
			row.createCell(20, Cell.CELL_TYPE_NUMERIC).setCellValue(
					getCleaningType(team, CleaningType.SHOVEL));
		}

		row.createCell(21).setCellValue("TBD");

		if (team.getGarbageEnrollements() == null
				|| team.getGarbageEnrollements().size() == 0) {
			row.createCell(22, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
			row.createCell(24, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
		} else {
			row.createCell(22, Cell.CELL_TYPE_NUMERIC).setCellValue(
					team.getGarbageEnrollements().size());

			StringBuffer buf = new StringBuffer();
			int volume = 0, i = 0;
			Garbage leftover = null;
			for (GarbageEnrollment enrollment : team.getGarbageEnrollements()) {
				if (enrollment.getGarbage() == null)
					continue;
				if (i == team.getGarbageEnrollements().size() - 1) {
					leftover = enrollment.getGarbage();
					break;
				}

				buf.append(enrollment.getGarbage().getGarbageId() + ", ");
				volume += enrollment.getGarbage().getBagCount();
				i++;

			}

			if (leftover != null) {
				buf.append(leftover.getGarbageId() + "");
				volume += leftover.getBagCount();
			}
			row.createCell(23).setCellValue(buf.toString());
			row.createCell(24, Cell.CELL_TYPE_NUMERIC).setCellValue(volume);

		}
	}

	private static void teamHeader(Row row, Team team) {
		row.createCell(0, Cell.CELL_TYPE_NUMERIC)
				.setCellValue(team.getTeamId());
		if (team.getTeamManager() != null) {
			row.createCell(1).setCellValue(team.getTeamManager().getEmail());
			row.createCell(2).setCellValue(team.getTeamManager().getLastName());
			row.createCell(3).setCellValue(team.getTeamManager().getTown());
			row.createCell(4).setCellValue(team.getTeamManager().getCounty());
		}
		row.createCell(5, Cell.CELL_TYPE_NUMERIC)
				.setCellValue(team.getTeamId());
		row.createCell(6).setCellValue(team.getTeamName());
		row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(
				team.getVolunteerMembers().size());
		if (team.getTeamManager() != null)
			row.createCell(8, Cell.CELL_TYPE_BOOLEAN).setCellValue(
					team.getTeamManager().isMultiRole());
	}

	private List<Team> teams;

	public TeamExcelFormatter(List<Team> teams) {
		this.teams = teams;
	}

	public final Workbook convert(Workbook wb) {
		Sheet sheet = wb.createSheet("Liste echipe");

		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Email manager");
		row.createCell(2).setCellValue("Nume manager");
		row.createCell(3).setCellValue("Comun\u04d1 manager");
		row.createCell(4).setCellValue("Jude\u0163 manager");
		row.createCell(5).setCellValue("Cod acces");
		row.createCell(6).setCellValue("Nume");
		row.createCell(7).setCellValue("Num\u04d1r membri");
		row.createCell(8).setCellValue("Multi-manager");
		row.createCell(9).setCellValue("Nume organiza\u0163ie");
		row.createCell(10).setCellValue("Comun\u04d1 organiza\u0163ie");
		row.createCell(11).setCellValue("Jude\u0163 organiza\u0163ie");
		row.createCell(12).setCellValue("Tip organiza\u0163ie");
		row.createCell(13).setCellValue("Nume persoan\u04d1 de contact");
		row.createCell(14).setCellValue("Email persoan\u04d1 de contact");
		row.createCell(15).setCellValue("Num\u04d1r membri organiza\u0163ie");
		row.createCell(16).setCellValue("Num\u04d1r GPS");
		row.createCell(17).setCellValue("Transport");
		row.createCell(18).setCellValue("Num\u04d1r saci");
		row.createCell(19).setCellValue("Num\u04d1r m\u04d1nu\015Fi");
		row.createCell(20).setCellValue("Num\u04d1r lope\u0163i");
		row.createCell(21).setCellValue("Utilaje");
		row.createCell(22).setCellValue("Num\u04d1r mormane alocate");
		row.createCell(23).setCellValue("List\u04d1 mormane alocate");
		row.createCell(24).setCellValue("Sum\u04d1 volum mormane alocate");

		int i = 0;
		for (Team team : teams) {
			if (team.getOrganizationMembers() == null
					|| team.getOrganizationMembers().size() == 0) {
				i++;
				row = sheet.createRow(i);
				teamHeader(row, team);
				teamFooter(row, team);
				continue;
			}
			for (Organization org : team.getOrganizationMembers()) {
				i++;
				row = sheet.createRow(i);
				teamHeader(row, team);
				organization(row, org);
				teamFooter(row, team);
			}
		}

		return wb;
	}
}
