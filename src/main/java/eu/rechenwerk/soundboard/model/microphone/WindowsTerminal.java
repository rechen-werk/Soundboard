package eu.rechenwerk.soundboard.model.microphone;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class WindowsTerminal extends Terminal {

	@Override
	public Process playSound(VirtualMicrophone microphone, File audio) {
		return null;
	}

	@Override
	public void addMicrophone(VirtualMicrophone microphone, String other) {

	}

	@Override
	public void removeMicrophone(VirtualMicrophone microphone) {

	}

	@Override
	public void setVolume(int volume, VirtualMicrophone microphone) {

	}

	@Override
	public List<String> listInputDevices() {
		return null;
	}

	@Override
	public List<String> listOutputDevices() {
		return null;
	}

	@Override
	public void openFolder(Path folder) throws IOException {
		Runtime.getRuntime().exec(new String[] {"explorer", folder.toString()});
	}
}
