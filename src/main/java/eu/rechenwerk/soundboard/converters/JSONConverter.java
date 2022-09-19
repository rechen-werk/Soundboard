package eu.rechenwerk.soundboard.converters;

import java.util.List;

public abstract class JSONConverter<T> {

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
	protected final <I> String putArray(String label, List<I> items, JSONConverter<I> listItemConverter) {
		ListConverter<I, JSONConverter<I>> converter = new ListConverter<>(listItemConverter);
		return "\"" + label + "\": " + converter.serialize(items);

	}
}
