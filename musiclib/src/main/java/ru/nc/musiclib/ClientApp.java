package ru.nc.musiclib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nc.musiclib.controller.fxController.AuthorizationController;

import java.util.concurrent.Callable;

import static ru.nc.musiclib.utils.LoaderFX.getStage;

public class ClientApp extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{
//        String fxmlFile = "/fxml/authorization.fxml";
//        FXMLLoader loader = new FXMLLoader();
//        Parent root = loader.load(getClass().getResourceAsStream(fxmlFile));
//        stage.setResizable(false);
//        stage.setTitle("Авторизация");
//        stage.setScene(new Scene(root));
    }
}
