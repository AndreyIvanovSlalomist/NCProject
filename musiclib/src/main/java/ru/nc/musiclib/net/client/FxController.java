package ru.nc.musiclib.net.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.net.ConstProtocol;
import ru.nc.musiclib.net.Role;
import ru.nc.musiclib.net.StreamFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import static ru.nc.musiclib.net.ClientUtils.*;
import static ru.nc.musiclib.net.LoaderFX.getStage;

public class FxController {

    public MenuItem users;
    public TextField filterName;
    public TextField filterSinger;
    public TextField filterAlbum;
    public TextField filterGenre;
    public Button filterBtn;
    public Button filterCancelBtn;
    public MenuItem saveToFile;
    public MenuItem loadFromFile;
    public MenuItem refresh;
    public TableColumn<Track, String> nameColumn;
    public TableColumn<Track, String> singerColumn;
    public TableColumn<Track, String> albumColumn;
    public TableColumn<Track, Integer> lengthColumn;
    public TableColumn<Track, String> genreColumn;
    @FXML
    public MenuItem add;
    @FXML
    public MenuItem update;
    @FXML
    public MenuItem delete;
    @FXML
    public TableView<Track> table;
    private ClientSocket clientSocket;
    private Role role;

    private void setCurrentRole(Role role) {
        final boolean value = role.equals(Role.administrator) || role.equals(Role.moderator);
        add.setVisible(value);
        update.setVisible(value);
        delete.setVisible(value);
        saveToFile.setVisible(value);
        loadFromFile.setVisible(value);
        refresh.setVisible(value || role.equals(Role.user));
        users.setVisible(role.equals(Role.administrator));
    }

    @FXML
    void initialize() {
        setCurrentRole(role);
        onClickRefresh(null);
    }

    public FxController(ClientSocket clientSocket, Role role) {
        this.clientSocket = clientSocket;
        this.role = role;
    }

    @FXML
    public void onClickAdd(ActionEvent actionEvent) {
        if (clientSocket == null)
            return;
        Stage stage = getStage(AddFormController.class, (Callable<AddFormController>) () -> new AddFormController(clientSocket, null),
                "/fxml/addForm.fxml", false, "Добавить трек");
        stage.setOnShown(event -> {
            add.getParentMenu().setDisable(true);
        });
        stage.setOnHiding(event -> {
            add.getParentMenu().setDisable(false);
            onClickRefresh(null);
        });
        stage.show();

    }

    private Track getSelectedItemFromTable() {
        return table.getSelectionModel().getSelectedItem();
    }

    @FXML
    public void onClickUpdate(ActionEvent actionEvent) {
        if (clientSocket == null)
            return;
        if (!alertSelectedItem(getSelectedItemFromTable(), "Ошибка!", "Трек не выбран!")) {
            return;
        }

        Track track = (Track) getSelectedItemFromTable().clone();

        Stage stage = getStage(AddFormController.class, (Callable<AddFormController>) () -> new AddFormController(clientSocket, track),
                "/fxml/addForm.fxml", false, "Изменить трек");
        stage.setOnShown(event -> {
            add.getParentMenu().setDisable(true);
        });
        stage.setOnHiding(event -> {
            add.getParentMenu().setDisable(false);
            onClickRefresh(null);
        });
        stage.show();
    }

    @FXML
    public void onClickDelete(ActionEvent actionEvent) {
        if (clientSocket == null)
            return;
        if (!alertSelectedItem(getSelectedItemFromTable(), "Ошибка!", "Трек не выбран!")) {
            return;
        }
        deleteTrack(clientSocket, (Track) getSelectedItemFromTable().clone());
        onClickRefresh(null);
    }

    public void onClickSaveToFile(ActionEvent actionEvent) throws IOException {
        if (clientSocket == null)
            return;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + File.separator));
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
        fileChooser.setInitialDirectory(new File(System.getProperty("user.dir") + File.separator));
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
        table.getItems().clear();
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        singerColumn.setCellValueFactory(new PropertyValueFactory<>("singer"));
        albumColumn.setCellValueFactory(new PropertyValueFactory<>("album"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        for (Track track : getAllTrack(clientSocket, filterName.getText(), filterSinger.getText(), filterAlbum.getText(), filterGenre.getText())) {
            table.getItems().add(track);
        }
    }

    public void onClickUsers(ActionEvent actionEvent) {
        if (clientSocket == null)
            return;
        Stage stage = getStage(UsersController.class, (Callable<UsersController>) () -> new UsersController(clientSocket),
                "/fxml/users.fxml", false, "Пользователи");
        stage.setOnShown(event -> {
            add.getParentMenu().setDisable(true);
        });
        stage.setOnHiding(event -> {
            add.getParentMenu().setDisable(false);
        });
        stage.show();
    }

    public void onClickFilter() {
        if (clientSocket == null)
            return;
        if (filterName.getText().isEmpty() && filterSinger.getText().isEmpty() && filterAlbum.getText().isEmpty() && filterGenre.getText().isEmpty()) {
        } else {
            onClickRefresh(null);
        }
    }

    public void onClickFilterCancel() {
        if (clientSocket == null)
            return;
        filterName.setText("");
        filterSinger.setText("");
        filterAlbum.setText("");
        filterGenre.setText("");
        onClickRefresh(null);
    }
}
