package drawer;

import java.awt.Color;

import cM.CameraMovement;
import console.GraphicsConsole;
import drawer.objects.Line;
import drawer.objects.Plane;
import drawer.objects.ThreeDObjects;
import renderer.ButtonPrinter;
import renderer.Main;

/**
 * This class hold everything to do with drawing a line.
 * 
 * Created: May 16, 2024 
 * Last updated: June 5, 2024
 * 
 * @author Blake McCulligh
 */
public class DrawLine {

	/**
	 * When true in hold to draw mode when false click spot for first moint and
	 * click to add next point, escape to end drawing
	 */
	static boolean mode = true;

	/**
	 * The two end points for the line being drawn.
	 */
	static Point point1, point2;

	/**
	 * Is it looking for a first point.
	 */
	static boolean timeOne = true;

	/**
	 * The main method for the drawing line function.
	 * 
	 * @param gc           The graphics console.
	 * @param drawingPlane The plane the drawing is on.
	 */
	public static void drawLine(GraphicsConsole gc, Plane drawingPlane) {

		LinesToShapes.closedShapes();

		int m[] = Snaping.snap(gc.getMouseX(), gc.getMouseY(), point1, drawingPlane);

		if (gc.getMouseButton(0) && Main.shift == false && timeOne == true && m[1] > 75
				&& m[1] < (int) Main.screenSize.getHeight() - 75
				&& !(m[0] > (int) Main.screenSize.getWidth() - 140 && m[1] > (int) Main.screenSize.getHeight() - 250) && !ButtonPrinter.butionClicked()) {

			point1 = FindPoint.convertCursorTo3D(m[0], m[1], new Point(CameraMovement.viewFrom), drawingPlane);
			point2 = point1;

			ThreeDObjects.Lines.add(new Line(point1, point2, Color.BLACK, drawingPlane));

			timeOne = false;
		}

		if (timeOne == false && gc.getMouseButton(0) == false) {
			point2 = FindPoint.convertCursorTo3D(m[0], m[1], new Point(CameraMovement.viewFrom), drawingPlane);

			// Determines what mode the user wants
			double difx = Math.abs(point1.x - point2.x);
			double dify = Math.abs(point1.y - point2.y);
			if (difx < 0.5 && dify < 0.5)
				mode = false;

			// drag mode
			if (mode) {
				ThreeDObjects.Lines.set(ThreeDObjects.Lines.size() - 1,
						new Line(point1, point2, Color.BLACK, drawingPlane));
				timeOne = true;
			}
		}
		// 2 click mode
		if (gc.getMouseButton(0) && Main.shift == false && mode == false && m[1] > 75
				&& m[1] < (int) Main.screenSize.getHeight() - 75
				&& !(m[0] > (int) Main.screenSize.getWidth() - 140 && m[1] > (int) Main.screenSize.getHeight() - 250) && !ButtonPrinter.butionClicked()) {
			point2 = FindPoint.convertCursorTo3D(m[0], m[1], new Point(CameraMovement.viewFrom), drawingPlane);
			ThreeDObjects.Lines.set(ThreeDObjects.Lines.size() - 1,
					new Line(point1, point2, Color.BLACK, drawingPlane));
			timeOne = true;
			mode = true;
		}
		if (Main.escape ) {
		endDrawing();
		}
		updateWhileDrawing(drawingPlane, m);

	}

	/**
	 * When escape is clicked end the drawing.
	 */
	public static void endDrawing() {
		
			if (timeOne == false) {
				ThreeDObjects.Lines.remove(ThreeDObjects.Lines.size() - 1);
			}
			mode = true;
			timeOne = true;
			Scetch.plainSelected = false;
			Scetch.setLook = false;

			if(Main.escape) {
			ButtonPrinter.buttonClick[3] = false;
			ButtonPrinter.buttonClick[0] = true;
			}
			try {
			ThreeDObjects.Planes.get(Scetch.indexOfPlane).selected = false;
			} catch(IndexOutOfBoundsException e) {
				
			}
		
	}

	/**
	 * Renders the line while it is being drawn.
	 * 
	 * @param drawingPlane The plane the drawing is on.
	 * @param m            The coordinates of the cusser.
	 */
	static void updateWhileDrawing(Plane drawingPlane, int[] m) {
		if (timeOne == false) {
			point2 = FindPoint.convertCursorTo3D(m[0], m[1], new Point(CameraMovement.viewFrom), drawingPlane);
			ThreeDObjects.Lines.set(ThreeDObjects.Lines.size() - 1,
					new Line(point1, point2, Color.BLACK, drawingPlane));
		}
	}
}
