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
	public static void setupTriangle() throws Exception {
		List<Point2D.Float> points = new ArrayList<Point2D.Float>();
		points.add(new Point2D.Float(0, 0));
		points.add(new Point2D.Float(2, 0));
		points.add(new Point2D.Float(1, 2));
		triangle.setPolyline(points);
		triangle.setBoundingBox();
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
