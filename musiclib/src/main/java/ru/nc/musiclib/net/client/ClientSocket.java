package ru.nc.musiclib.net.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.nc.musiclib.utils.MusicLibLogger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
@Data
@AllArgsConstructor
public class ClientSocket {
    private String host;
    private int port;
    private String token;
/*    private Socket socket = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;*/

    private final static MusicLibLogger logger = new MusicLibLogger(ClientSocket.class);

/*
    public ClientSocket(String host, int port) {
        this.host = host;
        this.port = port;
*/
/*        try {
            startSocket(InetAddress.getByName(host), port);
        } catch (UnknownHostException e) {
            logger.error("Ошибка: Неизвестен хост. " + e.toString());
        }*//*

    }
*/


/*    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }*/

/*    private void startSocket(InetAddress inetAddress, int port) {
        try{
            socket = new Socket(inetAddress, port);
        } catch (IOException e) {
            logger.error("Ошибка при открытии сокета на порту: " + port);
        }
        if(socket != null) {
            try {
                oos = new ObjectOutputStream(socket.getOutputStream());
                ois = new ObjectInputStream(socket.getInputStream());
            } catch (IOException e) {
                logger.error("Ошибка при открытии потока на запись/чтение. " + e.getMessage());
            }
        }
    }*/
}

