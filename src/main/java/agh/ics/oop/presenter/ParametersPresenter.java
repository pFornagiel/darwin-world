package agh.ics.oop.presenter;

import agh.ics.oop.model.simulation.SimulationApp;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;

public class ParametersPresenter {

    @FXML
    private TextField mapWidth;

    @FXML
    private TextField mapHeight;

    @FXML
    private Button accept;

    @FXML
    private void mapWidth() {
    }

    @FXML
    private void mapHeight() {
    }

    @FXML
    private void accept() {
        SimulationApp.switchScene("simulation.fxml");
    }

    @FXML
    private void initialize() {
    }
}
