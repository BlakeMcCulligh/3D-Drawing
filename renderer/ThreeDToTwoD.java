package renderer;

/**
 * This class does the bulk of the converting of a point from the 3D world to
 * the 2D screen
 * 
 * Created: February 20, 2024 
 * Last updated: June 6, 2024
 * 
 * @author Blake Mcculligh
 * @author JavaTutorials101
 */
public class ThreeDToTwoD {

	/**
	 * Multiplier for converting 3D to 2D
	 */
	static double t = 0;

	/*
	 * Bunch of vectors needed.
	 */
	public static RenderingVector W1, W2;
	static RenderingVector ViewVector, RotationVector, DirectionVector, PlaneVector1, PlaneVector2;
	/*
	 * plane needed
	 */
	public static RenderingPlane P;

	/**
	 * The focus position of the camera.
	 */
	public static double[] CalcFocusPos = new double[2];

	/**
	 * Converts a 3D point to 2D.
	 * 
	 * @param x The x coordinate.
	 * @param y The y coordinate.
	 * @param z THe z coordinate.
	 * @return The 2D point as a double array.
	 */
	public static int[] threeDToTwoD(double x, double y, double z) {

		final double ZOOM = 1000;
		int TwoDP[] = new int[2];

		double CalcPos[] = calculatePoint(cM.CameraMovement.viewFrom, cM.CameraMovement.viewTo, x, y, z);

		TwoDP[0] = (int) ((Main.screenSize.getWidth() / 2 - ThreeDToTwoD.CalcFocusPos[0]) + CalcPos[0] * ZOOM);
		TwoDP[1] = (int) ((Main.screenSize.getHeight() / 2 - ThreeDToTwoD.CalcFocusPos[1]) + CalcPos[1] * ZOOM);

		return TwoDP;
	}

	/**
	 * Does the bulk of the converting for a point in the 3D world to a point on the
	 * screen.
	 * 
	 * @param ViewFrom Where the camera is.
	 * @param ViewTo   Where the camera is focused on.
	 * @param x        The x coordinate of the point.
	 * @param y        The y coordinate of the point.
	 * @param z        The z coordinate of the point.
	 * @return The location of the point on the screen.
	 */
	private static double[] calculatePoint(double[] ViewFrom, double[] ViewTo, double x, double y, double z) {

		RenderingVector ViewToPoint = new RenderingVector(x - ViewFrom[0], y - ViewFrom[1], z - ViewFrom[2]);

		t = (P.PV.x * P.P[0] + P.PV.y * P.P[1] + P.PV.z * P.P[2]
				- (P.PV.x * ViewFrom[0] + P.PV.y * ViewFrom[1] + P.PV.z * ViewFrom[2]))
				/ (P.PV.x * ViewToPoint.x + P.PV.y * ViewToPoint.y + P.PV.z * ViewToPoint.z);

		x = ViewFrom[0] + ViewToPoint.x * t;
		y = ViewFrom[1] + ViewToPoint.y * t;
		z = ViewFrom[2] + ViewToPoint.z * t;

		double DrawX = (W2.x * x + W2.y * y + W2.z * z);
		double DrawY = (W1.x * x + W1.y * y + W1.z * z);
		return new double[] { DrawX, DrawY };
	}

	/**
	 * Calculates all information that is general for the current position and
	 * rotation of the camera this information is then used in each individual side
	 * for converting form 3D to 2D.
	 */
	public static void setPrederterminedInfo() {

		// vector in the direction the camera is pointing
		ViewVector = new RenderingVector(cM.CameraMovement.viewTo[0] - cM.CameraMovement.viewFrom[0],
				cM.CameraMovement.viewTo[1] - cM.CameraMovement.viewFrom[1],
				cM.CameraMovement.viewTo[2] - cM.CameraMovement.viewFrom[2]);

		// creating a plane
		DirectionVector = new RenderingVector(1, 1, 1);
		PlaneVector1 = ViewVector.crossProduct(DirectionVector);
		PlaneVector2 = ViewVector.crossProduct(PlaneVector1);
		P = new RenderingPlane(PlaneVector1, PlaneVector2, cM.CameraMovement.viewTo);

		// creating more needed vectors
		RotationVector = getRotationVector(cM.CameraMovement.viewFrom, cM.CameraMovement.viewTo);
		W1 = ViewVector.crossProduct(RotationVector);
		W2 = ViewVector.crossProduct(W1);

		// calculating where the camera is focused on
		CalcFocusPos = calculatePoint(cM.CameraMovement.viewFrom, cM.CameraMovement.viewTo, cM.CameraMovement.viewTo[0],
				cM.CameraMovement.viewTo[1], cM.CameraMovement.viewTo[2]);
		CalcFocusPos[0] = cM.CameraMovement.ZOOM * CalcFocusPos[0];
		CalcFocusPos[1] = cM.CameraMovement.ZOOM * CalcFocusPos[1];
	}

	/**
	 * Creates a vector storing the current rotation of the camera.
	 * 
	 * @param viewFrom Where the camera is.
	 * @param viewTo   Where the camera is focused on.
	 * @return A vector to do with the rotation.
	 */
	static RenderingVector getRotationVector(double[] viewFrom, double[] viewTo) {
		double dx = Math.abs(viewFrom[0] - viewTo[0]);
		double dy = Math.abs(viewFrom[1] - viewTo[1]);
		double xRot, yRot;
		xRot = dy / (dx + dy);
		yRot = dx / (dx + dy);

		if (viewFrom[1] > viewTo[1])
			xRot = -xRot;
		if (viewFrom[0] < viewTo[0])
			yRot = -yRot;

		RenderingVector V = new RenderingVector(xRot, yRot, 0);
		return V;
	}
}
