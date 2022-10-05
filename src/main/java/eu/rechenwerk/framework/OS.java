package eu.rechenwerk.framework;

import java.nio.file.Path;

/**
 * Class to represent the current operating system. Helpful for data management.
 */
public enum OS {
	WINDOWS, LINUX, MAC, UNSUPPORTED;

	public static final OS THIS_OS;

	static {
		String OS = (System.getProperty("os.name")).toLowerCase();
		if(OS.contains("win")) THIS_OS = WINDOWS;
		else if (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")) THIS_OS = LINUX;
		//else if (OS.contains("mac")) THIS_OS = MAC;
		else THIS_OS = UNSUPPORTED;
	}

	/**
	 * Gets the Path to the Directory where Program data is stored on the current operating system.
	 * On *nix systems this is ~/home/{user} and on Windows systems this is RootDrive:\User\{user}\AppData\Roaming
	 * @return Path to Program data
	 * @throws OsNotSupportedException if the Operating system is not supported (currently only Mac I think)
	 */
	public static Path getDataDirectory() throws OsNotSupportedException {
		return Path.of(
			switch (THIS_OS) {
				case WINDOWS -> System.getenv("AppData");
				case LINUX -> System.getProperty("user.home");
				case MAC, UNSUPPORTED-> throw new OsNotSupportedException();
		});
	}
}
