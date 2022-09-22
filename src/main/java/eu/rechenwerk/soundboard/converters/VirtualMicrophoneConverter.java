package eu.rechenwerk.soundboard.converters;

import eu.rechenwerk.framework.OsNotSupportedException;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import org.json.JSONObject;

public final class VirtualMicrophoneConverter extends JSONConverter<VirtualMicrophone> {
	private final static String NAME = "name";
	private final static String DEVICE = "device";
	private final static String VOLUME = "volume";

	@Override
	public String serialize(VirtualMicrophone obj) {
		return
			startObject() +
			putString(NAME, obj.getName()) + comma() +
			putString(DEVICE, obj.getDevice()) + comma() +
			putInt(VOLUME, obj.getVolume()) +
			endObject();
	}

	@Override
	public VirtualMicrophone deserialize(String json) {
		JSONObject jsonObject = new JSONObject(json);
		try {
			return VirtualMicrophone.create(
				jsonObject.getString(NAME),
				jsonObject.getString(DEVICE),
				jsonObject.getInt(VOLUME)
			);
		} catch (OsNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}