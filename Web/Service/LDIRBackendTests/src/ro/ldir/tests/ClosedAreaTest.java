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
 *  Filename: GarbageManager.java
 *  Author(s): Stefan Guna, svguna@gmail.com
 *
 */

package ro.ldir.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import ro.ldir.dto.ChartedArea;
import ro.ldir.dto.ClosedArea;

/**
 * Tester for the {@link ro.ldir.dro.ClosedArea} class.
 */
public class ClosedAreaTest {
	private static ClosedArea arrowHead = new ChartedArea();
	private static ClosedArea square = new ChartedArea();
	private static ClosedArea triangle = new ChartedArea();

	@BeforeClass
	public static void setupArrowHead() throws Exception {
		List<Point2D.Float> points = new ArrayList<Point2D.Float>();
		points.add(new Point2D.Float(0, 0));
		points.add(new Point2D.Float(1, 1));
		points.add(new Point2D.Float(0, 2));
		points.add(new Point2D.Float(2, 1));
		arrowHead.setPolyline(points);
		arrowHead.setBoundingBox();
	}

	@BeforeClass
	public static void setupSquare() throws Exception {
		List<Point2D.Float> points = new ArrayList<Point2D.Float>();
		points.add(new Point2D.Float(0, 0));
		points.add(new Point2D.Float(2, 0));
		points.add(new Point2D.Float(2, 2));
		points.add(new Point2D.Float(0, 2));
		square.setPolyline(points);
		square.setBoundingBox();
	}

	@BeforeClass
	public static void setupTriangle() throws Exception {
		List<Point2D.Float> points = new ArrayList<Point2D.Float>();
		points.add(new Point2D.Float(0, 0));
		points.add(new Point2D.Float(2, 0));
		points.add(new Point2D.Float(1, 2));
		triangle.setPolyline(points);
		triangle.setBoundingBox();
	}

	@Test
	public void testArrowHead1() {
		Point2D.Float p = new Point2D.Float((float) 1.5, 1);
		assertTrue(arrowHead.containsPoint(p));
	}

	@Test
	public void testArrowHead2() {
		Point2D.Float p = new Point2D.Float(0, 1);
		assertFalse(arrowHead.containsPoint(p));
	}

	@Test
	public void testArrowHead3() {
		Point2D.Float p = new Point2D.Float(1, 0);
		assertFalse(arrowHead.containsPoint(p));
	}

	@Test
	public void testSquare1() {
		Point2D.Float p = new Point2D.Float(1, 1);
		assertTrue(square.containsPoint(p));
	}

	@Test
	public void testSquare2() {
		Point2D.Float p = new Point2D.Float(3, 1);
		assertFalse(square.containsPoint(p));
	}

	@Test
	public void testTriangle1() {
		Point2D.Float p = new Point2D.Float(1, 1);
		assertTrue(triangle.containsPoint(p));
	}

	@Test
	public void testTriangle2() {
		Point2D.Float p = new Point2D.Float(2, 1);
		assertFalse(triangle.containsPoint(p));
	}
}
