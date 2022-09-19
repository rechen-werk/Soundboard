package eu.rechenwerk.soundboard.view;

import eu.rechenwerk.soundboard.model.images.GradientGenerator;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.awt.*;

public class SoundPane extends Pane {

	public SoundPane() {
		Image image = GradientGenerator.generateImage("speaker.png", Color.MAGENTA, Color.RED, Color.CYAN, Color.ORANGE);

		this.setBackground(new Background(new BackgroundImage(
			image,
			BackgroundRepeat.NO_REPEAT,
			BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.DEFAULT,
			new BackgroundSize(1.0, 1.0, true, true, false, false)
		)));
	}
}
