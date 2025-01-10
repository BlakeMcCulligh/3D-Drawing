package renderer;

/**
 * Sorts things so they are printed in the correct order so that the ones
 * closest to the camera are printed last. 
 * 
 * Created: February 21, 2024 
 * Last updated: May 3, 2024
 * 
 * @author Blake McCulligh (not my sorting algorithm)
 */
public class Sorter {

	/**
	 * How many lines and sides are currently being drawn.
	 */
	public static int amountOf;

	/**
	 * Higher the number closer to the camera and later in the printing order.
	 */
	public static int printOrder[];

	/**
	 * Finds the order so that the side or line closest to the camera is drawn last
	 * so that objects behind others are not shown in front.
	 * 
	 * It does not reorder the array it instead makes a new array storing the index
	 * of the side or line and reorders it.
	 */
	public static void setOrder() {

		// updating the number of things to be drawn
		amountOf = Main.ThreeDLines.size() + Main.ThreeDPolygon.size();

		// setting up print order array
		printOrder = new int[amountOf];
		for (int i = 0; i < amountOf; i++) {
			printOrder[i] = i;
		}

		//setting up distance array
		double dist[] = new double[printOrder.length];
		for (int i = 0; i < printOrder.length; i++) {
			dist[i] = Main.POrder.get(i).distance;
		}

		quickSort(printOrder, dist, 0, printOrder.length - 1);
	}

	/**
	 * QuickSort algorithm to sort the the array distances into descending order by
	 * sorting an array of their indexes.
	 * 
	 * @param arr  Array of the indexes.
	 * @param dist Array of the distances to each object.
	 * @param low  The lowest index.
	 * @param high The highest index.
	 */
	static void quickSort(int[] arr, double[] dist, int low, int high) {
		if (low < high) {

			// pa is partitioning index
			int pa = partition(arr, dist, low, high);

			// Separately sort elements before and after partition
			quickSort(arr, dist, low, pa - 1);
			quickSort(arr, dist, pa + 1, high);
		}
	}

	/**
	 * This function takes last element as pivot, places the pivot element at its
	 * correct position in sorted array, and places all greater to left and all
	 * smaller elements to right of pivot.
	 * 
	 * @param arr  Array of the indexes.
	 * @param dist Array of the distances to each object.
	 * @param low  The lowest index.
	 * @param high The highest index.
	 * @return The parting index.
	 */
	static int partition(int[] arr, double[] dist, int low, int high) {

		double pivot = dist[arr[high]];

		// Index of larger element and indicates the right position of pivot found so
		// far
		int i = (low - 1);

		for (int j = low; j <= high - 1; j++) {

			// If current element is larger than the pivot
			if (dist[arr[j]] > pivot) {

				// Increment index of larger element
				i++;
				swap(arr, i, j);
			}
		}
		swap(arr, i + 1, high);
		return (i + 1);
	}

	/**
	 * Swaps two elements in a array.
	 * 
	 * @param arr The array.
	 * @param i   The first index.
	 * @param j   The second index.
	 */
	static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
	
	/**
	 * Using a bubble sort algorithm sorters the numbers in decendinging order.
	 * 
	 * @param array The array of numbers to be sorted.
	 */
	public static int[] bubbleSort(int array[]) {

		int temp;
		boolean swapped;

		for (int i = 0; i < array.length - 1; i++) {

			swapped = false;

			for (int j = 0; j < array.length - i - 1; j++) {

				// does a swap need to take place
				if (array[j] < array[j + 1]) {

					// Swap array[j] and array[j+1]
					temp = array[j];
					array[j] = array[j + 1];
					array[j + 1] = temp;
					swapped = true;
				}
			}

			// If no two elements were
			// swapped by inner loop, then break
			if (swapped == false)
				break;
		}
		return array;
	}
}
