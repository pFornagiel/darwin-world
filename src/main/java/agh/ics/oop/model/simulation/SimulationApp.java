package agh.ics.oop.model.simulation;

import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.configuration.ConfigMap;
import agh.ics.oop.model.configuration.ConfigPlant;
import agh.ics.oop.presenter.SimulationPresenter;
import agh.ics.oop.presenter.util.StageUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ArrayList;

public class SimulationApp extends Application {

  private static Stage primaryStage;
  private static final String PARAMETERS = "parameters.fxml";
  private static final String SIMULATION_PARAMETERS = "Simulation Parameters";
  private static final SimulationEngine engine = new SimulationEngine(new ArrayList<>());

  public static void addSimulation(Simulation simulation) {
    engine.getSimulationList().add(simulation);
  }
  public static void removeSimulation(Simulation simulation) {
    if (simulation == null) {
      return;
    }
    synchronized (engine.getSimulationList()) {
      if (engine.getSimulationList().contains(simulation)) {
        engine.getSimulationList().remove(simulation);
        if (engine.getSimulationList().isEmpty()) {
          System.exit(0);
        }
      }
    }
  }
  public void start(Stage stage) {
    try {
      primaryStage = stage;
      primaryStage.setResizable(false);
      StageUtil.openNewStage(PARAMETERS, SIMULATION_PARAMETERS, null, null);
      primaryStage.setScene(primaryStage.getScene());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}