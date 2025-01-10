package drawer;

/**
 * This class is an object for a point in 3D.
 * 
 * Created March 29, 2024
 * 
 * @author Blake McCulligh
 */
public class Point {

	/**
	 * Coordinates of the point.
	 */
	public double x, y, z;
	
	/**
	 * Constructor: creates a point using a double in for the coordinate in each axis.
	 * 
	 * @param x Double storing the coordinate for the X axis.
	 * @param y Double storing the coordinate for the Y axis.
	 * @param z Double storing the coordinate for the Z axis.
	 */
	public Point(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	/**
	 * Constructor: creates a point using a double array, each slot storing a different coordinate.
	 * 
	 * @param p The double array storing the coordinates of point.
	 */
	public Point(double p[]) {
		this.x = p[0];
		this.y = p[1];
		this.z = p[2];
	}
}
