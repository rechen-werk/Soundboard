package eu.rechenwerk.soundboard.converters;

import eu.rechenwerk.soundboard.records.PathInfo;
import org.json.JSONObject;

public class PathInfoConverter extends JSONConverter<PathInfo> {
	private final static String COMP_DIR = "COMP_DIR";
	private final static String APP_DIR = "APP_DIR";
	private final static String SOUNDS_DIR = "SOUNDS_DIR";
	private final static String LOGS_DIR = "LOGS_DIR";
	private final static String IMAGES_DIR = "IMAGES_DIR";
	private final static String CONFIG_FILE = "CONFIG_FILE";
	@Override
	public String serialize(PathInfo obj) {
		return
			startObject() +
				putString(COMP_DIR, obj.compDir()) + comma() +
				putString(APP_DIR, obj.appDir()) + comma() +
				putString(SOUNDS_DIR, obj.soundsDir()) + comma() +
				putString(LOGS_DIR, obj.logsDir()) + comma() +
				putString(IMAGES_DIR, obj.imagesDir()) + comma() +
				putString(CONFIG_FILE, obj.configFile()) +
			endObject();
	}

	@Override
	public PathInfo deserialize(String json) {
		JSONObject obj = new JSONObject(json);
		return new PathInfo(
			obj.getString(COMP_DIR),
			obj.getString(APP_DIR),
			obj.getString(SOUNDS_DIR),
			obj.getString(LOGS_DIR),
			obj.getString(IMAGES_DIR),
			obj.getString(CONFIG_FILE)
		);
	}
}
