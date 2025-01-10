package drawer;

import java.awt.Color;

import drawer.objects.Shape;
import drawer.objects.Side;
import drawer.objects.ThreeDObjects;
import renderer.ButtonPrinter;
import renderer.Main;

/**
 * This class holds everything to do with the extruding function.
 * 
 * Created: May 20, 2024 
 * Last updated: May 22, 2024
 * 
 * @author Blake McCulligh
 */
public class Extruded {

	/**
	 * If the creation process is currently in process.
	 */
	static boolean createNew = true;

	/**
	 * What is the index of the shape being used.
	 */
	static int indexOfShape = -1;

	/**
	 * What is the first index of the sides for this extrud.
	 */
	static int startIndex = -1;

	/**
	 * The length the extrud is to be.
	 */
	static double dist = 0;
	/**
	 * The last distance the extrud was to be.
	 */
	static double oldDist = 0;

	/**
	 * The main method for the extrud function.
	 */
	public static void main() {

		if (createNew) {
			startNew();
		}

		if (!createNew) {
			updateSides();

			// escape key
			if (Main.escape) {
				createNew = true;
				for (int i = ThreeDObjects.Sides.size() - 1; i >= startIndex; i--) {
					ThreeDObjects.Sides.remove(i);
				}
				ButtonPrinter.buttonClick[4] = false;
				ButtonPrinter.buttonClick[0] = true;
			}

			if (Main.enter) {
				Finish();
			}
		}
	}

	/**
	 * Starts the creation process by getting the selected shape and creating the
	 * sides.
	 */
	static void startNew() {
		for (int i = 0; i < ThreeDObjects.Shapes.size(); i++) {
			if (ThreeDObjects.Shapes.get(i).selected) {
				createNew = false;
				indexOfShape = i;

			}
		}

		if (createNew == false) {
			createSides();
		}
	}

	/**
	 * Creates the sides after the shape is picked.
	 */
	static void createSides() {

		Shape s = null;

			s = ThreeDObjects.Shapes.get(indexOfShape);
		
		int numOfEdges = s.points.length;

		// first Side
		Point side1[] = new Point[numOfEdges];
		for (int i = 0; i < side1.length; i++) {
			side1[i] = new Point(s.points[i].x, s.points[i].y, s.points[i].z);
		}
		ThreeDObjects.Sides.add(new Side(side1, Color.WHITE));
		startIndex = ThreeDObjects.Sides.size() - 1;

		// second side
		ThreeDObjects.Sides.add(new Side(side1, Color.WHITE));

		// other sides
		for (int i = 0; i < numOfEdges; i++) {
			Point sideOther[] = new Point[4];
			sideOther[0] = new Point(side1[i].x, side1[i].y, side1[i].z);
			sideOther[1] = new Point(side1[i].x, side1[i].y, side1[i].z);

			if ((i + 1) > (side1.length - 1)) {
				sideOther[2] = new Point(side1[0].x, side1[0].y, side1[0].z);
				sideOther[3] = new Point(side1[0].x, side1[0].y, side1[0].z);
			} else {
				sideOther[2] = new Point(side1[i + 1].x, side1[i + 1].y, side1[i + 1].z);
				sideOther[3] = new Point(side1[i + 1].x, side1[i + 1].y, side1[i + 1].z);
			}
			ThreeDObjects.Sides.add(new Side(sideOther, Color.WHITE));
		}
	}

	/**
	 * Updates the sides whenever the text box is changed to match the current value
	 * in the text box.
	 */
	static void updateSides() {

		dist = Main.getNumberFromTextBox();

		if (dist != oldDist) {
			
			double offSetVector[] = new double[3];
		
			offSetVector[0] = ThreeDObjects.Shapes.get(indexOfShape).plane.normal.x * dist;
			offSetVector[1] = ThreeDObjects.Shapes.get(indexOfShape).plane.normal.y * dist;
			offSetVector[2] = ThreeDObjects.Shapes.get(indexOfShape).plane.normal.z * dist;
		

			// add that vector to the first side to get the second side
			Point side2[] = new Point[ThreeDObjects.Sides.get(startIndex).points.length];
			for (int i = 0; i < side2.length; i++) {
				side2[i] = new Point(ThreeDObjects.Sides.get(startIndex).points[i].x,
						ThreeDObjects.Sides.get(startIndex).points[i].y,
						ThreeDObjects.Sides.get(startIndex).points[i].z);
			}

			for (int i = 0; i < side2.length; i++) {
				side2[i].x = side2[i].x + offSetVector[0];
				side2[i].y = side2[i].y + offSetVector[1];
				side2[i].z = side2[i].z + offSetVector[2];
			}

			ThreeDObjects.Sides.set(startIndex + 1, new Side(side2, Color.WHITE));

			// setting the other sides
			for (int i = 0; i < side2.length; i++) {

				Point sideOther[] = new Point[4];

				sideOther[0] = new Point(ThreeDObjects.Sides.get(startIndex).points[i].x,
						ThreeDObjects.Sides.get(startIndex).points[i].y,
						ThreeDObjects.Sides.get(startIndex).points[i].z);
				sideOther[1] = new Point(side2[i].x, side2[i].y, side2[i].z);

				if ((i + 1) > (side2.length - 1)) {
					sideOther[2] = new Point(side2[0].x, side2[0].y, side2[0].z);
					sideOther[3] = new Point(ThreeDObjects.Sides.get(startIndex).points[0].x,
							ThreeDObjects.Sides.get(startIndex).points[0].y,
							ThreeDObjects.Sides.get(startIndex).points[0].z);

				} else {
					sideOther[2] = new Point(side2[i + 1].x, side2[i + 1].y, side2[i + 1].z);
					sideOther[3] = new Point(ThreeDObjects.Sides.get(startIndex).points[i + 1].x,
							ThreeDObjects.Sides.get(startIndex).points[i + 1].y,
							ThreeDObjects.Sides.get(startIndex).points[i + 1].z);

				}
				ThreeDObjects.Sides.set(startIndex + 2 + i, new Side(sideOther, Color.WHITE));
			}
			oldDist = dist;
		}
	}

	/**
	 * Resets variables and removes the shape used.
	 */
	static void Finish() {
		createNew = true;

			ThreeDObjects.Shapes.remove(indexOfShape);

	}

}
