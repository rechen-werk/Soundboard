package eu.rechenwerk.soundboard.records;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;

import java.awt.*;
import java.util.List;

public record Config(
	List<VirtualMicrophone> microphones,
	VirtualMicrophone selected,
	List<Color> colors
){}
