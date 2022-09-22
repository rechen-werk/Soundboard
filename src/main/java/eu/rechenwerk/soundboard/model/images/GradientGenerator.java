package eu.rechenwerk.soundboard.model.images;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class GradientGenerator {
	/**
	 * Generates a Image with a gradient between the corners.
	 * @param width The width of the image
	 * @param height The height of the image
	 * @param c0 The color in the top left corner
	 * @param c1 The color in the bottom left corner
	 * @param c2 The color in the top right corner
	 * @param c3 The color in the bottom right corner
	 * @return The image with gradient
	 */
	public static Image generateImage(int width, int height, Color c0, Color c1, Color c2, Color c3) {
		return convertToFxImage(createGradient(width, height, c0, c1, c2, c3));
	}

	/**
	 * Generates a gradient image overlayed with another image.
	 * @param resourceName name of the resource with the image
	 * @param c0 The color in the top left corner
	 * @param c1 The color in the bottom left corner
	 * @param c2 The color in the top right corner
	 * @param c3 The color in the bottom right corner
	 * @return The image with gradient overlayed with the image
	 */
	public static Image generateImage(String resourceName, Color c0, Color c1, Color c2, Color c3){
		BufferedImage speaker;
		try {
			URL url = GradientGenerator.class.getClassLoader().getResource(resourceName);
			if(url == null) {
				speaker = new BufferedImage(500,500, BufferedImage.TYPE_INT_ARGB);
			} else {
				speaker = ImageIO.read(url);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		BufferedImage image = createGradient(speaker.getWidth(),speaker.getHeight(),c0,c1,c2,c3);
		image.getGraphics().drawImage(speaker, 0,0, null);

		return convertToFxImage(image);
	}
	private static BufferedImage createGradient(int width, int height, Color c0, Color c1, Color c2, Color c3) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		double w = (1.* (width - 1));
		double h = (1.* (height - 1));
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double w1 = x/w;
				double w0 = 1.-w1;
				double h1 = y/h;
				double h0 = 1.-h1;

				int r = (int) (c0.getRed() * (w0*h0) + c1.getRed() * (w0*h1) + c2.getRed() * (w1*h0) + c3.getRed() * (w1*h1));
				int g = (int) (c0.getGreen() * (w0*h0) + c1.getGreen() * (w0*h1) + c2.getGreen() * (w1*h0) + c3.getGreen() * (w1*h1));
				int b = (int) (c0.getBlue() * (w0*h0) + c1.getBlue() * (w0*h1) + c2.getBlue() * (w1*h0) + c3.getBlue() * (w1*h1));
				int rgb = (0xFF << 24) + (r << 16) + (g << 8) + b;
				image.setRGB(x, y, rgb);
			}
		}
		Graphics g = image.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,width/150, height);
		g.fillRect(0,0,width, height/150);
		g.fillRect(width-width/150,0,width/150, height);
		g.fillRect(0,height-height/150,width, height/150);
		return image;
	}

	private static Image convertToFxImage(BufferedImage image) {
		WritableImage wr = null;
		if (image != null) {
			wr = new WritableImage(image.getWidth(), image.getHeight());
			PixelWriter pw = wr.getPixelWriter();
			for (int x = 0; x < image.getWidth(); x++) {
				for (int y = 0; y < image.getHeight(); y++) {
					pw.setArgb(x, y, image.getRGB(x, y));
				}
			}
		}

		return new ImageView(wr).getImage();
	}

}
