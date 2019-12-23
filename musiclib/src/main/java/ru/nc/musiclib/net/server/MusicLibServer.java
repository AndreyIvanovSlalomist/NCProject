package ru.nc.musiclib.net.server;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.Model;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
public class MusicLibServer {
    private static ExecutorService executeIt = Executors.newFixedThreadPool(100);

    private final static MusicLibLogger logger = new MusicLibLogger(MusicLibServer.class);

    public void startServer(int port, Model model, Controller controller) {
        logger.info("Сервер запускается на порту: " + port);
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            logger.error("Не удалось запустить сервер на порту: " + port);
        }

        while (!server.isClosed()) {
            Socket client;
            try {
                client = server.accept();
            } catch (IOException e) {
                logger.error("Ошибка при подключении пользователя через порт: " + port);
                break;
            }
            logger.info("Подключился пользователь");

            executeIt.execute(new MusicLibServerSocket(client, model, controller));
        }

        executeIt.shutdown();
        logger.info("Сервер выключается");
    }



}

