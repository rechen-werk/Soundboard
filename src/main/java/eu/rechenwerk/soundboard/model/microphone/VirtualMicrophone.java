package eu.rechenwerk.soundboard.model.microphone;

import java.io.File;

import eu.rechenwerk.soundboard.model.OsNotSupportedException;

public final class VirtualMicrophone {
	public static String INPUT = "-INPUT";
	public static String SINK = "-SINK";
	private final String name;
	private final Terminal terminal;

	private Process runningSoundProcess;

	private VirtualMicrophone(String name, String other) throws OsNotSupportedException {
		terminal = Terminal.getInstance();
		this.name = name;
		terminal.addMicrophone(this, other);
	}

	public static VirtualMicrophone create(String name, String other) throws OsNotSupportedException {
		return new VirtualMicrophone(name, other);
	}

	public void play(File audio) {
		runningSoundProcess = terminal.playSound(this, audio);
		runningSoundProcess.onExit().thenAccept(p -> {
			p.destroy();
			runningSoundProcess = null;
		});
	}

	public void stopSound() {
		if(runningSoundProcess != null && runningSoundProcess.isAlive()) {
			runningSoundProcess.destroy();
			runningSoundProcess = null;
		}
	}

	public void setVolume(int volume) {
		terminal.setVolume(volume, this);
	}

	public void delete() {
		stopSound();
		terminal.removeMicrophone(this);
	}

	public String getName() {
		return name;
	}

	public String getSinkName() {
		return name + SINK;
	}

	public String getInputName() {
		return name + INPUT;
	}
}
