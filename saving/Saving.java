package saving;

import java.awt.Component;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import drawer.objects.ThreeDObjects;
import renderer.Main;

/**
 * This class holds everything to do with the saving of a project.
 * 
 * Created: May 28, 2024
 * Last updated: June 2, 2024
 * 
 * @author Blake McCulligh
 */
public class Saving {

	/**
	 * Writes all the info to the save.txt that is needed to be saved for the
	 * objects before the closing of the program
	 */
	public static void save() {
		if (Opening.projectFile != null) {
			try {
				BufferedWriter writer = new BufferedWriter(new FileWriter(Opening.projectFile));

				// lines
				writer.write(String.valueOf(ThreeDObjects.Lines.size()));
				writer.newLine();
				for (int i = 0; i < ThreeDObjects.Lines.size(); i++) {
					writer.write(String.valueOf(ThreeDObjects.Lines.get(i).points[0].x));
					writer.newLine();
					writer.write(String.valueOf(ThreeDObjects.Lines.get(i).points[0].y));
					writer.newLine();
					writer.write(String.valueOf(ThreeDObjects.Lines.get(i).points[0].z));
					writer.newLine();

					writer.write(String.valueOf(ThreeDObjects.Lines.get(i).points[1].x));
					writer.newLine();
					writer.write(String.valueOf(ThreeDObjects.Lines.get(i).points[1].y));
					writer.newLine();
					writer.write(String.valueOf(ThreeDObjects.Lines.get(i).points[1].z));
					writer.newLine();

					writer.write(String.valueOf(ThreeDObjects.Lines.get(i).originalC.getGreen()));
					writer.newLine();
					writer.write(String.valueOf(ThreeDObjects.Lines.get(i).originalC.getRed()));
					writer.newLine();
					writer.write(String.valueOf(ThreeDObjects.Lines.get(i).originalC.getBlue()));
					writer.newLine();

					for (int j = 0; j < 4; j++) {
						writer.write(String.valueOf(ThreeDObjects.Lines.get(i).plane.points[j].x));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Lines.get(i).plane.points[j].y));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Lines.get(i).plane.points[j].z));
						writer.newLine();
					}
				}

				// Planes
				writer.newLine();
				writer.write(String.valueOf(ThreeDObjects.Planes.size()));
				writer.newLine();
				for (int i = 0; i < ThreeDObjects.Planes.size(); i++) {
					for (int j = 0; j < 4; j++) {
						writer.write(String.valueOf(ThreeDObjects.Planes.get(i).points[j].x));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Planes.get(i).points[j].y));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Planes.get(i).points[j].z));
						writer.newLine();
					}
				}

				// Shapes
				writer.newLine();
				writer.write(String.valueOf(ThreeDObjects.Shapes.size()));
				writer.newLine();
				for (int i = 0; i < ThreeDObjects.Shapes.size(); i++) {
					writer.write(String.valueOf(ThreeDObjects.Shapes.get(i).points.length));
					writer.newLine();
					for (int j = 0; j < ThreeDObjects.Shapes.get(i).points.length; j++) {
						writer.write(String.valueOf(ThreeDObjects.Shapes.get(i).points[j].x));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Shapes.get(i).points[j].y));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Shapes.get(i).points[j].z));
						writer.newLine();
					}

					for (int j = 0; j < 4; j++) {
						writer.write(String.valueOf(ThreeDObjects.Shapes.get(i).plane.points[j].x));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Shapes.get(i).plane.points[j].y));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Shapes.get(i).plane.points[j].z));
						writer.newLine();
					}
				}

				// Sides
				writer.newLine();
				writer.write(String.valueOf(ThreeDObjects.Sides.size()));
				writer.newLine();
				for (int i = 0; i < ThreeDObjects.Sides.size(); i++) {
					writer.write(String.valueOf(ThreeDObjects.Sides.get(i).points.length));
					writer.newLine();
					for (int j = 0; j < ThreeDObjects.Sides.get(i).points.length; j++) {
						writer.write(String.valueOf(ThreeDObjects.Sides.get(i).points[j].x));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Sides.get(i).points[j].y));
						writer.newLine();
						writer.write(String.valueOf(ThreeDObjects.Sides.get(i).points[j].z));
						writer.newLine();
					}

					writer.write(String.valueOf(ThreeDObjects.Sides.get(i).originalC.getRed()));
					writer.newLine();
					writer.write(String.valueOf(ThreeDObjects.Sides.get(i).originalC.getGreen()));
					writer.newLine();
					writer.write(String.valueOf(ThreeDObjects.Sides.get(i).originalC.getBlue()));
					writer.newLine();
				}

				writer.close();

				System.out.print("saved susesfully");

			} catch (IOException e) {
				System.err.println(e);
				System.exit(1);
			}

			// savePrevewImage can not be called from hear as the frame is needed and can
			// only be accessed in main.
			Main.saveImage();
		}

	}

	/**
	 * Saves the preview image for the save when closing.
	 * 
	 * @param frame The frame for the graphics console.
	 */
	public static void savePrevewImage(JFrame frame) {

		String fileName[] = String.valueOf(Opening.projectFile).split(".txt");
		String filePath = fileName[0] + ".png";

		try {
			// Get the content pane of the frame
			Component contentPane = frame.getContentPane();

			// Create a BufferedImage to hold the screenshot
			BufferedImage image = new BufferedImage(contentPane.getWidth(), contentPane.getHeight(),
					BufferedImage.TYPE_INT_RGB);

			// Create a graphics context from the BufferedImage
			contentPane.paint(image.getGraphics());

			// Crop the image to the center pixels
			int centerX = (image.getWidth() - 706) / 2;
			int centerY = (image.getHeight() - 500) / 2;
			BufferedImage croppedImage = image.getSubimage(centerX, centerY, 706, 500);

			// Save the cropped screenshot
			ImageIO.write(croppedImage, "png", new File(filePath));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
