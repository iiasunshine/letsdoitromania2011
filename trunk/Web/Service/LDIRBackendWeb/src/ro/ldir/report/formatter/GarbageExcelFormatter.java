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

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import ro.ldir.dto.Garbage;

/** Converts a list of garbage to an Excel workbook. */
public abstract class GarbageExcelFormatter {
	private List<Garbage> garbages;

	public GarbageExcelFormatter(List<Garbage> garbages) {
		this.garbages = garbages;
	}

	protected final Workbook convert(Workbook wb) {
		Sheet sheet = wb.createSheet("Mormane gunoi");

		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Jude\u0163");
		row.createCell(2).setCellValue("Comun\u04d1");
		row.createCell(3).setCellValue("Descriere");
		row.createCell(4).setCellValue("Stare");
		row.createCell(5).setCellValue("Dispersat");
		row.createCell(6).setCellValue("Voluminos");
		row.createCell(7).setCellValue("Num\u04d1r saci");
		row.createCell(8).setCellValue("Longitudine");
		row.createCell(9).setCellValue("Latitudine");

		for (int i = 0; i < garbages.size(); i++) {
			row = sheet.createRow(i + 1);
			Garbage garbage = garbages.get(i);

			row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getGarbageId());
			row.createCell(1).setCellValue(garbage.getCounty().getName());
			if (garbage.getTown() != null)
				row.createCell(2).setCellValue(garbage.getTown().getName());
			if (garbage.getDescription() != null) {
				int len = garbage.getDescription().length();
				if (len > Garbage.DESCRIPTION_LENGTH)
					len = Garbage.DESCRIPTION_LENGTH;
				row.createCell(3).setCellValue(
						garbage.getDescription().substring(0, len)
								.replaceAll("\\r\\n|\\r|\\n", " "));
			}
			if (garbage.getStatus() != null)
				row.createCell(4).setCellValue(
						garbage.getStatus().getTranslation());
			row.createCell(5, Cell.CELL_TYPE_BOOLEAN).setCellValue(
					garbage.isDispersed());
			if (garbage.getDetails() != null) {
				int len = garbage.getDetails().length();
				if (len > Garbage.DETAILS_LENGTH)
					len = Garbage.DETAILS_LENGTH;
				row.createCell(6).setCellValue(
						garbage.getDetails().substring(0, len)
								.replaceAll("\\r\\n|\\r|\\n", " "));
			}
			row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getBagCount());
			row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getX());
			row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getY());
		}
		return wb;
	}

	/** Return the report in a binary form. */
	public abstract byte[] getBytes();
}
