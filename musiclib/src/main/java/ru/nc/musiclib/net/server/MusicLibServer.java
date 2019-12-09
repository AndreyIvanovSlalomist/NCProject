package ru.nc.musiclib.net.server;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.model.Model;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MusicLibServer {
    static ExecutorService executeIt = Executors.newFixedThreadPool(100);

    public void startServer(int port, Model model, Controller controller) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            System.out.println("Не удалось запустить сервер на порту: " + port);
        }

        while (!server.isClosed()) {
            Socket client = null;
            try {
                client = server.accept();
            } catch (IOException e) {
                System.out.println("Ошибка при подключении пользователя через порт: " + port);
                break;
            }
            System.out.print("Подключился пользователь");

            executeIt.execute(new MusicLibServerSocket(client, model, controller));
        }

        executeIt.shutdown();
        System.out.print("Сервер выключается");
    }



}

