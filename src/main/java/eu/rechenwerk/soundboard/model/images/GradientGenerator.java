package eu.rechenwerk.soundboard.model.images;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import java.awt.Color;

public class GradientGenerator {
	public static void generateImage(int width, int height, Color c0, Color c1, Color c2, Color c3) throws IOException {
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
		ImageIO.write(image, "png", new File("/home/rechenwerk/Downloads/gradient.png"));
	}

	public static void generateImage(int width, int height, Color c0, Color c1) throws IOException {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				double w0 = x / (1.* (width - 1));
				double w1 = 1. - w0;

				int r = (int) (c0.getRed() * w0 + c1.getRed() * w1);
				int g = (int) (c0.getGreen() * w0 + c1.getGreen() * w1);
				int b = (int) (c0.getBlue() * w0 + c1.getBlue() * w1);
				int rgb = (0xFF << 24) + (r << 16) + (g << 8) + b;
				image.setRGB(x, y, rgb);
			}
		}
		ImageIO.write(image, "png", new File("/home/rechenwerk/Downloads/gradient.png"));
	}
}
