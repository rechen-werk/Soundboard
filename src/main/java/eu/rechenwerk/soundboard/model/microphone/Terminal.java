package eu.rechenwerk.soundboard.model.microphone;

import eu.rechenwerk.soundboard.model.OsNotSupportedException;

import java.io.File;
import java.util.List;


import org.apache.commons.exec.OS;

public abstract class Terminal {
	private static Terminal instance;

	abstract Process playSound(VirtualMicrophone microphone, File audio);
	abstract void addMicrophone(VirtualMicrophone microphone, String other);
	abstract void removeMicrophone(VirtualMicrophone microphone);
	abstract void setVolume(int volume, VirtualMicrophone microphone);
	public abstract List<String> listInputDevices();
	public abstract List<String> listOutputDevices();

	public static Terminal getInstance() throws OsNotSupportedException {
		if(instance != null) return instance;

		if (OS.isFamilyUnix()) return instance = new LinuxTerminal();
		if (OS.isFamilyWindows()) return instance = new WindowsTerminal();

		throw new OsNotSupportedException();
	}
}
