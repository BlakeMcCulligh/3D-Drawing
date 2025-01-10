package drawer.objects;

import java.util.ArrayList;

import drawer.Point;
import renderer.Main;
import renderer.ThreeDLine;
import renderer.ThreeDPolygon;

/**
 * This class holds all the things the user can create and converts it to the array to get
 * printed
 * 
 * Created April 15, 2024
 * Last Updated June 2, 2024
 * 
 * @author Blake McCulligh
 */
public class ThreeDObjects {

	/**
	 * All the points in the 3D world.
	 */
	public static ArrayList<Point> Points = new ArrayList<Point>();

	/**
	 * Lines drawn by the user, does include the axis lines in the first 3 slots.
	 */
	public static ArrayList<Line> Lines = new ArrayList<Line>();

	/**
	 * Planes created by the user.
	 */
	public static ArrayList<Plane> Planes = new ArrayList<Plane>();

	/**
	 * Shapes created by the user.
	 */
	public static ArrayList<Shape> Shapes = new ArrayList<Shape>();
	
	/**
	 * Side created by the user.
	 */
	public static ArrayList<Side> Sides = new ArrayList<Side>();


	/**
	 * Moves all the lines to the threeDLines array in order to get printed.
	 */
	public static void updateThreeDLines() {
		
		Main.POrder.clear();
		Main.ThreeDLines.clear();
		Main.ThreeDPolygon.clear();
		Points.clear();

		updateThreeDPolygons();

		for (int i = 0; i < ThreeDObjects.Lines.size()-1; i++) {
			ThreeDObjects.Points.add(ThreeDObjects.Lines.get(i).points[0]);
			ThreeDObjects.Points.add(ThreeDObjects.Lines.get(i).points[1]);
		}

		for (int i = 0; i < Lines.size(); i++) {

			// if the line is to long to get printed without cutting out, move it over in
			// segments
			if (Lines.get(i).length > 2) {

				Point last = subPoint(Lines.get(i).points[0], Lines.get(i).points[1], Lines.get(i).length - 2,
						Lines.get(i).length);

				for (int j = 0; j < Lines.get(i).length - 2; j += 2) {

					Main.ThreeDLines.add(new ThreeDLine(
							subPoint(Lines.get(i).points[0], Lines.get(i).points[1], j, Lines.get(i).length),
							subPoint(Lines.get(i).points[0], Lines.get(i).points[1], j + 2, Lines.get(i).length),
							Lines.get(i).c, i));

					last = subPoint(Lines.get(i).points[0], Lines.get(i).points[1], j + 2, Lines.get(i).length);
				}

				Main.ThreeDLines.add(new ThreeDLine(last, Lines.get(i).points[1], Lines.get(i).c, i));

				// if not simply move the info over
			} else {
				Main.ThreeDLines.add(new ThreeDLine(Lines.get(i).points[0], Lines.get(i).points[1], Lines.get(i).c, i));
			}
		}
	}

	/**
	 * Moves all the planes, shapes, and sides to the threeDLines array in order to get printed.
	 */
	public static void updateThreeDPolygons() {

		for (int i = 0; i < Planes.size(); i++) {

			Main.ThreeDPolygon.add(new ThreeDPolygon(
					new double[] { Planes.get(i).points[0].x, Planes.get(i).points[1].x, Planes.get(i).points[2].x,
							Planes.get(i).points[3].x },
					new double[] { Planes.get(i).points[0].y, Planes.get(i).points[1].y, Planes.get(i).points[2].y,
							Planes.get(i).points[3].y },
					new double[] { Planes.get(i).points[0].z, Planes.get(i).points[1].z, Planes.get(i).points[2].z,
							Planes.get(i).points[3].z },
					Planes.get(i).c, i,1));
		}

		for (int i = 0; i < Shapes.size(); i++) {

			double[] x = new double[Shapes.get(i).points.length];
			double[] y = new double[Shapes.get(i).points.length];
			double[] z = new double[Shapes.get(i).points.length];

			for (int k = 0; k < x.length; k++) {
				x[k] = Shapes.get(i).points[k].x;
				y[k] = Shapes.get(i).points[k].y;
				z[k] = Shapes.get(i).points[k].z;
			}

			Main.ThreeDPolygon.add(new ThreeDPolygon(x, y, z, Shapes.get(i).c, i,2));
		}
		
		for (int i = 0; i < Sides.size(); i++) {

			double[] x = new double[Sides.get(i).points.length];
			double[] y = new double[Sides.get(i).points.length];
			double[] z = new double[Sides.get(i).points.length];

			for (int k = 0; k < x.length; k++) {
				x[k] = Sides.get(i).points[k].x;
				y[k] = Sides.get(i).points[k].y;
				z[k] = Sides.get(i).points[k].z;
			}

			Main.ThreeDPolygon.add(new ThreeDPolygon(x, y, z, Sides.get(i).c, i,3));
		}
	}

	/**
	 * Finds a point on a line, a certain distance from the start.
	 * 
	 * @param startPoint The start of the line.
	 * @param endPoint The end of the line.
	 * @param distanceIn The distance form the start the point is to be found at.
	 * @param length The length of the line.
	 * @return The point on the line distanceIn from startPoint.
	 */
	static Point subPoint(Point startPoint, Point endPoint, double distanceIn, double length) {

		double midX = (startPoint.x + ((endPoint.x - startPoint.x) / length) * distanceIn);
		double midY = (startPoint.y + ((endPoint.y - startPoint.y) / length) * distanceIn);
		double midZ = (startPoint.z + ((endPoint.z - startPoint.z) / length) * distanceIn);

		Point midPoint = new Point(midX, midY, midZ);

		return midPoint;
	}
}
