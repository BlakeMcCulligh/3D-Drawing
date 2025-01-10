package drawer;

import java.util.ArrayList;

import drawer.objects.Shape;
import drawer.objects.ThreeDObjects;
import renderer.Sorter;

/**
 * Finds lines that make a loop and then deletes them from the line array and
 * saves them as a 2D shape.
 * 
 * Created: May 14, 2024 
 * Last updated: May 15, 2024
 * 
 * @author Blake McCulligh
 */
public class LinesToShapes {

	/**
	 * If a loop is found.
	 */
	static boolean loopFound = false;

	/**
	 * Points in the chain currently being explored.
	 */
	static ArrayList<Point> p = new ArrayList<Point>();

	/**
	 * Lines in the chain currently being explored.
	 */
	static ArrayList<Integer> line = new ArrayList<Integer>();

	/**
	 * Moves closed loops of lines from the line array to the 3D shape array.
	 */
	public static void closedShapes() {

		for (int i = 0; i < ThreeDObjects.Lines.size() - 1; i++) {
			loopFound = false;
			p.clear();
			line.clear();

			// adding the first line to their arrays
			p.add(ThreeDObjects.Lines.get(i).points[0]);
			p.add(ThreeDObjects.Lines.get(i).points[1]);
			line.add(i);
			loop(i);

			if (loopFound) {

				// converting points to an array
				Point points[] = new Point[p.size()];
				for (int k = 0; k < p.size(); k++) {
					points[k] = p.get(k);
				}
			if(ThreeDObjects.Lines.get(line.get(0)).plane == null) {
				System.out.println("errer");
			}
				// creating the 2D shape
				ThreeDObjects.Shapes.add(new Shape(points, ThreeDObjects.Lines.get(line.get(0)).plane));

				// removing the lines that are now apart of the shape
				int l[] = new int[line.size()];
				for (int k = 0; k < line.size(); k++) {
					l[k] = line.get(k);
				}
				l = Sorter.bubbleSort(l);

				for (int k = 0; k < line.size(); k++) {
					ThreeDObjects.Lines.remove(l[k]);
				}

				break;
			}
		}
	}

	/**
	 * Recursion method that looks for a chin of connected lines and checks if they
	 * eventually form a loop.
	 * 
	 * @param i Integer of the first line in the chain.
	 */
	public static void loop(int i) {

		// if a loop is found exit the exit out of the loop method
		if (p.get(0).x == p.get(p.size() - 1).x && p.get(0).y == p.get(p.size() - 1).y
				&& p.get(0).z == p.get(p.size() - 1).z && line.size() > 2) {

			loopFound = true;

		} else {
			for (int j = 0; j < ThreeDObjects.Lines.size() - 1; j++) {
				for (int k = 0; k < 2; k++) {

					// looking for matching points
					if (ThreeDObjects.Lines.get(j).points[k].x == p.get(p.size() - 1).x
							&& ThreeDObjects.Lines.get(j).points[k].y == p.get(p.size() - 1).y
							&& ThreeDObjects.Lines.get(j).points[k].z == p.get(p.size() - 1).z) {

						// making sure it is not its own point
						boolean notUsed = true;
						for (int m = 0; m < line.size(); m++) {
							if (j == line.get(m)) {
								notUsed = false;
							}
						}

						if (notUsed) {

							// add point on the other side of the newly found connected line
							if (k == 0) {
								p.add(ThreeDObjects.Lines.get(j).points[1]);
							} else {
								p.add(ThreeDObjects.Lines.get(j).points[0]);
							}

							line.add(j);
							loop(i);
						}
					}
				}
			}
		}
	}
}
