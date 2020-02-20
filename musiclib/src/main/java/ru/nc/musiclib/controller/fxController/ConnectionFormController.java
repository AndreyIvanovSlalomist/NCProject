package ru.nc.musiclib.controller.fxController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.nc.musiclib.net.client.ClientSocket;
import ru.nc.musiclib.utils.ClientUtils;

import java.util.concurrent.Callable;

import static ru.nc.musiclib.utils.ClientUtils.alertSelectedItem;
import static ru.nc.musiclib.utils.LoaderFX.getStage;

public class ConnectionFormController {
    @FXML
    private TextField hostField;
    @FXML
    private TextField portField;
    @FXML
    private Button connectButton;

    private ClientSocket clientSocket;

    public void onClickConnect(ActionEvent actionEvent) {
        String host = hostField.getText();
        int port;
        try {
            port = Integer.parseInt(portField.getText());
        } catch (NumberFormatException e) {
            alertSelectedItem(null, "Ошибка!", "Неверный формат номера порта!");
            return;
        }

        clientSocket = ClientUtils.connect(host, port);
        if((clientSocket != null) && (clientSocket.getSocket() != null)) {
            Stage stage = getStage(AuthorizationController.class, (Callable<AuthorizationController>) () -> new AuthorizationController(clientSocket), "/fxml/authorization.fxml", false, "Авторизация");
            stage.setOnShown(event -> hostField.getScene().getWindow().hide());
            stage.show();
        }else   alertSelectedItem(null, "Ошибка!", "Не удалось подключиться к серверу.\nХост: " + host + "\nПорт: " + port);
    }
}
