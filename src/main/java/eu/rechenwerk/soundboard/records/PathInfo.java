package eu.rechenwerk.soundboard.records;

import eu.rechenwerk.framework.OS;
import eu.rechenwerk.framework.OsNotSupportedException;

import java.io.File;

public record PathInfo (
	String compDir,
	String appDir,
	String soundsDir,
	String configFile
) {
	public File getConfigFile() throws OsNotSupportedException {
		return OS.getDataDirectory()
			.resolve(compDir)
			.resolve(appDir)
			.resolve(configFile)
			.toFile();
	}
	public File getSoundsDirectory() throws OsNotSupportedException {
		return OS.getDataDirectory()
			.resolve(compDir)
			.resolve(appDir)
			.resolve(soundsDir)
			.toFile();
	}
}
