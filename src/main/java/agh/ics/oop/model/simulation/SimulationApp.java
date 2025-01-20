package agh.ics.oop.model.simulation;

import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.configuration.ConfigMap;
import agh.ics.oop.model.configuration.ConfigPlant;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.util.StageUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.util.ArrayList;

public class SimulationApp extends Application {

  private static Stage primaryStage;
  private static SimulationPresenter presenter;
  private static final String PARAMETERS = "parameters.fxml";
  private static final String SIMULATION_PARAMETERS = "Simulation Parameters";
  private static ConfigMap mapConfig;
  private static ConfigAnimal animalConfig;
  private static ConfigPlant plantConfig;
  private static final SimulationEngine engine = new SimulationEngine(new ArrayList<>());

  public static void addSimulation(Simulation simulation) {
    engine.getSimulationList().add(simulation);
  }

  public static void startSimulations() {
    engine.runAsyncInThreadPool();
  }

  public static void stopSimulations() {
    try {
      engine.awaitSimulationEnd();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
  public void start(Stage stage) {
    try {
      primaryStage = stage;
      StageUtil.openNewStage(PARAMETERS, SIMULATION_PARAMETERS, null);
      primaryStage.setScene(primaryStage.getScene());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static SimulationPresenter getPresenter() {
    return presenter;
  }

  public static void main(String[] args) {
    launch(args);
  }
}