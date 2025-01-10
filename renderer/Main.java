package renderer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;

import cM.CameraMovement;
import console.GraphicsConsole;
import drawer.Drawing;
import drawer.objects.ThreeDObjects;
import saving.Opening;
import saving.Saving;

/**
 * This main class is the holder of the main loop, and the functions for
 * controlling the amount of sleep and the printing.
 * 
 * Created: February 15, 2024 
 * Last updated: June 2, 2024
 * 
 * @author Blake McCulligh
 */
public class Main {

	/**
	 * The text box for the user to enter measurements.
	 */
	public static JFormattedTextField field = new JFormattedTextField();

	/**
	 * Screen size dimensions.
	 */
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	/**
	 * Full screen console.
	 */
	public static GraphicsConsole gc = new GraphicsConsole((int) screenSize.getWidth(), (int) screenSize.getHeight(),
			"SketchDown (Bata)");

	/**
	 * ArrayList of all the 3D polygon.
	 * 
	 * Planes, sides, and shapes all feed into this to be printed
	 * 
	 * Each 3D polygon has a 2D polygon inside called 'DrawablePolygon' witch has
	 * been converted form the 3D world to the 2D screen so it can be printed
	 */
	public static ArrayList<ThreeDPolygon> ThreeDPolygon = new ArrayList<ThreeDPolygon>();

	/**
	 * ArrayLists of all lines.
	 * 
	 * Lines form the drawer and axis lines feed into here
	 * 
	 * Each 3D line has a 2D line inside called 'DrawableLine' witch has been
	 * converted form the 3D world to the 2D screen so it can be printed
	 */
	public static ArrayList<ThreeDLine> ThreeDLines = new ArrayList<ThreeDLine>();

	/**
	 * stores everything to do with ordering the polygons and lines in order to be
	 * printed
	 */
	public static ArrayList<PrintOrder> POrder = new ArrayList<PrintOrder>();

	//************************************************
	// Variables used for FPS calculations and control
	//************************************************
	/**
	 * What the FPS is limited to.
	 */
	static double maxFPS = 60;
	/**
	 * How long the program sleeps between prints.
	 */
	static double sleepTime = 1000.0 / maxFPS;
	/**
	 * When the screen was last reprinted.
	 */
	static double lastRefresh = 0;
	/**
	 * When the program was lunched.
	 */
	static double startTime = System.currentTimeMillis();
	/**
	 * What was the time the FPS was last checked.
	 */
	static double lastFPSCheck = 0;
	/**
	 * Used so that the displayed FPS is only updated every 15 cycles.
	 */
	static double checks = 0;
	/**
	 * The FPS being displayed, average of the last 15 cycles.
	 */
	static double drawFPS = 0;

	//*******************
	// Keys that are down
	//*******************
	/**
	 * If shift key is down.
	 */
	public static boolean shift = false;
	/**
	 * If enter key is down.
	 */
	public static boolean enter = false;
	/**
	 * If escape key is down.
	 */
	public static boolean escape = false;

	/**
	 * Images for the buttons.
	 */
	public static Image[] buttons = new Image[14];

	/**
	 * Has the mouse been clicked in the last loop.
	 */
	public static boolean mouseClick;

	/**
	 * Main loop.
	 * 
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException, IOException {

		gc.enableMouse();
		gc.enableMouseMotion();
		gc.enableMouseWheel();

		Opening.homePage(gc);

		boolean check = false;
		
		while (true) {

			if (gc.getMouseClick() > 0) {
				mouseClick = true;
			} else {
				mouseClick = false;
			}
			
			if (check == true) {
				CameraMovement.Movement(gc);
			}
			
			ThreeDToTwoD.setPrederterminedInfo();
			ThreeDObjects.updateThreeDLines();
			ThreeDObjects.updateThreeDPolygons();

			if (check == true) {
				Sorter.setOrder();
				print(gc);
				
				Drawing.drawing(gc);
			}
			check = true;
			
			sleep(gc);
		}
	}

	/**
	 * Prints all of the lines and polygons.
	 * 
	 * @param gc The Graphics Console.
	 */
	static void print(GraphicsConsole gc) {

		synchronized (gc) {

			// Clear screen and draw background color
			gc.clear();
			gc.setColor(new Color(140, 180, 180));
			gc.fillRect(0, 0, (int) Main.screenSize.getWidth(), (int) Main.screenSize.getHeight());

			for (int i = 0; i < Sorter.amountOf; i++) {

				// printing polygons
				if (POrder.get(Sorter.printOrder[i]).type == 1) {

					ThreeDPolygon.get(POrder.get(Sorter.printOrder[i]).placeInArray).DrawablePolygon.drawSide(gc);

					// printing lines
				} else {

					gc.setStroke(4);
					ThreeDLines.get(POrder.get(Sorter.printOrder[i]).placeInArray).DrawableLine.drawLine(gc);
					gc.setStroke(1);
				}
			}
			ButtonPrinter.buttonPrinter(gc);
		}
	}

	/*
	 * Controls how long the program sleeps for and calculates the current FPS.
	 * 
	 * @param gc The graphics console.
	 */
	static void sleep(GraphicsConsole gc) {

		// time between now and the last refresh
		long timeSLU = (long) (System.currentTimeMillis() - lastRefresh);

		// updates the FPS display every 15 cycles using the average of the last 15
		// cycles
		checks++;
		if (checks >= 15) {
			drawFPS = checks / ((System.currentTimeMillis() - lastFPSCheck) / 1000.0);
			lastFPSCheck = System.currentTimeMillis();
			checks = 0;
		}

		// having the system sleep for the time needed so that the FPS is kept under the
		// max FPS
		if (timeSLU < 1000.0 / maxFPS) {
			try {
				Thread.sleep((long) (1000.0 / maxFPS - timeSLU));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		// updating the last refresh time after the sleep has occurred
		lastRefresh = System.currentTimeMillis();
	}

	/**
	 * Saves the preview image. See saving Saving.java line 144 for why this is
	 * here.
	 */
	public static void saveImage() {
		Saving.savePrevewImage(gc);
	}
	
	/**
	 * Gets the number form the text box.
	 * @return The number in the text box.
	 */
	public static double getNumberFromTextBox() {
		double number = 0;
		
		boolean neg = false;

		if (!Main.field.getText().isEmpty()) {
			String d = Main.field.getText();
			if (d.charAt(0) == '-') {
				d = d.substring(1);
				neg = true;
			}
			if (!d.isEmpty()) {
				if (number != Double.parseDouble(d) || number != -1 * Double.parseDouble(d)) {

					if (neg) {
						number = -1 * Double.parseDouble(d);
					} else {
						number = Double.parseDouble(d);
					}
				}
			}
		}
		
		return number;
	}
}
