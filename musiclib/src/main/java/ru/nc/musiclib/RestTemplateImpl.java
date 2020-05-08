package ru.nc.musiclib;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import ru.nc.musiclib.model.Track;
import ru.nc.musiclib.transfer.TokenDto;
import ru.nc.musiclib.transfer.TrackDto;
import ru.nc.musiclib.transfer.TrackListDto;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class RestTemplateImpl {
    public static void main(String[] args) throws IOException {
/*
        HttpHeaders headers = new HttpHeaders();
        byte[] encodedAuth = Base64.encodeBase64()
        headers.add("Authorization", "Basic QWxhZGRpbupvcRVuIHNlc2FtZQ=="); //here is some login and pass like this login:pass
        HttpEntity<String> request = new HttpEntity<String>(headers);
        Track track = restTemplate.exchange(url, HttpMethod.GET, request, Track.class).getBody();
*/

        String plainCreds = "admin:admin";
        String baseUrl = "http://localhost:8082/api";

      /*  URL url = new URL(baseUrl+"/login");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

        String jsonInputString = "{\"userName\": \"admin\", \"password\": \"admin\"}";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
        TokenDto tokenDto;
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            ObjectMapper objectMapper = new ObjectMapper();
            tokenDto = objectMapper.readValue(response.toString(), TokenDto.class);
            System.out.println(tokenDto.getValue());


        }*/

      String  token = "TVpEj0phB7";
        String param = "token=" + token;
        URL url = new URL(baseUrl+"/tracks"+"?"+param);
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type", "application/json; utf-8");
        con.setRequestProperty("Accept", "application/json");
        con.setDoOutput(true);

/*        String jsonInputString = "{\"userName\": \"admin\", \"password\": \"admin\"}";

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }*/
        TrackListDto  tracks;
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());
            ObjectMapper objectMapper = new ObjectMapper();
            tracks = objectMapper.readValue(response.toString(), TrackListDto.class);
            tracks.getTrackListDto().forEach(System.out::println);


        }


      //  String jsonInputString = "{\"userName\": \"admin\", \"password\": \"admin\"}";

/*        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }*/
/*
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());*/
/*            ObjectMapper objectMapper = new ObjectMapper();
            TokenDto tokenDto = objectMapper.readValue(response.toString(), TokenDto.class);
            System.out.println(tokenDto.getValue());*/


     //   }


/*        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> request = new HttpEntity<String>(headers);
        TrackListDto response = restTemplate.getForObject(url, TrackListDto.class);
       List<Track> tracks = TrackListDto.getTrackList(response);
        tracks.forEach(t -> System.out.println(t.getName()));*/

    }
}
