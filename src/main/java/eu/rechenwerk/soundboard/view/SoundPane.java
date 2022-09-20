package eu.rechenwerk.soundboard.view;

import eu.rechenwerk.soundboard.model.images.GradientGenerator;

import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.awt.*;
import java.io.File;
import java.util.Optional;
import java.util.Random;

public class SoundPane extends Pane {
	private final Optional<File> audio;
	public SoundPane(Color tl, Color bl, Color tr, Color br, Optional<File> audio) {
		this.audio = audio;
		Image image = new Random().nextBoolean()
			? GradientGenerator.generateImage(500,500, tl, bl, tr, br)
			: GradientGenerator.generateImage("speaker.png", tl, bl, tr, br);

		this.setBackground(new Background(new BackgroundImage(
			image,
			BackgroundRepeat.NO_REPEAT,
			BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.DEFAULT,
			new BackgroundSize(1.0, 1.0, true, true, false, false)
		)));
		this.setOnMouseClicked(event -> {
			System.out.println(audio);
		});
	}
}
