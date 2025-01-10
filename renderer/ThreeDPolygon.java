package renderer;

import java.awt.Color;

import drawer.objects.ThreeDObjects;

/**
 * This class is an Object that stores a 3D polygon.
 * 
 * Created: February 17, 2024 
 * Last updated: April 16, 2024
 * 
 * @author Blake Mcculligh
 */
public class ThreeDPolygon {
	
	/**
	 * The coordinates of the corners of the polygon.
	 */
	public double[] x, y, z;
	
	/**
	 *  The color of the polygon.
	 */
	Color c;

	/**
	 * If draw is false the polygon is in a state in witch it is broken and should
	 * not be drawn or it would crash the program.
	 */
	boolean draw = true;

	/**
	 * The converted position of a corner of the polygon from the 3D world to the 2D
	 * screen.
	 */
	double[] CalcPos;

	/**
	 * The average distance between the polygon and the camera.
	 */
	public double AvgDist = 0;

	/**
	 * Object that stores the polygon that has been converted into the 2D rendering
	 * that can be printed to the screen.
	 */
	public TwoDPolygon DrawablePolygon;
	
	/**
	 * What is the index of the polygon in the plain, shape, or side array depending
	 * on its type.
	 */
	int index;
	/**
	 * Is it a plain, shape, or side (1 = plain ,2 = shape, 3 = side).
	 */
	int type;

	/**
	 * Constructor: takes in the coordinates of the corners of the polygon and the
	 * color, and creates the 2D rendering of the 3D polygon.
	 * 
	 * @param x The x coordinates of the polygon.
	 * @param y The y coordinates of the polygon.
	 * @param z The z coordinates of the polygon.
	 * @param c The color of the polygon.
	 * @param i    The index of the plain, shape, or side that this polygon is for.
	 * @param type Is it a plain, shape, or side (1 = plain ,2 = shape, 3 = side).
	 */
	public ThreeDPolygon(double[] x, double[] y, double[] z, Color c, int i, int type) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.c = c;

		index = i;
		this.type = type;
		
		
		
		// creates the side that is converted into the 2D rendering to be printed
		DrawablePolygon = new TwoDPolygon(convertPolygon(), x.length, c, i, type);

		// setting how far the polygon is from the camera
		AvgDist = getDist();
		if(type == 1) {
			ThreeDObjects.Planes.get(i).distance = AvgDist;
		} else if(type == 2) {
			ThreeDObjects.Shapes.get(i).distance = AvgDist;
		} else if(type == 3) {
			ThreeDObjects.Sides.get(i).distance = AvgDist;
		}
		
		// Transferring if the side can be drawn to the drawable line object
		DrawablePolygon.draw = draw;
				
		// adds a new side to the ordering system
		Main.POrder.add(new PrintOrder(1, AvgDist, Main.ThreeDPolygon.size()));
	}

	/**
	 * Converts a 3D polygon to 2D polygon for the current camera location.
	 * 
	 * @return The 2D corners of the polygon.
	 */
	public int[][] convertPolygon() {

		// new pixel locations of the corners
		int[] newX = new int[x.length];
		int[] newY = new int[x.length];
		draw = true;

		// for every corner calculate the corners new location on the screen for the
		// cameras new location
		for (int i = 0; i < x.length; i++) {
			int newP[] = ThreeDToTwoD.threeDToTwoD(x[i],y[i],z[i]);
			newX[i] = newP[0];
			newY[i] = newP[1];
			
			// if polygon is broken do not draw
			if (ThreeDToTwoD.t < 0)
				draw = false;
		}

		int points[][] = new int[2][x.length];
		points[0] = newX;
		points[1] = newY;
		
		
		return points;
	}

	/**
	 * Calculates the average distance from the camera to a polygon.
	 * 
	 * @return Average distance to polygon.
	 */
	double getDist() {
		double total = 0;
		for (int i = 0; i < x.length; i++)
			total += getDistanceToP(i);
		return total / x.length;
	}

	/**
	 * Calculates the distance from the camera to a corner of the polygon.
	 * 
	 * @param i The index of the corner being calculated.
	 * @return Distance to the point.
	 */
	double getDistanceToP(int i) {
		return Math.sqrt(Math.pow((cM.CameraMovement.viewFrom[0] - x[i]),2)
				+ Math.pow((cM.CameraMovement.viewFrom[1] - y[i]),2)
				+ Math.pow((cM.CameraMovement.viewFrom[2] - z[i]),2));
	}

}
