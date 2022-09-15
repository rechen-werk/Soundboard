package eu.rechenwerk.soundboard.model;

public class OsNotSupportedException extends Exception {
	public OsNotSupportedException() {
		super("There is no implementation for " + System.getProperty("os.name") + " yet.");
	}
}
