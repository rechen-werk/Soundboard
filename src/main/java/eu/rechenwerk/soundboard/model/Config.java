package eu.rechenwerk.soundboard.model;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;

import java.util.List;

public record Config(
	String sounds,
	List<VirtualMicrophone> microphones
) {}
