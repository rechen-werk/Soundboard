package eu.rechenwerk.soundboard.converters;

import eu.rechenwerk.soundboard.model.Config;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class JSONConverter<T> {

	public static VirtualMicrophoneConverter VIRTUAL_MICROPHONE;
	public static ConfigConverter CONFIG;
	public static ListConverter<VirtualMicrophone, VirtualMicrophoneConverter> VIRTUAL_MICROPHONE_LIST;
	public static ListConverter<Config, ConfigConverter> CONFIG_LIST;

	static {
		VIRTUAL_MICROPHONE = new VirtualMicrophoneConverter();
		CONFIG = new ConfigConverter();
		VIRTUAL_MICROPHONE_LIST = new ListConverter<>(VIRTUAL_MICROPHONE);
		CONFIG_LIST = new ListConverter<>(CONFIG);
	}

	public abstract String serialize(T obj);
	public abstract T deserialize(String json);

	protected final String startObject() {
		return "{";
	}

	protected final String endObject() {
		return "}";
	}

	protected final String startArray() {
		return "[";
	}

	protected final String endArray() {
		return "]";
	}
	protected final String comma() {
		return ",";
	}

	protected final String putString(String label, String item) {
		return "\"" + label + "\": \"" + item + "\"";
	}

	protected final String putInt(String label, int item) {
		return "\"" + label + "\": " + item;
	}

	protected final <I> String putArray(String label, List<I> items, JSONConverter<I> listItemConverter) {
		ListConverter<I, JSONConverter<I>> converter = new ListConverter<>(listItemConverter);
		return "\"" + label + "\": " + converter.serialize(items);

	}

	public String serializeIndented(T obj, int indent) {
		try {
			return new JSONObject(serialize(obj)).toString(indent);
		} catch (JSONException e) {
			try {
				return new JSONArray(serialize(obj)).toString(indent);
			} catch (JSONException ee) {
				throw new RuntimeException("JSON String is neither OBJECT nor ARRAY", ee);
			}
		}
	}
}
