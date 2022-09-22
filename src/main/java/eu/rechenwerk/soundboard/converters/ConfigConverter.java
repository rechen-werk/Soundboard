package eu.rechenwerk.soundboard.converters;

import eu.rechenwerk.soundboard.records.Config;
import org.json.JSONObject;

public class ConfigConverter extends JSONConverter<Config>{
	private final static String MICROPHONES = "microphones";
	private final static String COLORS = "colors";

	@Override
	public String serialize(Config obj) {
		return
			startObject() +
				putArray(MICROPHONES, obj.microphones(), new VirtualMicrophoneConverter()) + comma() +
				putArray(COLORS, obj.colors(), new ColorConverter()) +
			endObject();
	}

	@Override
	public Config deserialize(String json) {
		JSONObject jsonObject = new JSONObject(json);
		return new Config(
			getArray(jsonObject.getJSONArray(MICROPHONES).toString(), new VirtualMicrophoneConverter()),
			getArray(jsonObject.getJSONArray(COLORS).toString(), new ColorConverter())
		);
	}
}
