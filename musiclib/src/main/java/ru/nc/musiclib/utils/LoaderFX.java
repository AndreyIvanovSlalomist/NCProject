package ru.nc.musiclib.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class LoaderFX {
    private LoaderFX() {
    }

    public static Stage getStage(Class aClass, Callable<?> callableIn, String resource, boolean resizable, String title) {

        Map<Class, Callable<?>> creators = new HashMap<>();
        if (callableIn != null)
            creators.put(aClass, callableIn);
        FXMLLoader loader = new FXMLLoader(aClass.getResource(resource));

        loader.setControllerFactory(param -> {
            Callable<?> callable = null;
            if (!creators.isEmpty())
                callable = creators.get(param);
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
        stage.setResizable(resizable);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        return stage;
    }
}
