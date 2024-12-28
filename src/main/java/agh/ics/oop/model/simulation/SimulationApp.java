package agh.ics.oop.model.simulation;

import agh.ics.oop.model.worldmap.GrassField;
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
  @Override
  public void start(Stage stage) throws Exception {
    primaryStage = stage;
    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("parameters.fxml"));
    Parent viewRoot = loader.load();
    createStage(viewRoot);
    primaryStage.show();
  }
  public static void initMap() {
    GrassField grassField = new GrassField(5);
    presenter.setWorldMap(grassField);
    grassField.addToListeners(presenter);
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

  /**
   * Switches the current scene to a new one based on the given FXML file.
   *
   * @param fxmlFile The name of the FXML file to load.
   */
  public static void switchScene(String fxmlFile) {
    try {
      FXMLLoader loader = new FXMLLoader(SimulationApp.class.getClassLoader().getResource(fxmlFile));
      Parent root = loader.load();
      presenter = loader.getController();
      createStage(root);
    } catch (IOException e) {
      System.err.println("Failed to load FXML: " + fxmlFile);
    }
  }

  public static void main(String[] args) {
    launch(args);
  }
}
