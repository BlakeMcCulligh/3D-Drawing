package drawer.objects;

import java.awt.Color;

import drawer.Point;

/**
 * Object for a side.
 * 
 * Created May 20, 2024
 * 
 * @author Blake McCulligh
 */
public class Side {

	/**
	 * The corners of the side.
	 */
	public Point points[];
	
	/**
	 * Current color being displayed.
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
	 * The distance from the camera to the side.
	 */
	public double distance = 10000;
	
	/**
	 * Constructor: creates a side using a array of points as corners and the color.
	 * 
	 * @param p Array of points.
	 * @param c The color of the side.
	 */
	public Side(Point p[], Color c) {
		
		points = new Point[p.length];

		for (int i = 0; i < p.length; i++) {
			points[i] = new Point(p[i].x, p[i].y, p[i].z);

		}

		this.c = c;
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
			c = new Color(0, 0, 150);
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
