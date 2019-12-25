package ru.nc.musiclib.net.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.net.ConstProtocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class FxController {

    private ClientSocket clientSocket;

    public TableColumn<Track, String> nameColumn;
    public TableColumn<Track, String> singerColumn;
    public TableColumn<Track, String> albumColumn;
    public TableColumn<Track, Integer> lengthColumn;
    public TableColumn<Track, String> genreColumn;
    @FXML
    public MenuItem connect;
    @FXML
    public MenuItem add;
    @FXML
    public MenuItem update;
    @FXML
    public MenuItem delete;
    @FXML
    public MenuItem exit;
    @FXML
    public TableView<Track> table;


    @FXML
    public void onClickConnect(ActionEvent actionEvent) {
        if (clientSocket == null) {
            try {
                clientSocket = new ClientSocket(InetAddress.getLocalHost(), 4444);
            } catch (UnknownHostException e) {
                System.out.println("Ошибка Неизвестен хост");
            }
            if (!clientSocket.getSocket().isOutputShutdown()) {
                try {
                    clientSocket.getOos().writeObject(ConstProtocol.getAll);
                } catch (IOException e) {
                    System.out.println("Ошибка записи в поток");
                }
                try {
                    clientSocket.getOos().flush();
                } catch (IOException e) {
                    System.out.println("Ошибка при отправки потока");
                }
                Object inputObject = null;
                try {
                    inputObject = clientSocket.getOis().readObject();
                }catch (ClassNotFoundException e) {
                    System.out.println("Ошибка класс не найден");
                } catch (IOException e) {
                    System.out.println("Ошибка чтения из поток");
                }
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                singerColumn.setCellValueFactory(new PropertyValueFactory<>("singer"));
                albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
                lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
                genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
                if (inputObject instanceof List) {
                    for (Object o : (List<?>) inputObject) {
                        if (o instanceof Track) {
                            table.getItems().add((Track) o);
                        }
                    }
                }
            }
        }


    }

    @FXML
    public void onClickExit(ActionEvent actionEvent) {
        if (clientSocket != null)
        {
            if (!clientSocket.getSocket().isOutputShutdown()) {
                System.out.println("Отправляем на сервер exit");

                try {
                    clientSocket.getOos().writeObject(ConstProtocol.exit);
                } catch (IOException e) {
                    System.out.println("Ошибка при записи в поток");
                }
                try {
                    clientSocket.getOos().flush();
                } catch (IOException e) {
                    System.out.println("Ошибка при отправке");
                }
                try {
                    clientSocket.getOis().close();
                } catch (IOException e) {

                    System.out.println("Ошибка при закрытии потока на чтение");
                }
                try {
                    clientSocket.getOos().close();
                } catch (IOException e) {
                    System.out.println("Ошибка при закрытии потока на запись");
                }
                clientSocket = null;
                table.getItems().clear();
            }
        }
    }


    @FXML
    public void onClickAdd(ActionEvent actionEvent) {

        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = (Parent) loader.load(getClass().getResourceAsStream("/fxml/addForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setResizable(false);
        stage.setOnShown(event -> {
            add.getParentMenu().setDisable(true);
            connect.getParentMenu().setDisable(true);
        });
        stage.setOnHiding(event -> {
            add.getParentMenu().setDisable(false);
            connect.getParentMenu().setDisable(false);
            if(AddFormController.objects!=null){
                try {
                    clientSocket.getOos().writeObject(ConstProtocol.add);
                    clientSocket.getOos().writeObject(AddFormController.objects.get(0));
                    clientSocket.getOos().writeObject(AddFormController.objects.get(1));
                    clientSocket.getOos().writeObject(AddFormController.objects.get(2));
                    clientSocket.getOos().writeObject(Integer.parseInt(AddFormController.objects.get(3)));
                    clientSocket.getOos().writeObject(AddFormController.objects.get(4));
                    clientSocket.getOos().flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        stage.setTitle("Добавить трек");
        stage.setScene(new Scene(root));
        stage.show();


    }

    @FXML
    public void onClickUpdate(ActionEvent actionEvent) {
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = (Parent) loader.load(getClass().getResourceAsStream("/fxml/addForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage  = new Stage();
        stage.setTitle("Изменить трек");
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.show();
    }

    @FXML
    public void onClickDelete(ActionEvent actionEvent) {
        Integer n = table.getSelectionModel().getFocusedIndex();
        try {
            clientSocket.getOos().writeObject(ConstProtocol.delete);
            clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getName());
            clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getSinger());
            clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getAlbum());
            clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getLengthInt());
            clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getGenreName());
            clientSocket.getOos().flush();
        } catch (IOException e) {
            System.out.println("Ошибка записи в поток");
        }
    }

}
