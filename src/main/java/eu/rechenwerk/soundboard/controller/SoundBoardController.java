package eu.rechenwerk.soundboard.controller;

import eu.rechenwerk.soundboard.converters.JSONConverter;
import eu.rechenwerk.soundboard.model.Config;
import eu.rechenwerk.soundboard.model.exceptions.OsNotSupportedException;
import eu.rechenwerk.soundboard.model.microphone.Terminal;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import eu.rechenwerk.soundboard.view.MicrophoneCell;
import eu.rechenwerk.soundboard.view.SoundPane;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import static eu.rechenwerk.soundboard.framework.IO.*;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.List;

public class SoundBoardController {
	public final static String CONFIG_FILE = "config.json";
	private String sounds;
	private List<Color> colors;

	@FXML private TextField soundNameField;
	@FXML private TextField soundImageField;
	@FXML private TextField soundFileField;
	@FXML private TextField virtualMicrophoneNameField;
	@FXML private ComboBox<String> inputDevicesComboBox;
	@FXML private ListView<MicrophoneCell> microphoneListView;
	@FXML private GridPane soundGridPane;

	public void init(Stage stage) throws FileNotFoundException {
		Config config = JSONConverter.CONFIG.deserialize(readResource(CONFIG_FILE));
		sounds = config.sounds();
		colors = config.colors();
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

		initAudioBoard();
		stage.setOnCloseRequest(event -> cleanup());
	}

	@FXML protected void onAddSoundClick() {
		String audioFileString = soundNameField.getText();
		String audioImageString = soundImageField.getText();
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

	private void persist() {
		try {
			writeResource(
				CONFIG_FILE,
				JSONConverter.CONFIG.serializeIndented(new Config(
					sounds,
					microphoneListView
						.getItems()
						.stream()
						.filter(MicrophoneCell::isLocked)
						.map(MicrophoneCell::getMicrophone)
						.toList(),
					colors
				), 4)
			);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void cleanup(){
		persist();
		microphoneListView
			.getItems()
			.stream()
			.map(MicrophoneCell::getMicrophone)
			.forEach(VirtualMicrophone::delete);
	}

	private void initAudioBoard() {
		if(colors.size() == 0) {
			colors = List.of(Color.WHITE);
		}
		Color[][] gridColors = new Color[soundGridPane.getRowCount()+1][soundGridPane.getColumnCount()+1];

		for (int row = 0; row < gridColors.length; row++) {
			for (int col = 0; col < gridColors[row].length; col++) {
				gridColors[row][col] = colors.get(new Random().nextInt(colors.size()));
			}
		}
		URL soundsDirURL = getClass().getResource(sounds);
		if(soundsDirURL == null) throw new RuntimeException("Sounds directory could not be found!");
		File soundsDir = new File(soundsDirURL.getFile());
		List<File> audios = Arrays
			.stream(soundsDir.listFiles())
			.filter(File::isFile)
			.filter(file-> file.getName().endsWith(".ogg"))
			.toList();

		int index = 0;

		for (int row = 0; row < soundGridPane.getRowCount(); row++) {
			for (int col = 0; col < soundGridPane.getColumnCount(); col++) {
				soundGridPane.add(new SoundPane(
					colors.get(new Random().nextInt(colors.size())),
					gridColors[row+1][col],
					gridColors[row][col+1],
					gridColors[row+1][col+1],
					index < audios.size()
						? Optional.of(audios.get(index))
						: Optional.empty(),
						microphoneListView.getItems().stream().map(MicrophoneCell::getMicrophone).toList())
					,col, row);
				index++;
			}
		}
	}
}