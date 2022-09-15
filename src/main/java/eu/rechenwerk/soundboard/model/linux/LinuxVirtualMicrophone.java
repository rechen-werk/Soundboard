package eu.rechenwerk.soundboard.model.linux;

import eu.rechenwerk.soundboard.model.abstractions.VirtualMicrophone;

import java.io.File;

public class LinuxVirtualMicrophone extends VirtualMicrophone {

	public LinuxVirtualMicrophone(String name) {
		super(name);
	}

	@Override
	public void play(File audio) {

	}
}
