package ru.nc.musiclib.net.server;

import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.net.ConstProtocol;
import ru.nc.musiclib.net.StreamFile;

import java.io.*;
import java.net.Socket;

public class MusicLibServerSocket implements Runnable {
    private final static MusicLibLogger logger = new MusicLibLogger(MusicLibServerSocket.class);
    private Socket clientSocket;
    private Model model;
    private Controller controller;

    public MusicLibServerSocket(Socket client, Model model, Controller controller) {
        this.clientSocket = client;
        this.model = model;
        this.controller = controller;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());

            logger.info("Клиент подключился");
            while (!clientSocket.isClosed()) {
                Object inputObject = in.readObject();
                logger.info("От клиента получен запрос " + inputObject);
                if (inputObject instanceof ConstProtocol) {
                    ConstProtocol inputConstProtocol = (ConstProtocol) inputObject;
                    if (inputObject == ConstProtocol.exit) {
                        break;
                    }
                    switch (inputConstProtocol) {
                        case find: {
                            find(out, in);
                            break;
                        }
                        case getAll: {
                            getAll(out);
                            break;
                        }
                        case add: {
                            add(in);
                            break;
                        }
                        case delete: {
                            delete(in);
                            break;
                        }
                        case update: {
                            update(in);
                            break;
                        }
                        case sort: {
                            sortList(out, in);
                            break;
                        }
                        case getFile: {
                            getFile(out);
                            break;
                        }
                        case loadFromFile: {
                            loadFromFile(in);
                            break;
                        }
                    }
                }
            }
            in.close();
            out.close();
            clientSocket.close();
            logger.info("Пользователь закрывает соединение");
        } catch (ClassNotFoundException e) {

            logger.error("Ошибка класс не найден");
        } catch (IOException e) {
            logger.error("Ошибка чтения/записи в поток");
        }
    }

    private void loadFromFile(ObjectInputStream inputStream) {
        StreamFile.streamToFile(inputStream, "loadFile.xml");
        model.addFromFile("loadFile.xml");
    }

    private void getFile(ObjectOutputStream out) {
        StreamFile.fileToStream(out, "tracks.xml");
        logger.info("Файл передан");
    }

    private void sortList(ObjectOutputStream out, ObjectInputStream in) {
    }

    private void update(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object name;
        Object singer;
        Object album;
        Object length;
        Object genreName;
        Object track;
        name = in.readObject();
        singer = in.readObject();
        album = in.readObject();
        length = in.readObject();
        genreName = in.readObject();
        track = in.readObject();
        if (name instanceof String &&
                singer instanceof String &&
                album instanceof String &&
                length instanceof Integer &&
                genreName instanceof String &&
                track instanceof Track) {
            controller.isValidUpdate((Track) track, (String) name, (String) singer, (String) album, (Integer) length, (String) genreName);
        }
    }

    private void getAll(ObjectOutputStream out) throws IOException {
        out.writeObject(model.getAll());
        out.flush();
    }

    private void find(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object inputObject;
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
    }

    private void delete(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object name;
        Object singer;
        Object album;
        Object length;
        Object genreName;
        name = in.readObject();
        singer = in.readObject();
        album = in.readObject();
        length = in.readObject();
        genreName = in.readObject();
        if (name instanceof String &&
                singer instanceof String &&
                album instanceof String &&
                length instanceof Integer &&
                genreName instanceof String) {
            controller.isValidDelete((String) name, (String) singer, (String) album, (Integer) length, (String) genreName);
        }
    }

    private void add(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object name;
        Object singer;
        Object album;
        Object length;
        Object genreName;
        name = in.readObject();
        singer = in.readObject();
        album = in.readObject();
        length = in.readObject();
        genreName = in.readObject();
        if (name instanceof String &&
                singer instanceof String &&
                album instanceof String &&
                length instanceof Integer &&
                genreName instanceof String) {
            controller.isValidAdd((String) name, (String) singer, (String) album, (Integer) length, (String) genreName);
        }

    }


}
