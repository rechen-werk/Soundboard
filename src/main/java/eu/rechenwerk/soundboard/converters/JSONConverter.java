package eu.rechenwerk.soundboard.converters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public abstract class JSONConverter<T> {
	/**
	 * Serializes the Object into a string if a Converter for it is implemented.
	 * @param obj The object to serialize.
	 * @return A json string representation of the Object.
	 */
	public abstract String serialize(T obj);

	/**
	 * Creates an Object from a json string if a Converter for it is implemented.
	 * @param json The json string to deserialize.
	 * @return The Object of Type {@code <T>}
	 */
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
	protected final <I> List<I> getArray(String json, JSONConverter<I> listItemConverter) {
		ListConverter<I, JSONConverter<I>> converter = new ListConverter<>(listItemConverter);
		return converter.deserialize(json);
	}

	/**
	 * Serializes the Object with {@code indent} spaces per layer
	 * @param obj The object to serialize.
	 * @param indent the indent per layer
	 * @return A nicely formatted json string
	 */
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
