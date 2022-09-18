package eu.rechenwerk.soundboard.converters;

import eu.rechenwerk.soundboard.model.exceptions.OsNotSupportedException;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import org.json.JSONObject;

public class VirtualMicrophoneConverter implements JSONConverter<VirtualMicrophone> {
	private final static String NAME = "name";
	private final static String DEVICE = "device";

	@Override
	public String serialize(VirtualMicrophone obj) {
		return "{\""+NAME+"\":\""+obj.getName()+"\",\""+DEVICE+"\":\""+obj.getDevice()+"\"}";
	}

	@Override
	public VirtualMicrophone deserialize(String json) {
		JSONObject jsonObject = new JSONObject(json);
		try {
			return VirtualMicrophone.create(
				jsonObject.getString(NAME),
				jsonObject.getString(DEVICE)
			);
		} catch (OsNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}
}