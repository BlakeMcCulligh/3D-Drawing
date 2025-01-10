package saving;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import console.GraphicsConsole;
import drawer.Point;
import drawer.objects.Line;
import drawer.objects.Plane;
import drawer.objects.Shape;
import drawer.objects.Side;
import drawer.objects.ThreeDObjects;
import renderer.ButtonPrinter;
import renderer.Main;

/**
 * This class controls the entire opening page along with the process to load
 * and open a file.
 * 
 * Created: May 29, 2024
 * Last updated: June 1, 2024
 * 
 * @author Blake McCulligh
 */
public class Opening {

	/**
	 * The names of all the files.
	 */
	static ArrayList<String> files = new ArrayList<String>();
	/**
	 * All the preview images.
	 */
	static ArrayList<Image> images = new ArrayList<Image>();
	/**
	 * The date the file was created.
	 */
	static ArrayList<String> datesCreated = new ArrayList<String>();

	/**
	 * The file the selected project is saved in.
	 */
	static File projectFile;

	/**
	 * If a new save was successfully created.
	 */
	static boolean successfullyCreated = false;

	/**
	 * If a file has been picket to open.
	 */
	static boolean selected = false;

	/**
	 * The index of the file selected.
	 */
	static int indexOfFile = 0;

	/**
	 * If a three dot option panel is open.
	 */
	static boolean threeDots[] = new boolean[10];

	/**
	 * What of the 3 buttons on the option panel was picked.
	 */
	static boolean button[] = new boolean[3];

	/**
	 * Runs everything to do with the home page.
	 * 
	 * @param gc The graphics console.
	 * @throws IOException
	 */
	public static void homePage(GraphicsConsole gc) throws IOException {

		boolean createNew = false;

		Font largerFont = new Font("SansSerif", Font.PLAIN, 20);
		Font smallerFont = new Font("SansSerif", Font.BOLD, 15);

		for (int i = 0; i < files.size(); i++) {
			threeDots[i] = false;
		}

		for (int i = 0; i < 3; i++) {
			button[i] = false;
		}

		getFileNames();

		loadPrevewImages(gc);

		gc.setBackgroundColor(new Color(235, 235, 245));
		gc.clear();

		gc.setStroke(2);

		// main home page loop
		while (true) {

			int X = gc.getMouseX();
			int Y = gc.getMouseY();

			// printing the home page
			synchronized (gc) {

				gc.clear();

				createNew = creteNewButton(gc, X, Y, largerFont);

				for (int i = 0; i < files.size(); i++) {

					gc.setColor(Color.WHITE);

					if (i < 5) {
						topRow(gc, i, largerFont, smallerFont, X, Y);
					} else {
						bottomRow(gc, i, largerFont, smallerFont, X, Y);
					}

				}

				threeDotDropdownPrinting(gc, smallerFont);
			}

			if (createNew || selected) {
				break;
			}

			if (button[0]) {

				deleteFile(gc, indexOfFile);
				button[0] = false;

			} else if (button[1]) {

				renameFile(gc, indexOfFile);
				button[1] = false;

			} else if (button[2]) {

				duplicateFile(gc, indexOfFile);
				button[2] = false;
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (createNew) {
			createNew(gc);
		}

		// make sure the save is not tried to be opened before it is created
		while (!successfullyCreated && !selected) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		load();

		setUp(gc);
	}

	/**
	 * Gets the names of all the files and saves them to 'files' and all the dates
	 * they were created and saves them to 'datesCreated'.
	 */
	static void getFileNames() {

		File nameFile = new File("Names.txt");

		try {

			Scanner freader = new Scanner(nameFile);

			while (freader.hasNextLine()) {
				files.add(freader.nextLine());
				datesCreated.add(freader.nextLine());
			}

			freader.close();

		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Loads all the save preview images. If there is no image for a save load a
	 * blank image to fill the slot.
	 * 
	 * @param gc The graphics console.
	 * @throws IOException
	 */
	static void loadPrevewImages(GraphicsConsole gc) throws IOException {

		for (int i = 0; i < files.size(); i++) {

			// creating a file with the same name as the save just .png instead of .txt
			String file = files.get(i);
			String fileName[] = file.split(".txt");
			File fileImage = new File(fileName[0] + ".png");

			// checking is it exists
			if (fileImage.exists()) {

				images.add(ImageIO.read(fileImage));

				// if it does not exist load a blank image
			} else {

				Image im = Toolkit.getDefaultToolkit()
						.getImage(gc.getClass().getClassLoader().getResource("photos/blank.png"));

				images.add(im);
			}
		}
	}

	/**
	 * Prints and runs the create new save button.
	 * 
	 * @param gc         The graphics console.
	 * @param X          The X coordinate of the mouse.
	 * @param Y          The Y coordinate of the mouse.
	 * @param largerFont Font used.
	 * @return If the button has been pressed.
	 */
	static boolean creteNewButton(GraphicsConsole gc, int X, int Y, Font largerFont) {

		// if the mouse is over the create new button
		if (X > 25 && X < 175 && Y > 25 && Y < 65) {

			gc.setColor(new Color(50, 50, 245));
			gc.fillRoundRect(25, 25, 150, 40, 15, 15);
			gc.setColor(new Color(235, 235, 245));
			gc.setFont(largerFont);
			gc.drawString("Create New", 48, 52);

			if (gc.getMouseButton(0) && gc.getMouseClick() ==1) {
				return true;
			}

		} else {
			gc.setColor(new Color(50, 50, 245));
			gc.drawRoundRect(25, 25, 150, 40, 15, 15);
			gc.setFont(largerFont);
			gc.drawString("Create New", 48, 52);
		}
		return false;
	}

	/**
	 * Prints the save if it is located in the top row on the display and handles if
	 * any of its buttons are clicked.
	 * 
	 * @param gc          The graphics console.
	 * @param i           The index being printed.
	 * @param largerFont  Used for the name of the save.
	 * @param smallerFont Used for the info about the save.
	 * @param X           The X coordinate of the mouse.
	 * @param Y           The Y coordinate of the mouse.
	 */
	static void topRow(GraphicsConsole gc, int i, Font largerFont, Font smallerFont, int X, int Y) {

		// printing the general box
		gc.fillRect(20 + i * 270, 100, 250, 325);
		gc.setColor(Color.BLACK);
		gc.drawRect(20 + i * 270, 100, 250, 325);
		gc.fillOval(255 + i * 270, 390, 5, 5);
		gc.fillOval(255 + i * 270, 400, 5, 5);
		gc.fillOval(255 + i * 270, 410, 5, 5);

		// printing the preview image
		// try catch is needed as if a duplicate is made it will not have an image
		// assigned
		try {
			gc.drawImage(images.get(i), 25 + i * 270, 105, 240, 170);
		} catch (IndexOutOfBoundsException x) {
			System.out.println("Failed to draw image");
		}

		printTitle(gc, largerFont, i, true);

		printSmallerInfo(gc, smallerFont, i, true);

		if (X > 250 + i * 270 && X < 270 + i * 270 && Y > 385 && Y < 420 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1) {

			// Three Dots clicked
			if (threeDots[i]) {
				threeDots[i] = false;
			} else {
				threeDots[i] = true;
			}

		} else if (X > 275 + i * 270 && X < 350 + i * 270 && Y > 385 && Y < 400 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1 && threeDots[i]) {

			// delete clicked
			threeDots[i] = false;
			button[0] = true;
			indexOfFile = i;

		} else if (X > 275 + i * 270 && X < 350 + i * 270 && Y > 410 && Y < 435 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1 && threeDots[i]) {

			// Rename clicked
			threeDots[i] = false;
			indexOfFile = i;
			button[1] = true;

		} else if (X > 275 + i * 270 && X < 350 + i * 270 && Y > 435 && Y < 460 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1 && threeDots[i]) {

			// duplicate clicked
			threeDots[i] = false;
			indexOfFile = i;
			button[2] = true;

		} else if (X > 20 + i * 270 && X < 20 + i * 270 + 250 && Y > 100 && Y < 425 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1) {

			// Project clicked to be open
			projectFile = new File(files.get(i));
			selected = true;
		}
	}

	/**
	 * Prints the save if it is located in the bottom row on the display and handles
	 * if any of its buttons are clicked.
	 * 
	 * @param gc          The graphics console.
	 * @param i           The index being printed.
	 * @param largerFont  Used for the name of the save.
	 * @param smallerFont Used for the info about the save.
	 * @param X           The X coordinate of the mouse.
	 * @param Y           The Y coordinate of the mouse.
	 */
	static void bottomRow(GraphicsConsole gc, int i, Font largerFont, Font smallerFont, int X, int Y) {

		// printing the general box
		gc.fillRect(20 + (i - 5) * 270, 450, 250, 325);
		gc.setColor(Color.BLACK);
		gc.drawRect(20 + (i - 5) * 270, 450, 250, 325);
		gc.fillOval(255 + (i - 5) * 270, 740, 5, 5);
		gc.fillOval(255 + (i - 5) * 270, 750, 5, 5);
		gc.fillOval(255 + (i - 5) * 270, 760, 5, 5);

		// printing the preview image
		// try catch is needed as if a duplicate is made it will not have an image
		// assigned
		try {
			gc.drawImage(images.get(i), 25 + (i - 5) * 270, 455, 240, 170);
		} catch (IndexOutOfBoundsException x) {
			System.out.println("Failed to draw image");
		}

		printTitle(gc, largerFont, i, false);

		printSmallerInfo(gc, smallerFont, i, false);

		if (X > 250 + (i - 5) * 270 && X < 270 + (i - 5) * 270 && Y > 735 && Y < 770 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1) {

			// Three Dots clicked
			if (threeDots[i]) {
				threeDots[i] = false;
			} else {
				threeDots[i] = true;
			}

		} else if (X > 275 + (i - 5) * 270 && X < 350 + (i - 5) * 270 && Y > 735 && Y < 750 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1 && threeDots[i]) {

			// delete clicked
			threeDots[i] = false;
			button[0] = true;
			indexOfFile = i;

		} else if (X > 275 + (i - 5) * 270 && X < 350 + (i - 5) * 270 && Y > 760 && Y < 785 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1 && threeDots[i]) {

			// rename clicked
			threeDots[i] = false;
			indexOfFile = i;
			button[1] = true;

		} else if (X > 275 + (i - 5) * 270 && X < 350 + (i - 5) * 270 && Y > 785 && Y < 810 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1 && threeDots[i]) {

			// duplicate clicked
			threeDots[i] = false;
			indexOfFile = i;
			button[2] = true;

		} else if (X > 20 + (i - 5) * 270 && X < 20 + (i - 5) * 270 + 250 && Y > 450 && Y < 775 && gc.getMouseButton(0)
				&& gc.getMouseClick() == 1) {

			// project clicked to be opened
			projectFile = new File(files.get(i));
			selected = true;
		}
	}

	/**
	 * Prints the title of the save
	 * 
	 * @param gc         The graphics console.
	 * @param largerFont The font used to print the title.
	 * @param i          The index of the save currently being printed.
	 * @param upperRow   If the current file is in the upper row.
	 */
	static void printTitle(GraphicsConsole gc, Font largerFont, int i, boolean upperRow) {

		FontMetrics largerFontMetrics = gc.getFontMetrics(largerFont);

		// removing the .txt
		String n = files.get(i);
		String na[] = n.split(".txt");

		gc.setFont(largerFont);
		int length = largerFontMetrics.stringWidth(na[0]);

		// handling if the title is to long to fit in the box
		if (length < 240) {

			if (upperRow) {
				gc.drawString(na[0], 25 + i * 270, 300);
			} else {
				gc.drawString(na[0], 25 + (i - 5) * 270, 650);
			}

		} else {

			while (length > 215) {
				na[0] = na[0].substring(0, na[0].length() - 1);
				length = largerFontMetrics.stringWidth(na[0]);
			}
			if (upperRow) {
				gc.drawString(na[0] + "...", 25 + i * 270, 300);
			} else {
				gc.drawString(na[0] + "...", 25 + (i - 5) * 270, 650);
			}

		}

	}

	/**
	 * Prints all the smaller info about a save (Date of creation and last opened
	 * and the size of the save).
	 * 
	 * @param gc          The graphics console.
	 * @param smallerFont The font used for printing this data.
	 * @param i           The index of the save currently being printed.
	 * @param upperRow    If the current file is in the upper row.
	 */
	static void printSmallerInfo(GraphicsConsole gc, Font smallerFont, int i, boolean upperRow) {

		gc.setFont(smallerFont);

		File f = new File(files.get(i));

		// getting the date the file was last modified
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTime = dateFormat.format(f.lastModified());

		if (upperRow) {
			gc.drawString("File Created: " + datesCreated.get(i), 25 + i * 270, 325);
			gc.drawString("Last Edited: " + String.valueOf(currentDateTime), 25 + i * 270, 350);
			gc.drawString("File Size: " + String.valueOf(f.length() / 1000) + " Kb", 25 + i * 270, 375);
		} else {
			gc.drawString("File Created: " + datesCreated.get(i), 25 + (i - 5) * 270, 675);
			gc.drawString("Last Edited: " + String.valueOf(currentDateTime), 25 + (i - 5) * 270, 700);
			gc.drawString("File Size: " + String.valueOf(f.length() / 1000) + " Kb", 25 + (i - 5) * 270, 725);
		}
	}

	/**
	 * Prints the 3 dot drop down if needed.
	 * 
	 * @param gc          The graphics console.
	 * @param smallerFont The font used.
	 */
	static void threeDotDropdownPrinting(GraphicsConsole gc, Font smallerFont) {

		for (int i = 0; i < files.size(); i++) {

			// top or bottom row
			if (i < 5) {

				if (threeDots[i]) {
					gc.setFont(smallerFont);
					gc.setColor(Color.WHITE);
					gc.fillRoundRect(275 + i * 270, 385, 100, 75, 10, 10);
					gc.setColor(Color.BLACK);
					gc.drawRoundRect(275 + i * 270, 385, 100, 75, 10, 10);
					gc.drawString("Delete", 283 + i * 273, 403);
					gc.drawString("Rename", 283 + i * 273, 426);
					gc.drawString("Duplicate", 283 + i * 273, 449);
				}

			} else {

				if (threeDots[i]) {
					gc.setFont(smallerFont);
					gc.setColor(Color.WHITE);
					gc.fillRoundRect(275 + (i - 5) * 270, 735, 100, 75, 10, 10);
					gc.setColor(Color.BLACK);
					gc.drawRoundRect(275 + (i - 5) * 270, 735, 100, 75, 10, 10);
					gc.drawString("Delete", 283 + (i - 5) * 273, 753);
					gc.drawString("Rename", 283 + (i - 5) * 273, 776);
					gc.drawString("Duplicate", 283 + (i - 5) * 273, 799);
				}
			}
		}
	}

	/**
	 * Deletes a save if the user confirms they want it deleted.
	 * 
	 * @param gc The graphics console.
	 * @param i  The index of the save.
	 */
	static void deleteFile(GraphicsConsole gc, int i) {

		String f[] = files.get(i).split(".txt");

		// Confirming they want the file deleted
		int choice = JOptionPane.showConfirmDialog(gc, "Are you sure you want to delete '" + f[0] + "'?",
				"Delete Confirmation", JOptionPane.YES_NO_OPTION);

		if (choice == JOptionPane.YES_OPTION) {

			// Deleting the text file
			File remove = new File(files.get(i));
			remove.delete();

			// deleting the preview image
			String n = files.get(i);
			String na[] = n.split(".txt");
			File file = new File(na[0] + ".png");
			file.delete();

			// try catch used for if an preview image has never been made for the save.
			try {
				images.remove(i);
			} catch (IndexOutOfBoundsException x) {
				System.out.println("Failed to delete image");
			}

			files.remove(i);
			datesCreated.remove(i);

			updateNamesFile();
		}
	}

	/**
	 * Renames a save to whatever the user enters as the new name.
	 * 
	 * @param gc The graphics console.
	 * @param i  The index of the save.
	 */
	static void renameFile(GraphicsConsole gc, int i) {

		// getting the new name
		String newName = JOptionPane.showInputDialog("Enter the new name for the text file:");

		// Check if the user entered a new name
		if (newName != null && !newName.isEmpty()) {

			File newNameFile = new File(newName + ".txt");
			File newNameImage = new File(newName + ".png");

			File rename = new File(files.get(i));
			rename.renameTo(newNameFile);

			String na[] = files.get(i).split(".txt");
			File renameImage = new File(na[0] + ".png");
			renameImage.renameTo(newNameImage);

			files.set(i, String.valueOf(newNameFile));

			updateNamesFile();

		}
	}

	/**
	 * Creates a copy of a save.
	 * 
	 * @param gc The graphics console.
	 * @param i  The index of the save to be coped.
	 * @throws IOException
	 */
	static void duplicateFile(GraphicsConsole gc, int i) throws IOException {

		boolean made = false;

		String f = files.get(i);
		File copy;

		// adding how ever many 'copy of's are needed for the file name to not be taken
		do {
			f = "Copy of " + f;
			copy = new File(f);

			if (copy.createNewFile()) {

				made = true;
			}

		} while (!made);

		File file = new File(files.get(i));

		// coping all the data to the new file
		try (BufferedReader reader = new BufferedReader(new FileReader(file));
				BufferedWriter writer = new BufferedWriter(new FileWriter(copy))) {

			String line;

			while ((line = reader.readLine()) != null) {
				writer.write(line);
				writer.newLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String currentDateTime = dateFormat.format(currentDate);

		files.add(String.valueOf(copy));
		datesCreated.add(currentDateTime);

		updateNamesFile();

	}

	/**
	 * Updates the Names.txt file to match the current data stored in the arrays.
	 */
	static void updateNamesFile() {

		File names = new File("Names.txt");

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(names));

			for (int i = 0; i < files.size(); i++) {
				writer.write(files.get(i));
				writer.newLine();
				writer.write(datesCreated.get(i));
				writer.newLine();
			}

			writer.close();
		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Creates a new save. Creates the popup to enter the new saves name.
	 * 
	 * @param gc The graphics console.
	 */
	static void createNew(GraphicsConsole gc) {

		// creating the popup to enter the saves name.
		JFrame frame = new JFrame("New Project");

		// Create label and text field for project name
		JLabel nameLabel = new JLabel("Project Name:");
		JTextField nameField = new JTextField(20);

		// Create button to create the project
		JButton createButton = new JButton("Create");
		createButton.addActionListener(e -> {

			String projectName = nameField.getText();

			if (!projectName.isEmpty()) {

				try {
					// Create a new text file with the entered project name
					projectFile = new File(projectName + ".txt");

					if (projectFile.createNewFile()) {

						addAxis();
						successfullyCreated = true;

						addToFileList();

						JOptionPane.showMessageDialog(frame, "Project file created: " + projectName + ".txt");

						// Close the popup window after project creation
						frame.dispose();

					} else {
						JOptionPane.showMessageDialog(frame, "Project file already exists.");
					}

				} catch (IOException ex) {
					JOptionPane.showMessageDialog(frame,
							"Error occurred while creating project file: " + ex.getMessage());
				}

				frame.dispose();
			} else {
				JOptionPane.showMessageDialog(frame, "Please enter a project name.");
			}
		});

		// Create panel and add components
		JPanel panel = new JPanel();
		panel.add(nameLabel);
		panel.add(nameField);
		panel.add(createButton);

		// Add panel to frame and set frame properties
		frame.add(panel);
		frame.setSize(250, 125);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.setLocationRelativeTo(null); // Center the frame on the screen
		frame.setVisible(true);

	}

	/**
	 * Adds a new save to the names.txt file
	 */
	static void addToFileList() {

		File names = new File("Names.txt");

		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter(names));

			Date currentDate = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentDateTime = dateFormat.format(currentDate);

			for (int i = 0; i < files.size(); i++) {
				writer.write(files.get(i));
				writer.newLine();

				writer.write(String.valueOf(currentDateTime));
				writer.newLine();
				writer.write(String.valueOf(currentDateTime));
				writer.newLine();
			}

			writer.write(String.valueOf(projectFile));
			writer.newLine();
			writer.write(String.valueOf(currentDateTime));
			writer.newLine();

			writer.close();

		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Reads in all the objects from the save file and adds them to the
	 * corresponding array lists.
	 */
	public static void load() {

		try {
			Scanner freader = new Scanner(projectFile);

			// lines
			int numLines = freader.nextInt();

			for (int i = 0; i < numLines; i++) {
				double p[][] = new double[2][3];
				p[0][0] = freader.nextDouble();
				p[0][1] = freader.nextDouble();
				p[0][2] = freader.nextDouble();

				p[1][0] = freader.nextDouble();
				p[1][1] = freader.nextDouble();
				p[1][2] = freader.nextDouble();

				int c[] = new int[3];
				c[0] = freader.nextInt();
				c[1] = freader.nextInt();
				c[2] = freader.nextInt();

				double plain[][] = new double[4][3];
				for (int j = 0; j < 4; j++) {
					plain[j][0] = freader.nextDouble();
					plain[j][1] = freader.nextDouble();
					plain[j][2] = freader.nextDouble();
				}
				ThreeDObjects.Lines.add(new Line(p[0], p[1], new Color(c[0], c[1], c[2]),
						new Plane(plain[0], plain[1], plain[2], plain[3])));
			}

			// planes
			freader.nextLine();
			int numPlanes = freader.nextInt();

			for (int i = 0; i < numPlanes; i++) {

				double p[][] = new double[4][3];
				for (int j = 0; j < 4; j++) {
					p[j][0] = freader.nextDouble();
					p[j][1] = freader.nextDouble();
					p[j][2] = freader.nextDouble();
				}

				ThreeDObjects.Planes.add(new Plane(p[0], p[1], p[2], p[3]));
			}

			// shapes
			freader.nextLine();
			int numShapes = freader.nextInt();

			for (int i = 0; i < numShapes; i++) {
				int numCorners = freader.nextInt();

				Point p[] = new Point[numCorners];
				for (int j = 0; j < numCorners; j++) {
					double pc[] = new double[3];
					pc[0] = freader.nextDouble();
					pc[1] = freader.nextDouble();
					pc[2] = freader.nextDouble();
					p[j] = new Point(pc);
				}

				double plain[][] = new double[4][3];
				for (int j = 0; j < 4; j++) {
					plain[j][0] = freader.nextDouble();
					plain[j][1] = freader.nextDouble();
					plain[j][2] = freader.nextDouble();
				}

				ThreeDObjects.Shapes.add(new Shape(p, new Plane(plain[0], plain[1], plain[2], plain[3])));
			}

			// Sides
			freader.nextLine();
			int numSides = freader.nextInt();

			for (int i = 0; i < numSides; i++) {
				int numCorners = freader.nextInt();

				Point p[] = new Point[numCorners];
				for (int j = 0; j < numCorners; j++) {
					double pc[] = new double[3];
					pc[0] = freader.nextDouble();
					pc[1] = freader.nextDouble();
					pc[2] = freader.nextDouble();
					p[j] = new Point(pc);
				}

				int c[] = new int[3];
				c[0] = freader.nextInt();
				c[1] = freader.nextInt();
				c[2] = freader.nextInt();

				ThreeDObjects.Sides.add(new Side(p, new Color(c[0], c[1], c[2])));
			}

			freader.close();

		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Everything that is needed to be done while lunching a save.
	 * 
	 * @param gc The graphics console.
	 */
	static void setUp(GraphicsConsole gc) {

		for (int i = 0; i < 14; i++) {
			ButtonPrinter.buttonClick[i] = false;
		}

		ButtonPrinter.dropdown[0] = false;
		ButtonPrinter.dropdown[1] = false;
		ButtonPrinter.dropdown[2] = false;

		Main.buttons[0] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Mouse.png"));
		Main.buttons[1] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Eraser.png"));
		Main.buttons[2] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Plain.png"));
		Main.buttons[3] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Line.png"));
		Main.buttons[4] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Extrude.png"));
		Main.buttons[5] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Move.png"));
		Main.buttons[6] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Paint.png"));
		Main.buttons[7] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Measure.png"));

		Main.buttons[8] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Rectangle.png"));
		Main.buttons[9] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Polygon.png"));
		Main.buttons[10] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Outline.png"));

		Main.buttons[11] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/ChangeSize.png"));
		Main.buttons[12] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/Rotate.png"));
		Main.buttons[13] = Toolkit.getDefaultToolkit()
				.getImage(gc.getClass().getClassLoader().getResource("photos/ColorPicker.png"));

		Font mesurFont = new Font("SansSerif", Font.PLAIN, 25);

		// setting up the text box for the input of measurements.
		Main.field.getDocument().addDocumentListener(new DocumentListener() {

			// restricts the text to being a decimal
			@Override
			public void insertUpdate(DocumentEvent e) {
				Runnable format = new Runnable() {
					@Override
					public void run() {
						String text = Main.field.getText();
						if (!text.matches("\\-?\\d*(\\.\\d{0,2})?")) {
							Main.field.setText(text.substring(0, text.length() - 1));
						}

					}
				};
				SwingUtilities.invokeLater(format);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}

		});

		Main.field.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					Main.shift = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Main.enter = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					Main.escape = true;
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
					Main.shift = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Main.enter = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					Main.escape = false;
				}
			}
		});

		Main.field.setFont(mesurFont);
		Main.field.setColumns(12);

		SpringLayout lay = new SpringLayout();

		lay.putConstraint(SpringLayout.WEST, Main.field, -300, SpringLayout.EAST, gc);
		lay.putConstraint(SpringLayout.NORTH, Main.field, -105, SpringLayout.SOUTH, gc);

		gc.canvas.setLayout(lay);

		gc.canvas.add(Main.field);
		Main.field.requestFocusInWindow();
	}

	/**
	 * Adds the axis to a new save.
	 */
	static void addAxis() {

		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(projectFile));

			// lines
			writer.write(String.valueOf(3));
			writer.newLine();

			//red axis
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(255));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();

			// blue axis
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(10));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(255));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(10));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(10));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();

			// green axis
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(255));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();

			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();
			writer.write(String.valueOf(10));
			writer.newLine();

			// Planes
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			// Shapes
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			// Sides
			writer.newLine();
			writer.write(String.valueOf(0));
			writer.newLine();

			writer.close();

		} catch (IOException e) {
			System.err.println(e);
			System.exit(1);
		}
	}

}
