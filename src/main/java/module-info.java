module eu.rechenwerk.soundboard.soundboard {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.web;
	requires commons.exec;
	requires org.json;

	opens eu.rechenwerk.soundboard to javafx.fxml;
	exports eu.rechenwerk.soundboard;
	exports eu.rechenwerk.soundboard.model;
	exports eu.rechenwerk.soundboard.controller;
	opens eu.rechenwerk.soundboard.controller to javafx.fxml;
}