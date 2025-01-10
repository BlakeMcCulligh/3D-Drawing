package renderer;

/**
 * This class is an Object that stores a Cartesian vector.
 * 
 * Created: February 15, 2024 
 * Last updated: February 15, 2024
 * 
 * @author Blake McCulligh
 */
public class RenderingVector {

	/**
	 * Values of the Cartesian vector.
	 */
	public double x, y, z;

	/**
	 * Constructor: saves the unit vector for the values.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z The z coordinate.
	 */
	public RenderingVector(double x, double y, double z) {

		double Length = Math.sqrt(x * x + y * y + z * z);

		if (Length > 0) {
			this.x = x / Length;
			this.y = y / Length;
			this.z = z / Length;
		}
	}

	/**
	 * Creates a vector that is the cross product of this vector and an other vector.
	 * 
	 * @param V The second vector.
	 * @return A vector that is the cross product of the two vectors.
	 */
	public RenderingVector crossProduct(RenderingVector V) {
		RenderingVector CrossVector = new RenderingVector(y * V.z - z * V.y, z * V.x - x * V.z, x * V.y - y * V.x);
		return CrossVector;
	}
}
