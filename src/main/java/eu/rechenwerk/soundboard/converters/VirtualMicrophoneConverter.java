package eu.rechenwerk.soundboard.converters;

import eu.rechenwerk.soundboard.model.exceptions.OsNotSupportedException;
import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import org.json.JSONObject;

final class VirtualMicrophoneConverter extends JSONConverter<VirtualMicrophone> {
	private final static String NAME = "name";
	private final static String DEVICE = "device";

	@Override
	protected String serialize(VirtualMicrophone obj) {
		return
			startObject()
			+"\""+NAME+"\":\"" +obj.getName()+"\"," +
			"\""+DEVICE+"\":\""+obj.getDevice()+"\""+
			endObject();
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