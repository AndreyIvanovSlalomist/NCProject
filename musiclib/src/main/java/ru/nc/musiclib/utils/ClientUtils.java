package ru.nc.musiclib.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.scene.control.Alert;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.nc.musiclib.model.Genre;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.Track;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.net.client.ClientSocket;
import ru.nc.musiclib.transfer.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.io.*;
import java.net.*;
import java.nio.charset.Charset;
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

    private static String getJsonFromObject(Object object){
        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = null;
        try {
            jsonInString = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonInString;
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
        track.setName(name);
        track.setSinger(singer);
        track.setAlbum(album);
        track.setLengthInt(lengthInt);
        track.getGenre().setGenreName(genre);
        String JSON = getJsonFromObject(TrackDto.from(track));
        String param = "token=" + clientSocket.getToken();
        HttpURLConnection con;
        try {
            con = getHttpURLConnection(clientSocket,"POST",getBaseUrl(clientSocket)+"/tracks/"+track.getId()+"/update?"+param);
            jsonToConnection(con,JSON);

            if (con.getResponseCode() != 200) {
                logger.error("Connection failed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addTrack(ClientSocket clientSocket, String name, String singer, String album, int lengthInt, String genre) {
        Track track = new Track(name,singer,album,lengthInt,new Genre(genre));
        String JSON = getJsonFromObject(TrackDto.from(track));
        String param = "token=" + clientSocket.getToken();
        HttpURLConnection con;
        try {
             con = getHttpURLConnection(clientSocket,"POST",getBaseUrl(clientSocket)+"/tracks/add?"+param);
             jsonToConnection(con,JSON);

            if (con.getResponseCode() != 200) {
                logger.error("Connection failed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Boolean signUpUser(ClientSocket clientSocket, String login, String password) {
        try {
            HttpURLConnection con = getHttpURLConnection(clientSocket, "POST", getBaseUrl(clientSocket) + "/signUp");
            jsonToConnection(con, "{\"userName\": \"" + login + "\", \"password\": \"" + password + "\"}");
            return (Boolean) getObjectFromJSON(getJSONFromHttpURLConnection(con), Boolean.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
        UserDto[] userDtos = (UserDto[]) getObjectFromUrl(clientSocket, "GET", "/users", UserDto[].class);
        if (userDtos != null) {
            return Arrays.stream(userDtos).map(UserDto::fromUserDto).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    public static void setRole(ClientSocket clientSocket, String name, Role role) {
        String JSON = getJsonFromObject(RoleDto.fromRole(role));
        String param = "token=" + clientSocket.getToken();
        HttpURLConnection con;
        try {
            con = getHttpURLConnection(clientSocket,"POST",getBaseUrl(clientSocket)+"/user/"+name+"/setRole?"+param);
            jsonToConnection(con,JSON);

            if (con.getResponseCode() != 200) {
                logger.error("Connection failed");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser(ClientSocket clientSocket, String name) {
        String param = "token=" + clientSocket.getToken();
        HttpURLConnection con;
        try {
            con = getHttpURLConnection(clientSocket,"POST",getBaseUrl(clientSocket)+"/user/"+name+"/delete?"+param);
            if (con.getResponseCode() != 200) {
                logger.error("Connection failed");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
