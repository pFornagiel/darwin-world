package agh.ics.oop.model.simulation;
import agh.ics.oop.model.worldmap.GrassField;
import agh.ics.oop.presenter.SimulationPresenter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


public class SimulationApp extends Application{
  @Override
  public void start(Stage primaryStage) throws Exception {
    FXMLLoader loader = new FXMLLoader();
    loader.setLocation(getClass().getClassLoader().getResource("simulation.fxml"));
    BorderPane viewRoot = loader.load();
    SimulationPresenter presenter = loader.getController();
    configureStage(primaryStage, viewRoot);


//    Simulation and map
    GrassField grassField = new GrassField(5);
    presenter.setWorldMap(grassField);
    grassField.addToListeners(presenter);

//    Show stage
    primaryStage.show();

  }

  private void configureStage(Stage primaryStage, BorderPane viewRoot) {
    var scene = new Scene(viewRoot);
    primaryStage.setScene(scene);
    primaryStage.setTitle("Simulation app");
    primaryStage.minWidthProperty().bind(viewRoot.minWidthProperty());
    primaryStage.minHeightProperty().bind(viewRoot.minHeightProperty());
  }
}
