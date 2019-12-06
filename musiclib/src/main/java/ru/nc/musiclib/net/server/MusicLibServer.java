package ru.nc.musiclib.net.server;

import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.model.Model;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
            System.out.println(
                    "Не удалось запустить сервер на порту: " + port);
            System.exit(-1);
        }

        while (!server.isClosed()) {
            Socket client = null;
            try {
                client = server.accept();
            } catch (IOException e) {
                System.out.println("Ошибка при подключении пользователя через порт: "+ port);
                System.exit(-1);
            }
            System.out.print("Подключился пользователь");

            executeIt.execute(new Server(client, model, controller));
        }

        executeIt.shutdown();
        System.out.print("Сервер выключается");
    }

    private static class Server implements Runnable {
        private Socket clientSocket;
        private Model model;
        private Controller controller;

        public Server(Socket client, Model model, Controller controller) {
            this.clientSocket = client;
            this.model = model;
            this.controller = controller;
        }

        @Override
        public void run() {
            try {
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

                System.out.println("Клиент подключился");
                while (!clientSocket.isClosed()) {
                    Object inputObject = in.readObject();
                    if (inputObject instanceof String && inputObject.toString().equals("exit")) {
                        break;
                    }
                    System.out.println("От клиента получен запрос: " + inputObject.toString());
                    if (inputObject instanceof String && inputObject.toString().equals("exit")) {
                        break;
                    }

                    switch (inputObject.toString()) {
                        case "find": {

                            inputObject = in.readObject();
                            if (inputObject instanceof Integer) {
                                int inputInt = (int) inputObject;
                                inputObject = in.readObject();
                                if (inputObject instanceof String) {
                                    String findValue = (String) inputObject;
                                    out.writeObject(model.find(inputInt, findValue));
                                    out.flush();
                                }
                            }

                            break;
                        }
                        case "showAll": {
                            out.writeObject(model.getAll());
                            out.flush();
                            break;
                        }
                        case "add": {
                            String name;
                            String singer;
                            String album;
                            int length;
                            String genreName;
                            inputObject = in.readObject();
                            if (inputObject instanceof String) {
                                name = (String) inputObject;
                                if (inputObject instanceof String) {
                                    singer = (String) inputObject;
                                    if (inputObject instanceof String) {
                                        album = (String) inputObject;
                                        if (inputObject instanceof Integer) {
                                            length = (Integer) inputObject;
                                            if (inputObject instanceof String) {
                                                genreName = (String) inputObject;
                                                controller.isValidAdd(name, singer, album, length, genreName);
                                            }
                                        }
                                    }
                                }
                            }


                            break;
                        }
                    }

                }

                in.close();
                out.close();
                clientSocket.close();
                System.out.println("Пользователь закрывает соединение");
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

}

