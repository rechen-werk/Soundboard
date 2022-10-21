package eu.rechenwerk.soundboard.view;

import eu.rechenwerk.soundboard.model.microphone.VirtualMicrophone;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import static java.lang.Long.MAX_VALUE;

public class MicrophoneCell extends ListCell<VirtualMicrophone> {

	private boolean persist;
	private final static String unlocked = "\uD83D\uDD13";
	private final static String locked = "\uD83D\uDD12";
	private final VirtualMicrophone microphone;
	private final HBox hbox;
	private final Button button;
	private final RadioButton radioButton;


	public MicrophoneCell(VirtualMicrophone microphone, boolean persisted, ToggleGroup group) {
		super();
		this.microphone = microphone;
		this.persist = persisted;

		hbox = new HBox();
		Pane pane = new Pane();
		pane.setPrefWidth(20);
		button = new Button(this.persist ? locked : unlocked);
		radioButton = new RadioButton(microphone.getName());
		radioButton.setToggleGroup(group);
		radioButton.setUserData(microphone);
		hbox.getChildren().addAll(button, pane, radioButton);
		HBox.setHgrow(pane, Priority.ALWAYS);
		pane.setMaxSize(MAX_VALUE, USE_PREF_SIZE);
		button.setOnAction(event ->{
			button.setText(persist ? unlocked : locked);
			persist = !persist;
		});
		updateItem(microphone, false);
	}

	public VirtualMicrophone getMicrophone() {
		return microphone;
	}

	public RadioButton getRadioButton() {
		return radioButton;
	}

	public boolean persistent() {
		return persist;
	}

	@Override
	protected void updateItem(VirtualMicrophone item, boolean empty) {
		super.updateItem(item, empty);
		setText(item!=null ? item.getName() : "<null>");
		if (empty) {
			setGraphic(null);
		} else {
			radioButton.setText(item!=null ? item.getName() : "<null>");
			setGraphic(hbox);
		}
	}
}
