package cM;

import console.GraphicsConsole;
import renderer.Main;
import renderer.RenderingVector;
import renderer.Sorter;

/**
 * This class controls the movement of the camera.
 * 
 * Created: February 19, 2024 
 * Last updated: June 6, 2024
 * 
 * @author Blake Mcculligh
 */
public class CameraMovement {

	/**
	 * Where the camera is.
	 */
	public static double[] viewFrom = new double[] { -5, 5, 5 };
	/**
	 * Where the camera is pointing to.
	 */
	public static double[] viewTo = new double[] { -4, 5, 4 };

	/**
	 * The amount of zoom.
	 */
	public static final int ZOOM = 1000;

	/**
	 * How fast zooming is.
	 */
	static final double ZOOMSPEED = 0.1;
	/**
	 * How fast panning is.
	 */
	static final double MOVEMENTSPPED = 0.001;

	/**
	 * How fast rotating up and down is, lower number is faster.
	 */
	static final double VERTACLROTATINGSPEED = 1700;
	/**
	 * How fast rotating side to side is, lower number is faster.
	 */
	static final double HORAZONTALROTATINGSPEED = 150;

	/**
	 * Vertical Look goes from 0.999 to -0.999, - being down and + up.
	 */
	public static double vertLook = -0.5;
	/**
	 * Horizontal Look takes any number and goes round in radians.
	 */
	public static double horLook = 0;

	/**
	 * The old location of the mouse, used for finding the difference of its
	 * location.
	 */
	static double oldMouseX = 0, oldMouseY = 0;

	/**
	 * Keeps the average distance finder from running the first time through the
	 * loop.
	 */
	static boolean check = false;

	/**
	 * Runs all the different functions for the moving of the camera.
	 * 
	 * @param gc The graphics console.
	 */
	public static void Movement(GraphicsConsole gc) {

		RenderingVector VerticalVector = new RenderingVector(0, 0, 1);

		// in direction the camera is pointing
		RenderingVector ViewVector = new RenderingVector(viewTo[0] - viewFrom[0], viewTo[1] - viewFrom[1],
				viewTo[2] - viewFrom[2]);

		// side to side compared to the angle of view
		RenderingVector SideViewVector = ViewVector.crossProduct(VerticalVector);

		// up and down compared to the angle of view
		RenderingVector VerticalViewVector = SideViewVector.crossProduct(ViewVector);

		// distance to the closest polygon to the camera
		// does not set it on first loop as the information has not been found yet
		double distTo = 1;
		if (check == true) {
			int i = Sorter.amountOf;
			while (true && i > 0) {
				i--;
				if (Main.POrder.get(Sorter.printOrder[i]).type == 1) {
					distTo = Main.ThreeDPolygon.get(Main.POrder.get(Sorter.printOrder[i]).placeInArray).AvgDist;
					break;
				}
			}
		}
		check = true;

		// the direction the camera needs to move when panning
		panning(gc, ViewVector, VerticalViewVector, SideViewVector, distTo);
		// the direction the camera needs to move when zooming
		zoom(gc, ViewVector, distTo);

		// moves the camera when rotating
		rotating(gc);

		// updates the old locations of the mouse
		oldMouseX = gc.getMouseX();
		oldMouseY = gc.getMouseY();
	}

	/**
	 * Controls the panning side to side and up and down.
	 * 
	 * @param gc                 The graphics console.
	 * @param ViewVector         In direction the camera is pointing.
	 * @param VerticalViewVector Up and down compared to the angle of view.
	 * @param SideViewVector     Side to side compared to the angle of view.
	 * @param distTo             Distance to the closest polygon..
	 */
	static void panning(GraphicsConsole gc, RenderingVector ViewVector, RenderingVector VerticalViewVector,
			RenderingVector SideViewVector, double distTo) {

		double[] move = new double[] { 0, 0, 0 };

		if (gc.getMouseButton(0) && Main.shift) {

			double newMouseX = gc.getMouseX();
			double newMouseY = gc.getMouseY();

			move[0] -= VerticalViewVector.x * Math.pow(oldMouseY - newMouseY, 1) * Math.pow(distTo, 1);
			move[1] -= VerticalViewVector.y * Math.pow(oldMouseY - newMouseY, 1) * Math.pow(distTo, 1);
			move[2] -= VerticalViewVector.z * Math.pow(oldMouseY - newMouseY, 1) * Math.pow(distTo, 1);

			move[0] -= SideViewVector.x * Math.pow(oldMouseX - newMouseX, 1) * Math.pow(distTo, 1);
			move[1] -= SideViewVector.y * Math.pow(oldMouseX - newMouseX, 1) * Math.pow(distTo, 1);
			move[2] -= SideViewVector.z * Math.pow(oldMouseX - newMouseX, 1) * Math.pow(distTo, 1);

			viewFrom[0] += move[0] * MOVEMENTSPPED;
			viewFrom[1] += move[1] * MOVEMENTSPPED;
			viewFrom[2] += move[2] * MOVEMENTSPPED;
		}
	}

	/**
	 * Controls the zooming in and out of the camera.
	 * 
	 * @param gc         The graphics console.
	 * @param ViewVector In direction the camera is pointing.
	 * @param distTo     Distance to the closest polygon.
	 */
	static void zoom(GraphicsConsole gc, RenderingVector ViewVector, double distTo) {

		double[] move = new double[] { 0, 0, 0 };

		// amount the scroll wheel has moved
		int scrollAmount = gc.getMouseWheelUnitsToScroll();

		move[0] -= ViewVector.x * scrollAmount * Math.pow(distTo, 0.5);
		move[1] -= ViewVector.y * scrollAmount * Math.pow(distTo, 0.5);
		move[2] -= ViewVector.z * scrollAmount * Math.pow(distTo, 0.5);

		viewFrom[0] += move[0] * ZOOMSPEED;
		viewFrom[1] += move[1] * ZOOMSPEED;
		viewFrom[2] += move[2] * ZOOMSPEED;
	}

	/**
	 * Controls the rotation of the camera. It also is used for setting the camera
	 * to face the plane when the sketch function is started.
	 * 
	 * @param gc The graphics console.
	 */
	public static void rotating(GraphicsConsole gc) {

		if (gc.getMouseButton(1)) {

			double newMouseX = gc.getMouseX();
			double newMouseY = gc.getMouseY();

			double difX = (newMouseX - oldMouseX);
			double difY = (newMouseY - oldMouseY);

			difY *= 6 - Math.abs(vertLook) * 5;

			// Rotating at the desired speed
			vertLook -= difY / VERTACLROTATINGSPEED;
			horLook += difX / HORAZONTALROTATINGSPEED;

			// keeping view from getting flipped up side down
			if (vertLook > 0.999)
				vertLook = 0.999;
			if (vertLook < -0.999)
				vertLook = -0.999;

		}

		double r = Math.sqrt(1 - (vertLook * vertLook));

		// Setting where the camera is pointing
		viewTo[0] = viewFrom[0] + r * Math.cos(horLook);
		viewTo[1] = viewFrom[1] + r * Math.sin(horLook);
		viewTo[2] = viewFrom[2] + vertLook;

	}

}
