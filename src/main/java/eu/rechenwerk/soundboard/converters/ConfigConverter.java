package eu.rechenwerk.soundboard.converters;

import eu.rechenwerk.soundboard.model.Config;
import org.json.JSONObject;

public class ConfigConverter extends JSONConverter<Config>{

	private final static String SOUNDS = "sounds";
	private final static String MICROPHONES = "microphones";

	@Override
	public String serialize(Config obj) {
		return
			startObject() +
				putString(SOUNDS, obj.sounds()) + comma() +
				putArray(MICROPHONES, obj.microphones(), new VirtualMicrophoneConverter()) +
			endObject();
	}

	@Override
	public Config deserialize(String json) {
		JSONObject jsonObject = new JSONObject(json);
		return new Config(
			jsonObject.getString(SOUNDS),
			new ListConverter<>(new VirtualMicrophoneConverter()).deserialize(jsonObject.getJSONArray(MICROPHONES).toString())
		);
	}
}
