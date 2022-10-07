package eu.rechenwerk.soundboard.model.terminal;

import eu.rechenwerk.framework.OsNotSupportedException;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static eu.rechenwerk.framework.OS.*;

public abstract class Terminal {
	private static Terminal instance;

	public abstract Process playSound(VirtualMicrophone microphone, File audio);
	public abstract void addMicrophone(VirtualMicrophone microphone, String other);
	public abstract void removeMicrophone(VirtualMicrophone microphone);
	public abstract void setVolume(int volume, VirtualMicrophone microphone);
	public abstract List<String> listInputDevices();
	public abstract List<String> listOutputDevices();
	public abstract void openFolder(Path folder) throws IOException;
	public abstract File convertToOgg(File audio);

	public static Terminal getInstance() throws OsNotSupportedException {
		if(instance != null) return instance;

		if (THIS_OS == LINUX) return instance = new LinuxTerminal();
		if (THIS_OS == WINDOWS) return instance = new WindowsTerminal();

		throw new OsNotSupportedException();
	}
}
