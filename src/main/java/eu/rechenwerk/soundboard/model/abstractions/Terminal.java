package eu.rechenwerk.soundboard.model.abstractions;

import eu.rechenwerk.soundboard.model.OsNotSupportedException;
import eu.rechenwerk.soundboard.model.linux.LinuxTerminal;

import java.io.File;
import java.util.List;

import eu.rechenwerk.soundboard.model.windows.WindowsTerminal;
import org.apache.commons.exec.OS;

public abstract class Terminal {
	public Terminal instance;
	public abstract void playSound(VirtualMicrophone microphone, File audio);
	public abstract void addMicrophone(VirtualMicrophone microphone, int... mux);
	public abstract void removeMicrophone(VirtualMicrophone microphone);
	public abstract void setVolume(float volume, VirtualMicrophone microphone);
	public abstract List<String> listMicrophones();

	public Terminal getInstance() throws OsNotSupportedException {
		if(instance != null) return instance;

		if (OS.isFamilyUnix()) return instance = new LinuxTerminal();
		if (OS.isFamilyWindows()) return instance = new WindowsTerminal();

		throw new OsNotSupportedException();
	}
}
