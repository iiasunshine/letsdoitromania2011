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
 *  Filename: GarbagePackageBuilder.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */
package ro.ldir.chartpackage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.Set;

import javax.xml.xpath.XPathExpressionException;

import ro.ldir.dto.Garbage;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.List;
import com.itextpdf.text.ListItem;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Builds a chart package PDF.
 */
public class GarbagePackageBuilder {
	private String origin;
	private Set<Garbage> garbages;

	private byte[] getImage(Garbage garbage) throws XPathExpressionException,
			IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte buf[] = new byte[1024];
		int len;
		MapResolver resolver = new MapResolver(origin, garbage.getX(),
				garbage.getY());
		InputStream in = resolver.getInputStream();
		while ((len = in.read(buf)) > 0)
			out.write(buf, 0, len);
		return out.toByteArray();
	}

	public void writePDF(OutputStream out) throws DocumentException,
			MalformedURLException, XPathExpressionException, IOException {
		BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, "Cp1250",
				BaseFont.NOT_EMBEDDED);
		final Font hfFont = new Font(bf, 8, Font.NORMAL, BaseColor.GRAY);

		Document document = new Document(PageSize.A4, 50, 50, 50, 50);
		PdfWriter writer = PdfWriter.getInstance(document, out);
		writer.setBoxSize("art", new Rectangle(36, 54, 559, 788));

		writer.setPageEvent(new PdfPageEventHelper() {
			private int page = 0;

			@Override
			public void onEndPage(PdfWriter writer, Document arg1) {
				page++;
				Rectangle rect = writer.getBoxSize("art");
				ColumnText.showTextAligned(
						writer.getDirectContent(),
						Element.ALIGN_CENTER,
						new Phrase(
								"Pachet mormane - \u00a9 Let's Do It, Romania!",
								hfFont),
						(rect.getLeft() + rect.getRight()) / 2,
						rect.getTop() + 18, 0);
				ColumnText.showTextAligned(writer.getDirectContent(),
						Element.ALIGN_CENTER, new Phrase("- " + page + " -",
								hfFont),
						(rect.getLeft() + rect.getRight()) / 2, rect
								.getBottom() - 18, 0);
			}
		});

		document.open();
		document.addAuthor("Let's Do It, Romania!");
		document.addTitle("Pachet mormane");
		document.addCreationDate();

		Font titleFont = new Font(bf, 24, Font.BOLD);
		Font noteFont = new Font(bf, 12, Font.NORMAL, BaseColor.RED);
		Font headerFont = new Font(bf, 12, Font.BOLD);
		Font normalFont = new Font(bf, 11);
		Font defFont = new Font(bf, 11, Font.BOLD);
		Paragraph par;
		int page = 0;

		for (Garbage garbage : garbages) {
			par = new Paragraph();
			par.setAlignment(Element.ALIGN_CENTER);
			par.add(new Chunk("Morman " + garbage.getGarbageId() + "\n",
					titleFont));
			par.add(new Chunk("Citi\u0163i cu aten\u0163ie!", noteFont));
			document.add(par);

			par = new Paragraph();
			par.setSpacingBefore(20);
			par.add(new Chunk("1. Date generale\n", headerFont));
			par.add(new Chunk("Jude\u0163ul: ", defFont));
			par.add(new Chunk(garbage.getCounty().getName() + "\n", normalFont));
			par.add(new Chunk("Comuna: ", defFont));
			par.add(new Chunk(garbage.getTown().getName() + "\n", normalFont));
			if (garbage.getChartedArea() != null) {
				par.add(new Chunk("Zona cartare: ", defFont));
				par.add(new Chunk(garbage.getChartedArea().getName() + "\n",
						normalFont));
			}
			par.add(new Chunk("Pozi\u0163ie: ", defFont));
			par.add(new Chunk(garbage.getY() + ", " + garbage.getX() + "\n",
					normalFont));
			par.add(new Chunk("Descriere:\n", defFont));
			par.add(new Phrase(garbage.getDescription() + "\n", normalFont));
			par.add(new Chunk("Componen\u0163\u0103 gunoi:\n", defFont));
			List list = new List();
			list.add(new ListItem(new Chunk("Procent plastic: "
					+ garbage.getPercentagePlastic() + "%", normalFont)));
			list.add(new ListItem(new Chunk("Procent sticl\u0103: "
					+ garbage.getPercentageGlass() + "%", normalFont)));
			list.add(new ListItem(new Chunk("Procent metale: "
					+ garbage.getPercentageMetal() + "%", normalFont)));
			list.add(new ListItem(new Chunk("Procent nereciclabile: "
					+ garbage.getPercentageWaste() + "%", normalFont)));
			par.add(list);
			document.add(par);

			par = new Paragraph();
			par.setSpacingBefore(20);
			par.add(new Chunk("2. Indica\u0163ii rutiere\n", headerFont));
			Image img = Image.getInstance(getImage(garbage));
			img.scaleToFit((float) (PageSize.A4.getWidth() * .75),
					(float) (PageSize.A4.getHeight() * .75));
			img.setAlignment(Element.ALIGN_CENTER);
			par.add(img);
			document.add(par);

			if (page < garbages.size() - 1)
				document.newPage();
			page++;
		}

		document.close();
	}

	public GarbagePackageBuilder(String origin, Set<Garbage> garbages)
			throws IOException {
		this.origin = origin;
		this.garbages = garbages;
	}
}
