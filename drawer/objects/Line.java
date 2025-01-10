package drawer.objects;

import java.awt.Color;

import drawer.Point;

/**
 * Object for 3D lines.
 * 
 * Created March 29, 2024
 * Last Edited June 6, 2024
 * 
 * @author Blake mcculligh
 */
public class Line {

	/**
	 * The two end points of the line.
	 */
	public Point points[] = new Point[2];

	/**
	 * The color of the line.
	 */
	Color c;
	/**
	 * The color of the line when it is not selected.
	 */
	public Color originalC;

	/**
	 * The length of the line.
	 */
	double length;

	/**
	 * If the line is selected.
	 */
	public boolean selected;

	/**
	 * The plane the line was drawn on.
	 */
	public Plane plane;

	/**
	 * The distance from the camera to the line.
	 */
	public double distance = 10000;

	/**
	 * Constructor, creates a line using two double arrays for points and calculates
	 * its length
	 * 
	 * @param p1    The first end for the line.
	 * @param p2    The second end for the line
	 * @param c     The color of the line.
	 * @param plane The plane the line was drawn on.
	 */
	public Line(double p1[], double p2[], Color c, Plane plane) {

		points[0] = new Point(p1);
		points[1] = new Point(p2);

		this.c = c;
		originalC = c;

		selected = false;

		this.plane = plane;

		double l[] = { Math.abs(points[0].x - points[1].x), Math.abs(points[0].y - points[1].y),
				Math.abs(points[0].z - points[1].z) };

		length = Math.sqrt(l[0] * l[0] + l[1] * l[1] + l[2] * l[2]);
	}

	/**
	 * Constructor, creates a line using two points and calculates its length.
	 * 
	 * @param p1    The first end for the line.
	 * @param p2    The second end for the line
	 * @param c     The color of the line.
	 * @param plane The plane the line was drawn on.
	 */
	public Line(Point p1, Point p2, Color c, Plane plane) {

		points[0] = p1;
		points[1] = p2;

		this.c = c;
		originalC = c;
		this.plane = plane;
		selected = false;

		double l[] = { Math.abs(points[0].x - points[1].x), Math.abs(points[0].y - points[1].y),
				Math.abs(points[0].z - points[1].z) };

		length = Math.sqrt(l[0] * l[0] + l[1] * l[1] + l[2] * l[2]);
	}

	/**
	 * Constructor, creates a line using double arrays for points. DO NOT USE FOR A
	 * LINE DRAWN.
	 * 
	 * @param p1 Point 1.
	 * @param p2 Point 2.
	 */
	public Line(double p1[], double p2[]) {
		points[0] = new Point(p1);
		points[1] = new Point(p2);
	}

	/**
	 * Finds the cross-product of 2 lines.
	 * 
	 * @param L1 Line 1.
	 * @param L2 Line 2.
	 * @return The cross-product Cartesian vector.
	 */
	public static double[] crossProduct(Line L1, Line L2) {

		double[] v1 = { L1.points[1].x - L1.points[0].x, L1.points[1].y - L1.points[0].y,
				L1.points[1].z - L1.points[0].z };
		double[] v2 = { L2.points[1].x - L1.points[0].x, L2.points[1].y - L1.points[0].y,
				L2.points[1].z - L1.points[0].z };

		double[] crossProduct = { v1[1] * v2[2] - v1[2] * v2[1], v1[2] * v2[0] - v1[0] * v2[2],
				v1[0] * v2[1] - v1[1] * v2[0] };

		return crossProduct;
	}
	
	/**
	 * Calculate the dot product of two points representing Cartesian vectors.
	 * 
	 * @param a The first point.
	 * @param b The second point.
	 * @return The dot product.
	 */
	public static double dotProduct(Point a, Point b) {
		return a.x * b.x + a.y * b.y + a.z * b.z;
	}

	/**
	 * Finds the point along a line that is a certain distance from the first end of
	 * the line.
	 * 
	 * @param p1   Start of the line.
	 * @param p2   End of the line.
	 * @param dist The distance along the line.
	 * @return The point a certain distance along the line.
	 */
	public static Point pointAtDistAlongLine(Point p1, Point p2, double dist) {

		double totalDist = Math.sqrt(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) + Math.pow(p2.z - p1.z, 2));
		double ratio = dist / totalDist;

		double newX = p1.x + (p2.x - p1.x) * ratio;
		double newY = p1.y + (p2.y - p1.y) * ratio;
		double newZ = p1.z + (p2.z - p1.z) * ratio;

		return new Point(newX, newY, newZ);
	}

	/**
	 * Sets the color to the mouse over color.
	 */
	public void mouseOver() {

		if (selected) {
			c = new Color(0, 0, 150);
		} else {
			c = new Color(0, 0, 255);
		}
	}

	/**
	 * Sets the color to its normal color.
	 */
	public void notMouseOver() {
		if (selected) {
			c = new Color(0, 0, 150);
		} else {
			c = originalC;
		}

	}

	/**
	 * Toggles the selection of the line and the color between selected color and
	 * the mouse over color.
	 */
	public void cliked() {
		if (selected == false) {
			selected = true;
			c = new Color(0, 0, 150);
		} else {
			selected = false;
			c = new Color(0, 0, 255);
		}
	}

}
