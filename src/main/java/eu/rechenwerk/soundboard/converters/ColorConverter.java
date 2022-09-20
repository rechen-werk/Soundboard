package eu.rechenwerk.soundboard.converters;

import org.json.JSONObject;

import java.awt.*;

public class ColorConverter extends JSONConverter<Color>{
	private final static String CODE = "code";
	@Override
	public String serialize(Color obj) {
		return
			startObject() +
				putString(CODE, Integer.toHexString(obj.getRGB())) +
			endObject();
	}

	@Override
	public Color deserialize(String json) {
		JSONObject jsonObject = new JSONObject(json);
		return new Color(Integer.parseUnsignedInt(jsonObject.getString(CODE), 16));
	}
}
