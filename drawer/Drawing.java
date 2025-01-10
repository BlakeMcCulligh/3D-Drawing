package drawer;

import java.util.ArrayList;

import console.GraphicsConsole;
import drawer.objects.ThreeDObjects;
import renderer.ButtonPrinter;
import renderer.Main;

/**
 * This class calls the methods needed depending on what drawing function is
 * currently being used. This class also deals with the selection of an object
 * and the deleting function.
 * 
 * Created: April 15, 2024 Last updated: June 2, 2024
 * 
 * @author Blake McCulligh
 */
public class Drawing {

	/**
	 * Main for all the drawing functions. Calls the appropriate methods depending
	 * on what drawing function is currently being used.
	 * 
	 * @param gc The Graphics Console.
	 */
	public static void drawing(GraphicsConsole gc) {

		int mouseX = gc.getMouseX();
		int mouseY = gc.getMouseY();

		select(mouseX, mouseY, gc);

		// Select button
		if (ButtonPrinter.buttonClick[0]) {
			if (Main.escape) {
				unselectAll();
			}
		}

		// delete button
		if (ButtonPrinter.buttonClick[1]) {
			delete();
		}

		// plain button
		if (ButtonPrinter.buttonClick[2]) {
			CreatePlane.plainCreator(gc);
		}

		// Sketching buttons
		if (ButtonPrinter.buttonClick[3] || ButtonPrinter.buttonClick[8] || ButtonPrinter.buttonClick[9]
				|| ButtonPrinter.buttonClick[10]) {
			Scetch.main(gc);
		}

		// extrude button
		if (ButtonPrinter.buttonClick[4]) {
			Extruded.main();
		}

		// color button
		if (ButtonPrinter.buttonClick[6]) {
			ColorSeter.pickColor(gc);
			unselectAll();
		}

		if (ButtonPrinter.buttonClick[13]) {
			ColorSeter.colorPicker();
			unselectAll();
		}
	}

	/**
	 * Unselects everything that is selected.
	 */
	static void unselectAll() {
		for (int i = 0; i < ThreeDObjects.Lines.size(); i++) {
			if (ThreeDObjects.Lines.get(i).selected) {
				ThreeDObjects.Lines.get(i).cliked();
			}
		}
		for (int i = 0; i < ThreeDObjects.Planes.size(); i++) {
			if (ThreeDObjects.Planes.get(i).selected) {
				ThreeDObjects.Planes.get(i).cliked();
			}
		}
		for (int i = 0; i < ThreeDObjects.Shapes.size(); i++) {
			if (ThreeDObjects.Shapes.get(i).selected) {
				ThreeDObjects.Shapes.get(i).cliked();
			}
		}
		for (int i = 0; i < ThreeDObjects.Sides.size(); i++) {
			if (ThreeDObjects.Sides.get(i).selected) {
				ThreeDObjects.Sides.get(i).cliked();
			}
		}
	}

	/**
	 * If the enter key is clicked everything that is selected is deleted.
	 */
	public static void delete() {
		if (Main.enter) {
			for (int i = (ThreeDObjects.Lines.size() - 1); i >= 3; i--) {
				if (ThreeDObjects.Lines.get(i).selected) {
					ThreeDObjects.Lines.remove(i);
				}
			}
			for (int i = (ThreeDObjects.Planes.size() - 1); i >= 0; i--) {
				if (ThreeDObjects.Planes.get(i).selected) {
					ThreeDObjects.Planes.remove(i);
				}
			}
			for (int i = (ThreeDObjects.Shapes.size() - 1); i >= 0; i--) {
				if (ThreeDObjects.Shapes.get(i).selected) {
					ThreeDObjects.Shapes.remove(i);
				}
			}
			for (int i = (ThreeDObjects.Sides.size() - 1); i >= 0; i--) {
				if (ThreeDObjects.Sides.get(i).selected) {
					ThreeDObjects.Sides.remove(i);
				}
			}
		}
	}

	/**
	 * Changes the selection boolean of the object clicked closest to the camera.
	 * 
	 * @param mouseX The x coordinate of the mouse.
	 * @param mouseY The y coordinate of the mouse.
	 * @param gc     The Graphics Console.
	 */
	static void select(int mouseX, int mouseY, GraphicsConsole gc) {

		if (mouseY > 75 && mouseY < (int) Main.screenSize.getHeight() - 75 && !ButtonPrinter.butionClicked()) {

			// arrays storing everything that has been selected
			// what is it that was clicked (1 = plane, 2 = shape, 3 = side, 4 = line)
			ArrayList<Integer> clikedType = new ArrayList<Integer>();
			// what is the index
			ArrayList<Integer> clikedIndex = new ArrayList<Integer>();
			// how far away was it
			ArrayList<Double> clikedDist = new ArrayList<Double>();

			// saving everything that was clicked
			for (int i = 0; i < Main.ThreeDPolygon.size(); i++) {
				int[] a = Main.ThreeDPolygon.get(i).DrawablePolygon.contains(mouseX, mouseY, gc);
				if (a[0] != 0) {
					clikedType.add(a[0]);
					clikedIndex.add(a[1]);
				}
			}

			for (int i = 0; i < Main.ThreeDLines.size(); i++) {
				int[] a = Main.ThreeDLines.get(i).DrawableLine.contains(mouseX, mouseY, gc);
				if (a[0] != 0) {
					clikedType.add(a[0]);
					clikedIndex.add(a[1]);
				}
			}

			// Getting the distance to everything that was clicked
			for (int i = 0; i < clikedType.size(); i++) {
				if (clikedType.get(i) == 1) {
					clikedDist.add(ThreeDObjects.Planes.get(clikedIndex.get(i)).distance);
				} else if (clikedType.get(i) == 2) {
					clikedDist.add(ThreeDObjects.Shapes.get(clikedIndex.get(i)).distance);
				} else if (clikedType.get(i) == 3) {
					clikedDist.add(ThreeDObjects.Sides.get(clikedIndex.get(i)).distance);
				} else if (clikedType.get(i) == 4) {
					clikedDist.add(ThreeDObjects.Lines.get(clikedIndex.get(i)).distance);
				}
			}

			// finding what is closest to the camera
			double closestDist = 10000;
			int closestIndex = -1;
			int closestType = 0;
			for (int i = 0; i < clikedType.size(); i++) {
				if (closestDist >= clikedDist.get(i)) {
					closestDist = clikedDist.get(i);
					closestIndex = clikedIndex.get(i);
					closestType = clikedType.get(i);
				}
			}

			// running the clicked method for what was closest to the camera
			if (closestType == 1) {
				ThreeDObjects.Planes.get(closestIndex).cliked();
			} else if (closestType == 2) {
				ThreeDObjects.Shapes.get(closestIndex).cliked();
			} else if (closestType == 3) {
				ThreeDObjects.Sides.get(closestIndex).cliked();
			} else if (closestType == 4) {
				ThreeDObjects.Lines.get(closestIndex).cliked();
			}
		}

	}

	/**
	 * Finds the index of the plain selected.
	 * 
	 * @return The index of the plain selected.
	 */
	static int getSelectedPlane() {

		int indexOfPlane = -1;

		for (int i = 0; i < ThreeDObjects.Planes.size(); i++) {
			if (ThreeDObjects.Planes.get(i).selected) {
				indexOfPlane = i;
			}
		}
		return indexOfPlane;
	}
}
