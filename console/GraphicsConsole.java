package console;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

import saving.Saving;

/**
 * This is a re-implementation of the old hsa console by Holt Software Associates.
 * Re-done from scratch in Swing with much code imported from the old hsa console.
 * The main goals were to reduce screen flicker during animations and eliminate a 
 * couple of small bugs in the input routines. April 30, 2010.
 * <p>
 * Differences from hsa console:
 *   - Row &amp; Column for text start at 0, 0
 *   - When creating console, specify width, height in pixels (not rows, columns)
 *   - setColor sets the drawing color for print and println as well as graphics
 *   - setTextBackgroundColor no longer works. Use setBackgroundColor instead.
 *   - use getDrawHeight() and getDrawWidth() for screen size in pixels
 *   - no more readString. Use readToken or readLine instead.
 *   - no more readChar. Use getChar instead.
 *   - methods added to poll the keyboard state without pausing the program (ketKeyChar, 
 *   getKeyCode, getLastKeyChar, etc.). Good for live-action games.
 *   - dropped support for print, quit, save buttons.
 *   - fixed drawImage to be more reliable
 *   - added mouse listener code
 * <p>
 * @author Tom West (old hsa code)
 * @author Sam Scott
 * @author Josh Gray (mouse code) 
 * @author Michael Harwood (setStroke, antiAlias, updated dialogs to JOptionPane, drawimage can do sprites)
 * @version 4.6
 */
public class GraphicsConsole extends JFrame implements MouseListener, MouseMotionListener, MouseWheelListener, ComponentListener {

	// Constants for setting up the window 
	private static final long serialVersionUID = 1L;

	// The main drawing surface
	public ConsoleCanvas canvas;

	// Mouse Variables
	private boolean mouseButton[] = { false, false, false };
	private int mouseX = 0, mouseY = 0, mouseClick = 0, mouseWheelUnitsToScroll = 0;

	// ****************
	// *** CONSTRUCTORS
	// ****************

	/** Creates GraphicsConsole with specified window width, height, and name
	 * @param width GraphicsConsole width in pixels
	 * @param height GraphicsConsole height in pixels
	 * @param name GraphicsConsole window name
	 */
	public GraphicsConsole(int width, int height, String name) {
		this(width, height, 12, name);
	}

	/** Creates GraphicsConsole with specified window width, height, font size, and name (title)
	 * invokeAnd Wait() is used to make HSA2 thread safe, since Timers can be used to update graphics.
	 * and invokeLater() does not return fast enough, and so causes null pointer errors for gc.methods
	 * 
	 * @param width GraphicsConsole width in pixels
	 * @param height GraphicsConsole height in pixels
	 * @param fontSize Default font size for println and print 
	 * @param name GraphicsConsole window name
	 */
	public GraphicsConsole(int width, int height, int fontSize, String name) {		 
		super(name);
		
		this.addWindowListener(new WindowAdapter() {
		    @Override
		    public void windowClosing(WindowEvent event) {
		        Saving.save();
		    }
		});
		
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					makeGUI(width, height, fontSize, name);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

	// *************************************
	// **** PUBLIC GRAPHIC OUTPUT METHODS
	// *************************************

	/**
	 * Clears the drawing area to the current background color
	 */
	public void clear() {
		canvas.clear();
	}

	/** Set the graphics (foreground) colour
	 * @param c New drawing color for graphics and text
	 */
	public void setColor(Color c) {
		canvas.setColor(c);
	}
	/**
	 * NOTE: This command only sets the background color. Nothing will change on the drawing
	 * surface until you use clearRect() or clear()
	 * @param c New background color
	 */
	public void setBackgroundColor(Color c) {
		canvas.setBackgroundColor(c);
	}
	
	/**
	 * Draws a filled rectangle on the drawing area.
	 * @param x Top left X coordinate
	 * @param y Top left Y coordinate
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public void fillRect(int x, int y, int width, int height)  {
		canvas.fillRect(x, y, width, height);
	}
	/**
	 * Draws a rectangle outline on the drawing area.
	 * Stroke width can be set using setStroke().
	 * @param x Top left X coordinate
	 * @param y Top left Y coordinate
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public void drawRect(int x, int y, int width, int height) {
		canvas.drawRect(x, y, width, height);
	}
	/**
	 * Draws a filled in oval on the drawing area, inscribed within a rectangle.
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width in pixels
	 * @param height Height in pixels
	 */
	public void fillOval(int x, int y, int width, int height) {
		canvas.fillOval(x, y, width, height);
	}

	/**
	 * Draws a straight line on the drawing area.
	 * Stroke width can be set using setStroke().
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x1 Starting x coordinate
	 * @param y1 Starting y coordinate
	 * @param x2 Ending x coordinate
	 * @param y2 Ending y coordinate
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		canvas.drawLine(x1, y1, x2, y2);
	} 

	/**
	 * Draws a polygon outline on the drawing area.
	 * Stroke width can be set using setStroke().
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x An array of x coordinates for the corner points of the polygon (related to array y)
	 * @param y An array of y coordinates for the corner points of the polygon (related to array x)
	 * @param n Number of points in the polygon
	 */
	public void drawPolygon(int[] x, int[] y, int n) {
		canvas.drawPolygon(x, y, n);
	}
	/**
	 * Draws a polygon outline on the drawing area.
	 * Stroke width can be set using setStroke().
	 * AntiAliasing can be set using setAntiAlias()
	 * @param p The Polygon to draw.
	 */
	public void drawPolygon(Polygon p) {
		canvas.drawPolygon(p);
	}

	/**
	 * Draws a filled in polygon on the drawing area.
	 * Stroke width can be set using setStroke().
	 * @param x An array of x coordinates for the corner points of the polygon (related to array y)
	 * @param y An array of y coordinates for the corner points of the polygon (related to array x)
	 * @param n Number of points in the polygon
	 */
	public void fillPolygon(int[] x, int[] y, int n) {
		canvas.fillPolygon(x, y, n);
	}
	/**
	 * Draws a filled in polygon on the drawing area.
	 * Stroke width can be set using setStroke().
	 * @param p The Polygon to draw.
	 */
	public void fillPolygon(Polygon p) {
		canvas.fillPolygon(p);
	}

	/**
	 * Draws a rectangle outline on the drawing area with rounded corners.
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width in pixels
	 * @param height Height in pixels
	 * @param xRadius Horizontal radius of arc for corners
	 * @param yRadius Vertical radius of arc for corners
	 */
	public void drawRoundRect(int x, int y, int width, int height, int xRadius, int yRadius) {
		canvas.drawRoundRect(x, y, width, height, xRadius, yRadius);
	}
	/**
	 * Draws a filled in rectangle on the drawing area with rounded corners.
	 * AntiAliasing can be set using setAntiAlias()
	 * @param x Top left X coordinate of the rectangle
	 * @param y Top left Y coordinate of the rectangle
	 * @param width Width in pixels
	 * @param height Height in pixels
	 * @param xRadius Horizontal radius of arc for corners
	 * @param yRadius Vertical radius of arc for corners
	 */
	public void fillRoundRect(int x, int y, int width, int height, int xRadius, int yRadius) {
		canvas.fillRoundRect(x, y, width, height, xRadius, yRadius);
	}  

	/**
	 * Draws a string on the drawing area using the current font and color.
	 * Note that the coordinates specify the bottom left corner rather than the
	 * top left corner for drawing.
	 * AntiAliasing can be set using setAntiAlias()
	 * @param str The string to draw. 
	 * @param x Bottom left X coordinate
	 * @param y Bottom left Y coordinate
	 */
	public void drawString(String str, int x, int y) {
		canvas.drawString(str, x, y);
	}
	/**
	 * Draws specified image on the drawing area. Note that if the image takes a while
	 * to load, this method will delay until it is loaded, timing out after 1000 ms.
	 * @param img The image to draw
	 * @param x Top left x coordinate
	 * @param y Top left y coordinate
	 */
	public void drawImage(Image img, int x, int y) {
		canvas.drawImage(img, x, y);
	}
	/**
	 * Draws specified image on the drawing area. Note that if the image takes a while
	 * to load, this method will delay until it is loaded, timing out after 1000 ms.
	 * @param img The image to draw
	 * @param x Top left x coordinate
	 * @param y Top left y coordinate
	 * @param width Compress or stretch image to this width
	 * @param height Compress or stretch image to this width
	 */
	public void drawImage(Image img, int x, int y, int width, int height) {
		canvas.drawImage(img, x, y, width, height);
	}

	/**
	 * Sets the font for drawString (not for print or println)
	 * v4.3 Only sets the font if the font has changed (because setting fonts slows graphics down a lot).
	 * @param f The new font
	 */
	public void setFont(Font f) {
		Font oldFont = canvas.getFont();
		if (oldFont.equals(f)) return;
		
		//MH: Is this necessary?
		//super.setFont(f);	//set the JFrame font
				
		canvas.setFont(f);	//set the JPanel font
		
	}
	/**
	 * This sets the stroke size for drawLine ONLY
	 * @param strokeSize in pixels 
	 */
	public void setStroke(int strokeSize) {
		canvas.setStroke(strokeSize);
	}
	/**
	 * This turns antialiasing on or off.
     * Antialiasing makes likes look a lot smoother, so one normally wants this turned on.
     * Example: gc.setAntiAlias(true);
	 * @param onOff set to TRUE or FALSE 
	 */
	public void setAntiAlias(boolean onOff) {
		canvas.setAntiAlias(onOff);
	}

	// ************************
	// *** OTHER PUBLIC METHODS
	// ************************

	/**
	 * Closes the GraphicsConsole window.
	 */
	public void close () {
		canvas.killThread ();
		this.dispose ();
	} 
	
	/** 
	 * A simpler sleep function
	 * handles the try/catch or "throws InterruptedException" 
	 * that Thread.sleep() produces.
	 * @param milliSeconds  the time to sleep in ms.
	 */
	public void sleep(long milliSeconds) {		
		try {
			Thread.sleep(milliSeconds);
		} catch (InterruptedException e) {}
	}	

	// **********************
	// *** MOUSE METHODS
	// **********************

	/*
	 * The enableMouse... methods are included so that the overhead of
	 * listening for mouse events is not a factor if the programmer doesn't
	 * want to use the mouse.
	 * 
	 * You MUST invoke the appropriate enableMouse... methods() first, if you
	 * want to use the mouse.
	 * 
	 * enableMouse(): Listens for mouse button events, and also saves the
	 * coordinates of the mouse when a mouse button event occurs, so that
	 * getMouseX() and getMouseY() return the coordinates of the mouse as of
	 * the last time a button was clicked/pressed/released.
	 * 
	 *  - getMouseButton() and getMouseClick() are enabled.
	 * 
	 * enableMouseMotion(): Listens for mouse move and drag events, thereby
	 * allowing getMouseX() and getMouseY() to return the current coordinates
	 * of the mouse, regardless of button or wheel events.
	 * 
	 * enableMouseWheel(): Listens for mouse wheel events, and also saves the
	 * coordinates of the mouse when a mouse wheel event occurs, so that
	 * getMouseX() and getMouseY() return the coordinates of the mouse as of
	 * the last time the mouse wheel was scrolled.
	 * 
	 *  - getMouseWheelRotation() and getMouseWheelUnitsToScroll() are enabled.
	 * 
	 * NOTE: if enableMouseMotion() has been invoked, then getMouseX() and
	 * getMouseY() will return the current coordinates of the mouse, rather
	 * than the coordinates of the mouse as of the last time a button was
	 * clicked/pressed/released or wheel was scrolled.
	 * 
	 * enableMouseMotion() is by far the most demanding in terms of overhead,
	 * so don't invoke this method unless it is absolutely necessary.
	 * 
	 * disableMouse... methods are provided to avoid mouse event overhead
	 * when they are no longer needed.
	 */
	
	/**
	 * Adds MouseListener to the GraphicsConsole
	 */
	public void enableMouse() {
		canvas.addMouseListener(this);
	}

	/**
	 * Adds MouseMotionListener to the GraphicsConsole
	 */
	public void enableMouseMotion() {
		canvas.addMouseMotionListener(this);
	}

	/**
	 * Adds MouseWheelListener to the GraphicsConsole
	 */
	public void enableMouseWheel() {
		canvas.addMouseWheelListener(this);
	}

	/**
	 * Returns true if the specified button is pressed, false otherwise.
	 * 
	 * Buttons are numbered 0, 1 or 2.
	 * @param buttonNum mouse button number (0,1,2)
	 * @return T/F if that button has been pressed.
	 */
	public boolean getMouseButton(int buttonNum) {

		if (( buttonNum >= 0)&& (buttonNum < mouseButton.length))
			return mouseButton[ buttonNum ];
		else
			return false;
	}

	/**
	 * Returns non-zero if the mouse has been clicked since the last time
	 * the click was queried, zero if the mouse was not clicked.
	 * 
	 * 1 = single click
	 * 2 = double click
	 * 3 = triple click
	 * etc.
	 * @return number of clicks
	 */
	public int getMouseClick() {
		int toReturn = mouseClick;
		mouseClick = 0;
		return toReturn;
	}

	/**
	 * Returns the number of units the mouse wheel has been scrolled since
	 * the last time the wheel was queried.
	 * @return the number of units to scroll based on the direction and amount 
	 * of mouse wheel rotation, and on the wheel scrolling settings of the native platform
	 */
	public int getMouseWheelUnitsToScroll() {
		int toReturn = mouseWheelUnitsToScroll;
		mouseWheelUnitsToScroll = 0;
		return toReturn;
	}

	/**
	 * Returns the X coordinate of the mouse pointer position within the drawing
	 * area.
	 * @return x coordinate of mouse pointer position
	 */
	public int getMouseX() {
		return mouseX;
	}

	/**
	 * Returns the Y coordinate of the mouse pointer position within the drawing
	 * area.
	 * @return y coordinate of mouse pointer position
	 */
	public int getMouseY() {
		return mouseY;
	}

	/* *********************************************
	 * MOUSE LISTENER EVENTS
	 * "Background" mouse methods 
	 * (i.e., don't try to invoke these directly!)
	 * *********************************************/
	@Override
	public void mouseClicked(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseExited(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		if (e.getButton() == MouseEvent.BUTTON1)
			mouseButton[ 0 ] = true;

		else if (e.getButton() == MouseEvent.BUTTON2)
			mouseButton[ 1 ] = true;

		else if (e.getButton() == MouseEvent.BUTTON3)
			mouseButton[ 2 ] = true;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
		mouseClick = e.getClickCount(); //for better response when using gc.getMouseClick()

		if (e.getButton() == MouseEvent.BUTTON1)
			mouseButton[ 0 ] = false;

		else if (e.getButton() == MouseEvent.BUTTON2)
			mouseButton[ 1 ] = false;

		else if (e.getButton() == MouseEvent.BUTTON3)
			mouseButton[ 2 ] = false;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();

		mouseWheelUnitsToScroll += e.getUnitsToScroll();
	}
	
	@Override
	public void componentResized(ComponentEvent e) {}
	@Override
	public void componentMoved(ComponentEvent e) {}
	@Override
	public void componentShown(ComponentEvent e) {}
	@Override
	public void componentHidden(ComponentEvent e) {}

	// **********************
	// *** NON-PUBLIC METHODS
	// **********************

	private void makeGUI (int width, int height, int fontSize, String title) { 
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);		

		JPanel newPanel = new JPanel ();
		newPanel.setLayout (new GridLayout(1,1));

		canvas = new ConsoleCanvas(width, height, fontSize, title, this);
		newPanel.add(canvas);

		this.setContentPane(newPanel);
		this.pack();
		this.setVisible(true);
	}
	
}

