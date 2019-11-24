package ru.nc.musiclib;

import ru.nc.musiclib.controller.MusicLibController;
import ru.nc.musiclib.interfaces.Controller;
import ru.nc.musiclib.interfaces.Model;
import ru.nc.musiclib.interfaces.View;
import ru.nc.musiclib.model.MusicModel;
import ru.nc.musiclib.view.ConsoleView;

public class Main {
    public static void main(String[] args) {
        View view = new ConsoleView();
        Controller controller = new MusicLibController();
        Model model = new MusicModel();
        view.setController(controller);
        view.setModel(model);
        controller.setModel(model);
        controller.setView(view);
        view.runView();
    }
}
