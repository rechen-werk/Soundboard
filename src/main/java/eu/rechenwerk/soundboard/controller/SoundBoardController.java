package eu.rechenwerk.soundboard.controller;

import eu.rechenwerk.soundboard.converters.ConfigConverter;
import eu.rechenwerk.soundboard.records.Config;
import eu.rechenwerk.framework.OsNotSupportedException;
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

import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

import static eu.rechenwerk.soundboard.SoundBoard.PATH_INFO;

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
	 * @throws OsNotSupportedException should not be thrown, because the os is checked at the beginning and already throws a runtime exception then
	 * @throws IOException if the Config file is missing or something is wrong with it.
	 */
	public void init(Stage stage) throws OsNotSupportedException, IOException {
		try(FileInputStream fis = new FileInputStream(PATH_INFO.getConfigFile())) {
			String configJson = new String(fis.readAllBytes());
			CONFIG = new ConfigConverter().deserialize(configJson);
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
		stage.setOnCloseRequest(event -> {
			try {
				cleanup();
			} catch (OsNotSupportedException | IOException e) {
				throw new RuntimeException(e);
			}
		});
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

	private void persist() throws OsNotSupportedException, IOException {
		FileWriter fw = new FileWriter(PATH_INFO.getConfigFile());
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
		fw.close();
	}

	private void cleanup() throws OsNotSupportedException, IOException {
		persist();
		microphoneListView
			.getItems()
			.stream()
			.map(MicrophoneCell::getMicrophone)
			.forEach(VirtualMicrophone::delete);
	}

	private void initAudioBoard() throws OsNotSupportedException {
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
			.getSoundsDirectory()
			.listFiles(File::isFile)))
			.filter(it -> it.getName().endsWith(".ogg"))
			.toList();

		int index = 0;

		for (int row = 0; row < soundGridPane.getRowCount(); row++) {
			for (int col = 0; col < soundGridPane.getColumnCount(); col++) {
				soundGridPane.add(new SoundPane(
						CONFIG
							.colors().get(new Random().nextInt(CONFIG
								.colors().size())),
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