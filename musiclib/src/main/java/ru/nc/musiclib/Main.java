package ru.nc.musiclib;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.controller.impl.MusicLibController;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.model.impl.MusicModel;
import ru.nc.musiclib.view.View;
import ru.nc.musiclib.view.impl.ConsoleView;

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
