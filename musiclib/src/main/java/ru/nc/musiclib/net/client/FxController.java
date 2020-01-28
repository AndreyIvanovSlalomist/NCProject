package ru.nc.musiclib.net.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.net.ConstProtocol;
import ru.nc.musiclib.net.StreamFile;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class FxController {

    private final static MusicLibLogger logger = new MusicLibLogger(FxController.class);
    public static boolean edit;
    public static String name;
    public static String singer;
    public static String album;
    public static String length;
    public static String genre;
    public MenuItem saveToFile;
    public MenuItem loadFromFile;
    public MenuItem refresh;
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
    private ClientSocket clientSocket;

    @FXML
    public void onClickConnect(ActionEvent actionEvent) {
        if (clientSocket == null) {
            try {
                clientSocket = new ClientSocket(InetAddress.getLocalHost(), 4444);
            } catch (UnknownHostException e) {
                logger.error("Ошибка Неизвестен хост");
            }
            if (!clientSocket.getSocket().isOutputShutdown()) {
                onClickRefresh(null);
            }
        }
    }

    @FXML
    public void onClickExit(ActionEvent actionEvent) {
        if (clientSocket != null) {
            if (!clientSocket.getSocket().isOutputShutdown()) {
                logger.info("Отправляем на сервер exit");

                try {
                    clientSocket.getOos().writeObject(ConstProtocol.exit);
                } catch (IOException e) {
                    logger.error("Ошибка при записи в поток" + e.getLocalizedMessage());
                }
                try {
                    clientSocket.getOos().flush();
                } catch (IOException e) {
                    logger.error("Ошибка при отправке");
                }
                try {
                    clientSocket.getOis().close();
                } catch (IOException e) {

                    logger.error("Ошибка при закрытии потока на чтение");
                }
                try {
                    clientSocket.getOos().close();
                } catch (IOException e) {
                    logger.error("Ошибка при закрытии потока на запись");
                }
                clientSocket = null;
                table.getItems().clear();
            }
        }
    }


    @FXML
    public void onClickAdd(ActionEvent actionEvent) {
        if (clientSocket == null)
            return;
        edit = false;
        FXMLLoader loader = new FXMLLoader();
        Parent root = null;
        try {
            root = (Parent) loader.load(getClass().getResourceAsStream("/fxml/addForm.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setOnShown(event -> {
            add.getParentMenu().setDisable(true);
            connect.getParentMenu().setDisable(true);
        });
        stage.setOnHiding(event -> {
            add.getParentMenu().setDisable(false);
            connect.getParentMenu().setDisable(false);
            if (!AddFormController.isCanceled) {
                try {
                    clientSocket.getOos().writeObject(ConstProtocol.add);
                    clientSocket.getOos().writeObject(name);
                    clientSocket.getOos().writeObject(singer);
                    clientSocket.getOos().writeObject(album);
                    clientSocket.getOos().writeObject(Integer.parseInt(length));
                    clientSocket.getOos().writeObject(genre);
                    clientSocket.getOos().flush();
                    onClickRefresh(null);
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
        if (clientSocket == null)
            return;
        edit = true;
        if (table.getSelectionModel().getSelectedItem() != null) {
            name = table.getSelectionModel().getSelectedItem().getName();
            singer = table.getSelectionModel().getSelectedItem().getSinger();
            album = table.getSelectionModel().getSelectedItem().getAlbum();
            length = Integer.toString(table.getSelectionModel().getSelectedItem().getLengthInt());
            genre = table.getSelectionModel().getSelectedItem().getGenreName();
            Track track = new Track(name, singer, album, Integer.parseInt(length), new Genre(genre));
            FXMLLoader loader = new FXMLLoader();
            Parent root = null;
            try {
                root = (Parent) loader.load(getClass().getResourceAsStream("/fxml/addForm.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.setOnShown(event -> {
                add.getParentMenu().setDisable(true);
                connect.getParentMenu().setDisable(true);
            });
            stage.setOnHiding(event -> {
                add.getParentMenu().setDisable(false);
                connect.getParentMenu().setDisable(false);
                if (!AddFormController.isCanceled) {
                    try {
                        clientSocket.getOos().writeObject(ConstProtocol.update);
                        clientSocket.getOos().writeObject(name);
                        clientSocket.getOos().writeObject(singer);
                        clientSocket.getOos().writeObject(album);
                        clientSocket.getOos().writeObject(Integer.parseInt(length));
                        clientSocket.getOos().writeObject(genre);
                        clientSocket.getOos().writeObject(track);
                        clientSocket.getOos().flush();
                        onClickRefresh(null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            stage.setTitle("Изменить трек");
            stage.setScene(new Scene(root));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Трек не выбран!");
            alert.showAndWait();
        }

    }

    @FXML
    public void onClickDelete(ActionEvent actionEvent) {
        if (clientSocket == null)
            return;
        if (table.getSelectionModel().getSelectedItem() != null) {
            try {
                clientSocket.getOos().writeObject(ConstProtocol.delete);
                clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getName());
                clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getSinger());
                clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getAlbum());
                clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getLengthInt());
                clientSocket.getOos().writeObject(table.getSelectionModel().getSelectedItem().getGenreName());
                clientSocket.getOos().flush();
                onClickRefresh(null);
            } catch (IOException e) {
                logger.error("Ошибка записи в поток");
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Трек не выбран!");
            alert.showAndWait();
        }
    }

    public void onClickSaveToFile(ActionEvent actionEvent) throws IOException {
        if (clientSocket == null)
            return;
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showSaveDialog(null);
        if (file == null)
            return;
        clientSocket.getOos().writeObject(ConstProtocol.getFile);
        StreamFile.streamToFile(clientSocket.getOis(), file.getPath());

        onClickRefresh(null);

    }

    public void onClickLoadFromFile(ActionEvent actionEvent) throws IOException {
        if (clientSocket == null)
            return;

        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(null);
        if (file == null)
            return;
        clientSocket.getOos().writeObject(ConstProtocol.loadFromFile);
        StreamFile.fileToStream(clientSocket.getOos(), file.getPath());

        onClickRefresh(null);
    }

    public void onClickRefresh(ActionEvent actionEvent) {
        if (clientSocket == null)
            return;

        logger.info("gat all");
        table.getItems().clear();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        singerColumn.setCellValueFactory(new PropertyValueFactory<>("singer"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        {
            {
                try {
                    clientSocket.getOos().writeObject(ConstProtocol.getAll);
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

                    if (inputObject instanceof Track) {
                        logger.info(((Track) inputObject).toString());
                        table.getItems().add((Track) inputObject);
                    }
                } while (!((inputObject instanceof ConstProtocol) && ((ConstProtocol) inputObject == ConstProtocol.finish)));
            }
        }

    }

    public void onClickUsers(ActionEvent actionEvent) {
        if (clientSocket == null)
            return;
        Map<Class, Callable<?>> creators = new HashMap<>();
        creators.put(UsersController.class, (Callable<UsersController>) () -> new UsersController(clientSocket));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/users.fxml"));

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
            add.getParentMenu().setDisable(true);
            connect.getParentMenu().setDisable(true);
        });
        stage.setOnHiding(event -> {
            add.getParentMenu().setDisable(false);
            connect.getParentMenu().setDisable(false);
        });
        stage.setTitle("Пользователи");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
