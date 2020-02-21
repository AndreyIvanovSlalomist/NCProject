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
        try {
            Map<Class, Callable<?>> creators = new HashMap<>();
            if (callableIn != null)
                creators.put(aClass, callableIn);
            FXMLLoader loader = new FXMLLoader(aClass.getResource(resource));
            loader.setControllerFactory(param -> {
                try {
                    Callable<?> callable = null;
                    if (!creators.isEmpty())
                        callable = creators.get(param);
                    if (callable == null) {
                        return param.newInstance();
                    } else {
                        return callable.call();
                    }
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            });
            Parent root;
            root = loader.load();
            Stage stage = new Stage();
            stage.setResizable(resizable);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            return stage;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
