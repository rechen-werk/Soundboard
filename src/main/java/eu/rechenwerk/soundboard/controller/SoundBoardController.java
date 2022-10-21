package eu.rechenwerk.soundboard.controller;

import eu.rechenwerk.framework.FileExtension;
import eu.rechenwerk.soundboard.converters.ConfigConverter;
import eu.rechenwerk.soundboard.model.sounds.Sound;
import eu.rechenwerk.soundboard.records.Config;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import eu.rechenwerk.soundboard.view.MicrophoneCell;
import eu.rechenwerk.soundboard.view.SoundPane;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sound.sampled.UnsupportedAudioFileException;
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
	@FXML private TextField soundFileField;
	@FXML private TextField virtualMicrophoneNameField;
	@FXML private ComboBox<String> inputDevicesComboBox;
	@FXML private ListView<MicrophoneCell> microphoneListView;
	@FXML private GridPane soundGridPane;

	private final ToggleGroup microphoneSelection = new ToggleGroup();

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
					new MicrophoneCell(it, true, microphoneSelection)
				).toList()
			);
		microphoneListView
			.getItems()
			.stream()
			.filter(it -> it.getMicrophone().equals(CONFIG.selected()))
			.findFirst()
			.ifPresent(it -> microphoneSelection.selectToggle(it.getRadioButton()));

		initAudioBoard();
		stage.setOnCloseRequest(event -> cleanup());
	}

	@FXML protected void onAddSoundClick() {
		String soundNameString = soundNameField.getText();
		String audioFileString = soundFileField.getText();

		Path source = Path.of(audioFileString);

		String fe = FileExtension.getExtension(source.toFile());

		if (!fe.equals("mp3") && !fe.equals("ogg")) {
			exceptionPopUp(new IllegalArgumentException("Audio File has to be submitted!"));
			return;
		}

		Path destination = PATH_INFO.getSoundsDirectory().resolve(soundNameString.isBlank() ? source.getFileName().toString() : soundNameString + "." + fe);

		if(fe.equals("mp3")) {
			TERMINAL.convertToOgg(destination.toFile());
		}

		if(Files.notExists(destination)) {
			try {
				Files.copy(source, destination);
			} catch (IOException e) {
				exceptionPopUp(e);
			}
		}

		try {
			Thread.sleep(2000);
			Files.delete(destination);
		} catch (IOException | InterruptedException e) {
			exceptionPopUp(e);
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
				false, microphoneSelection)
			);
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
				(VirtualMicrophone) (microphoneSelection.getSelectedToggle() != null ? microphoneSelection.getSelectedToggle().getUserData() : null),
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
			CONFIG = new Config(CONFIG.microphones(), CONFIG.selected(), List.of(Color.WHITE));
		}
		Color[][] gridColors = new Color[soundGridPane.getRowCount()+1][soundGridPane.getColumnCount()+1];

		for (int row = 0; row < gridColors.length; row++) {
			for (int col = 0; col < gridColors[row].length; col++) {
				gridColors[row][col] = CONFIG
					.colors().get(new Random().nextInt(CONFIG
						.colors().size()));
			}
		}

		List<Sound> audios = Arrays
			.stream(Objects.requireNonNull(
			PATH_INFO
			.getSoundsDirectory().toFile()
			.listFiles(File::isFile)))
			.filter(it -> it.getName().endsWith(".ogg"))
			.map(sound -> {
				try {
					return new Sound(sound);
				} catch (UnsupportedAudioFileException | IOException e) {
					exceptionPopUp(e);
				}
				return new Sound();
			}).toList();
		int index = 0;

		for (int row = 0; row < soundGridPane.getRowCount(); row++) {
			for (int col = 0; col < soundGridPane.getColumnCount(); col++) {
				soundGridPane.add(
					new SoundPane(
						CONFIG.colors().get(new Random().nextInt(CONFIG.colors().size())),
						gridColors[row+1][col],
						gridColors[row][col+1],
						gridColors[row+1][col+1],
						index < audios.size() ? audios.get(index) : null,
						microphoneSelection
					), col, row);
				index++;
			}
		}
	}
}