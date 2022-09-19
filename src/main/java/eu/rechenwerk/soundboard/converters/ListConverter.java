package eu.rechenwerk.soundboard.converters;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListConverter<T, S extends JSONConverter<T>> extends JSONConverter<List<T>> {

	private final JSONConverter<T> converter;

	public ListConverter(S converter) {
		this.converter = converter;
	}

	@Override
	public String serialize(List<T> obj) {
		StringBuilder str = new StringBuilder();
		str.append(startArray());

		Iterator<T> iterator = obj.iterator();
		while (iterator.hasNext()) {
			str.append(converter.serialize(iterator.next()));
			if(iterator.hasNext()) {
				str.append(comma());
			}
		}

		str.append(endArray());
		return str.toString();
	}

	@Override
	public List<T> deserialize(String json) {
		JSONArray array = new JSONArray(json);
		List<T> returnValue = new ArrayList<>();
		for (Object item: array) {
			returnValue.add(converter.deserialize(item.toString()));
		}
		return returnValue;
	}
}
