package eu.rechenwerk.soundboard.model;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;

import java.awt.*;
import java.util.List;

public record Config(
	String sounds,
	List<VirtualMicrophone> microphones,
	List<Color> colors
) {}
