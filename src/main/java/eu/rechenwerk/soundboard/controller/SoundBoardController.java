package eu.rechenwerk.soundboard.controller;

import eu.rechenwerk.soundboard.converters.ConfigConverter;
import eu.rechenwerk.soundboard.model.Config;
import eu.rechenwerk.soundboard.model.exceptions.OsNotSupportedException;
import eu.rechenwerk.soundboard.model.microphone.Terminal;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import eu.rechenwerk.soundboard.view.MicrophoneCell;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


import org.json.JSONObject;
import static eu.rechenwerk.soundboard.framework.IO.*;


import java.io.*;
import java.util.List;

public class SoundBoardController {
	public final static String CONFIG_FILE = "config.json";

	private String soundDir;
	@FXML private TextField soundNameField;
	@FXML private TextField soundImageField;
	@FXML private TextField soundFileField;
	@FXML private TextField virtualMicrophoneNameField;
	@FXML private ComboBox<String> inputDevicesComboBox;
	@FXML private ListView<MicrophoneCell> microphoneListView;

	public void init(Stage stage) throws FileNotFoundException {


		JSONObject object = new JSONObject(readResource(CONFIG_FILE));
		Config config = new ConfigConverter().deserialize(object.toString());
		soundDir = config.sounds();
		microphoneListView
			.getItems()
			.addAll(
				config
					.microphones()
					.stream()
					.map(it ->
						new MicrophoneCell(it, true)
					).toList()
			);

		stage.setOnCloseRequest(event -> {
			List<VirtualMicrophone> persist = microphoneListView
				.getItems()
				.stream()
				.filter(MicrophoneCell::isLocked)
				.map(MicrophoneCell::getMicrophone)
				.toList();
			JSONObject configJSON = new JSONObject(
				new ConfigConverter().serialize(
					new Config(soundDir, persist)
				)
			);
			microphoneListView
				.getItems()
				.stream()
				.map(MicrophoneCell::getMicrophone)
				.forEach(VirtualMicrophone::delete);

			try {
				writeResource(CONFIG_FILE, configJSON.toString(4));
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		});
	}

	@FXML protected void onAddSoundClick() {
			soundNameField.setText("HI");
	}

	@FXML protected void onCreateMicrophoneClick() throws OsNotSupportedException {
		String microphoneName = virtualMicrophoneNameField.getText();
		if(microphoneName == null || microphoneName.isBlank()) {
			return;
		}
		microphoneListView
			.getItems()
			.add(
				new MicrophoneCell(VirtualMicrophone.create(
					microphoneName, inputDevicesComboBox.getSelectionModel().getSelectedItem()
				),
				false)

			);
	}

	@FXML protected void onChooseImageClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select image file");
		File audio = fileChooser.showOpenDialog(soundImageField.getScene().getWindow());
		if(audio != null) {
			soundImageField.setText(audio.getAbsolutePath());
		}
	}

	@FXML protected void onChooseFileClick() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Select audio file");
		File audio = fileChooser.showOpenDialog(soundNameField.getScene().getWindow());
		if(audio != null) {
			soundFileField.setText(audio.getAbsolutePath());
		}
	}

	@FXML protected void refreshCombobox() throws OsNotSupportedException {
		List<String> devices = Terminal.getInstance().listOutputDevices();
		inputDevicesComboBox.setItems(FXCollections.observableList(devices));
	}
}