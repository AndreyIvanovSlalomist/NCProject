package ru.nc.musiclib.controller.fxController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.nc.musiclib.model.Track;
import ru.nc.musiclib.net.client.ClientSocket;

import static ru.nc.musiclib.utils.ClientUtils.*;

public class AddFormController {
    @FXML
    private Button cancelButton;
    @FXML
    private TextField name;
    @FXML
    private TextField singer;
    @FXML
    private TextField album;
    @FXML
    private TextField length;
    @FXML
    private TextField genre;
    @FXML
    private Button addButton;
    private ClientSocket clientSocket;
    private Track track;

    public AddFormController(ClientSocket clientSocket, Track track) {
        this.clientSocket = clientSocket;
        this.track = track;
    }

    @FXML
    void initialize() {
        if (track != null) {
            addButton.setText("Изменить");
            name.setText(track.getName());
            singer.setText(track.getSinger());
            album.setText(track.getAlbum());
            length.setText(Integer.toString(track.getLengthInt()));
            genre.setText(track.getGenreName());
        }
        cancelButton.setOnAction(event -> {
            cancelButton.getScene().getWindow().hide();
        });

    }

    public void onClickAdd(ActionEvent actionEvent) {
        int lengthInt = 0;
        try {
            lengthInt = Integer.parseInt(length.getText());
        } catch (NumberFormatException e) {
            alertSelectedItem(null, "Ошибка!", "Длина трека должна быть целым числом!");
            return;
        }
        if (track == null) {
            addTrack(clientSocket, name.getText(), singer.getText(), album.getText(), lengthInt, genre.getText());
        } else {
            updateTrack(clientSocket, track, name.getText(), singer.getText(), album.getText(), lengthInt, genre.getText());
        }
        addButton.getScene().getWindow().hide();
    }
}
