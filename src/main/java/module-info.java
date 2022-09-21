module eu.rechenwerk.soundboard.soundboard {
	//requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires commons.exec;
	requires org.json;
	requires java.desktop;

	exports eu.rechenwerk.soundboard;
	exports eu.rechenwerk.soundboard.controller;
	exports eu.rechenwerk.soundboard.model.exceptions;
	exports eu.rechenwerk.soundboard.framework;

	opens eu.rechenwerk.soundboard;
	opens eu.rechenwerk.soundboard.controller;
	opens eu.rechenwerk.soundboard.framework;
}