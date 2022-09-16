package eu.rechenwerk.soundboard.controller;

import eu.rechenwerk.soundboard.model.OsNotSupportedException;
import eu.rechenwerk.soundboard.model.microphone.Terminal;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.File;

public class SoundBoardController {
	@FXML
	private Label welcomeText;

	@FXML
	protected void onHelloButtonClick() {
		try {
			VirtualMicrophone mic = VirtualMicrophone.create("VirtualMicrophone", "alsa_input.usb-Trust_USB_microphone_Trust_USB_microphone_20191101-00.mono-fallback:capture_MONO");
			Thread.sleep(1000);
			mic.play(new File("/home/rechenwerk/Downloads/boner_alert.ogg"));
			Thread.sleep(1000);
			mic.stopSound();
			Thread.sleep(2000);
			mic.play(new File("/home/rechenwerk/Downloads/boner_alert.ogg"));
			Thread.sleep(6000);
			mic.delete();
			welcomeText.setText(Terminal.getInstance().listInputDevices().get(2));
		} catch (OsNotSupportedException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
}