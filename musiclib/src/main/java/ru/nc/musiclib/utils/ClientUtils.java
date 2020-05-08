package ru.nc.musiclib.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.Track;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.net.client.ClientSocket;
import ru.nc.musiclib.transfer.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        return new ClientSocket(host, port, "");

    }

    private static String getBaseUrl(ClientSocket clientSocket) {
        return "http://" + clientSocket.getHost() + ":" + clientSocket.getPort() + "/api";
    }

    private static void jsonToConnection(HttpURLConnection con, String jsonInputString ){
        try (OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean signInUser(ClientSocket clientSocket, String login, String password) {
        try {
            HttpURLConnection con = getHttpURLConnection(clientSocket, "POST", getBaseUrl(clientSocket) + "/login");
            jsonToConnection(con, "{\"userName\": \"" + login + "\", \"password\": \"" + password + "\"}");

            TokenDto tokenDto = (TokenDto) getObjectFromJSON(getJSONFromHttpURLConnection(con), TokenDto.class);
            if (tokenDto != null) {
                clientSocket.setToken(tokenDto.getValue());
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            clientSocket.setToken("");
            e.printStackTrace();
            return false;
        }
    }

    public static boolean checkPassword(ClientSocket clientSocket, String login, String password) {
/*        try {
            clientSocket.getOos().writeObject(ConstProtocol.checkPassword);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().writeObject(password);
            clientSocket.getOos().flush();
            return clientSocket.getOis().readBoolean();
        } catch (IOException e) {
            logger.error("Ошибка чтения из потока! " + e.toString());
        }*/
        return false;
    }

    private static HttpURLConnection getHttpURLConnection(ClientSocket clientSocket, String requestMethod, String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection con = null;
        con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(requestMethod);
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);
        return con;
    }

    private static String getJSONFromHttpURLConnection(HttpURLConnection con) {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getObjectFromJSON(String jsonString, Class aClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonString, aClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Object getObjectFromUrl(ClientSocket clientSocket, String requestMethod, String addUrl, Class aClass) {
        try {
            String param = "token=" + clientSocket.getToken();
            HttpURLConnection con = getHttpURLConnection(clientSocket, requestMethod, getBaseUrl(clientSocket) + addUrl + "?" + param);

            return getObjectFromJSON(getJSONFromHttpURLConnection(con), aClass);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Role getRole(ClientSocket clientSocket, String login) {
        return RoleDto.fromRoleDto((RoleDto) getObjectFromUrl(clientSocket, "GET", "/getRole/" + login, RoleDto.class));
    }

    public static void updateTrack(ClientSocket clientSocket, Track track, String name, String singer, String album, int lengthInt, String genre) {
/*        try {
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
        }*/
    }

    public static void addTrack(ClientSocket clientSocket, String name, String singer, String album, int lengthInt, String genre) {
/*        try {
            clientSocket.getOos().writeObject(ConstProtocol.add);
            clientSocket.getOos().writeObject(name);
            clientSocket.getOos().writeObject(singer);
            clientSocket.getOos().writeObject(album);
            clientSocket.getOos().writeObject(lengthInt);
            clientSocket.getOos().writeObject(genre);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток. " + e.toString());
        }*/
    }

    public static Object signUpUser(ClientSocket clientSocket, String login, String password) {
/*        try {
            clientSocket.getOos().writeObject(ConstProtocol.addUser);
            clientSocket.getOos().writeObject(login);
            clientSocket.getOos().writeObject(password);
            clientSocket.getOos().flush();
            return clientSocket.getOis().readObject();
        } catch (IOException e) {
            logger.error("Ошибка чтения из потока!. " + e.toString());
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка класс не найден!. " + e.toString());
        }*/
        return null;
    }

    public static void deleteTrack(ClientSocket clientSocket, Track track) {
        getObjectFromUrl(clientSocket, "GET", "/tracks/" + track.getId() + "/delete", Integer.class);
    }

    public static List<Track> getAllTrack(ClientSocket clientSocket, String name, String singer, String album, String genre) {
        String param = "token=" + clientSocket.getToken();
        TrackListDto tracks = (TrackListDto) getObjectFromUrl(clientSocket, "GET", "/tracks", TrackListDto.class);
        if (tracks != null) {
            return TrackListDto.getTrackList(tracks);
        } else {
            return new ArrayList<>();
        }
    }

    public static List<User> getAllUser(ClientSocket clientSocket) {

        String param = "token=" + clientSocket.getToken();

        UserDto[] userDtos = (UserDto[]) getObjectFromUrl(clientSocket, "GET", "/users", UserDto[].class);
        if (userDtos != null) {
            return Arrays.stream(userDtos).map(UserDto::fromUserDto).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }

    }

    public static void setRole(ClientSocket clientSocket, String name, Role role) {
/*        try {
            clientSocket.getOos().writeObject(ConstProtocol.setRole);
            clientSocket.getOos().writeObject(name);
            clientSocket.getOos().writeObject(role);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток. " + e.toString());
        }*/
    }

    public static void deleteUser(ClientSocket clientSocket, String name) {
/*        try {
            clientSocket.getOos().writeObject(ConstProtocol.deleteUser);
            clientSocket.getOos().writeObject(name);
            clientSocket.getOos().flush();
        } catch (IOException e) {
            logger.error("Ошибка записи в поток. " + e.toString());
        }*/
    }
}
