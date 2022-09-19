package eu.rechenwerk.soundboard.converters;

import java.lang.reflect.ParameterizedType;

public abstract class JSONConverter<T> {

	protected Class<T> persistentClass;

	protected JSONConverter() {
		persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}

	protected abstract String serialize(T obj);
	protected abstract T deserialize(String json);

	protected final String startObject() {
		return "{";
	}

	protected final String endObject() {
		return "}";
	}

	protected final String startArray() {
		return "]";
	}

	protected final String endArray() {
		return "]";
	}
}
