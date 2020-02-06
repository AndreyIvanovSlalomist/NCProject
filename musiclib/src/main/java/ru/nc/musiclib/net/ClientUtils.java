package ru.nc.musiclib.net;

import javafx.scene.control.Alert;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.net.client.ClientSocket;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class ClientUtils {

    private final static MusicLibLogger logger = new MusicLibLogger(ClientUtils.class);

    private ClientUtils() {
    }

    public static boolean alertSelectedItem(Object selectedItem, String title, String messageText) {
        if (selectedItem == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(messageText);
            alert.showAndWait();
            return false;
        }
        return true;
    }

    public static ClientSocket connect() {
        try {
            return new ClientSocket(InetAddress.getLocalHost(), 4444);
        } catch (UnknownHostException e) {
            logger.error("Ошибка: Неизвестен хост");
        }
        return null;
    }

    public static Object signInUser(ClientSocket clientSocket, String login, String password) {

        try {
            clientSocket.getOos().writeObject(ConstProtocol.checkUser);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().writeObject(password);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток!");
            return null;
        }
        try {
            return clientSocket.getOis().readObject();
        } catch (IOException e) {
            logger.error("Ошибка чтения из потока!");
            return null;
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка класс не найден!");
            return null;
        }
    }

    public static void updateTrack(ClientSocket clientSocket, Track track, String name, String singer, String album, int lengthInt, String genre) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.update);
            clientSocket.getOos().writeObject(name);
            clientSocket.getOos().writeObject(singer);
            clientSocket.getOos().writeObject(album);
            clientSocket.getOos().writeObject(lengthInt);
            clientSocket.getOos().writeObject(genre);
            clientSocket.getOos().writeObject(track);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток");
        }
    }

    public static void addTrack(ClientSocket clientSocket, String name, String singer, String album, int lengthInt, String genre) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.add);
            clientSocket.getOos().writeObject(name);
            clientSocket.getOos().writeObject(singer);
            clientSocket.getOos().writeObject(album);
            clientSocket.getOos().writeObject(lengthInt);
            clientSocket.getOos().writeObject(genre);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток");
        }
    }

    public static Object signUpUser(ClientSocket clientSocket, String login, String password) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.addUser);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().writeObject(password);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток!");
            return null;
        }
        try {
            return clientSocket.getOis().readObject();
        } catch (IOException e) {
            logger.error("Ошибка чтения из потока!");
            return null;
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка класс не найден!");
            return null;
        }
    }

    public static void deleteTrack(ClientSocket clientSocket, Track track) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.delete);
            clientSocket.getOos().writeObject(track.getName());
            clientSocket.getOos().writeObject(track.getSinger());
            clientSocket.getOos().writeObject(track.getAlbum());
            clientSocket.getOos().writeObject(track.getLengthInt());
            clientSocket.getOos().writeObject(track.getGenreName());
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток");
        }
    }

    public static List<Track> getAllTrack(ClientSocket clientSocket, String name, String singer, String album, String genre) {
        List<Track> trackList = new ArrayList<>();
        boolean isFiltered = !(name.isEmpty() && singer.isEmpty() && album.isEmpty() && genre.isEmpty());
        try {
            if (isFiltered) {
                logger.info("get filter");
                clientSocket.getOos().writeObject(ConstProtocol.filter);
            } else {
                logger.info("get all");
                clientSocket.getOos().writeObject(ConstProtocol.getAll);
            }
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка при отправке потока");
            return new ArrayList<>();
        }
        if (isFiltered) {
            try {
                clientSocket.getOos().writeObject(name);
                clientSocket.getOos().writeObject(singer);
                clientSocket.getOos().writeObject(album);
                clientSocket.getOos().writeObject(genre);
                clientSocket.getOos().flush();
            } catch (IOException e) {
                logger.error("Ошибка записи в поток");
                return new ArrayList<>();
            }
        }
        Object inputObject = null;
        do {
            try {
                inputObject = clientSocket.getOis().readObject();
            } catch (ClassNotFoundException e) {
                logger.error("Ошибка класс не найден");
                return new ArrayList<>();
            } catch (IOException e) {
                logger.error("Ошибка чтения из потока");
                return new ArrayList<>();
            }
            if (inputObject instanceof Track) {
                logger.info((inputObject).toString());
                trackList.add((Track) inputObject);
            }
        } while (!((inputObject == ConstProtocol.finish)));
        return trackList;
    }

    public static List<User> getAllUser(ClientSocket clientSocket) {
        List<User> userList = new ArrayList<>();

        logger.info("show Users");
        try {
            clientSocket.getOos().writeObject(ConstProtocol.getAllUsers);
        } catch (IOException e) {
            logger.error("Ошибка записи в поток");
        }
        try {
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка при отправки потока");
        }
        Object inputObject = null;
        do {
            try {
                inputObject = clientSocket.getOis().readObject();
            } catch (ClassNotFoundException e) {
                logger.error("Ошибка класс не найден");
            } catch (IOException e) {
                logger.error("Ошибка чтения из поток");
            }

            if (inputObject instanceof User) {
                logger.info(inputObject.toString());
                userList.add((User) inputObject);
            }
        } while (!((inputObject == ConstProtocol.finish)));

        return userList;
    }

    public static void setRole(ClientSocket clientSocket, String name, Role role) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.setRole);
            clientSocket.getOos().writeObject(name);
            clientSocket.getOos().writeObject(role);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток");
        }
    }
}
