package eu.rechenwerk.soundboard.converters;

import eu.rechenwerk.soundboard.records.Init;
import org.json.JSONObject;

public class InitConverter extends JSONConverter<Init> {
	public final static String PATH_INFO = "PATH_INFO";
	public final static String CONFIG_CONTENT = "CONFIG_CONTENT";
	@Override
	public String serialize(Init obj) {
		return
			startObject() +
				new PathInfoConverter().serialize(obj.pathInfo()) + comma() +
				new ConfigConverter().serialize(obj.config()) +
			endObject();
	}

	@Override
	public Init deserialize(String json) {
		JSONObject obj = new JSONObject(json);
		return new Init(
			new PathInfoConverter().deserialize(obj.getJSONObject(PATH_INFO).toString()),
			new ConfigConverter().deserialize(obj.getJSONObject(CONFIG_CONTENT).toString())
		);
	}
}
