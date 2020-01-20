package ru.nc.musiclib.net.server;

import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.model.UserModel;
import ru.nc.musiclib.net.ConstProtocol;
import ru.nc.musiclib.net.Role;
import ru.nc.musiclib.net.StreamFile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class MusicLibServerSocket implements Runnable {
    private final static MusicLibLogger logger = new MusicLibLogger(MusicLibServerSocket.class);
    private Socket clientSocket;
    private Model model;
    private Controller controller;
    private UserModel userModel;
    private User currentUser;

    public MusicLibServerSocket(Socket client, Model model, Controller controller, UserModel userModel) {
        this.clientSocket = client;
        this.model = model;
        this.controller = controller;
        this.userModel = userModel;
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

                        case addUser: {
                            addUser(in);
                            break;
                        }
                        case checkUser: {
                            checkUser(out, in);
                            break;
                        }
                        case setRole: {
                            setRole(in);
                            break;
                        }
                        case getAllUsers: {
                            getAllUsers(out);
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

    private void getAllUsers(ObjectOutputStream out) {

        List<User> userList = userModel.getAllUser();
        for (User user : userList) {

            try {
                out.writeObject(user);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out.writeObject(ConstProtocol.finish);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setRole(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object userName;
        Object role;
        userName = in.readObject();
        role = in.readObject();
        if (userName instanceof String &&
                role instanceof Role) {
            userModel.setRole((String) userName, (Role) role);
        }

    }

    private void checkUser(ObjectOutputStream out, ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object userName;
        Object password;
        userName = in.readObject();
        password = in.readObject();
        if (userName instanceof String &&
                password instanceof String) {
            if (userModel.checkUser((String) userName, (String) password)) {
                out.writeObject(ConstProtocol.errorUser);
            } else {
                out.writeObject(userModel.findUser((String) userName).getRole());
            }
        }
    }

    private void addUser(ObjectInputStream in) throws IOException, ClassNotFoundException {
        Object userName;
        Object password;
        userName = in.readObject();
        password = in.readObject();
        if (userName instanceof String &&
                password instanceof String) {
            userModel.add((String) userName, (String) password);
        }
    }

    private void getAll(ObjectOutputStream out) {

        List<Track> trackList = model.getAll();
        for (Track track : trackList) {

            try {
                out.writeObject(track);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            out.writeObject(ConstProtocol.finish);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadFromFile(ObjectInputStream inputStream) {
        StreamFile.streamToFile(inputStream, "loadFile.xml");
        model.addFromFile("loadFile.xml");
        model.saveTrack();
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
