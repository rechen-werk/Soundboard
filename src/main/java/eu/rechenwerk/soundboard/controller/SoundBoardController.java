package eu.rechenwerk.soundboard.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SoundBoardController {
	@FXML
	private Label welcomeText;

	@FXML
	protected void onHelloButtonClick() {
		welcomeText.setText("Welcome to JavaFX Application!");
	}
}