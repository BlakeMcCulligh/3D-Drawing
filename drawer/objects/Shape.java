package drawer.objects;

import java.awt.Color;

import drawer.Point;

/**
 * Object for 2D shapes. One is created when the user uses the rectangle or
 * polygon function, or when they draw a closed shape with the line tool.
 * 
 * Created May 15, 2024
 * 
 * @author Blake McCulligh
 */
public class Shape {

	/**
	 * Array of the points in the corners
	 */
	public Point points[];

	/**
	 * Current color being displayed
	 */
	Color c;
	/**
	 * The original color the object had been set to (non-selection color).
	 */
	public Color originalC;

	/**
	 * If the shape is selected.
	 */
	public boolean selected;

	/**
	 * The plane the shape is located on.
	 */
	public Plane plane;

	/**
	 * The distance from the camera to the shape.
	 */
	public double distance = 10000;

	/**
	 * Constructor: Creates a 2D shape using a array of points.
	 * 
	 * @param p     Array of points.
	 * @param plane The plane the shape is on.
	 */
	public Shape(Point p[], Plane plane) {

		points = new Point[p.length];

		for (int i = 0; i < p.length; i++) {
			points[i] = new Point(p[i].x, p[i].y, p[i].z);
		}

		this.plane = plane;

		// See though middle
		c = new Color(0, 0, 0, 0);
		originalC = c;

		selected = false;

	}

	/**
	 * Constructor: Creates a 2D shape using a side.
	 * 
	 * @param side The side used to create the shape.
	 */
	public Shape(Side side) {

		points = new Point[side.points.length];

		for (int i = 0; i < side.points.length; i++) {
			points[i] = new Point(side.points[i].x, side.points[i].y, side.points[i].z);
		}

		plane = new Plane(points[0], points[1], points[2], points[0]);

		// See though middle
		c = new Color(0, 0, 0, 0);
		originalC = c;

		selected = false;
	}

	/**
	 * Ran if the mouse us over the shape. Changes the color to the mouse over
	 * colors.
	 */
	public void mouseOver() {

		if (selected) {
			c = new Color(10, 5, 255, 150);
		} else {
			c = new Color(10, 5, 255, 100);
		}
	}

	/**
	 * Ran if the mouse is not over the shape. Sets the color to the shapes original
	 * color.
	 */
	public void notMouseOver() {
		if (selected) {
			c = new Color(10, 5, 255, 150);
		} else {
			c = originalC;
		}
	}

	/**
	 * Ran if the shape has been clicked. Changes the selection boolean.
	 */
	public void cliked() {
		if (selected == false) {
			selected = true;
		} else {
			selected = false;
		}
	}
}
