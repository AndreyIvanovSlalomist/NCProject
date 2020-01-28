package ru.nc.musiclib.net.client;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.net.ConstProtocol;

import java.io.IOException;

public class UsersController {
    private final static MusicLibLogger logger = new MusicLibLogger(UsersController.class);
    private final ClientSocket clientSocket;
    public Button returnBtn;
    public Button setRoleBtn;
    public TableView<User> usersTable;
    public TableColumn<Object, Object> name;
    public TableColumn<Object, Object> role;


    UsersController(ClientSocket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @FXML
    void initialize() {
        showUsers();
    }

    private void showUsers(){

        logger.info("show Users");
        usersTable.getItems().clear();
        name.setCellValueFactory(new PropertyValueFactory<>("userName"));
        role.setCellValueFactory(new PropertyValueFactory<>("role"));
        {
            {
                try {
                    clientSocket.getOos().writeObject(ConstProtocol.getAllUsers);
                } catch (IOException e) {
                    logger.error("Ошибка записи в поток");
                }
                try {
                    clientSocket.getOos().flush();
                } catch (IOException e) {
                    logger.error("Ошибка при отправки потока");
                }
                Object inputObject = null;
                do {
                    try {
                        inputObject = clientSocket.getOis().readObject();
                    } catch (ClassNotFoundException e) {
                        logger.error("Ошибка класс не найден");
                    } catch (IOException e) {
                        logger.error("Ошибка чтения из поток");
                    }

                    if (inputObject instanceof User) {
                        usersTable.getItems().add((User) inputObject);
                    }
                } while (!((inputObject == ConstProtocol.finish)));
            }
        }

    }

    public void onClickReturn() {
        returnBtn.getScene().getWindow().hide();
    }

    public void onClickSetRole() {
    }

}
