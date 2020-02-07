package ru.nc.musiclib.net.server;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.model.UserModel;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicLibServer {
    private final static MusicLibLogger logger = new MusicLibLogger(MusicLibServer.class);
    private static ExecutorService executeIt = Executors.newFixedThreadPool(20);

    public void startServer(int port, Model model, Controller controller, UserModel userModel) {
        logger.info("Сервер запускается на порту: " + port);
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            while (!server.isClosed()) {
                Socket client = server.accept();
                logger.info("Подключился пользователь");
                executeIt.execute(new MusicLibServerSocket(client, model, controller, userModel));
            }
        } catch (IOException e) {
            logger.error(e.toString());
        }
        executeIt.shutdown();
        logger.info("Сервер выключается");
    }


}

