package renderer;

import java.awt.Color;

import console.GraphicsConsole;
import drawer.objects.ThreeDObjects;

/**
 * This class is an Object that stores the 2D rendering of a 3D line.
 * 
 * Created: February 19, 2024 
 * Last updated: June 11, 2024
 * 
 * @author Blake McCulligh
 */
public class TwoDLine {

	/**
	 * The two end points of the line.
	 */
	int points[][] = new int[2][2];

	/**
	 * The color of the line.
	 */
	Color c;

	/**
	 * The index of the 3D line.
	 */
	int indexOfLine;

	/**
	 * If draw is false the line is in a state in witch it is broken and should not
	 * be drawn or it would crash the program.
	 */
	boolean draw = true;

	/**
	 * Constructor: Takes the two end points of the line, the index of the 3D line
	 * it represents, and the color
	 * 
	 * @param point The end points of the line
	 * @param c     The color of the line.
	 * @param i     The index of the line that this 2D line represents.
	 */
	public TwoDLine(int[][] point, Color c, int i) {

		points[0][0] = point[0][0];
		points[1][0] = point[0][1];
		points[0][1] = point[1][0];
		points[1][1] = point[1][1];

		this.c = c;

		indexOfLine = i;
	}

	/**
	 * Draws the line.
	 * 
	 * @param gc The graphics console.
	 */
	void drawLine(GraphicsConsole gc) {

		if (draw) {

			gc.setColor(c);

			gc.drawLine(points[0][0], points[0][1], points[1][0], points[1][1]);
		}
	}

	/**
	 * Finds if a point is over top of the line and runs the corresponding function.
	 * 
	 * @param X  The x coordinate of the point.
	 * @param Y  THe y coordinate of the point.
	 * @param gc The graphics console.
	 * @return 4 to represent it is a line and the index of the line.
	 */
	public int[] contains(int X, int Y, GraphicsConsole gc) {

		int[] L1 = ThreeDToTwoD.threeDToTwoD(ThreeDObjects.Lines.get(indexOfLine).points[0].x,
				ThreeDObjects.Lines.get(indexOfLine).points[0].y, ThreeDObjects.Lines.get(indexOfLine).points[0].z);
		int[] L2 = ThreeDToTwoD.threeDToTwoD(ThreeDObjects.Lines.get(indexOfLine).points[1].x,
				ThreeDObjects.Lines.get(indexOfLine).points[1].y, ThreeDObjects.Lines.get(indexOfLine).points[1].z);

		if (distancePointToSegment(new int[] { X, Y }, L1, L2) < 10) {
			ThreeDObjects.Lines.get(indexOfLine).mouseOver();

			if (Main.mouseClick) {
				return new int[] { 4, indexOfLine };
			}
		} else {
			ThreeDObjects.Lines.get(indexOfLine).notMouseOver();
		}
		return new int[] { 0, 0 };
	}

	/**
	 * Determines the distance form a point to a line segment.
	 * 
	 * @param p  Point.
	 * @param L1 First L2 point for the line.
	 * @param L2 Second L2 point for the line.
	 * @return The distance from the point to the line.
	 */
	public static double distancePointToSegment(int[] p, int[] L1, int[] L2) {

		double dx = L2[0] - L1[0];
		double dy = L2[1] - L1[1];

		double t = ((p[0] - L1[0]) * dx + (p[1] - L1[1]) * dy) / (dx * dx + dy * dy);

		// Seeing if this represents one of the segment's end point or a point in
		// between.
		if (t < 0) {
			dx = p[0] - L1[0];
			dy = p[1] - L1[1];
		} else if (t > 1) {
			dx = p[0] - L2[0];
			dy = p[1] - L2[1];
		} else {
			dx = p[0] - (L1[0] + t * dx);
			dy = p[1] - (L1[1] + t * dy);
		}

		return Math.sqrt(dx * dx + dy * dy);
	}

	/**
	 * Finds the closest point on a line to a point in 2D.
	 * 
	 * @param L1 First point for the line.
	 * @param L2 Second point for the line.
	 * @param p  Point to the closest spot.
	 * @return The closest point on the line to p.
	 */
	public static int[] closestPointOnLine(int[] L1, int[] L2, int[] p) {
		double length = Math.sqrt(Math.pow(L2[0] - L1[0], 2) + Math.pow(L2[1] - L1[1], 2));

		// Parametric equations for the line segment
		double t = ((p[0] - L1[0]) * (L2[0] - L1[0]) + (p[1] - L1[1]) * (L2[1] - L1[1])) / (length * length);

		// making sure t is between 0 and 1
		t = Math.max(0, Math.min(1, t));

		// Calculate the closest point on the line segment
		double closestX = L1[0] + t * (L2[0] - L1[0]);
		double closestY = L1[1] + t * (L2[1] - L1[1]);

		return new int[] { (int) closestX, (int) closestY };
	}
}
