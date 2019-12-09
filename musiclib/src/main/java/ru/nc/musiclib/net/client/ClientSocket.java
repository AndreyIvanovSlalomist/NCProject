package ru.nc.musiclib.net.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocket {
    private Socket socket = null;
    private ObjectOutputStream oos = null;
    private ObjectInputStream ois = null;

    public ClientSocket(InetAddress inetAddress, int porn) {
        startSocket(inetAddress, porn);
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

        try {
            socket = new Socket(inetAddress, port);
        } catch (IOException e) {
            System.out.println("Ошибка при открытии сокена на порту " + port);
        }
        try {
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Ошибка при открытии потока на запись ");
        }
        try {
            ois = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.out.println("Ошибка при открытии потока на чтение ");
        }

    }
}

