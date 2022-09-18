package eu.rechenwerk.soundboard.controller;

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

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;


import java.io.*;
import java.util.List;

public class SoundBoardController {
	public final static String CONFIG_FILE = "config.json";
	public final static String SOUNDS = "sounds";
	public final static String MICROPHONES = "microphones";
	public final static String NAME = "name";
	public final static String DEVICE = "device";
	private String soundDir;
	@FXML private TextField soundNameField;
	@FXML private TextField soundImageField;
	@FXML private TextField soundFileField;
	@FXML private TextField virtualMicrophoneNameField;
	@FXML private ComboBox<String> inputDevicesComboBox;
	@FXML private ListView<MicrophoneCell> microphoneListView;

	public void init(Stage stage) throws OsNotSupportedException {

		InputStream is = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
		if (is == null) {
			throw new NullPointerException("Cannot find resource file " + CONFIG_FILE);
		}

		JSONObject object = new JSONObject(new JSONTokener(is));
		soundDir = object.getString(SOUNDS);

		JSONArray microphones = object.getJSONArray(MICROPHONES);
		for (Object obj: microphones) {

			microphoneListView
				.getItems()
				.add(
					new MicrophoneCell(VirtualMicrophone
						.create(((JSONObject)obj).getString(NAME),
							((JSONObject)obj).getString(DEVICE)
						),
						true)
				);
		}

		stage.setOnCloseRequest(event -> {
			JSONObject state = new JSONObject();
			state.put(SOUNDS, soundDir);
			JSONArray microState = new JSONArray();
			state.put(MICROPHONES, microState);
			microphoneListView
			.getItems()
			.forEach(it ->
				{
					if(it.isLocked()) {
						JSONObject mic = new JSONObject();
						mic.put(NAME, it.getMicrophone().getName());
						mic.put(DEVICE, it.getMicrophone().getDevice());
						microState.put(mic);
					}
					it.getMicrophone().delete();
				});
			PrintWriter writer;
			try {
				String f = getClass().getClassLoader().getResource(CONFIG_FILE).getFile();
				System.out.println(f);
				writer = new PrintWriter(new FileOutputStream(f));
			} catch (FileNotFoundException e) {
				throw new NullPointerException("Cannot find resource file " + CONFIG_FILE);
			}
			state.write(writer, 4, 0);
			writer.flush();
			writer.close();
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