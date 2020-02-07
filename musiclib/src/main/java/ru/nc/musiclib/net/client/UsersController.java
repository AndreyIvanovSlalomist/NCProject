package ru.nc.musiclib.net.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.net.ClientUtils;

import java.util.concurrent.Callable;

import static ru.nc.musiclib.net.ClientUtils.alertSelectedItem;
import static ru.nc.musiclib.net.ClientUtils.deleteUser;
import static ru.nc.musiclib.net.LoaderFX.getStage;

public class UsersController {
    private final ClientSocket clientSocket;
    public Button returnBtn;
    public Button setRoleBtn;
    public TableView<User> usersTable;
    public TableColumn<Object, Object> name;
    public TableColumn<Object, Object> role;
    public Button deleteBtn;


    UsersController(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @FXML
    void initialize() {
        showUsers();
    }

    private void showUsers() {
        usersTable.getItems().clear();
        name.setCellValueFactory(new PropertyValueFactory<>("userName"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        for (User user : ClientUtils.getAllUser(clientSocket)) {
            usersTable.getItems().add(user);
        }
    }

    public void onClickReturn() {
        returnBtn.getScene().getWindow().hide();
    }

    private User getSelectedItemFromTable() {
        return usersTable.getSelectionModel().getSelectedItem();
    }

    public void onClickSetRole() {
        if (clientSocket == null)
            return;
        if (!alertSelectedItem(getSelectedItemFromTable(), "Ошибка!", "Пользователь не выбран!")) {
            return;
        }
        Stage stage = getStage(SetRole.class,
                (Callable<SetRole>) () -> new SetRole(clientSocket,
                        getSelectedItemFromTable().getUserName(),
                        getSelectedItemFromTable().getRole()),
                "/fxml/setRole.fxml", false, "Изменить роль");
        stage.setOnShown(event -> {
            returnBtn.setDisable(true);
            setRoleBtn.setDisable(true);
        });
        stage.setOnHiding(event -> {
            returnBtn.setDisable(false);
            setRoleBtn.setDisable(false);
            showUsers();
        });
        stage.show();
    }

    public void onClickDelete(ActionEvent actionEvent) {
        if (clientSocket == null)
            return;
        if (!alertSelectedItem(getSelectedItemFromTable(), "Ошибка!", "Пользователь не выбран!")) {
            return;
        }
        deleteUser(clientSocket, getSelectedItemFromTable().getUserName());
        showUsers();
    }
}
