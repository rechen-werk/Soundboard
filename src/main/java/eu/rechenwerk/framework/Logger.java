package eu.rechenwerk.framework;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Logger {
	private final Path directory;
	private final boolean debug;
	private final static SimpleDateFormat FILE = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat LOGTIME = new SimpleDateFormat("HH:mm:ss.SSS");


	public Logger(Path directory, boolean debug) {
		this.directory = directory;
		this.debug = debug;
	}

	private void log(String message, String type, PrintStream stream) {
		Date now = new Date();
		String line = String.format("[%s] %s: %s\n", LOGTIME.format(now), type, message);
		stream.print(line);
		try {
			Files.writeString(directory.resolve(FILE.format(now)),line, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public void info(String message) {
		log(message, "INFO", System.out);
	}

	public void warn(String message) {
		log(message, "WARN", System.out);
	}

	public void error(String message) {
		log(message, "ERROR", System.err);
	}

	public void debug(String message) {
		if(debug) System.out.printf("[%s] DEBUG: %s\n", LOGTIME.format(new Date()), message);
	}
}
