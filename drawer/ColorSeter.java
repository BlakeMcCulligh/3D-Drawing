package drawer;

import java.awt.Color;

import javax.swing.JColorChooser;

import console.GraphicsConsole;
import drawer.objects.ThreeDObjects;

/**
 * This class holds everything to do with the changing of the colors for lines and sides.
 * 
 * Created: May 27, 2024 
 * 
 * @author Blake McCulligh
 */
public class ColorSeter {

	/**
	 * The color things are to be set to.
	 */
	static Color c;
	
	/**
	 * If one of the two color functions was just selected.
	 */
	public static boolean start = false;
	
	/**
	 * For the get color form existing thing, has a thing been selected yet to use its color. 
	 */
	static boolean colorPiked = false;

	/**
	 * The user can pick any color using the color chooser panel then any clicked on lines or sides are turned to that color.
	 * 
	 * @param gc The graphic console.
	 */
	public static void pickColor(GraphicsConsole gc) {

		if (start) {
			
			Color initialcolor = Color.WHITE;
			c = JColorChooser.showDialog(gc, "Select a color", initialcolor);
			
			start = false;
		}
		setColor();
	}

	/**
	 * Gets the color from the selected line or side and then uses it to change the
	 * color of any clicked on lines or sides.
	 */
	public static void colorPicker() {
		
		if (start) {
			
			colorPiked = false;
			
			for (int i = (ThreeDObjects.Lines.size() - 1); i >= 3; i--) {
				if (ThreeDObjects.Lines.get(i).selected) {
					c = ThreeDObjects.Lines.get(i).originalC;
					start = false;
					colorPiked = true;
				}
			}
			
			for (int i = (ThreeDObjects.Sides.size() - 1); i >= 0; i--) {
				if (ThreeDObjects.Sides.get(i).selected) {
					c = ThreeDObjects.Sides.get(i).originalC;
					start = false;
					colorPiked = true;
				}
			}
		}
		
		if (colorPiked) {
			setColor();
		}
	}

	/**
	 * Sets the color of any selected side or line to that of the color chosen.
	 */
	public static void setColor() {
		
		for (int i = (ThreeDObjects.Lines.size() - 1); i >= 3; i--) {
			if (ThreeDObjects.Lines.get(i).selected) {
				ThreeDObjects.Lines.get(i).originalC = c;
			}
		}
		
		for (int i = (ThreeDObjects.Sides.size() - 1); i >= 0; i--) {
			if (ThreeDObjects.Sides.get(i).selected) {
				ThreeDObjects.Sides.get(i).originalC = c;
			}
		}
	}

}
