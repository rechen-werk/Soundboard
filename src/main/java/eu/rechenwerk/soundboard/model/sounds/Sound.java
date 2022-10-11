package eu.rechenwerk.soundboard.model.sounds;

import eu.rechenwerk.framework.FileExtension;
import eu.rechenwerk.soundboard.SoundBoard;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;

public class Sound {
	private final File sound;
	private final File image;
	private final double seconds;
	private final String name;

	public Sound() {
		sound = null;
		image = null;
		seconds = 0;
		name = null;
	}

	public Sound(File sound) throws UnsupportedAudioFileException, IOException {
		this.sound = sound;
		this.image = null;
		seconds = SoundBoard.TERMINAL.getDuration(sound);
		name = FileExtension.getSimpleName(sound);
	}

	public Sound(File sound, File image) {
		this.sound = sound;
		this.image = image;
		seconds = SoundBoard.TERMINAL.getDuration(sound);
		name = FileExtension.getSimpleName(sound);
	}

	public File getSound() {
		return sound;
	}

	public String getName() {
		return name;
	}

	public File getImage() {
		return image;
	}

	public double getDuration() {
		return seconds;
	}

	public void play(VirtualMicrophone microphone) {
		microphone.play(this);
	}
}
