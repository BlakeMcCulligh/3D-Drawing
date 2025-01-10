package drawer;

import console.GraphicsConsole;
import drawer.objects.Line;
import drawer.objects.Plane;
import drawer.objects.ThreeDObjects;
import renderer.ButtonPrinter;
import renderer.Main;

/**
 * This class holds everything to do with the creation of a plain.
 * 
 * Created: April 15, 2024 
 * Last updated: June 3, 2024
 * 
 * @author Blake McCulligh
 */
public class CreatePlane {

	/*
	 * corners of the plain
	 */
	static Point point1 = new Point(new double[] { 0, 0, 0 });
	static Point point2 = new Point(new double[] { 0, 0, 0 });
	static Point point3 = new Point(new double[] { 0, 0, 0 });
	static Point point4 = new Point(new double[] { 0, 0, 0 });

	/**
	 * Does a new polygon need to be created or is one currently in the making
	 * presses.
	 */
	static boolean createNew = true;

	/**
	 * What is the index of the plain currently being made.
	 */
	static int index = 0;

	/**
	 * What is the plane being created being referenced to. 1: two lines. 2: another
	 * plain.
	 */
	static int typeOfCreate;

	/**
	 * Main method for the creation of a plain.
	 * 
	 * @param gc The Graphics Console.
	 */
	static void plainCreator(GraphicsConsole gc) {

		boolean pointsMade = false;

		if (createNew) {
			startCreate();
			createNew = false;
		}

		int indexOfLine[] = new int[2];
		int indexOfPlane = -1;

		indexOfLine = getSelectedLines(indexOfLine);

		// if the user wants to create using another plain
		if (indexOfLine[0] == -1 || indexOfLine[1] == -1) {

			indexOfPlane = Drawing.getSelectedPlane();
			
			if (indexOfPlane != -1) {
				typeOfCreate = 2;
				ThreeDObjects.Planes.get(indexOfPlane).getNormal();
				pointsMade = setPoints1Side(indexOfPlane, ThreeDObjects.Planes.size() - 1);
			}

			// if the user wants to create using two lines
		} else {

			drawer.objects.Line L1 = ThreeDObjects.Lines.get(indexOfLine[0]);
			drawer.objects.Line L2 = ThreeDObjects.Lines.get(indexOfLine[1]);

			typeOfCreate = 1;

			pointsMade = setPoints2Lines(L1, L2, pointsMade);
		}

		// if the points were set make the update the plain
		if (pointsMade) {
			try {
				ThreeDObjects.Planes.get(index).points[0] = point1;
				ThreeDObjects.Planes.get(index).points[1] = point2;
				ThreeDObjects.Planes.get(index).points[2] = point3;
				ThreeDObjects.Planes.get(index).points[3] = point4;

				// does the user want to complete the creating processes
				if (Main.enter) {
					create(indexOfLine, indexOfPlane);
				}
			} catch (IndexOutOfBoundsException e) {
			}
		}

		if (Main.escape) {
			exit();
		}
	}

	/**
	 * Resetting everything and creating a new plain.
	 */
	static void startCreate() {
		Main.field.selectAll();

		point1 = new Point(new double[] { 0, 0, 0 });
		point2 = new Point(new double[] { 0, 0, 0 });
		point3 = new Point(new double[] { 0, 0, 0 });
		point4 = new Point(new double[] { 0, 0, 0 });

		ThreeDObjects.Planes.add(new Plane(point1, point2, point3, point4));

		index = ThreeDObjects.Planes.size() - 1;
	}

	/**
	 * Finds the indexes of the lines selected. If more then two lines are selected
	 * use the last two.
	 * 
	 * @param indexOfLine The indexes of the selected lines.
	 * @return The indexes of the lines selected.
	 */
	static int[] getSelectedLines(int indexOfLine[]) {
		int j = 0;

		indexOfLine[0] = -1;
		indexOfLine[1] = -1;

		for (int i = 0; i < ThreeDObjects.Lines.size(); i++) {
			if (ThreeDObjects.Lines.get(i).selected) {

				if (j < 2) {
					indexOfLine[j] = i;
					j++;
				} else {
					j = 0;
					indexOfLine[j] = i;
				}
			}
		}
		return indexOfLine;
	}

	/**
	 * Sets the points of the plain if the plain is referenced to another plain.
	 * 
	 * @param indexOfPlane    Index of the plain it is referenced to.
	 * @param indexOfNewPlane The index of the plain being created.
	 * @return If the points were set.
	 */
	static boolean setPoints1Side(int indexOfPlane, int indexOfNewPlane) {

		Plane P = ThreeDObjects.Planes.get(indexOfPlane);

		double dist = Main.getNumberFromTextBox();

		double offSetVector[] = new double[3];

		offSetVector[0] = P.normal.x * dist;
		offSetVector[1] = P.normal.y * dist;
		offSetVector[2] = P.normal.z * dist;

		point1.x = ThreeDObjects.Planes.get(indexOfPlane).points[0].x + offSetVector[0];
		point1.y = ThreeDObjects.Planes.get(indexOfPlane).points[0].y + offSetVector[1];
		point1.z = ThreeDObjects.Planes.get(indexOfPlane).points[0].z + offSetVector[2];

		point2.x = ThreeDObjects.Planes.get(indexOfPlane).points[1].x + offSetVector[0];
		point2.y = ThreeDObjects.Planes.get(indexOfPlane).points[1].y + offSetVector[1];
		point2.z = ThreeDObjects.Planes.get(indexOfPlane).points[1].z + offSetVector[2];

		point3.x = ThreeDObjects.Planes.get(indexOfPlane).points[2].x + offSetVector[0];
		point3.y = ThreeDObjects.Planes.get(indexOfPlane).points[2].y + offSetVector[1];
		point3.z = ThreeDObjects.Planes.get(indexOfPlane).points[2].z + offSetVector[2];

		point4.x = ThreeDObjects.Planes.get(indexOfPlane).points[3].x + offSetVector[0];
		point4.y = ThreeDObjects.Planes.get(indexOfPlane).points[3].y + offSetVector[1];
		point4.z = ThreeDObjects.Planes.get(indexOfPlane).points[3].z + offSetVector[2];

		return true;
	}

	/**
	 * Sets the corners of the plain depending on the types of lines.
	 * 
	 * @param L1      The first line.
	 * @param L2      The second line.
	 * @param created If the points were set.
	 * @return If the corners of the plain were set.
	 */
	static boolean setPoints2Lines(Line L1, Line L2, boolean created) {

		double[] a = { 0, 0, 0 };
		double[] b = { 0, 0, 1 };

		Line verticalLine = new Line(a, b);

		Line normal = new Line(a, Line.crossProduct(L1, L2));

		double hor[] = Line.crossProduct(normal, verticalLine);

		hor[0] = -hor[0] / 10;
		hor[1] = -hor[1] / 10;
		hor[2] = -hor[2] / 10;
		if (hor[0] == 0 && hor[0] == 0 && hor[0] == 0) {
			hor[1] = 10;
		}

		Line horazontal = new Line(a, hor);

		double edge[] = Line.crossProduct(normal, horazontal);

		edge[0] = edge[0] / 100;
		edge[1] = edge[1] / 100;
		edge[2] = edge[2] / 100;

		point1.x = L1.points[0].x;
		point1.y = L1.points[0].y;
		point1.z = L1.points[0].z;

		point2.x = L1.points[0].x + hor[0];
		point2.y = L1.points[0].y + hor[1];
		point2.z = L1.points[0].z + hor[2];

		point3.x = L1.points[0].x + hor[0] + edge[0];
		point3.y = L1.points[0].y + hor[1] + edge[1];
		point3.z = L1.points[0].z + hor[2] + edge[2];

		point4.x = L1.points[0].x + edge[0];
		point4.y = L1.points[0].y + edge[1];
		point4.z = L1.points[0].z + edge[2];

		return true;
	}

	/**
	 * Completes the creation presses.
	 * 
	 * @param indexOfLines The indexes of the lines used.
	 * @param indexOfPlane The index of the plain used.
	 */
	static void create(int indexOfLines[], int indexOfPlane) {

		createNew = true;

		if (typeOfCreate == 1) {
			ThreeDObjects.Lines.get(indexOfLines[0]).cliked();
			ThreeDObjects.Lines.get(indexOfLines[1]).cliked();
		}
		if (typeOfCreate == 2) {
			ThreeDObjects.Planes.get(indexOfPlane).cliked();
		}
	}

	/**
	 * When the escape key is clicked during the drawing presses reset everything
	 * and change to the selecting function.
	 */
	public static void exit() {

		if (!createNew) {
			ThreeDObjects.Planes.remove(ThreeDObjects.Planes.size() - 1);
		}
		createNew = true;
		if (Main.escape) {
			ButtonPrinter.buttonClick[2] = false;
			ButtonPrinter.buttonClick[0] = true;
		}
		Drawing.unselectAll();

	}
}
