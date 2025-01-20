package agh.ics.oop.presenter.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.function.Consumer;

public class StageUtil {

    public static void openNewStage(String fxmlPath, String stageTitle, Consumer<FXMLLoader> controllerInitializer) throws IOException {
        FXMLLoader loader = new FXMLLoader(StageUtil.class.getClassLoader().getResource(fxmlPath));
        Parent root = loader.load();
        if (controllerInitializer != null) {
            controllerInitializer.accept(loader);
        }
        Stage stage = new Stage();
        stage.setTitle(stageTitle);
        stage.setScene(new Scene(root));
        stage.show();
    }
}
