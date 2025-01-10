package renderer;

/**
 * This class is an Object that stores a plane.
 * 
 * Created: February 17, 2024 
 * Last updated: February 17, 2024
 * 
 * @author Blake McCulligh
 */
public class RenderingPlane {

	/**
	 *  Vectors that create the the plane.
	 */
	RenderingVector V1, V2;
	
	/**
	 *  Vector that is the perpendicular to the plane.
	 */
	public RenderingVector PV;

	/**
	 * A point on the plane.
	 */
	public double[] P = new double[3];

	/**
	 * Constructor: creates a plane using the corners of a 3D polygon.
	 * 
	 * @param DP A 3D polygon.
	 */
	public RenderingPlane(ThreeDPolygon DP) {
		P[0] = DP.x[0];
		P[1] = DP.y[0];
		P[2] = DP.z[0];

		V1 = new RenderingVector(DP.x[1] - DP.x[0], DP.y[1] - DP.y[0], DP.z[1] - DP.z[0]);

		V2 = new RenderingVector(DP.x[2] - DP.x[0], DP.y[2] - DP.y[0], DP.z[2] - DP.z[0]);

		PV = V1.crossProduct(V2);
	}

	/**
	 * Constructor: creates a plane using two vectors and a point.
	 * 
	 * @param VE1 The first vector.
	 * @param VE2 The second vector.
	 * @param Z The point on the plane.
	 */
	public RenderingPlane(RenderingVector VE1, RenderingVector VE2, double[] Z) {
		P = Z;

		V1 = VE1;

		V2 = VE2;

		PV = V1.crossProduct(V2);
	}
}
