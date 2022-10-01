package eu.rechenwerk.soundboard.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class ExceptionPopupController {
	@FXML private TextArea textArea;
	@FXML private Label titleLabel;
	@FXML private CheckBox askAgainCheckbox;

	private Exception exception;

	public void onReportErrorClick(ActionEvent actionEvent) {
		//TODO
	}

	public void onOpenLogClick(ActionEvent actionEvent) {
		//TODO
	}

	public final void init(Exception e) {
		exception = e;
		textArea.setText(exception.getMessage());
		titleLabel.setText(exception.getClass().getSimpleName());
	}
}
