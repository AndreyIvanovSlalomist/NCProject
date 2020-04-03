package ru.nc.musiclib;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.controller.impl.MusicLibController;
import ru.nc.musiclib.services.Model;
import ru.nc.musiclib.services.UserModel;
import ru.nc.musiclib.services.impl.MusicModelWithDao;
import ru.nc.musiclib.services.impl.UsersModelWithDao;
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
        //Model model = new MusicModel();
        Model model = new MusicModelWithDao();
//        UserModel userModel = new UsersModelImpl();
        UserModel userModel = new UsersModelWithDao();
        controller.setModel(model);
        MusicLibServer musicLibServer = new MusicLibServer();
        musicLibServer.startServer(port, model, controller, userModel);
    }
}
