package eu.rechenwerk.soundboard.view;

import eu.rechenwerk.soundboard.model.images.GradientGenerator;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import javafx.scene.image.Image;
import javafx.scene.layout.*;


import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Optional;

public class SoundPane extends Pane {
	private final VirtualMicrophone selectedMicrophone;
	public SoundPane(Color tl, Color bl, Color tr, Color br, Optional<File> audio, List<VirtualMicrophone> mircophones) {
		selectedMicrophone = mircophones.get(0);
		Image image = audio.isEmpty()
			? GradientGenerator.generateImage(500,500, tl, bl, tr, br)
			: GradientGenerator.generateImage("speaker.png", tl, bl, tr, br);

		this.setBackground(new Background(new BackgroundImage(
			image,
			BackgroundRepeat.NO_REPEAT,
			BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.DEFAULT,
			new BackgroundSize(1.0, 1.0, true, true, false, false)
		)));
		this.setOnMouseClicked(event -> audio.ifPresent(selectedMicrophone::play));
	}
}
