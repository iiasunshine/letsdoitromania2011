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
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.CellStyle;

import ro.ldir.dto.Garbage;

/** Converts a list of garbage to an Excel workbook. */
public class GarbageExcelFormatter implements ExcelFormatter {
	private List<Garbage> garbages;

	public GarbageExcelFormatter(List<Garbage> garbages) {
		this.garbages = garbages;
	}

	public final Workbook convert(Workbook wb) {
		Sheet sheet = wb.createSheet("Lista Mormane gunoi");

		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("ID");
		row.createCell(1).setCellValue("Jude\u0163");
		row.createCell(2).setCellValue("Comun\u04d1");
		row.createCell(3).setCellValue("Latitudine");
		row.createCell(4).setCellValue("Longitudine");
		row.createCell(5).setCellValue("Dispersat");
		row.createCell(6).setCellValue("Num\u04d1r saci");
		row.createCell(7).setCellValue("Plastic");
		row.createCell(8).setCellValue("Metal");
		row.createCell(9).setCellValue("Sticl\u04d1");
		row.createCell(10).setCellValue("Nereciclabil");
		row.createCell(11).setCellValue("Greu de transportat");
		row.createCell(12).setCellValue("Descriere");
		row.createCell(13).setCellValue("Stare");
		row.createCell(14).setCellValue("Zon\u04d1 cartare");

		row.createCell(15).setCellValue("Numele mormanului");
		row.createCell(16).setCellValue("Raza");
		row.createCell(17).setCellValue("Numar de voturi");
		
		row.createCell(18).setCellValue("Data introducerii");

		for (int i = 0; i < garbages.size(); i++) {
			row = sheet.createRow(i + 1);
			Garbage garbage = garbages.get(i);

			row.createCell(0, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getGarbageId());
			row.createCell(1).setCellValue(garbage.getCounty().getName());
			if (garbage.getTown() != null)
				row.createCell(2).setCellValue(garbage.getTown().getName());
			row.createCell(3, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getY());
			row.createCell(4, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getX());
			row.createCell(5, Cell.CELL_TYPE_BOOLEAN).setCellValue(
					garbage.isDispersed());
			row.createCell(6, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getBagCount());
			row.createCell(7, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getPercentagePlastic());
			row.createCell(8, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getPercentageMetal());
			row.createCell(9, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getPercentageGlass());
			row.createCell(10, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getPercentageWaste());
			row.createCell(11).setCellValue(
					garbage.getBigComponentsDescription());

			if (garbage.getDescription() != null)
				row.createCell(12, Cell.CELL_TYPE_STRING).setCellValue(
						garbage.getDescription().replaceAll("\\r\\n|\\r|\\n",
								" "));

			if (garbage.getStatus() != null)
				row.createCell(13).setCellValue(
						garbage.getStatus().getTranslation());

			if (garbage.getChartedArea() != null)
				row.createCell(14).setCellValue(
						garbage.getChartedArea().getName());

			try {
				if (garbage.getName() != null)
					row.createCell(15).setCellValue(garbage.getName());
			} catch (Exception e) {
				// TODO: handle exception
			}
			try {
				row.createCell(16, Cell.CELL_TYPE_NUMERIC).setCellValue(
						garbage.getRadius());
			} catch (Exception e1) {
				// TODO: handle exception
			}
			try {
				if (garbage.getVotes() != null)
					row.createCell(17, Cell.CELL_TYPE_NUMERIC).setCellValue(
							garbage.getVoteCount());
			} catch (Exception ee) {

			}
			
			try {
				Cell cell;
				DataFormat df = wb.createDataFormat();
				CellStyle cs = wb.createCellStyle();
						  cs.setDataFormat(df.getFormat("dd-mm-yyyy"));
						  
				if (garbage.getRecordDate()!= null)
					{
						cell=row.createCell(18, Cell.CELL_TYPE_STRING);
						cell.setCellValue(garbage.getRecordDate());
						cell.setCellStyle(cs);
					}
			} catch (Exception ee) {

			}

		}
		return wb;
	}
}
