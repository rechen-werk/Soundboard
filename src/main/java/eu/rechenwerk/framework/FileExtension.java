package eu.rechenwerk.framework;

import java.io.File;

public class FileExtension {
	public static String getSimpleName(File file) {
		String name = file.getName();
		int pos = name.lastIndexOf(".");
		if (pos > 0 && pos < (name.length() - 1)) {
			name = name.substring(0, pos);
		}
		return name;
	}

	public static String getExtension(File file) {
		String fe = "";
		int i = file.getName().lastIndexOf('.');
		if (i > 0) {
			fe = file.getName().substring(i+1);
		}
		return fe;
	}
}
