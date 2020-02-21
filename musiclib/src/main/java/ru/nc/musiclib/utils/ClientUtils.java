package ru.nc.musiclib.utils;

import javafx.scene.control.Alert;
import ru.nc.musiclib.classes.Role;
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

    public static ClientSocket connect(String host, int port) {
        try {
            return new ClientSocket(InetAddress.getByName(host), port);
        } catch (UnknownHostException e) {
            logger.error("Ошибка: Неизвестен хост. " + e.toString());
        }
        return null;
    }

    public static Object signInUser(ClientSocket clientSocket, String login) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.checkUser);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().flush();
            return clientSocket.getOis().readObject();
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка класс не найден! " + e.toString());
        } catch (IOException e) {
            logger.error("Ошибка записи в поток! " + e.toString());
        }
        return null;
    }

    public static boolean checkPassword(ClientSocket clientSocket, String login, String password) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.checkPassword);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().writeObject(password);
            clientSocket.getOos().flush();
            return clientSocket.getOis().readBoolean();
        } catch (IOException e) {
            logger.error("Ошибка чтения из потока! " + e.toString());
        }
        return false;
    }

    public static Object getRole(ClientSocket clientSocket, String login) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.getRole);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().flush();
            return clientSocket.getOis().readObject();
        } catch (IOException e) {
            logger.error("Ошибка чтения из потока! " + e.toString());
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка класс не найден! " + e.toString());
        }
        return null;
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
            logger.error("Ошибка записи в поток. " + e.toString());
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
            logger.error("Ошибка записи в поток. " + e.toString());
        }
    }

    public static Object signUpUser(ClientSocket clientSocket, String login, String password) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.addUser);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().writeObject(password);
            clientSocket.getOos().flush();
            return clientSocket.getOis().readObject();
        } catch (IOException e) {
            logger.error("Ошибка чтения из потока!. " + e.toString());
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка класс не найден!. " + e.toString());
        }
        return null;
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
            logger.error("Ошибка записи в поток. " + e.toString());
        }
    }

    public static List<Track> getAllTrack(ClientSocket clientSocket, String name, String singer, String album, String genre) {
        try {
            List<Track> trackList = new ArrayList<>();
            boolean isFiltered = !(name.isEmpty() && singer.isEmpty() && album.isEmpty() && genre.isEmpty());
            if (isFiltered) {
                logger.info("get filter");
                clientSocket.getOos().writeObject(ConstProtocol.filter);
            } else {
                logger.info("get all");
                clientSocket.getOos().writeObject(ConstProtocol.getAll);
            }
            clientSocket.getOos().flush();
            if (isFiltered) {
                clientSocket.getOos().writeObject(name);
                clientSocket.getOos().writeObject(singer);
                clientSocket.getOos().writeObject(album);
                clientSocket.getOos().writeObject(genre);
                clientSocket.getOos().flush();
            }
            Object inputObject;
            do {
                inputObject = clientSocket.getOis().readObject();
                if (inputObject instanceof Track) {
                    logger.info((inputObject).toString());
                    trackList.add((Track) inputObject);
                }
            } while (!((inputObject == ConstProtocol.finish)));
            return trackList;
        } catch (IOException e) {
            logger.error("Ошибка при отправке потока. " + e.toString());
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка класс не найден. " + e.toString());
        }
        return new ArrayList<>();
    }

    public static List<User> getAllUser(ClientSocket clientSocket) {
        try {
            List<User> userList = new ArrayList<>();
            logger.info("show Users");
            clientSocket.getOos().writeObject(ConstProtocol.getAllUsers);
            clientSocket.getOos().flush();
            Object inputObject;
            do {
                inputObject = clientSocket.getOis().readObject();
                if (inputObject instanceof User) {
                    logger.info(inputObject.toString());
                    userList.add((User) inputObject);
                }
            } while (!((inputObject == ConstProtocol.finish)));
            return userList;
        } catch (IOException e) {
            logger.error("Ошибка записи в поток. " + e.toString());
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка класс не найден. " + e.toString());
        }
        return new ArrayList<>();
    }

    public static void setRole(ClientSocket clientSocket, String name, Role role) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.setRole);
            clientSocket.getOos().writeObject(name);
            clientSocket.getOos().writeObject(role);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток. " + e.toString());
        }
    }

    public static void deleteUser(ClientSocket clientSocket, String name) {
        try {
            clientSocket.getOos().writeObject(ConstProtocol.deleteUser);
            clientSocket.getOos().writeObject(name);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток. " + e.toString());
        }
    }
}
