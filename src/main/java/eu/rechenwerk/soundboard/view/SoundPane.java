package eu.rechenwerk.soundboard.view;

import eu.rechenwerk.soundboard.model.images.GradientGenerator;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import eu.rechenwerk.soundboard.model.sounds.Sound;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;


import java.awt.Color;
import java.util.List;

public class SoundPane extends Pane {
	private final VirtualMicrophone selectedMicrophone;


	public SoundPane(Color tl, Color bl, Color tr, Color br, Sound sound, List<VirtualMicrophone> microphones) {
		selectedMicrophone = microphones.get(0);
		Image image;
		if(sound == null) {
			image = GradientGenerator.generateImage(500,500, tl, bl, tr, br);
		} else {
			getChildren().addAll(
				new VBox(
					new Label(sound.getName()),
					new Label(String.format("%.2f", sound.getDuration()))
				)
			);
			image = GradientGenerator.generateImage("speaker.png", tl, bl, tr, br);
			setOnMouseClicked(event -> sound.play(selectedMicrophone));
			hoverProperty().addListener((obs, o, n) -> System.out.println(sound.getName()));
		}

		setBackground(new Background(new BackgroundImage(
			image,
			BackgroundRepeat.NO_REPEAT,
			BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.DEFAULT,
			new BackgroundSize(1.0, 1.0, true, true, false, false)
		)));



	}
}
