package renderer;

import java.awt.Color;

import drawer.Point;
import drawer.objects.ThreeDObjects;

/**
 * This class is an Object that stores a line in the 3D world.
 * 
 * Created: February 19, 2024 
 * Last updated: April 16, 2024
 * 
 * @author Blake McCulligh
 */
public class ThreeDLine {

	/**
	 * The two end points of the line.
	 */
	double points[][] = new double[2][3];

	/**
	 *  The color of the line.
	 */
	Color c;

	/**
	 * Object that stores the line that has been converted into the 2D rendering
	 * that can be printed to the screen.
	 */
	public TwoDLine DrawableLine;

	/**
	 * If draw is false the line is in a state in witch it is broken and should not
	 * be drawn or it would crash the program.
	 */
	boolean draw = true;

	/**
	 * The converted position of a end point of the line from the 3D world to the 2D
	 * screen.
	 */
	double[] CalcPos;

	/**
	 * The average distance between the polygon and the camera.
	 */
	double AvgDist = 0;
	
	/**
	 * The index of the line in the drawer line array.
	 */
	int indexOfLine;

	/**
	 * Constructor: takes in the coordinates of the two end points of the line and
	 * the color, and creates the corresponding drawable line.
	 * 
	 * @param p1 The first end of the line.
	 * @param p2 The second end of the line.
	 * @param c The color of the line.
	 * @parm i The index of the line in the drawer line array.
	 */
	public ThreeDLine(Point p1, Point p2, Color c, int i) {

		points[0][0] = p1.x;
		points[0][1] = p1.y;
		points[0][2] = p1.z;
		points[1][0] = p2.x;
		points[1][1] = p2.y;
		points[1][2] = p2.z;
		this.c = c;
		indexOfLine = i;

		// creates the side that is converted into the 2D rendering to be printed
		DrawableLine = new TwoDLine(convertLine(), c, i);

		// setting how far the line is from the camera
		AvgDist = getDist();
		ThreeDObjects.Lines.get(i).distance = AvgDist;
		
		// Transferring if the side can be drawn to the drawable line object
		DrawableLine.draw = draw;
				
		// adds a new line to the ordering system
		Main.POrder.add(new PrintOrder(2, AvgDist, Main.ThreeDLines.size()));
	}

	/**
	 * Converts a 3D line to 2D line for the current camera location.
	 * 
	 * @return The 2D ends of the line.
	 */
	public int[][] convertLine() {

		// new pixel locations of the points
		int[] newX = new int[2];
		int[] newY = new int[2];
		draw = true;

		// for every corner calculate the corners new location on the screen for the
		// cameras new location
		for (int i = 0; i < 2; i++) {
			int newP[] = ThreeDToTwoD.threeDToTwoD(points[i][0],points[i][1],points[i][2]);
			newX[i] = newP[0];
			newY[i] = newP[1];

			// if side is broken do not draw
			if (ThreeDToTwoD.t < 0)
				draw = false;
		}

		int points[][] = new int[2][2];
		points[0] = newX;
		points[1] = newY;
		
		return points;
	}

	/**
	 * Calculates the average distance from the camera to the line.
	 * 
	 * @return The average distance to line.
	 */
	double getDist() {
		double total = 0;
		for (int i = 0; i < 2; i++)
			total += getDistanceToP(i);
		return total / 2;
	}

	/**
	 * Calculates the distance from the camera to a end of the line.
	 * 
	 * @param i What end is being calculated.
	 * @return The distance to the point.
	 */
	double getDistanceToP(int i) {
		return Math.sqrt((cM.CameraMovement.viewFrom[0] - points[i][0]) * (cM.CameraMovement.viewFrom[0] - points[i][0])
				+ (cM.CameraMovement.viewFrom[1] - points[i][1]) * (cM.CameraMovement.viewFrom[1] - points[i][1])
				+ (cM.CameraMovement.viewFrom[2] - points[i][2]) * (cM.CameraMovement.viewFrom[2] - points[i][2]));
	}

}
