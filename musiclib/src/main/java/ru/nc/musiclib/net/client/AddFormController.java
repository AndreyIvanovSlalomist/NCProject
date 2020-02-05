package ru.nc.musiclib.net.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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

    public static boolean isCanceled;



    @FXML
    void initialize() {
        isCanceled=false;
        if(FxController.edit){
            addButton.setText("Изменить");
            name.setText(FxController.name);
            singer.setText(FxController.singer);
            album.setText(FxController.album);
            length.setText(FxController.length);
            genre.setText(FxController.genre);
        }
        cancelButton.setOnAction(event -> {
            isCanceled = true;
            cancelButton.getScene().getWindow().hide();
        });
        addButton.setOnAction(event -> {
            FxController.name=name.getText();
            FxController.singer=singer.getText();
            FxController.album=album.getText();
            FxController.length=length.getText();
            FxController.genre=genre.getText();
            addButton.getScene().getWindow().hide();
        });

    }

}
