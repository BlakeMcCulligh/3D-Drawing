package renderer;

/**
 * This class is an Object that holds everything that is printed to the screen
 * so that it can be sorted for farthest to closest.
 * 
 * Created: February 21, 2024 
 * Last updated: February 21, 2024
 * 
 * @author Blake McCulligh
 */
public class PrintOrder {

	/**
	 * Index of the array it is from.
	 */
	public int placeInArray;

	/**
	 * 1 is polygons, 2 is line
	 */
	public int type;

	/**
	 * Distance between the camera and the object.
	 */
	public double distance;

	/**
	 * Constructor: sets the type of thing being drawn, 1 is side, 2 is line; and
	 * distance; and place in array.
	 * 
	 * @param type     Is it a line of a polygon.
	 * @param distance The average distance form the camera to the object.
	 * @param place    Where in its array is it.
	 */
	public PrintOrder(int type, double distance, int place) {
		this.type = type;
		this.distance = distance;
		placeInArray = place;
	}
}
