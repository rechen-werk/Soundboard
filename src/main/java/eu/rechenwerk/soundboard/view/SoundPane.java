package eu.rechenwerk.soundboard.view;

import eu.rechenwerk.soundboard.model.images.GradientGenerator;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import eu.rechenwerk.soundboard.model.sounds.Sound;
import javafx.scene.image.Image;
import javafx.scene.layout.*;


import java.awt.*;
import java.util.List;

public class SoundPane extends Pane {
	private final VirtualMicrophone selectedMicrophone;
	public SoundPane(Color tl, Color bl, Color tr, Color br, Sound sound, List<VirtualMicrophone> microphones) {
		selectedMicrophone = microphones.get(0);
		Image image = sound == null
			? GradientGenerator.generateImage(500,500, tl, bl, tr, br)
			: GradientGenerator.generateImage("speaker.png", tl, bl, tr, br);

		setBackground(new Background(new BackgroundImage(
			image,
			BackgroundRepeat.NO_REPEAT,
			BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.DEFAULT,
			new BackgroundSize(1.0, 1.0, true, true, false, false)
		)));
		setOnMouseClicked(event -> {
			if(sound != null) {
				sound.play(selectedMicrophone);
			}
		});
		this.hoverProperty().addListener((obs, o, n) -> {
			if (sound != null) {
				System.out.println(sound.getName());
			}
		});
	}
}
