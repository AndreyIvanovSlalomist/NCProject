package ru.nc.musiclib.net.server;

import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.model.UserModel;
import ru.nc.musiclib.utils.ConstProtocol;
import ru.nc.musiclib.utils.Role;
import ru.nc.musiclib.utils.StreamFile;

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

    public MusicLibServerSocket(Socket client, Model model, Controller controller, UserModel userModel) {
        this.clientSocket = client;
        this.model = model;
        this.controller = controller;
        this.userModel = userModel;
    }

    @Override
    public void run() {
        try (ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream())) {
            logger.info("Клиент подключился");
            while (!clientSocket.isClosed()) {
                Object inputObject = readObject(in);
                logger.info("От клиента получен запрос " + inputObject);
                if (inputObject == null)
                    break;
                if (inputObject instanceof ConstProtocol) {
                    if (actionSelection(out, in, (ConstProtocol) inputObject)) break;
                }
            }
            clientSocket.close();
            logger.info("Пользователь закрывает соединение");
        } catch (IOException e) {
            logger.error("Ошибка чтения/записи в поток. " + e.toString());
        }
    }

    private boolean actionSelection(ObjectOutputStream out, ObjectInputStream in, ConstProtocol inputConstProtocol) {
        switch (inputConstProtocol) {
            case exit:
                return true;
            case find: {
                find(out, in);
                break;
            }
            case filter: {
                filter(out, in);
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
                addUser(in, out);
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
            case deleteUser: {
                deleteUser(in);
                break;
            }
        }
        return false;
    }

    private void deleteUser(ObjectInputStream in) {
        String name = readObjectToString(in);
        if (name != null) {
            userModel.delete(name);
        }
    }

    private void filter(ObjectOutputStream out, ObjectInputStream in) {
        String filterName = readObjectToString(in);
        String filterSinger = readObjectToString(in);
        String filterAlbum = readObjectToString(in);
        String filterGenreName = readObjectToString(in);
        if (filterName != null && filterSinger != null && filterAlbum != null && filterGenreName != null) {
            try {
                out.reset();
                List<Track> trackList = model.filter(filterName, filterSinger, filterAlbum, filterGenreName);
                for (Track track : trackList) {
                    logger.info(track.toString());
                    out.writeObject(track);
                }
                out.writeObject(ConstProtocol.finish);
                out.flush();
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }

    private void getAllUsers(ObjectOutputStream out) {
        try {
            out.reset();
            List<User> userList = userModel.getAllUser();
            for (User user : userList) {
                logger.info(user.toString());
                out.writeObject(user);
            }
            out.writeObject(ConstProtocol.finish);
            out.flush();
        } catch (IOException e) {
            logger.error(e.toString());
        }
    }

    private void setRole(ObjectInputStream in) {
        String userName = readObjectToString(in);
        Object role = readObject(in);
        if (userName != null && role instanceof Role) {
            userModel.setRole(userName, (Role) role);
        }
    }

    private void checkUser(ObjectOutputStream out, ObjectInputStream in) {
        String userName = readObjectToString(in);
        String password = readObjectToString(in);
        if (userName != null && password != null) {
            try {
                if (userModel.checkUser(userName, password)) {
                    out.reset();
                    out.writeObject(userModel.findUser(userName).getRole());
                } else {
                    out.writeObject(ConstProtocol.errorUser);
                }
            } catch (IOException e) {
                logger.error(e.toString());
            }
        }
    }

    private void addUser(ObjectInputStream in, ObjectOutputStream out) {
        String userName = readObjectToString(in);
        String password = readObjectToString(in);
        if (userName != null && password != null) {
            try {
                if (userModel.add(userName, password)) {
                    out.writeObject("OK");
                    out.flush();
                } else {
                    out.writeObject("Cancel");
                }
            } catch (IOException e) {
                logger.error( e.toString());
            }
        }
    }

    private void getAll(ObjectOutputStream out) {
        try {
            out.reset();
            List<Track> trackList = model.getAll();
            for (Track track : trackList) {
                logger.info(track.toString());
                out.writeObject(track);
            }
            out.writeObject(ConstProtocol.finish);
            out.flush();
        } catch (IOException e) {
            logger.error(e.toString());
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

    private void update(ObjectInputStream in) {
        String name = readObjectToString(in);
        String singer = readObjectToString(in);
        String album = readObjectToString(in);
        Integer length = readObjectToInteger(in);
        String genreName = readObjectToString(in);
        Object track = readObject(in);
        if (name != null && singer != null && album != null && length != null && genreName != null && track instanceof Track) {
            controller.isValidUpdate((Track) track, name, singer, album, length, genreName);
        }
    }

    private void find(ObjectOutputStream out, ObjectInputStream in) {
        Integer inputInt = readObjectToInteger(in);
        if (inputInt != null) {
            String findValue = readObjectToString(in);
            if (findValue != null) {
                try {
                    out.reset();
                    out.writeObject(model.find(inputInt, findValue));
                    out.flush();
                } catch (IOException e) {
                    logger.error(e.toString());
                }
            }
        }
    }

    private void delete(ObjectInputStream in) {
        String name = readObjectToString(in);
        String singer = readObjectToString(in);
        String album = readObjectToString(in);
        Integer length = readObjectToInteger(in);
        String genreName = readObjectToString(in);
        if (name != null && singer != null && album != null && length != null && genreName != null) {
            controller.isValidDelete(name, singer, album, length, genreName);
        }
    }

    private void add(ObjectInputStream in) {
        String name = readObjectToString(in);
        String singer = readObjectToString(in);
        String album = readObjectToString(in);
        Integer length = readObjectToInteger(in);
        String genreName = readObjectToString(in);
        if (name != null && singer != null && album != null && length != null && genreName != null) {
            controller.isValidAdd(name, singer, album, length, genreName);
        }
    }


    private Object readObject(ObjectInputStream in) {
        Object object = null;
        try {
            object = in.readObject();
        } catch (IOException e) {
            logger.error("Ошибка ввода вывода. " + e.toString());
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка. Класс не найден. " + e.toString());
        }
        return object;
    }

    private String readObjectToString(ObjectInputStream in) {
        Object string = readObject(in);
        if (string instanceof String)
            return (String) string;
        logger.error("Ошибка. Тип не соответствует. Ожидается String");
        return null;
    }

    private Integer readObjectToInteger(ObjectInputStream in) {
        Object integer = readObject(in);
        if (integer instanceof Integer)
            return (Integer) integer;
        logger.error("Ошибка. Тип не соответствует. Ожидается Integer");
        return null;
    }

}
