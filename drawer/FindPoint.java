package drawer;

import drawer.objects.Plane;
import renderer.ThreeDToTwoD;

/**
 * This class, using the cursors location and the z coordinate of 0 it finds the coordinates
 * of the courser in the rendering that has an z coordinate of 0.
 * 
 * Created Mar. 21, 2024
 * 
 * @author Blake McCulligh
 */
public class FindPoint {

	static double a, b, c;
	static double d, e;
	static double f, g, h;
	static double i, j, k;
	static double l, m, n;
	static double o, p, q;

	static double Var1, Var2, Var3, Var4, Var5, Var6, Var7, Var8;
	
	/**
	 * Converts the location of the courser to a location on the screen using a
	 * given plain.
	 * 
	 * @param cx    The x coordinate of the mouse.
	 * @param cy    The y coordinate of the mouse.
	 * @param p1    The point where the camera is.
	 * @param plain The plain the point being found is to be on.
	 * @return The coordinates of the point in the 3D world.
	 */
	public static Point convertCursorTo3D(int cx, int cy, Point p1, Plane plain) {

		Point p2 = new Point(FindPoint.find(cx, cy));

		return plain.findIntersection(p1, p2);
	}
	
	/**
	 * Finds the coordinate the mouse is located if the z coordinate is zero.
	 * 
	 * @param screenX The x coordinate of the courser interns of the graphics
	 *                console.
	 * @param screenY The y coordinate of the courser interns of the graphics
	 *                console.
	 * @return The point where the courser is located.
	 */
	public static double[] find(double screenX, double screenY) {

		int z = 0;

		ThreeDToTwoD.setPrederterminedInfo();

		setPrederterminedInfo(screenX, screenY);

		double y = (-Var4 * Var7 * z + Var3 * Var8 * z - Var4 * Var1 + Var3 * Var2) / (Var4 * Var5 - Var3 * Var6);
		double x = (-Var5 * y - Var7 * z - Var1) / Var3;

		return new double[] { x, y, z };
	}

	/**
	 * Sets the variables for the calculations.
	 * 
	 * @param screenX The x coordinate of the courser interns of the graphics
	 *                console.
	 * @param screenY The y coordinate of the courser interns of the graphics
	 *                console.
	 * 
	 */
	public static void setPrederterminedInfo(double screenX, double screenY) {

		a = cM.CameraMovement.viewFrom[0];
		b = cM.CameraMovement.viewFrom[1];
		c = cM.CameraMovement.viewFrom[2];

		d = (screenX - renderer.Main.screenSize.getWidth() / 2 + ThreeDToTwoD.CalcFocusPos[0]) / 1000;
		e = (screenY - renderer.Main.screenSize.getHeight() / 2 + ThreeDToTwoD.CalcFocusPos[1]) / 1000;

		f = ThreeDToTwoD.W1.x;
		g = ThreeDToTwoD.W1.y;
		h = ThreeDToTwoD.W1.z;

		i = ThreeDToTwoD.W2.x;
		j = ThreeDToTwoD.W2.y;
		k = ThreeDToTwoD.W2.z;

		l = ThreeDToTwoD.P.PV.x;
		m = ThreeDToTwoD.P.PV.y;
		n = ThreeDToTwoD.P.PV.z;

		o = ThreeDToTwoD.P.P[0];
		p = ThreeDToTwoD.P.P[1];
		q = ThreeDToTwoD.P.P[2];

		Var1 = (d * l * a + d * m * b + d * n * c) - (l * o + m * p + n * q) * (a * i + b * j + c * k);
		Var2 = (e * l * a + e * m * b + e * n * c) - (l * o + m * p + n * q) * (a * f + b * g + c * h);

		Var3 = i * (l * o + m * p + n * q - m * b - n * c) + j * b * l + k * c * l - d * l;
		Var4 = f * (l * o + m * p + n * q - m * b - n * c) + g * b * l + h * c * l - e * l;

		Var5 = i * a * m + j * (l * o + m * p + n * q - l * a - n * c) + k * c * m - d * m;
		Var6 = f * a * m + g * (l * o + m * p + n * q - l * a - n * c) + h * c * m - e * m;

		Var7 = i * a * n + j * b * n + k * (l * o + m * p + n * q - l * a - m * b) - d * n;
		Var8 = f * a * n + g * b * n + h * (l * o + m * p + n * q - l * a - m * b) - e * n;
	}
}
