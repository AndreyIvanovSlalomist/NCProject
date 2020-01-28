package ru.nc.musiclib.net.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import ru.nc.musiclib.net.ConstProtocol;
import ru.nc.musiclib.net.Role;

import java.io.IOException;

public class SetRole {

    private final ClientSocket clientSocket;
    public CheckBox userRole;
    public Label userName;
    public CheckBox moderatorRole;
    public CheckBox administratorRole;
    public Button cancelBtn;
    public Button saveBtn;
    private String name;
    private Role role;
    private Role newRole = null;

    SetRole(ClientSocket clientSocket, String name, Role role) {
        this.clientSocket = clientSocket;
        this.name = name;
        this.role = role;
    }

    public void onClickCancel(ActionEvent actionEvent) {
        cancelBtn.getScene().getWindow().hide();
    }

    public void onClickSave(ActionEvent actionEvent) {
        if (newRole == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Роль не выбрана!");
            alert.showAndWait();
            return;
        }
        try {
            clientSocket.getOos().writeObject(ConstProtocol.setRole);
            clientSocket.getOos().writeObject(name);
            clientSocket.getOos().writeObject(newRole);
            clientSocket.getOos().flush();
            saveBtn.getScene().getWindow().hide();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        newRole = null;
        userName.setText(name);
        userRole.setSelected(role.equals(Role.user));
        moderatorRole.setSelected(role.equals(Role.moderator));
        administratorRole.setSelected(role.equals(Role.administrator));

    }

    private void checkRole(CheckBox checkBox) {
        newRole = null;
        if (checkBox.isSelected()) {
            if (userRole.equals(checkBox))
                newRole = Role.user;
            else if (moderatorRole.equals(checkBox))
                newRole = Role.moderator;
            else if (administratorRole.equals(checkBox))
                newRole = Role.administrator;

            userRole.setSelected(userRole.equals(checkBox));
            moderatorRole.setSelected(moderatorRole.equals(checkBox));
            administratorRole.setSelected(administratorRole.equals(checkBox));
        }

    }

    public void onClickUserRole(ActionEvent actionEvent) {
        checkRole(userRole);
    }

    public void onClickModeratorRole(ActionEvent actionEvent) {
        checkRole(moderatorRole);
    }

    public void onClickAdministratorRole(ActionEvent actionEvent) {
        checkRole(administratorRole);
    }
}
