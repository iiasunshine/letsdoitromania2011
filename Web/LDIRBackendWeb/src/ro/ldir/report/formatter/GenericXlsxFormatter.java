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
 *  Filename: GenericXlsFormatter.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.report.formatter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/** Converts a list of teams to the XLS format. */
public class GenericXlsxFormatter {
	private static Logger log = Logger.getLogger(GenericXlsxFormatter.class
			.getName());
	private ExcelFormatter formatter;

	public GenericXlsxFormatter(ExcelFormatter formatter) {
		this.formatter = formatter;
	}

	/** Return the report in a binary form. */
	public byte[] getBytes() {
		Workbook wb = formatter.convert(new XSSFWorkbook());
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			wb.write(out);
			out.close();
		} catch (IOException e) {
			log.warning("Unable to save XLSX report: " + e.getMessage());
		}
		return out.toByteArray();
	}
}
