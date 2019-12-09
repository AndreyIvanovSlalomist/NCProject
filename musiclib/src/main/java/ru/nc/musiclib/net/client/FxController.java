package ru.nc.musiclib.net.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.net.ConstProtocol;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

public class FxController {

    private ClientSocket clientSocket;

    public TableColumn nameColumn;
    public TableColumn singerColumn;
    public TableColumn albumColumn;
    public TableColumn lengthColumn;
    public TableColumn genreColumn;
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
    public TableView table;


    @FXML
    public void onClickConnect(ActionEvent actionEvent) {
        table.setVisible(true);
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
        table.setVisible(false);
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
                table.getItems().removeAll();
            }
        }


    }


    @FXML
    public void onClickAdd(ActionEvent actionEvent) {
    }

    @FXML
    public void onClickUpdate(ActionEvent actionEvent) {
    }

    @FXML
    public void onClickDelete(ActionEvent actionEvent) {
    }
}
