package ru.nc.musiclib.net.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.Track;

public class AddFormController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

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

    public static List<String> objects;



    @FXML
    void initialize() {
        cancelButton.setOnAction(event -> {
            cancelButton.getScene().getWindow().hide();
        });
        addButton.setOnAction(event -> {
            objects = new ArrayList<>();

            objects.add(name.getText());
            objects.add(singer.getText());
            objects.add(album.getText());
            objects.add(length.getText());
            objects.add(genre.getText());
            addButton.getScene().getWindow().hide();
        });

    }
}
