package drawer;

import java.awt.Color;
import java.awt.Font;

import cM.CameraMovement;
import console.GraphicsConsole;
import drawer.objects.Line;
import drawer.objects.Plane;
import drawer.objects.ThreeDObjects;
import renderer.ButtonPrinter;
import renderer.Main;
import renderer.ThreeDToTwoD;
import renderer.TwoDLine;

/**
 * This class controls everything to do with snapping.
 * 
 * Created: May 14, 2024 
 * Last updated: June 11, 2024
 * 
 * @author Blake McCulligh
 */
public class Snaping {

	/**
	 * What snapping modes are on.
	 */
	static boolean snapingTogle[] = new boolean[4];

	/**
	 * Controls and prints the snapping button panel.
	 * 
	 * @param gc The graphics console.
	 */
	public static void snapingPrintingAndButtons(GraphicsConsole gc) {

		int h = (int) Main.screenSize.getHeight();
		int w = (int) Main.screenSize.getWidth();

		gc.setColor(new Color(240, 240, 245));
		gc.fillRect(w - 140, h - 250, 140, 250);

		gc.setFont(new Font("SansSerif", Font.PLAIN, 25));

		gc.setColor(Color.BLACK);
		gc.drawString("Snapping", w - 130, h - 220);
		gc.setFont(new Font("SansSerif", Font.PLAIN, 20));
		gc.drawString("Point", w - 90, h - 180);
		gc.drawString("Line", w - 90, h - 150);
		gc.drawString("Angle", w - 90, h - 120);
		gc.drawString("Length", w - 90, h - 90);

		if (snapingTogle[0]) {
			gc.fillRect(w - 125, h - 195, 15, 15);
		} else {
			gc.drawRect(w - 125, h - 195, 15, 15);
		}

		if (snapingTogle[1]) {
			gc.fillRect(w - 125, h - 165, 15, 15);
		} else {
			gc.drawRect(w - 125, h - 165, 15, 15);
		}

		if (snapingTogle[2]) {
			gc.fillRect(w - 125, h - 135, 15, 15);
		} else {
			gc.drawRect(w - 125, h - 135, 15, 15);
		}

		if (snapingTogle[3]) {
			gc.fillRect(w - 125, h - 105, 15, 15);
		} else {
			gc.drawRect(w - 125, h - 105, 15, 15);
		}

		if (w - 125 < gc.getMouseX() && h - 200 < gc.getMouseY() && gc.getMouseY() < h - 170 && Main.mouseClick) {
			snapingTogle[0] = togle(snapingTogle[0]);
		}
		if (w - 125 < gc.getMouseX() && h - 170 < gc.getMouseY() && gc.getMouseY() < h - 140 && Main.mouseClick) {
			snapingTogle[1] = togle(snapingTogle[1]);
		}
		if (w - 125 < gc.getMouseX() && h - 140 < gc.getMouseY() && gc.getMouseY() < h - 110 && Main.mouseClick) {
			snapingTogle[2] = togle(snapingTogle[2]);
		}
		if (w - 125 < gc.getMouseX() && h - 110 < gc.getMouseY() && gc.getMouseY() < h - 80 && Main.mouseClick) {
			snapingTogle[3] = togle(snapingTogle[3]);
		}
	}

	/**
	 * Toggles a boolean.
	 * 
	 * @param b The booleans current state.
	 * @return The opposite state to its current state.
	 */
	static boolean togle(boolean b) {
		if (b) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Called by the sketching function to get the snapped point of the cursor if
	 * that snapping mode is on.
	 * 
	 * @param mx X coordinate of the cusser.
	 * @param my Y coordinate of the cusser.
	 * @return The snapped point.
	 */

	/**
	 * Called by the sketching function to get the snapped point of the cursor if
	 * that snapping mode is on.
	 * 
	 * @param mx           X coordinate of the cusser.
	 * @param my           Y coordinate of the cusser.
	 * @param p1           The first point in the drawing.
	 * @param drawingPlane The plane the drawing is on.
	 * @return The snapped point.
	 */
	public static int[] snap(int mx, int my, Point p1, Plane drawingPlane) {

		int newPoint[] = { mx, my };

		if (p1 != null) {
			if (snapingTogle[2] && ButtonPrinter.buttonClick[3]) {
				newPoint = snapToAngle(newPoint[0], newPoint[1], p1);
			}
			if (snapingTogle[3] && ButtonPrinter.buttonClick[3]) {
				newPoint = snapToLength(newPoint[0], newPoint[1], p1, drawingPlane);
			}
		}
		if (snapingTogle[1]) {
			int lines[][][] = convertLines();
			newPoint = snapToLine(lines, newPoint[0], newPoint[1]);
		}
		if (snapingTogle[0]) {
			int points[][] = convertPoints();
			newPoint = snapToPoint(points, newPoint[0], newPoint[1]);
		}
		return newPoint;

	}

	/**
	 * Converts all the points in the 3D world to the screen.
	 * 
	 * @return Array of the points locations.
	 */
	public static int[][] convertPoints() {
		int points[][] = new int[ThreeDObjects.Points.size()][2];

		for (int i = 0; i < ThreeDObjects.Points.size(); i++) {
			points[i] = ThreeDToTwoD.threeDToTwoD(ThreeDObjects.Points.get(i).x, ThreeDObjects.Points.get(i).y,
					ThreeDObjects.Points.get(i).z);
		}

		return points;
	}

	/**
	 * If the currier is within 5 pixels of a point snap to it.
	 * 
	 * @param points Array of the points.
	 * @param mx     X coordinate of the cusser.
	 * @param my     Y coordinate of the cusser.
	 * @return The coordinate of the mouse or if snapping took place the coordinate
	 *         of the point.
	 */
	public static int[] snapToPoint(int[][] points, int mx, int my) {

		int newPoint[] = { mx, my };

		for (int i = 0; i < ThreeDObjects.Points.size(); i++) {
			if ((points[i][0] < (mx + 20) && points[i][0] > (mx - 20))
					&& (points[i][1] < (my + 20) && points[i][1] > (my - 20))) {
				newPoint[0] = points[i][0];
				newPoint[1] = points[i][1];
			}
		}
		return newPoint;
	}

	/**
	 * Moves all the lines into an array that stores their L2 points.
	 * 
	 * @return The array of line L2 points.
	 */
	static int[][][] convertLines() {

		int lines[][][] = new int[ThreeDObjects.Lines.size()][2][2];

		for (int i = 0; i < ThreeDObjects.Lines.size(); i++) {
			for (int j = 0; j < 2; j++) {
				lines[i][j] = ThreeDToTwoD.threeDToTwoD(ThreeDObjects.Lines.get(i).points[j].x,
						ThreeDObjects.Lines.get(i).points[j].y, ThreeDObjects.Lines.get(i).points[j].z);
			}
		}
		return lines;
	}

	/**
	 * If the cursor is within 20 pixels of a line change the cursors location to
	 * the closest point on the line.
	 * 
	 * @param lines Array of all the lines.
	 * @param mx    X coordinate of the mouse.
	 * @param my    Y coordinate of the mouse.
	 * @return The new location of the cursor.
	 */
	static int[] snapToLine(int[][][] lines, int mx, int my) {

		int newPoint[] = { mx, my };

		for (int i = 0; i < ThreeDObjects.Lines.size() - 1; i++) {

			int[] p = { mx, my };
			int L1[] = { lines[i][0][0], lines[i][0][1] };
			int L2[] = { lines[i][1][0], lines[i][1][1] };

			if (TwoDLine.distancePointToSegment(p, L1, L2) < 20) {
				newPoint = TwoDLine.closestPointOnLine(L1, L2, p);
			}
		}
		return newPoint;
	}

	/**
	 * If the line is within a few digress of a right angle or a 45 degree snap to
	 * it.
	 * 
	 * @param mx X coordinate of the mouse.
	 * @param my Y coordinate of the mouse.
	 * @param p1 The first point of the line.
	 * @return The new location of the cursor.
	 */
	static int[] snapToAngle(int mx, int my, Point p1) {

		int newPoint[] = { mx, my };

		int P1[] = ThreeDToTwoD.threeDToTwoD(p1.x, p1.y, p1.z);

		int xDif = mx - P1[0];
		int yDif = my - P1[1];

		double ratio;
		if (yDif == 0) {
			ratio = 0;
		} else {
			ratio = xDif / yDif;
		}

		// snap to x axis
		if (ratio > 5) {
			newPoint[0] = mx;
			newPoint[1] = P1[1];

			// snap to y axis
		} else if (ratio < 0.2 && ratio > -0.2) {
			newPoint[0] = P1[0];
			newPoint[1] = my;

			// snap to 45 (+,-),(-,+) quadrants
		} else if (ratio > 0.9 && ratio < 1.1) {
			newPoint[0] = P1[0] + xDif;
			newPoint[1] = P1[1] + xDif;

			// snap to 45 (+,+),(-,-) quadrants
		} else if (ratio < -0.9 && ratio > -1.1) {
			newPoint[0] = P1[0] + xDif;
			newPoint[1] = P1[1] - xDif;
		}

		return newPoint;
	}

	/**
	 * When drawing a line it snaps the line to 1/4 of a unit.
	 * 
	 * @param mx           X coordinate of the mouse.
	 * @param my           Y coordinate of the mouse.
	 * @param p1           The first point of the line.
	 * @param drawingPlane The plain the user is drawing on.
	 * @return The new location of the cursor.
	 */
	static int[] snapToLength(int mx, int my, Point p1, Plane drawingPlane) {

		int newPoint[] = { mx, my };

		// convert 2D point to 3D
		Point p2 = FindPoint.convertCursorTo3D(mx, my, new Point(CameraMovement.viewFrom), drawingPlane);

		double dist = Math.pow(Math.pow(p2.x - p1.x, 2) + Math.pow(p2.y - p1.y, 2) + Math.pow(p2.z - p1.z, 2), 0.5);

		// finds how far the distance is of a hole number
		int distInt = (int) Math.round(dist);
		double dif = dist - distInt;

		if ((dif > -0.5 && dif < 0.5)) {
			double length = distInt;
			p2 = Line.pointAtDistAlongLine(p1, p2, length);
		}

		// convert 3D point to 2D
		newPoint = ThreeDToTwoD.threeDToTwoD(p2.x, p2.y, p2.z);

		return newPoint;
	}

}
