package eu.rechenwerk.soundboard.view;

import eu.rechenwerk.soundboard.model.images.GradientGenerator;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import eu.rechenwerk.soundboard.model.sounds.Sound;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.awt.Color;

public class SoundPane extends Pane {

	public SoundPane(Color tl, Color bl, Color tr, Color br, Sound sound, ToggleGroup microphoneSelection) {
		Image[] images = GradientGenerator.generateImages(tl, bl, tr, br, "speaker.png", "play.png");
		Background[] backgrounds = new Background[images.length];
		for (int i = 0; i < images.length; i++) {
			backgrounds[i] = new Background(new BackgroundImage(
				images[i],
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(1.0, 1.0, true, true, false, false)
			));
		}
		if(sound != null) {
			getChildren().addAll(
				new VBox(
					new Label(sound.getName()),
					new Label(String.format("%.2f", sound.getDuration()))
				)
			);

			setOnMouseClicked(event -> sound.play((VirtualMicrophone) microphoneSelection.getSelectedToggle().getUserData()));
			setOnMouseEntered(event -> setBackground(backgrounds[2]));
			setOnMouseExited(event -> setBackground(backgrounds[1]));
			setBackground(backgrounds[1]);
		} else {
			setBackground(backgrounds[0]);
		}

	}
}
