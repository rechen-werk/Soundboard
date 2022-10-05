package eu.rechenwerk.soundboard.controller;

import eu.rechenwerk.soundboard.converters.ConfigConverter;
import eu.rechenwerk.soundboard.records.Config;
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

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import static eu.rechenwerk.soundboard.SoundBoard.*;

public class SoundBoardController {

	public static Config CONFIG;

	@FXML private TextField soundNameField;
	@FXML private TextField soundImageField;
	@FXML private TextField soundFileField;
	@FXML private TextField virtualMicrophoneNameField;
	@FXML private ComboBox<String> inputDevicesComboBox;
	@FXML private ListView<MicrophoneCell> microphoneListView;
	@FXML private GridPane soundGridPane;

	/**
	 * Initialize the SoundBoard window
	 * @param stage The Stage from the start method
	 */
	public void init(Stage stage) {
		try(FileInputStream fis = new FileInputStream(PATH_INFO.getConfigFile())) {
			String configJson = new String(fis.readAllBytes());
			CONFIG = new ConfigConverter().deserialize(configJson);
		} catch (IOException e) {
			exceptionPopUp(e);
		}

		microphoneListView
			.getItems()
			.addAll(CONFIG
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
		String soundNameString = soundNameField.getText();
		String audioFileString = soundFileField.getText();
		String audioImageString = soundImageField.getText();

		Path source = Path.of(audioFileString);
		LOGGER.debug(source.toString());
		String fe = "";
		int i = source.getFileName().toString().lastIndexOf('.');
		if (i > 0) {
			fe = source.getFileName().toString().substring(i+1);
		}
		Path destination = PATH_INFO.getSoundsDirectory().resolve(soundNameString.isBlank() ? source.getFileName().toString() : soundNameString + "." + fe);
		LOGGER.debug(destination.toString());
		if(Files.notExists(destination)) {
			try {
				Files.copy(source, destination);
			} catch (IOException e) {
				exceptionPopUp(e);
			}
		}
	}

	public void onOpenSoundsFolderClick() {
		try {
			TERMINAL.openFolder(PATH_INFO.getSoundsDirectory());
		} catch (IOException e) {
			exceptionPopUp(e);
		}
	}

	@FXML protected void onCreateMicrophoneClick() {
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

	@FXML protected void refreshCombobox() {
		List<String> devices = TERMINAL.listOutputDevices();
		inputDevicesComboBox.setItems(FXCollections.observableList(devices));
	}

	private void persist() {
		try (FileWriter fw = new FileWriter(PATH_INFO.getConfigFile())){
			fw.write(new ConfigConverter().serializeIndented(new Config(
				microphoneListView
					.getItems()
					.stream()
					.filter(MicrophoneCell::persistent)
					.map(MicrophoneCell::getMicrophone)
					.toList(),
				CONFIG.colors()
			), 4));
			fw.flush();
		} catch (IOException e) {
			exceptionPopUp(e);
		}
	}

	private void cleanup() {
		persist();
		microphoneListView
			.getItems()
			.stream()
			.map(MicrophoneCell::getMicrophone)
			.forEach(VirtualMicrophone::delete);
	}

	private void initAudioBoard() {
		if(CONFIG
			.colors().size() == 0) {
			CONFIG = new Config(CONFIG.microphones(), List.of(Color.WHITE));
		}
		Color[][] gridColors = new Color[soundGridPane.getRowCount()+1][soundGridPane.getColumnCount()+1];

		for (int row = 0; row < gridColors.length; row++) {
			for (int col = 0; col < gridColors[row].length; col++) {
				gridColors[row][col] = CONFIG
					.colors().get(new Random().nextInt(CONFIG
						.colors().size()));
			}
		}

		List<File> audios = Arrays
			.stream(Objects.requireNonNull(
			PATH_INFO
			.getSoundsDirectory().toFile()
			.listFiles(File::isFile)))
			.filter(it -> it.getName().endsWith(".ogg"))
			.toList();
		int index = 0;

		for (int row = 0; row < soundGridPane.getRowCount(); row++) {
			for (int col = 0; col < soundGridPane.getColumnCount(); col++) {
				soundGridPane.add(
					new SoundPane(
						CONFIG.colors().get(new Random().nextInt(CONFIG.colors().size())),
						gridColors[row+1][col],
						gridColors[row][col+1],
						gridColors[row+1][col+1],
						index < audios.size()
							? Optional.of(audios.get(index))
							: Optional.empty(),
						microphoneListView.getItems().stream().map(MicrophoneCell::getMicrophone).toList()
					), col, row);
				index++;
			}
		}
	}
}