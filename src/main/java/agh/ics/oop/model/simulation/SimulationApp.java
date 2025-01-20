package agh.ics.oop.model.simulation;

import agh.ics.oop.model.configuration.ConfigAnimal;
import agh.ics.oop.model.configuration.ConfigMap;
import agh.ics.oop.model.configuration.ConfigPlant;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.Region;

import java.io.IOException;

public class SimulationApp extends Application {

  private static Stage primaryStage;
  private static SimulationPresenter presenter;
  private static final String title = "Simulation Parameters";
  private static ConfigMap mapConfig;
  private static ConfigAnimal animalConfig;
  private static ConfigPlant plantConfig;
  @Override
  public void start(Stage stage) {
    try {
      primaryStage = stage;
      FXMLLoader loader = new FXMLLoader();
      loader.setLocation(getClass().getClassLoader().getResource("parameters.fxml"));
      Scene scene = new Scene(loader.load());
      primaryStage.setScene(scene);
      primaryStage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static Simulation createSimulation() {
    if (mapConfig == null || animalConfig == null || plantConfig == null) {
      throw new IllegalStateException("Configurations must be set before creating a simulation");
    }
    return new Simulation(mapConfig, animalConfig, plantConfig);
  }

  public static void setConfigurations(ConfigMap map, ConfigAnimal animal, ConfigPlant plant) {
    mapConfig = map;
    animalConfig = animal;
    plantConfig = plant;
  }

  private static void createStage(Parent viewRoot) {
    Scene scene = new Scene(viewRoot);
    primaryStage.setScene(scene);
    primaryStage.setTitle(title);
    if (viewRoot instanceof Region) {
      primaryStage.minWidthProperty().bind(((Region) viewRoot).minWidthProperty());
      primaryStage.minHeightProperty().bind(((Region) viewRoot).minHeightProperty());
    }
  }


  public static void openSimulationWindow() {
    try {
      FXMLLoader loader = new FXMLLoader(SimulationApp.class.getClassLoader().getResource("simulation.fxml"));
      Parent root = loader.load();
      SimulationPresenter presenter = loader.getController();
      Stage simulationStage = new Stage();
      simulationStage.setTitle("Simulation");
      simulationStage.setScene(new Scene(root));
      simulationStage.show();
    } catch (IOException e) {
      System.err.println("Failed to load simulation FXML: " + e.getMessage());
    }
  }

  public static SimulationPresenter getPresenter() {
    return presenter;
  }

  public static void main(String[] args) {
    launch(args);
  }
}
