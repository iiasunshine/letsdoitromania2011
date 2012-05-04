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

import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.CellStyle;


import ro.ldir.dto.User;
import ro.ldir.dto.Team;
import ro.ldir.report.formatter.TeamExcelFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


/** Converts a list of users to an Excel workbook. */
public class UserExcelFormatter implements ExcelFormatter {
	private List<User> users;
	private List<Team> teams;

	public UserExcelFormatter(List<User> users) {
		this.users = users;
	}

	
	public final Workbook convert(Workbook wb) {
		Sheet sheet = wb.createSheet("Utilizatori");
		CreationHelper createHelper = wb.getCreationHelper();
		teams = new ArrayList<Team>();
		
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("Prenume");
		row.createCell(1).setCellValue("Nume");
		row.createCell(2).setCellValue("Email");
		row.createCell(3).setCellValue("Telefon");
		row.createCell(4).setCellValue("Rol");
		row.createCell(5).setCellValue("Jude\u0163");
		row.createCell(6).setCellValue("Oras");
		row.createCell(7).setCellValue("Data \u00eenregistr\u04d1rii");
		row.createCell(8).setCellValue("ID");
		row.createCell(9).setCellValue("Nr. mormane");
		row.createCell(10).setCellValue("Nr. zone");
		row.createCell(11).setCellValue("Activitate");

		for (int i = 0; i < users.size(); i++) {
			row = sheet.createRow(i + 1);
			User user = users.get(i);
			if(user==null)continue;

			row.createCell(0).setCellValue(user.getFirstName());
			row.createCell(1).setCellValue(user.getLastName());
			row.createCell(2).setCellValue(user.getEmail());
			row.createCell(3).setCellValue(user.getPhone());
			row.createCell(4).setCellValue(user.getRole());
			row.createCell(5).setCellValue(user.getCounty());
			row.createCell(6).setCellValue(user.getTown());

			if (user.getRecordDate() != null) {
				CellStyle cellStyle = wb.createCellStyle();
				cellStyle.setDataFormat(createHelper.createDataFormat()
						.getFormat("m/d/yy h:mm"));
				Cell dateCell = row.createCell(7);
				dateCell.setCellValue(user.getRecordDate());
				dateCell.setCellStyle(cellStyle);
			}

			row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(
					user.getUserId());

			if (user.getGarbages() == null)
				row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
			else
				row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(
						user.getGarbages().size());
			
			if (user.getMemberOf() == null
					|| user.getMemberOf().getChartedAreas() == null)
				row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(0);
			else
				row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(
						user.getMemberOf().getChartedAreas().size());

			StringBuffer ab = new StringBuffer();
			List<User.Activity> activities = user.getActivities();
			if (activities != null && activities.size() > 0) 
			{
				for (User.Activity activity : activities)
				
					if(activity!=null)
					if(activity.getReportName()!=null)
					 ab.append(activity.getReportName() + ", ");
					else ab.append("  " + ", ");
				
				if(ab.length()>1)
				row.createCell(11).setCellValue(
						ab.substring(0, ab.length() - 2));
				else 
					row.createCell(11).setCellValue(" ");

			}
			
						
			List<Team> managedTeams = user.getManagedTeams();
			if (managedTeams != null && managedTeams.size() > 0) {
				for (Team team : managedTeams)
					teams.add(team);
			}
			
			
			        
			
		}
		
		TeamExcelFormatter teamWb = new TeamExcelFormatter(teams);
		wb = teamWb.convert(wb);
				
		return wb;
	}
}
