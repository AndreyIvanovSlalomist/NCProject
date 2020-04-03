package ru.nc.musiclib;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.controller.impl.MusicLibController;
import ru.nc.musiclib.services.Model;
import ru.nc.musiclib.services.impl.MusicModel;
import ru.nc.musiclib.console.view.View;
import ru.nc.musiclib.console.view.impl.ConsoleView;

public class ConsoleMain {
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
