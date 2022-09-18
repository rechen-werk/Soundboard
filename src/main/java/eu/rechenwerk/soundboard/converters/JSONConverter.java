package eu.rechenwerk.soundboard.converters;

public interface JSONConverter<T> {
	String serialize(T obj);
	T deserialize(String json);
}
