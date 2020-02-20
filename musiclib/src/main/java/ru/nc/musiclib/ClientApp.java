package ru.nc.musiclib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
       String fxmlFile = "/fxml/connectionForm.fxml";
       FXMLLoader loader = new FXMLLoader();
       Parent root = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
       stage.setResizable(false);
       stage.setTitle("Подключение");
       stage.setScene(new Scene(root));
       stage.show();
    }
}
