package ru.nc.musiclib;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.controller.impl.MusicLibController;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.model.impl.MusicModel;
import ru.nc.musiclib.net.server.MusicLibServer;

public class Main {
    public static void main(String[] args) {
      /*  View view = new ConsoleView();
        Controller controller = new MusicLibController();
        Model model = new MusicModel();
        view.setController(controller);
        view.setModel(model);
        controller.setModel(model);
        controller.setView(view);
        view.runView();*/
        int port;
        if (args.length == 1) {
            port = Integer.parseInt(args[0]);
        }else{
            port = 4444;
        }

        Controller controller = new MusicLibController();
        Model model = new MusicModel();
        controller.setModel(model);
        MusicLibServer musicLibServer = new MusicLibServer();
        musicLibServer.startServer(port, model, controller);
    }
}
