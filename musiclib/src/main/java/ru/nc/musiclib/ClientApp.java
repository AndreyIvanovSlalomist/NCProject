package ru.nc.musiclib;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.nc.musiclib.controller.fxController.FxController;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.net.client.ClientSocket;

import java.util.concurrent.Callable;

import static ru.nc.musiclib.utils.LoaderFX.getStage;

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

/*        Role role = new Role(Role.ROLE_ADMINISTRATOR);
        ClientSocket clientSocket = new ClientSocket("localhost", 8080);
        stage = getStage(FxController.class, (Callable<FxController>) () -> new FxController(clientSocket, role, "login"), "/fxml/main.fxml", false, "Музыкальная библиотека");
        //stage.setOnShown(event -> this.login.getScene().getWindow().hide());
        stage.show();*/


        }
        }
