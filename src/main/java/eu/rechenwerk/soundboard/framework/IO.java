package eu.rechenwerk.soundboard.framework;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@SuppressWarnings("unused")
public class IO {
	public static File getResource(String file) throws FileNotFoundException {
		URL resource = IO.class.getClassLoader().getResource(file);
		if(resource == null) {
			throw new FileNotFoundException("Someone has probably deleted " + file);
		}
		return new File(resource.getPath());
	}

	private static void writeToFile(File file, List<String> content, boolean append) throws IOException {
		FileWriter writer = new FileWriter(file, append);
		for (String line: content) {
			writer.write(line + "\n");
		}
		writer.flush();
		writer.close();
	}

	public static void writeLines(File file, List<String> content) throws IOException {
		writeToFile(file, content, false);
	}

	public static void writeLines(File file, String... content) throws IOException {
		writeLines(file, List.of(content));
	}

	public static void write(File file, String content) throws IOException {
		writeLines(file, List.of(content));
	}

	public static void writeResourceLines(String file, List<String> content) throws IOException {
		writeToFile(getResource(file), content, false);
	}

	public static void writeResourceLines(String file, String... content) throws IOException {
		writeLines(getResource(file), List.of(content));
	}

	public static void writeResource(String file, String content) throws IOException {
		writeLines(getResource(file), List.of(content));
	}

	public static void appendLines(File file, List<String> content) throws IOException {
		writeToFile(file, content, true);
	}

	public static void append(File file, String content) throws IOException {
		appendLines(file, List.of(content));
	}

	public static void appendLines(File file, String... content) throws IOException {
		appendLines(file, List.of(content));
	}

	public static void appendResourceLines(String file, List<String> content) throws IOException {
		writeToFile(getResource(file), content, true);
	}

	public static void appendResource(String file, String content) throws IOException {
		appendLines(getResource(file), List.of(content));
	}

	public static void appendResourceLines(String file, String... content) throws IOException {
		appendLines(getResource(file), List.of(content));
	}

	public static List<String> readLines(File file) throws IOException {
		List<String> content = new ArrayList<>();
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			content.add(scanner.nextLine());
		}
		scanner.close();
		return content;
	}

	public static List<String> readResourceLines(String file) throws IOException {
		return readLines(getResource(file));
	}

	public static String read(File file) throws FileNotFoundException {
		StringBuilder content = new StringBuilder();
		Scanner scanner = new Scanner(file);
		while(scanner.hasNextLine()) {
			content
				.append(scanner.nextLine())
				.append("\n");
		}
		scanner.close();
		return content.toString();
	}

	public static String readResource(String file) throws FileNotFoundException {
		return read(getResource(file));
	}

}
