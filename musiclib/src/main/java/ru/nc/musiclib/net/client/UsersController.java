package ru.nc.musiclib.net.client;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.net.ConstProtocol;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

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
                        logger.info(inputObject.toString());
                        usersTable.getItems().add((User) inputObject);
                        inputObject = null;
                    }
                } while (!((inputObject == ConstProtocol.finish)));
            }
        }

    }

    public void onClickReturn() {
        returnBtn.getScene().getWindow().hide();
    }

    public void onClickSetRole() {
        if (clientSocket == null)
            return;
        Map<Class, Callable<?>> creators = new HashMap<>();
        creators.put(SetRole.class, (Callable<SetRole>) () -> new SetRole(
                clientSocket,
                usersTable.getSelectionModel().getSelectedItem().getUserName(),
                usersTable.getSelectionModel().getSelectedItem().getRole()));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/setRole.fxml"));

        loader.setControllerFactory(param -> {
            Callable<?> callable = creators.get(param);
            if (callable == null) {
                try {
                    return param.newInstance();
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new IllegalStateException(ex);
                }
            } else {
                try {
                    return callable.call();
                } catch (Exception ex) {
                    throw new IllegalStateException(ex);
                }
            }
        });

        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setOnShown(event -> {
            returnBtn.setDisable(true);
            setRoleBtn.setDisable(true);
        });
        stage.setOnHiding(event -> {
            returnBtn.setDisable(false);
            setRoleBtn.setDisable(false);
            showUsers();
        });
        stage.setTitle("Изменить роль");
        stage.setScene(new Scene(root));
        stage.show();

    }

}
