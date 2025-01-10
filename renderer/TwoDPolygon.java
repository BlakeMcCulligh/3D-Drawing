package renderer;

import java.awt.Color;
import java.awt.Polygon;

import console.GraphicsConsole;
import drawer.objects.ThreeDObjects;

/**
 * This class is an Object that stores the 2D rendering of a 3D polygon.
 * 
 * Created: February 17, 2024
 * Last updated: June 2, 2024
 * @author Blake Mcculligh
 */
public class TwoDPolygon {

	/**
	 * The 2D rendering of the polygon converted into a java polygon so it can be
	 * drawn.
	 */
	Polygon P;

	/**
	 * The color of the polygon.
	 */
	Color c;

	/**
	 * If draw is false the polygon is in a state in witch it is broken and should
	 * not be drawn or it would crash the program.
	 */
	boolean draw = true;

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
	 * If the polygon is behind the camera do not draw it.
	 */
	boolean visible = true;

	/**
	 * How bright the polygon should be because of the light source.
	 */
	double lighting = 1;

	/**
	 * Constructor: Takes the pixel coordinates of the corners of the polygon and
	 * the color transfers the information into a java polygon so the side can be
	 * drawn. Also saves where the polygon is from.
	 * 
	 * @param x    The x coordinates of the polygon on the console.
	 * @param y    The y coordinates of the polygon on the console.
	 * @param c    The color of the polygon.
	 * @param i    The index of the plain, shape, or side that this polygon is for.
	 * @param type Is it a plain, shape, or side (1 = plain ,2 = shape, 3 = side).
	 */
	public TwoDPolygon(int[][] points, int l, Color c, int i, int type) {

		P = new Polygon();
		for (int j = 0; j < l; j++)
			P.addPoint(points[0][j], points[1][j]);

		this.c = c;

		this.type = type;
		index = i;
	}

	/**
	 * Draws the polygon to the screen.
	 * 
	 * @param gc The graphics console.
	 */
	void drawSide(GraphicsConsole gc) {

		if (draw && visible) {

			gc.setColor(c);

			gc.fillPolygon(P);

			gc.setColor(new Color(0, 0, 0));
			gc.drawPolygon(P);

		}
	}

	/**
	 * Determines if a point of the screen is within the polygon. Used for
	 * determining if the mouse courser is over the polygon and runs the
	 * corresponding methods for if it is in or not in the polygon.
	 * 
	 * @param X  The x coordinate of the mouse.
	 * @param Y  The y coordinate of the mouse
	 * @param gc The graphics console.
	 * @return The type of polygon (1 = plain ,2 = shape, 3 = side) and the index of
	 *         that polygon.
	 */
	public int[] contains(double X, double Y, GraphicsConsole gc) {

		if (P.contains(X, Y)) {

			if (type == 1) {
				ThreeDObjects.Planes.get(index).mouseOver();
			} else if (type == 2) {
				ThreeDObjects.Shapes.get(index).mouseOver();
			} else if (type == 3) {
				ThreeDObjects.Sides.get(index).mouseOver();
			}

			if (Main.mouseClick) {
				return new int[] { type, index };
			}

		} else {

			if (type == 1) {
				ThreeDObjects.Planes.get(index).notMouseOver();
			} else if (type == 2) {
				ThreeDObjects.Shapes.get(index).notMouseOver();
			} else if (type == 3) {
				ThreeDObjects.Sides.get(index).notMouseOver();
			}
		}
		return new int[] { 0, 0 };
	}

}
