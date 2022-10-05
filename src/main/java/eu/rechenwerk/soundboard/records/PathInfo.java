package eu.rechenwerk.soundboard.records;

import eu.rechenwerk.soundboard.SoundBoard;

import java.io.File;
import java.nio.file.Path;

public record PathInfo (
	String compDir,
	String appDir,
	String soundsDir,
	String logsDir,
	String configFile
) {
	public File getConfigFile() {
		return SoundBoard.DATA_DIRECTORY
			.resolve(compDir)
			.resolve(appDir)
			.resolve(configFile)
			.toFile();
	}
	public Path getSoundsDirectory() {
		return SoundBoard.DATA_DIRECTORY
			.resolve(compDir)
			.resolve(appDir)
			.resolve(soundsDir);
	}
	public Path getLogsDirectory() {
		return SoundBoard.DATA_DIRECTORY
			.resolve(compDir)
			.resolve(appDir)
			.resolve(logsDir);
	}
}
