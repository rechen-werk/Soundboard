package eu.rechenwerk.soundboard.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.io.File;

public class SoundBoardController {
	@FXML
	private TextField soundNameField;
	@FXML
	private TextField soundImageField;
	@FXML
	private TextField soundFileField;

	@FXML
	private ComboBox<String> inputDevicesComboBox;

	@FXML
	private TextField virtualMicrophoneNameField;

	@FXML
	protected void onAddSoundClick() {
			soundNameField.setText("HI");
	}

	@FXML
	protected void onCreateMicrophoneClick() {
		soundNameField.setText("HI");
	}

	@FXML
	protected void onChooseImageClick() {
		soundNameField.setText("HI");
	}

	@FXML
	protected void onChooseFileClick() {
		soundNameField.setText("HI");
	}

}