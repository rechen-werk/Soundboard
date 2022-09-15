package eu.rechenwerk.soundboard.model.windows;

import eu.rechenwerk.soundboard.model.abstractions.VirtualMicrophone;

import java.io.File;

public class WindowsVirtualMicrophone extends VirtualMicrophone {

	public WindowsVirtualMicrophone(String name) {
		super(name);
	}

	@Override
	public void play(File audio) {

	}
}
