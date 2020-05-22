package ru.nc.musiclib.controller.fxController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import ru.nc.musiclib.net.client.ClientSocket;
import ru.nc.musiclib.model.Role;

import static ru.nc.musiclib.utils.ClientUtils.*;

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
        if (!alertSelectedItem(newRole, "Ошибка!", "Роль не выбрана!")) {
            return;
        }
        setRole(clientSocket, name, newRole);
        saveBtn.getScene().getWindow().hide();
    }

    @FXML
    void initialize() {
        newRole = null;
        userName.setText(name);
        userRole.setSelected(role.equals(Role.ROLE_USER));
        moderatorRole.setSelected(role.equals(Role.ROLE_MODERATOR));
        administratorRole.setSelected(role.equals(Role.ROLE_ADMINISTRATOR));

    }

    private void checkRole(CheckBox checkBox) {
        newRole = null;
        if (checkBox.isSelected()) {
            if (userRole.equals(checkBox))
                newRole = new Role(Role.ROLE_USER,3);
            else if (moderatorRole.equals(checkBox))
                newRole = new Role(Role.ROLE_MODERATOR,2);
            else if (administratorRole.equals(checkBox))
                newRole = new Role(Role.ROLE_ADMINISTRATOR,1);

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
