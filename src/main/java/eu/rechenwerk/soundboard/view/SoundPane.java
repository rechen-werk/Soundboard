package eu.rechenwerk.soundboard.view;

import eu.rechenwerk.soundboard.model.images.GradientGenerator;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import eu.rechenwerk.soundboard.model.sounds.Sound;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;

import java.awt.Color;
import java.util.List;

public class SoundPane extends Pane {
	private final VirtualMicrophone selectedMicrophone;

	public SoundPane(Color tl, Color bl, Color tr, Color br, Sound sound, List<VirtualMicrophone> microphones) {
		selectedMicrophone = microphones.get(0);

		Image[] images = GradientGenerator.generateImages(tl, bl, tr, br, "speaker.png", "play.png");

		Image image;
		if(sound != null) {
			getChildren().addAll(
				new VBox(
					new Label(sound.getName()),
					new Label(String.format("%.2f", sound.getDuration()))
				)
			);

			setOnMouseClicked(event -> sound.play(selectedMicrophone));
			addEventHandler(MouseEvent.MOUSE_ENTERED, event ->
				setBackground(new Background(new BackgroundImage(
				images[2],
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(1.0, 1.0, true, true, false, false)
			))));
			addEventHandler(MouseEvent.MOUSE_EXITED, event ->
				setBackground(new Background(new BackgroundImage(
				images[1],
				BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT,
				BackgroundPosition.DEFAULT,
				new BackgroundSize(1.0, 1.0, true, true, false, false)
			))));
			image = images[1];
		} else {
			image = images[0];
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
