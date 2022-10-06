package eu.rechenwerk.soundboard;

import eu.rechenwerk.framework.Logger;
import eu.rechenwerk.framework.OS;
import eu.rechenwerk.framework.OsNotSupportedException;
import eu.rechenwerk.soundboard.controller.ExceptionPopupController;
import eu.rechenwerk.soundboard.converters.ConfigConverter;
import eu.rechenwerk.soundboard.converters.InitConverter;
import eu.rechenwerk.soundboard.model.microphone.Terminal;
import eu.rechenwerk.soundboard.records.Init;
import eu.rechenwerk.soundboard.records.PathInfo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * New Main Method because JavaFX forces itself to be installed when the Main Class extends Application.
 * This Class circumvents this by calling launch from the real Application.
 * It also prepares the environment according to init.json
 */
public class SoundBoard {
	private final static boolean DEBUG = true;
	public final static PathInfo PATH_INFO;
	public final static Logger LOGGER;
	public final static Terminal TERMINAL;
	public final static Path DATA_DIRECTORY;

	static {
		try {
			URL url = SoundBoard.class.getResource("/init.json");
			if (url == null) throw new NullPointerException("init.json could not be found in .jar!");
			InputStream is = url.openStream();
			String content = new String(is.readAllBytes());
			is.close();

			Init init = new InitConverter().deserialize(content);
			PATH_INFO = init.pathInfo();
			DATA_DIRECTORY = OS.getDataDirectory();
			Path compPath = DATA_DIRECTORY.resolve(PATH_INFO.compDir());
			Path appPath = compPath.resolve(PATH_INFO.appDir());
			Path soundsPath = appPath.resolve(PATH_INFO.soundsDir());
			Path logsPath = appPath.resolve(PATH_INFO.logsDir());
			Path imagesPath = appPath.resolve(PATH_INFO.imagesDir());
			Path configPath = appPath.resolve(PATH_INFO.configFile());

			if (!Files.exists(compPath)) {
				Files.createDirectory(compPath);
			}
			if (!Files.exists(appPath)) {
				Files.createDirectory(appPath);
			}
			if (!Files.exists(soundsPath)) {
				Files.createDirectory(soundsPath);
			}
			if (!Files.exists(logsPath)) {
				Files.createDirectory(logsPath);
			}
			if (!Files.exists(imagesPath)) {
				Files.createDirectory(imagesPath);
			}
			if (!Files.exists(configPath)) {
				Files.createFile(configPath);
				Files.write(configPath, new ConfigConverter()
					.serializeIndented(init.config(), 4)
					.getBytes(), StandardOpenOption.WRITE);
			}
			LOGGER = new Logger(PATH_INFO.getLogsDirectory(), DEBUG);
			TERMINAL = Terminal.getInstance();
		}catch (OsNotSupportedException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		FXSoundBoard.main(args);
	}

	public static void exceptionPopUp(Exception e) {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(SoundBoard.class.getResource("exception-popup-view.fxml"));
			Scene scene = new Scene(fxmlLoader.load(), 400, 500);
			Stage stage = new Stage();
			stage.setTitle("Something went wrong.");
			stage.setScene(scene);
			stage.initModality(Modality.APPLICATION_MODAL);
			ExceptionPopupController exceptionPopupController = fxmlLoader.getController();
			exceptionPopupController.init(e);
			stage.show();
		} catch (IOException ex) {
			throw new RuntimeException("Could not open Exception Window.", ex);
		}
	}
}
