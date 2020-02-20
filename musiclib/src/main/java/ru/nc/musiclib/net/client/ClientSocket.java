package ru.nc.musiclib.net.client;

import ru.nc.musiclib.logger.MusicLibLogger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocket {
    private Socket socket = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    private final static MusicLibLogger logger = new MusicLibLogger(ClientSocket.class);

    public ClientSocket(InetAddress inetAddress, int port) {
        startSocket(inetAddress, port);
    }


    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    private void startSocket(InetAddress inetAddress, int port) {
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
    }
}

