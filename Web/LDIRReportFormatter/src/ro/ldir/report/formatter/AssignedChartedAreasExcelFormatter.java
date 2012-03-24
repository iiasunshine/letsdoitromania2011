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
 *  Filename: AssignedChartedAreasExcelFormatter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.report.formatter;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.Team;
import ro.ldir.dto.User;
import ro.ldir.dto.helper.AssignedChartedAreaFilter;

/** Converts a list of teams to an Excel workbook. */
public class AssignedChartedAreasExcelFormatter implements ExcelFormatter {
	private AssignedChartedAreaFilter filter;
	private List<Team> teams;

	public AssignedChartedAreasExcelFormatter(List<Team> teams,
			AssignedChartedAreaFilter filter) {
		this.teams = teams;
		this.filter = filter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ro.ldir.report.formatter.ExcelFormatter#convert(org.apache.poi.ss.usermodel
	 * .Workbook)
	 */
	@Override
	public final Workbook convert(Workbook wb) {
		Sheet sheet = wb.createSheet("Zone cartare");
		int i = 0;

		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("Zon\u04d1 cartare");
		row.createCell(1).setCellValue("Scor");
		row.createCell(2).setCellValue("Jude\u0163");
		row.createCell(3).setCellValue("ID manager");
		row.createCell(4).setCellValue("Email manager");
		row.createCell(5).setCellValue("Rol manager");
		row.createCell(6).setCellValue("Comun\u04d1 manager");
		row.createCell(7).setCellValue("Jude\u0163 manager");

		for (Team team : teams) {
			if (!filter.pass(team))
				continue;
			for (ChartedArea ca : team.getChartedAreas()) {
				if (!filter.pass(ca))
					continue;

				User manager = team.getTeamManager();

				i++;
				row = sheet.createRow(i);

				row.createCell(0).setCellValue(ca.getName());
				row.createCell(1, Cell.CELL_TYPE_NUMERIC).setCellValue(
						ca.getScore());
				row.createCell(2).setCellValue(ca.getCounty());
				row.createCell(3, Cell.CELL_TYPE_NUMERIC).setCellValue(
						manager.getUserId());
				row.createCell(4).setCellValue(manager.getEmail());
				row.createCell(5).setCellValue(manager.getRole());
				row.createCell(6).setCellValue(manager.getTown());
				row.createCell(7).setCellValue(manager.getCounty());
			}
		}

		return wb;
	}
}
