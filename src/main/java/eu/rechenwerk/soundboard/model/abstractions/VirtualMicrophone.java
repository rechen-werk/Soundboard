package eu.rechenwerk.soundboard.model.abstractions;

import java.io.File;

import eu.rechenwerk.soundboard.model.OsNotSupportedException;
import eu.rechenwerk.soundboard.model.linux.LinuxVirtualMicrophone;
import eu.rechenwerk.soundboard.model.windows.WindowsVirtualMicrophone;
import org.apache.commons.exec.OS;

public abstract class VirtualMicrophone {
	private final String name;

	protected VirtualMicrophone(String name) {
		this.name = name;
	}

	public VirtualMicrophone create(String name) throws OsNotSupportedException {
		if(OS.isFamilyUnix()) return new LinuxVirtualMicrophone(name);
		if(OS.isFamilyWindows()) return new WindowsVirtualMicrophone(name);

		throw new OsNotSupportedException();
	}

	public abstract void play(File audio);
}
