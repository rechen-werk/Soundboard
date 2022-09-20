package eu.rechenwerk.soundboard.converters;

import eu.rechenwerk.soundboard.model.Config;
import org.json.JSONObject;

public class ConfigConverter extends JSONConverter<Config>{

	private final static String SOUNDS = "sounds";
	private final static String MICROPHONES = "microphones";
	private final static String COLORS = "colors";

	ConfigConverter() {}

	@Override
	public String serialize(Config obj) {
		return
			startObject() +
				putString(SOUNDS, obj.sounds()) + comma() +
				putArray(MICROPHONES, obj.microphones(), JSONConverter.VIRTUAL_MICROPHONE) + comma() +
				putArray(COLORS, obj.colors(), JSONConverter.COLOR) +
			endObject();
	}

	@Override
	public Config deserialize(String json) {
		JSONObject jsonObject = new JSONObject(json);
		return new Config(
			jsonObject.getString(SOUNDS),
			JSONConverter.VIRTUAL_MICROPHONE_LIST.deserialize(jsonObject.getJSONArray(MICROPHONES).toString()),
			JSONConverter.COLOR_LIST.deserialize(jsonObject.getJSONArray(COLORS).toString())
		);
	}
}
