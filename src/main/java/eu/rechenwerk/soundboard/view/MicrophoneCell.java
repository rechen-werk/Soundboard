package eu.rechenwerk.soundboard.view;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import static java.lang.Long.MAX_VALUE;

public class MicrophoneCell extends ListCell<VirtualMicrophone> {

	private boolean isLocked = false;
	private final static String unlocked = "\uD83D\uDD13";
	private final static String locked = "\uD83D\uDD12";
	private final VirtualMicrophone microphone;
	private final HBox hbox;
	private final Label label;
	private final Button button;


	public MicrophoneCell(VirtualMicrophone microphone) {
		super();
		this.microphone = microphone;

		hbox = new HBox();
		Pane pane = new Pane();
		label = new Label(microphone.getName());
		button = new Button(unlocked);
		hbox.getChildren().addAll(label, pane, button);
		HBox.setHgrow(pane, Priority.ALWAYS);
		pane.setMaxSize(MAX_VALUE, USE_PREF_SIZE);
		button.setOnAction(event ->{
			button.setText(isLocked
				? unlocked
				: locked);
			isLocked = !isLocked;
		});
		updateItem(microphone, false);
	}

	public VirtualMicrophone getMicrophone() {
		return microphone;
	}
	public boolean isLocked() {
		return isLocked;
	}

	@Override
	protected void updateItem(VirtualMicrophone item, boolean empty) {
		super.updateItem(item, empty);
		setText(item!=null ? item.getName() : "<null>");
		if (empty) {
			setGraphic(null);
		} else {
			label.setText(item!=null ? item.getName() : "<null>");
			setGraphic(hbox);
		}
	}
}
