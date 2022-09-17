package eu.rechenwerk.soundboard;

import eu.rechenwerk.soundboard.controller.SoundBoardController;
import eu.rechenwerk.soundboard.model.OsNotSupportedException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SoundBoard extends Application {
	@Override
	public void start(Stage stage) throws IOException, OsNotSupportedException {
		FXMLLoader fxmlLoader = new FXMLLoader(SoundBoard.class.getResource("soundboard-view.fxml"));
		Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
		stage.setTitle("Soundboard");
		stage.setScene(scene);

		SoundBoardController controller = fxmlLoader.getController();
		controller.init(stage);

		stage.show();
	}

	public static void main(String[] args) {
		launch();
	}
}