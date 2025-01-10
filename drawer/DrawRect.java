package drawer;

import cM.CameraMovement;
import console.GraphicsConsole;
import drawer.objects.Plane;
import drawer.objects.Shape;
import drawer.objects.ThreeDObjects;
import renderer.ButtonPrinter;
import renderer.Main;

/**
 * This class runs the drawing rectangle function.
 * 
 * Created: May 17, 2024 Last updated: June 5, 2024
 * 
 * @author Blake McCulligh
 */
public class DrawRect {

	/**
	 * Has the rectangle drawing presses been started.
	 */
	static boolean timeOne = true;

	/**
	 * The corners of the rectangle.
	 */
	static Point p[] = new Point[4];

	/**
	 * When true mode is in hold to draw mode, when false click spot for first point
	 * and click to add next point.
	 */
	static boolean mode = true;

	/**
	 * The main method for the rectangle drawing function.
	 * 
	 * @param gc           The graphics console.
	 * @param drawingPlane The plane the rectangle is to be drawn on.
	 */
	public static void drawRect(GraphicsConsole gc, Plane drawingPlane) {

		// getting the position of the mouse
		int m[] = Snaping.snap(gc.getMouseX(), gc.getMouseY(), p[0], drawingPlane);
		Point mouse = FindPoint.convertCursorTo3D(m[0], m[1], new Point(CameraMovement.viewFrom), drawingPlane);

		// first point of the rectangle
		if (gc.getMouseButton(0) && Main.shift == false && timeOne == true && m[1] > 75
				&& m[1] < (int) Main.screenSize.getHeight() - 75
				&& !(m[0] > (int) Main.screenSize.getWidth() - 140 && m[1] > (int) Main.screenSize.getHeight() - 250)) {
			p[0] = new Point(mouse.x, mouse.y, mouse.z);

			p[1] = new Point(p[0].x, p[0].y, p[0].z);
			p[2] = new Point(p[0].x, p[0].y, p[0].z);
			p[3] = new Point(p[0].x, p[0].y, p[0].z);

			ThreeDObjects.Shapes.add(new Shape(p, drawingPlane));

			timeOne = false;
		}

		if (timeOne == false && gc.getMouseButton(0) == false && m[1] > 75
				&& m[1] < (int) Main.screenSize.getHeight() - 75
				&& !(m[0] > (int) Main.screenSize.getWidth() - 140 && m[1] > (int) Main.screenSize.getHeight() - 250)) {

			// updating the rectangle in the middle of the drawing proses.
			p[2] = mouse;
			getOtherPoints();
			ThreeDObjects.Shapes.set(ThreeDObjects.Shapes.size() - 1, new Shape(p, drawingPlane));

			// Determines what mode the user wants
			double difx = Math.abs(p[0].x - p[2].x);
			double dify = Math.abs(p[0].y - p[2].y);
			if (difx < 0.5 && dify < 0.5)
				mode = false;

			// drag mode
			if (mode) {
				ThreeDObjects.Shapes.set(ThreeDObjects.Shapes.size() - 1, new Shape(p, drawingPlane));
				timeOne = true;
			}
		}

		// 2 click mode
		if (gc.getMouseButton(0) && Main.shift == false && mode == false && m[1] > 75
				&& m[1] < (int) Main.screenSize.getHeight() - 75
				&& !(m[0] > (int) Main.screenSize.getWidth() - 140 && m[1] > (int) Main.screenSize.getHeight() - 250)) {
			p[2] = mouse;
			getOtherPoints();

			ThreeDObjects.Shapes.set(ThreeDObjects.Shapes.size() - 1, new Shape(p, drawingPlane));

			for (int i = 0; i < 4; i++) {
				p[i] = null;
			}
			timeOne = true;
			mode = true;

			// waiting for user to release mouse button in order for there to not be a new
			// rectangle instantly started when this one is ended
			while (gc.getMouseButton(0)) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (Main.escape) {
			endDrawing();
		}
	}

	/**
	 * Determines the second and forth corners of the rectangle.
	 */
	static void getOtherPoints() {

		p[1].x = p[2].x;
		p[1].y = p[2].y;
		p[1].z = p[0].z;

		p[3].x = p[0].x;
		p[3].y = p[0].y;
		p[3].z = p[2].z;

		if (p[0].z == p[2].z) {
			p[1].x = p[0].x;
			p[1].y = p[2].y;
			p[1].z = p[0].z;

			p[3].x = p[2].x;
			p[3].y = p[0].y;
			p[3].z = p[0].z;
		}
	}

	/**
	 * When escape is clicked end the drawing.
	 */
	public static void endDrawing() {

		if (timeOne == false) {
			ThreeDObjects.Shapes.remove(ThreeDObjects.Shapes.size() - 1);
		}
		mode = true;
		timeOne = true;
		Scetch.plainSelected = false;
		Scetch.setLook = false;

		if (Main.escape) {
			ButtonPrinter.buttonClick[8] = false;
			ButtonPrinter.buttonClick[0] = true;
		}
		try {
			ThreeDObjects.Planes.get(Scetch.indexOfPlane).selected = false;
		} catch (IndexOutOfBoundsException e) {

		}

	}
}
