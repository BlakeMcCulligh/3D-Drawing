package renderer;

import java.awt.Color;
import java.awt.Font;

import console.GraphicsConsole;
import drawer.ColorSeter;
import drawer.CreatePlane;
import drawer.DrawLine;
import drawer.DrawRect;
import drawer.Snaping;

/**
 * This class controls all of the buttons along with the printing of other static parts on
 * the screen.
 * 
 * Created: April 25, 2024 
 * Last updated: June 5, 2024
 * 
 * @author Blake McCulligh
 */
public class ButtonPrinter {

	/**
	 * What drop down is open.
	 */
	public static boolean dropdown[] = new boolean[3];

	/**
	 * What button is selected.
	 */
	public static boolean buttonClick[] = new boolean[14];
	
	/**
	 * The button down last time though the loop.
	 */
	static boolean oldButton[] = new boolean[14];

	/**
	 * Controls all of the buttons along with the printing of other static parts on
	 * the screen.
	 * 
	 * @param gc The graphics console.
	 */
	public static void buttonPrinter(GraphicsConsole gc) {
		
		if (buttonClick[3] || buttonClick[8] || buttonClick[9] || buttonClick[10]) {
			Snaping.snapingPrintingAndButtons(gc);
		}
		gc.setColor(new Color(240, 240, 245));

		gc.fillRect(0, (int) Main.screenSize.getHeight() - 75, (int) Main.screenSize.getWidth(), 75);
		gc.fillRect(0, 0, (int) Main.screenSize.getWidth(), 60);

		gc.setColor(Color.BLACK);
		Font mesurFont = new Font("SansSerif", Font.PLAIN, 25);
		gc.setFont(mesurFont);

		// drawing the measurements title
		gc.drawString("Measurements", (int) Main.screenSize.getWidth() - 475, (int) Main.screenSize.getHeight() - 40);
		// draw the FPS display
		gc.drawString("FPS: " + (int) Main.drawFPS, 40, (int) Main.screenSize.getHeight() - 40);

		buttonsMain(gc);
	}

	/**
	 * Handles the printing and controlling of all the buttons.
	 * 
	 * @param gc The graphics console.
	 */
	static void buttonsMain(GraphicsConsole gc) {
		buttons(gc, 0, 13);

		buttons(gc, 1, 60);

		buttons(gc, 2, 110);

		buttons(gc, 3, 160);
		drawDropdown(gc);

		buttons(gc, 4, 230);

		buttons(gc, 5, 280);
		moveDropdown(gc);

		buttons(gc, 6, 350);
		colorDropdown(gc);

		buttons(gc, 7, 420);

		closedSelectionDesplay(gc);
		
		resetfunctions();
	}

	/**
	 * Draws and manages selection of a button.
	 * 
	 * @param gc  The graphics Console.
	 * @param num What button is it.
	 * @param x   What is the x coordinate of the left side of the button.
	 */
	static void buttons(GraphicsConsole gc, int num, int x) {
		if (buttonClick[num]) {
			gc.setColor(Color.LIGHT_GRAY);
			gc.fillRoundRect(x, 10, 45, 45, 20, 20);
		}
		gc.drawImage(Main.buttons[num], x, 10);
		if ((x - 5) < gc.getMouseX() && gc.getMouseX() < (x + 50) && gc.getMouseY() < 60 && Main.mouseClick) {
			buttonClicked(num);
		}
	}

	/**
	 * The drawing and selection of the drop down arrow for the drawing drop down
	 * and the button in the drop down.
	 * 
	 * @param gc The graphics Console.
	 */
	static void drawDropdown(GraphicsConsole gc) {
		gc.setColor(Color.BLACK);
		gc.fillPolygon(new int[] { 210, 225, 217 }, new int[] { 40, 40, 47 }, 3);

		if (210 < gc.getMouseX() && gc.getMouseX() < 225 && gc.getMouseY() < 60 && Main.mouseClick) {
			if (dropdown[0] == false) {
				dropdown[0] = true;
			} else {
				dropdown[0] = false;
			}
		}

		if (dropdown[0]) {
			gc.setColor(new Color(240, 240, 245));
			gc.fillRoundRect(155, 55, 55, 180, 10, 10);

			dropdownButton(gc, 8, 160, 70);

			dropdownButton(gc, 9, 160, 125);

			dropdownButton(gc, 10, 160, 180);

			if (!(210 < gc.getMouseX() && gc.getMouseX() < 225 && gc.getMouseY() < 60) && Main.mouseClick) {
				dropdown[0] = false;
			}
		}

	}

	/**
	 * The drawing and selection of the drop down arrow for the moving drop down and
	 * the button in the drop down.
	 * 
	 * @param gc The graphics Console.
	 */
	static void moveDropdown(GraphicsConsole gc) {
		gc.setColor(Color.BLACK);
		gc.fillPolygon(new int[] { 330, 345, 337 }, new int[] { 40, 40, 47 }, 3);

		if (325 < gc.getMouseX() && gc.getMouseX() < 350 && gc.getMouseY() < 60 && Main.mouseClick) {
			if (dropdown[1] == false) {
				dropdown[1] = true;
			} else {
				dropdown[1] = false;
			}
		}

		if (dropdown[1] == true) {

			gc.setColor(new Color(240, 240, 245));
			gc.fillRoundRect(280, 55, 55, 125, 10, 10);

			dropdownButton(gc, 11, 285, 70);
			dropdownButton(gc, 12, 285, 120);

			if (!(325 < gc.getMouseX() && gc.getMouseX() < 350 && gc.getMouseY() < 60) && Main.mouseClick) {
				dropdown[1] = false;
			}
		}

	}

	/**
	 * The drawing and selection of the drop down arrow for the color drop down and
	 * the button in the drop down.
	 * 
	 * @param gc The graphics Console.
	 */
	static void colorDropdown(GraphicsConsole gc) {
		gc.setColor(Color.BLACK);
		gc.fillPolygon(new int[] { 400, 415, 407 }, new int[] { 40, 40, 47 }, 3);

		if (395 < gc.getMouseX() && gc.getMouseX() < 415 && gc.getMouseY() < 60 && Main.mouseClick) {
			if (dropdown[2] == false) {
				dropdown[2] = true;
			} else {
				dropdown[2] = false;
			}
		}

		if (dropdown[2] == true) {
			gc.setColor(new Color(240, 240, 245));
			gc.fillRoundRect(345, 55, 55, 65, 10, 10);

			dropdownButton(gc, 13, 350, 70);

			if (!(395 < gc.getMouseX() && gc.getMouseX() < 415 && gc.getMouseY() < 60) && Main.mouseClick) {
				dropdown[2] = false;
			}
		}
	}

	/**
	 * Prints and controls the buttons in the drop downs.
	 * 
	 * @param gc  The graphics Console.
	 * @param num The index of the button.
	 * @param x   The x coordinate of the button.
	 * @param y   The y coordinate of the button.
	 */
	static void dropdownButton(GraphicsConsole gc, int num, int x, int y) {
		if ((x - 5) < gc.getMouseX() && gc.getMouseX() < (x + 55) && gc.getMouseY() > (y - 5)
				&& gc.getMouseY() < (y + 55) && Main.mouseClick) {

			buttonClicked(num);
		}
		if (buttonClick[13]) {
			gc.setColor(Color.LIGHT_GRAY);
			gc.fillRoundRect(x, y, 45, 45, 20, 20);
		}
		gc.drawImage(Main.buttons[num], x, y);
	}

	/**
	 * If a button in a drop down is selected show it instead of the one normally
	 * next to the drop down.
	 * 
	 * @param gc The graphics Console.
	 */
	static void closedSelectionDesplay(GraphicsConsole gc) {
		if (buttonClick[8] && dropdown[0] == false) {
			gc.setColor(new Color(240, 240, 245));
			gc.fillRoundRect(160, 10, 45, 45, 20, 20);
			gc.setColor(Color.LIGHT_GRAY);
			gc.fillRoundRect(160, 10, 45, 45, 20, 20);
			gc.drawImage(Main.buttons[8], 160, 10);
		}
		if (buttonClick[9] && dropdown[0] == false) {
			gc.setColor(new Color(240, 240, 245));
			gc.fillRoundRect(160, 10, 45, 45, 20, 20);
			gc.setColor(Color.LIGHT_GRAY);
			gc.fillRoundRect(160, 10, 45, 45, 20, 20);
			gc.drawImage(Main.buttons[9], 160, 10);
		}
		if (buttonClick[10] && dropdown[0] == false) {
			gc.setColor(new Color(240, 240, 245));
			gc.fillRoundRect(160, 10, 45, 45, 20, 20);
			gc.setColor(Color.LIGHT_GRAY);
			gc.fillRoundRect(160, 10, 45, 45, 20, 20);
			gc.drawImage(Main.buttons[10], 160, 10);
		}

		if (buttonClick[11] && dropdown[1] == false) {
			gc.setColor(new Color(240, 240, 245));
			gc.fillRoundRect(280, 10, 45, 45, 20, 20);
			gc.setColor(Color.LIGHT_GRAY);
			gc.fillRoundRect(280, 10, 45, 45, 20, 20);
			gc.drawImage(Main.buttons[11], 280, 10);
		}
		if (buttonClick[12] && dropdown[1] == false) {
			gc.setColor(new Color(240, 240, 245));
			gc.fillRoundRect(280, 10, 45, 45, 20, 20);
			gc.setColor(Color.LIGHT_GRAY);
			gc.fillRoundRect(280, 10, 45, 45, 20, 20);
			gc.drawImage(Main.buttons[12], 280, 10);
		}

		if (buttonClick[13] && dropdown[2] == false) {
			gc.setColor(new Color(240, 240, 245));
			gc.fillRoundRect(350, 10, 45, 45, 20, 20);
			gc.setColor(Color.LIGHT_GRAY);
			gc.fillRoundRect(350, 10, 45, 45, 20, 20);
			gc.drawImage(Main.buttons[13], 350, 10);
		}
	}

	/**
	 * If a button is selected.
	 * 
	 * @param num The index of the button.
	 */
	static void buttonClicked(int num) {
		if (buttonClick[num]) {
			buttonClick[num] = false;
		} else {
			if(num == 6 || num == 13) {
				ColorSeter.start = true;
			}
			for (int i = 0; i < buttonClick.length; i++) {
				buttonClick[i] = false;
			}
			dropdown[0] = false;
			dropdown[1] = false;
			dropdown[2] = false;

			buttonClick[num] = true;
		}
	}

	/**
	 * Checks if a button has been clicked.
	 * 
	 * @return True if a button has changed value in the last loop, false if non has.
	 */
	public static boolean butionClicked() {
		boolean buttonChange = false;

		for (int i = 0; i < 14; i++) {
			if (ButtonPrinter.buttonClick[i] != oldButton[i]) {
				buttonChange = true;
			}
			oldButton[i] = ButtonPrinter.buttonClick[i];
		}
		return buttonChange;
	}
	
	/**
	 * When a drawing function is exited by clicking on another function it runs the closing system for the old function.
	 */
	public static void resetfunctions() {
		for (int i = 0; i < 14; i++) {
			if (ButtonPrinter.buttonClick[i] != oldButton[i]) {
				if(!buttonClick[3] && oldButton[3]) {
					DrawLine.endDrawing();
				} else if (!buttonClick[8] && oldButton[8]) {
					DrawRect.endDrawing();
				} else if (!buttonClick[8] && oldButton[2]) {
					CreatePlane.exit();
				}
			}
			oldButton[i] = ButtonPrinter.buttonClick[i];
		}
		
	}

}
