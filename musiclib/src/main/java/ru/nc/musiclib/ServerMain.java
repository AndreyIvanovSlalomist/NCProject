package ru.nc.musiclib;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.controller.impl.MusicLibController;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.model.impl.MusicModel;
import ru.nc.musiclib.net.server.MusicLibServer;

public class ServerMain {
    public static void main(String[] args) {
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