package eu.rechenwerk.soundboard.model.microphone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;

public final class LinuxTerminal extends Terminal {
	private final Map<VirtualMicrophone, List<String>> ids = new HashMap<>();

	@Override
	public Process playSound(VirtualMicrophone microphone, File audio) {
		try {
			microphone.stopSound();

			return Runtime
				.getRuntime()
				.exec(new String[]{"paplay", audio.getName(), "-d", microphone.getInputName()}, null, audio.getParentFile());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	void addMicrophone(VirtualMicrophone microphone, String other) {
		ids.put(microphone, new ArrayList<>());

		String[] commands = new String[]{
			"pactl load-module module-null-sink media.class=Audio/Sink channel_map=stereo sink_name=" + microphone.getSinkName(),
			"pactl load-module module-null-sink media.class=Audio/Sink channel_map=stereo sink_name=" + microphone.getInputName(),
			"pactl load-module module-null-sink media.class=Audio/Source/Virtual channel_map=front-left,front-right sink_name=" + microphone.getName(),
			"pw-link " + other + " " + microphone.getSinkName() + ":playback_FL", "pw-link " + other + " " + microphone.getSinkName() + ":playback_FR",
			"pw-link " + microphone.getInputName() + ":monitor_FL " + microphone.getSinkName() + ":playback_FL",
			"pw-link " + microphone.getInputName() + ":monitor_FR " + microphone.getSinkName() + ":playback_FR",
			"pw-link " + microphone.getSinkName() + ":monitor_FL " + microphone.getName() + ":input_FL",
			"pw-link " + microphone.getSinkName() + ":monitor_FR " + microphone.getName() + ":input_FR"
		};

		try {
			Process process = new ProcessBuilder("/bin/bash").start();

			Thread inputHandler = new Thread(() -> {
				try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
					for (String command : commands) {
						writer.write(command);
						writer.newLine();
					}
					writer.flush();
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			});

			Thread outputHandler = new Thread(() -> {
				try (Scanner scanner = new Scanner(process.getInputStream())) {
					while (scanner.hasNextLine()) {
						String id = scanner.nextLine();
						ids
							.get(microphone)
							.add(id);
						System.out.println("IN" + id);
					}
				}
			});

			inputHandler.start();
			outputHandler.start();
			inputHandler.join();
			outputHandler.join();
			process.destroy();

		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void removeMicrophone(VirtualMicrophone microphone) {

		try {
			Process process = new ProcessBuilder("/bin/bash").start();

			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
				for (String id : ids.get(microphone)) {
					writer.write("pactl unload-module " + id);
					writer.newLine();
				}
				writer.write("exit");
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void setVolume(int volume, VirtualMicrophone microphone) {
		try {
			Process process = new ProcessBuilder("/bin/bash").start();

			try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
				writer.write("pactl --set-sink-volume " + ids
					.get(microphone)
					.get(1) + " " + volume + "%");
				writer.newLine();
				writer.write("exit");
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<String> listInputDevices() {
		return listDevices("-i");
	}

	@Override
	public List<String> listOutputDevices() {
		return listDevices("-o");
	}

	private List<String> listDevices(String param) {
		List<String> devices = new ArrayList<>();

		Process process;
		try {
			process = Runtime
				.getRuntime()
				.exec(new String[]{"pw-link", param});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		Scanner s = new Scanner(process.getInputStream());
		while (s.hasNextLine()) {
			devices.add(s.nextLine());
		}

		return devices;
	}
}
