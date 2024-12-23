module org.example.darwinworld {
  requires javafx.controls;
  requires javafx.fxml;


  opens agh.ics.oop to javafx.fxml;
  exports agh.ics.oop;
  exports agh.ics.oop.model.util;
  opens agh.ics.oop.model.util to javafx.fxml;
}