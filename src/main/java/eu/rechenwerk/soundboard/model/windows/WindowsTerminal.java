package eu.rechenwerk.soundboard.model.windows;

import eu.rechenwerk.soundboard.model.abstractions.Terminal;
import eu.rechenwerk.soundboard.model.abstractions.VirtualMicrophone;

import java.io.File;
import java.util.List;

public class WindowsTerminal extends Terminal {

	@Override
	public void playSound(VirtualMicrophone microphone, File audio) {

	}

	@Override
	public void addMicrophone(VirtualMicrophone microphone, int... mux) {

	}

	@Override
	public void removeMicrophone(VirtualMicrophone microphone) {

	}

	@Override
	public void setVolume(float volume, VirtualMicrophone microphone) {

	}

	@Override
	public List<String> listMicrophones() {
		return null;
	}
}
