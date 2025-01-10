package console;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This is a re-implementation of the old hsa console by Holt Software
 * Associates. Re-done from scratch in Swing with much code imported from the
 * old hsa console. The main goals were to reduce screen flicker during
 * animations and eliminate a couple of small bugs in the input routines. April
 * 30, 2010.
 *
 * See Console.java for differences between this version and the old one.
 *
 * Update August 2012: Changed synchronization to synchronize on the associated
 * Console object. Now application writers can also synchronize on the Console
 * object to kill the last remaining cases of screen flicker.
 * 
 * Update September 2014: Fixed bug in getRow() and getColumn(); they now report
 * the current cursor position without requiring a print() first.
 * 
 * Update 2017:
 * 
 * @author Michael Harwood (minor text printing bug fix)
 * @author Sam Scott
 * @author Josh Gray (getRow()/getColumn() bug fix)
 * @author Tom West (old hsa code)
 * 
 * @version 4.6
 */
public class ConsoleCanvas extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/** Window title **/
	private String title;
	/** Container of this object **/
	private console.GraphicsConsole container;

	// ***** Screen variables *****

	/** Off screen buffer **/
	// private final BufferedImage buffer;
	private BufferedImage buffer;
	/** Foreground color **/
	private Color foregroundColor = Color.black;
	/** Background color **/
	private Color backgroundColor = Color.white;
	/** Screen size **/
	private int width, height;
	/** Screen drawing mode **/
	private boolean xorMode = false;
	/** Color for xor mode **/
	private Color xorColor = backgroundColor;
	/** Font for drawString **/
	private Font drawStringFont;
	/** Refresh speed **/
	private static final int framesPerSecond = 60;
	/** Timer object for redrawing screen **/
	private Timer timer;
	/* MH added */
	private int strokeSize = 1;
	private boolean antiAlias = false;

	// ***** Text input/output variables *****
	private Font textFont;
	private int fontWidth;

	// ***** Keyboard Buffer & Input Variables - adapted from original hsa package
	// *****
	private static final int EMPTY_BUFFER = -1;
	protected int ungotChar = EMPTY_BUFFER;

	// ****************
	// *** CONSTRUCTORS
	// ****************

	public ConsoleCanvas(int width, int height, int fontSize, String title, console.GraphicsConsole console) {
		this.container = console;
		this.setTitle(title);

		// Sizing
		this.setPreferredSize(new Dimension(width, height));
		this.setMinimumSize(new Dimension(width, height));
		this.setMaximumSize(new Dimension(width, height));
		this.setAntiAlias(true);
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		this.height = height;
		this.width = width;

		// Adapted from old hsa code
		textFont = new Font("monospaced", Font.PLAIN, fontSize);
		FontMetrics fm = getFontMetrics(textFont);
		fontWidth = 0;
		for (int ch = 32; ch < 127; ch++) {
			fontWidth = Math.max(fontWidth, fm.charWidth(ch));
		}

		clear();
		timer = new Timer(1000 / framesPerSecond, this);
		timer.start();
	}

	// ************
	// *** GRAPHICS
	// ************
	void clear() {
		Graphics g = getOffscreenGraphics();
		g.setColor(backgroundColor);
		if (xorMode)
			g.setPaintMode();
		g.fillRect(0, 0, width, height);
		// setCursor(0,0);
		if (xorMode)
			g.setXORMode(xorColor);
	}

	void clearRect(int x, int y, int width, int height) {
		Graphics g = getOffscreenGraphics();
		g.setColor(backgroundColor);
		if (xorMode)
			g.setPaintMode();
		g.fillRect(x, y, width, height);
		if (xorMode)
			g.setXORMode(xorColor);
	}

	void setColor(Color c) {
		foregroundColor = c;
	}

	void setBackgroundColor(Color c) {
		backgroundColor = c;
	}

	void fillRect(int x, int y, int width, int height) {
		Graphics g = getOffscreenGraphics();
		g.setColor(foregroundColor);
		g.fillRect(x, y, width, height);
	}

	void drawRect(int x, int y, int width, int height) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(foregroundColor);
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g2.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawRect(x, y, width, height);
	}

	void fillOval(int x, int y, int width, int height) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		g2.fillOval(x, y, width, height);
	}

	void drawOval(int x, int y, int width, int height) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		g2.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawOval(x, y, width, height);
	}

	void drawLine(int x1, int y1, int x2, int y2) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		g2.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawLine(x1, y1, x2, y2);
	}

	void drawPolygon(Polygon p) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		g2.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_ROUND));
		g2.drawPolygon(p);
	}

	void drawPolygon(int[] x, int[] y, int n) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		g2.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawPolygon(x, y, n);
	}

	void fillPolygon(Polygon p) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		g2.fillPolygon(p);
	}

	void fillPolygon(int[] x, int[] y, int n) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		g2.fillPolygon(x, y, n);
	}

	void drawRoundRect(int x, int y, int width, int height, int xRadius, int yRadius) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		g2.setStroke(new BasicStroke(strokeSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g2.drawRoundRect(x, y, width, height, xRadius, yRadius);
	}

	void fillRoundRect(int x, int y, int width, int height, int xRadius, int yRadius) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		g2.fillRoundRect(x, y, width, height, xRadius, yRadius);
	}

	// MH. Prevent calls to g2.setFont() if the font has not changed.
	void drawString(String str, int x, int y) {
		Graphics g = getOffscreenGraphics();
		Graphics2D g2 = (Graphics2D) g;
		if (antiAlias) {
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		}
		g.setColor(foregroundColor);
		if (drawStringFont != null && !(drawStringFont.equals(g2.getFont())))
			g2.setFont(drawStringFont);
		g2.drawString(str, x, y);
	}

	// setFont does not actually seem to do much! It sets the font of the JComponent
	@Override
	public void setFont(Font f) {
		super.setFont(f);
		drawStringFont = f;
	}

	void setStroke(int strokeSize) {
		this.strokeSize = strokeSize;
	}

	void setAntiAlias(boolean onOff) {
		this.antiAlias = onOff;
	}

	void drawImage(Image img, int x, int y) {
		boolean success = false;
		Graphics g = getOffscreenGraphics();
		success = g.drawImage(img, x, y, null);
		// loop to timeout if image not drawn properly
		for (int i = 0; i < 1000 & !success; i++) {
			success = g.drawImage(img, x, y, null);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
		if (!success)
			throw new RuntimeException("Image not loaded.");
	}

	void drawImage(Image img, int x, int y, int width, int height) {
		boolean success = false;
		Graphics g = getOffscreenGraphics();
		success = g.drawImage(img, x, y, width, height, null);
		// loop to timeout if image not drawn properly
		for (int i = 0; i < 1000 & !success; i++) {
			success = g.drawImage(img, x, y, width, height, null);
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
			}
		}
		if (!success)
			throw new RuntimeException("Image not loaded.");
	}

	// **********************
	// *** UTILITY METHODS
	// **********************
	public void killThread() {
		timer.stop();
	}
	// **********************
	// *** NON-PUBLIC METHODS
	// **********************

	public void paintComponent(Graphics g) {
		synchronized (container) {
			g.drawImage(buffer, 0, 0, width, height, this);
		}
	}

	/*
	 * This is the action performed for the Swing Timer that is started in the
	 * constructor
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

	private Graphics getOffscreenGraphics() {
		Graphics g = buffer.getGraphics();
		if (xorMode)
			g.setXORMode(xorColor);
		else
			g.setPaintMode();
		return g;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
