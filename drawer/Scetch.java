package drawer;

import cM.CameraMovement;
import console.GraphicsConsole;
import drawer.objects.Plane;
import drawer.objects.ThreeDObjects;
import renderer.ButtonPrinter;
import renderer.Main;

/**
 * This class is the hub for all of the sketching function. When one of these is
 * selected, this class obtains the selected plane to be sketched on, set the
 * camera to be looking straight on to that plane, and calls that picked
 * sketching method.
 * 
 * Created: May 14, 2024 
 * Last updated: June 5, 2024
 * 
 * @author Blake McCulligh
 *
 */
public class Scetch {

	/**
	 * What plane is being sketched on.
	 */
	public static Plane drawingPlane;

	/**
	 * Has a plane been selected.
	 */
	public static boolean plainSelected = false;

	/**
	 * What is the index of the drawing plane.
	 */
	public static int indexOfPlane = -1;

	/**
	 * Has the user been set to look directly onto the drawing plane.
	 */
	static boolean setLook = false;

	/**
	 * Handles what of the sketching functions are being used and the set up for
	 * starting to sketch.
	 * 
	 * @param gc The graphics console.
	 */
	public static void main(GraphicsConsole gc) {

		selectsPlane();

		if (plainSelected) {

			if (!setLook) {
				lookStraightOn();
				setLook = true;
			}

			whatScetchingFunction(gc);
		}
	}

	/**
	 * Getting the most recently selected plane.
	 */
	static void selectsPlane() {
		if (plainSelected == false) {
			indexOfPlane = Drawing.getSelectedPlane();
			if (indexOfPlane != -1) {
				ThreeDObjects.Planes.get(indexOfPlane).getNormal();
				drawingPlane = ThreeDObjects.Planes.get(indexOfPlane);
				if (Main.enter) {
					plainSelected = true;
				}
			}
		}
	}

	/**
	 * When a user enters a drawing function and picks there plane this method makes
	 * it so they are automatically looking straight on to the plane.
	 */
	static void lookStraightOn() {
		
		double xAvg = (drawingPlane.points[0].x + drawingPlane.points[1].x + drawingPlane.points[2].x
				+ drawingPlane.points[3].x) / 4;
		double yAvg = (drawingPlane.points[0].y + drawingPlane.points[1].y + drawingPlane.points[2].y
				+ drawingPlane.points[3].y) / 4;
		double zAvg = (drawingPlane.points[0].z + drawingPlane.points[1].z + drawingPlane.points[2].z
				+ drawingPlane.points[3].z) / 4;
		drawingPlane.getNormal();
	
		CameraMovement.viewTo = new double[] { xAvg, yAvg, zAvg };
		CameraMovement.viewFrom = new double[] { xAvg + drawingPlane.normal.x * 1.4,
				yAvg + drawingPlane.normal.y * 1.4,
				zAvg + drawingPlane.normal.z * 1.4 };

		CameraMovement.vertLook = CameraMovement.viewTo[2] - CameraMovement.viewFrom[2];
		
		if (CameraMovement.vertLook > 0.999)
			CameraMovement.vertLook = 0.999;
		if (CameraMovement.vertLook < -0.999)
			CameraMovement.vertLook = -0.999;

		double viewToFind[] = new double[3];

		double closestDif = 10000;
		double closestIndex = 0;
		
		double r = Math.sqrt(1 - (CameraMovement.vertLook * CameraMovement.vertLook));
		viewToFind[2] = CameraMovement.viewFrom[2] + CameraMovement.vertLook;

		for(int i = 0; i < 63; i++) {
			viewToFind[0] = CameraMovement.viewFrom[0] + r * Math.cos(i*0.1);
			viewToFind[1] = CameraMovement.viewFrom[1] + r * Math.sin(i*0.1);
			double dif = Math.pow(Math.pow(Math.abs(CameraMovement.viewTo[0] - viewToFind[0]), 2)  +Math.pow(Math.abs(CameraMovement.viewTo[1] - viewToFind[1]), 2),0.5) ;
			if (dif < closestDif) {
				closestDif = dif;
				closestIndex = i;
			}
		}
		
		CameraMovement.horLook = closestIndex*0.1;
		
		
	}

	/**
	 * Calling the sketching function the user is using.
	 * 
	 * @param gc The graphics console.
	 */
	static void whatScetchingFunction(GraphicsConsole gc) {
		// draw line
		if (ButtonPrinter.buttonClick[3]) {
			DrawLine.drawLine(gc, drawingPlane);
		}
		if (ButtonPrinter.buttonClick[8]) {
			DrawRect.drawRect(gc, drawingPlane);
		}
		if (ButtonPrinter.buttonClick[9]) {

		}
		if (ButtonPrinter.buttonClick[10]) {

		}
	}
}
