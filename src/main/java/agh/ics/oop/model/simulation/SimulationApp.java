package agh.ics.oop.model.simulation;

import agh.ics.oop.presenter.util.StageUtil;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ArrayList;


/**
 * The main application class for launching the simulation app, extending {@link Application} for JavaFX integration.
 * Provides methods to add and remove simulations dynamically. The app starts a JavaFX stage for simulation configuration.
 *
 * <p>Key features include:
 * <ul>
 *   <li>Launching the JavaFX application and opening the simulation parameters interface</li>
 *   <li>Managing the list of simulations, including adding and removing simulations</li>
 *   <li>Gracefully shutting down the application when no simulations remain</li>
 * </ul>
 *
 * <p>This class serves as the entry point for the simulation GUI and is responsible for interacting with the
 * {@link SimulationEngine} to manage simulations.
 */
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