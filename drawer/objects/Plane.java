package drawer.objects;

import java.awt.Color;

import drawer.Point;

/**
 * Object for planes. One is created when the user uses the plane creator tool.
 * 
 * Created April 17, 2024
 * Last Updated: June 3, 2024
 * 
 * @author Blake McCulligh
 *
 */
public class Plane {

	/**
	 * Array of the points in the corners
	 */
	public Point points[] = new Point[4];

	/**
	 * Current color being displayed
	 */
	Color c;
	/**
	 * The original color the object had been set to (non-selection color).
	 */
	public Color originalC;

	/**
	 * If the plane is selected.
	 */
	public boolean selected;

	/**
	 * The distance from the camera to the plane.
	 */
	public double distance = 10000;

	/**
	 * THe normal of the plane
	 */
	public Point normal;

	/**
	 * Constructor: Creates a plane using 4 points as double arrays.
	 * 
	 * @param p1 Double array 1;
	 * @param p2 Double array 2;
	 * @param p3 Double array 3;
	 * @param p4 Double array 4;
	 */
	public Plane(double p1[], double p2[], double p3[], double p4[]) {

		points[0] = new Point(p1);
		points[1] = new Point(p2);
		points[2] = new Point(p3);
		points[3] = new Point(p4);

		normal = getNormal();

		// Setting color to plane color
		c = new Color(10, 5, 255, 100);
		originalC = c;

		selected = false;
	}

	/**
	 * Constructor: Creates a plane if using 4 points as point objects.
	 * 
	 * @param p1 Point 1;
	 * @param p2 Point 2;
	 * @param p3 Point 2;
	 * @param p4 Point 4;
	 */
	public Plane(Point p1, Point p2, Point p3, Point p4) {
		points[0] = p1;
		points[1] = p2;
		points[2] = p3;
		points[3] = p4;

		normal = getNormal();
		
		// Setting color to plane color
		c = new Color(10, 5, 255, 100);
		originalC = c;

		selected = false;
	}

	/**
	 * Gets the unit vector in the direction of the normal.
	 * 
	 * @return The normal of the plane.
	 */
	public Point getNormal() {

		double v1[] = { points[1].x - points[0].x, points[1].y - points[0].y, points[1].z - points[0].z };
		double v2[] = { points[2].x - points[1].x, points[2].y - points[1].y, points[2].z - points[1].z };
		Point normal = new Point(v1[1] * v2[2] - v1[2] * v2[1], v1[2] * v2[0] - v1[0] * v2[2],
				v1[0] * v2[1] - v1[1] * v2[0]);

		double normalMagnatude = Math.pow(Math.pow(normal.x, 2) + Math.pow(normal.y, 2) + Math.pow(normal.z, 2),
				0.5);
		normal.x = (normal.x / (normalMagnatude));
		normal.y = (normal.y / (normalMagnatude));
		normal.z = (normal.z / (normalMagnatude));
		this.normal = normal;
		return normal;
	}
	
	/**
	 * Finds the point where a line represented by 2 points intersects with a plane represented by 4 points.
	 * 
	 * @param L1 The first point of the line.
	 * @param L2 The second point of the line.
	 * @return Where the plane and line intersect.
	 */
	public Point findIntersection(Point L1, Point L2) {

		// Calculate the equation of the plane
		double d = -Line.dotProduct(this.normal, this.points[0]);

		// Calculate the direction vector of the line
		Point lineDir = new Point(L2.x - L1.x, L2.y - L1.y, L2.z - L1.z);

		// Calculate the parameter t of the intersection point
		double t = -(Line.dotProduct(this.normal, L1) + d) / Line.dotProduct(this.normal, lineDir);

		// Calculate the intersection point
		double intersectionX = L1.x + t * lineDir.x;
		double intersectionY = L1.y + t * lineDir.y;
		double intersectionZ = L1.z + t * lineDir.z;

		intersectionX = (double) Math.round((intersectionX * 20)) / 20;
		intersectionY = (double) Math.round((intersectionY * 20)) / 20;
		intersectionZ = (double) Math.round((intersectionZ * 20)) / 20;

		return new Point(intersectionX, intersectionY, intersectionZ);
	}

	/**
	 * Ran if the mouse us over the plane. Changes the color to the mouse over
	 * colors.
	 */
	public void mouseOver() {

		if (selected) {
			c = new Color(0, 0, 150);
		} else {
			c = new Color(0, 0, 255);
		}
	}

	/**
	 * Ran if the mouse is not over the plane. Sets the color to the shapes original
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
	 * Ran if the plane has been clicked. Changes the selection boolean.
	 */
	public void cliked() {
		if (selected == false) {
			selected = true;
		} else {
			selected = false;
		}
	}

}
