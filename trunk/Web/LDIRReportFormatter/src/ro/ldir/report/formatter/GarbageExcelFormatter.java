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
		int k=0;
		row.createCell(k).setCellValue("ID");k++;
		row.createCell(k).setCellValue("Jude\u0163");k++;
		row.createCell(k).setCellValue("Comun\u04d1");k++;
		row.createCell(k).setCellValue("Latitudine");k++;
		row.createCell(k).setCellValue("Longitudine");k++;
		row.createCell(k).setCellValue("Precizie GPS (metri)");k++;
		row.createCell(k).setCellValue("Dispersat");k++;
		row.createCell(k).setCellValue("Num\u04d1r saci");k++;
		
		row.createCell(k).setCellValue("Marime TrashOut (1=mic;2=medium;3=mare)");k++;
		row.createCell(k).setCellValue("Compozitie TrashOut");k++;
		
		row.createCell(k).setCellValue("Plastic");k++;
		row.createCell(k).setCellValue("Metal");k++;
		row.createCell(k).setCellValue("Sticl\u04d1");k++;
		row.createCell(k).setCellValue("Nereciclabil");k++;
		row.createCell(k).setCellValue("Greu de transportat");k++;
		row.createCell(k).setCellValue("Descriere");k++;
		row.createCell(k).setCellValue("Stare");k++;
		row.createCell(k).setCellValue("Zon\u04d1 cartare");k++;

		row.createCell(k).setCellValue("Numele mormanului");k++;
		row.createCell(k).setCellValue("Raza");k++;
		row.createCell(k).setCellValue("Numar de voturi");k++;
		
		row.createCell(k).setCellValue("Data introducerii");k++;
		row.createCell(k).setCellValue("Nominalizat pentru Votare");k++;
		row.createCell(k).setCellValue("Nominalizat pentru Curatare");k++;
		
		
		
		for (int i = 0; i < garbages.size(); i++) {
			
			k=0;
			row = sheet.createRow(i + 1);
			Garbage garbage = garbages.get(i);

			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getGarbageId());k++;
			row.createCell(k).setCellValue(garbage.getCounty().getName());k++;
			if (garbage.getTown() != null)
				row.createCell(k).setCellValue(garbage.getTown().getName());
			k++;
			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getY());k++;
			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getX());k++;
			
			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
								garbage.getAccuracy());k++;
			
			row.createCell(k, Cell.CELL_TYPE_BOOLEAN).setCellValue(
					garbage.isDispersed());k++;
			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getBagCount());k++;
			
			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
							garbage.getTrashOutSize());k++;
							
			if (garbage.getTrashOutTypes() != null)
				row.createCell(k, Cell.CELL_TYPE_STRING).setCellValue(
								garbage.getTrashOutTypes());
			k++;
			
			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getPercentagePlastic());k++;
			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getPercentageMetal());k++;
			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getPercentageGlass());k++;
			row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
					garbage.getPercentageWaste());k++;
			row.createCell(k).setCellValue(
					garbage.getBigComponentsDescription());k++;

			if (garbage.getDescription() != null)
				row.createCell(k, Cell.CELL_TYPE_STRING).setCellValue(
						garbage.getDescription().replaceAll("\\r\\n|\\r|\\n",
								" "));
			k++;
			if (garbage.getStatus() != null)
				row.createCell(k).setCellValue(
						garbage.getStatus().getTranslation());
			k++;
			if (garbage.getChartedArea() != null)
				row.createCell(k).setCellValue(
						garbage.getChartedArea().getName());
			k++;
			try {
				if (garbage.getName() != null)
					row.createCell(k).setCellValue(garbage.getName());
			} catch (Exception e) {
				// TODO: handle exception
			}
			k++;
			try {
				row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
						garbage.getRadius());
			} catch (Exception e1) {
				// TODO: handle exception
			}
			k++;
			try {
				if (garbage.getVotes() != null)
					row.createCell(k, Cell.CELL_TYPE_NUMERIC).setCellValue(
							garbage.getVoteCount());
			} catch (Exception ee) {

			}
			k++;
			try {
				Cell cell;
				DataFormat df = wb.createDataFormat();
				CellStyle cs = wb.createCellStyle();
						  cs.setDataFormat(df.getFormat("dd-mm-yyyy"));
						  
				if (garbage.getRecordDate()!= null)
					{
						cell=row.createCell(k, Cell.CELL_TYPE_STRING);
						cell.setCellValue(garbage.getRecordDate());
						cell.setCellStyle(cs);
					}
			} catch (Exception ee) {

			}
			k++;
			row.createCell(k, Cell.CELL_TYPE_STRING).setCellValue(garbage.isToVote());k++;
			row.createCell(k, Cell.CELL_TYPE_STRING).setCellValue(garbage.isToClean());k++;

		}
		return wb;
	}
}
