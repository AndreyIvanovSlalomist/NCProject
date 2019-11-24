package ru.nc.musiclib.view;

import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.interfaces.Controller;
import ru.nc.musiclib.interfaces.Model;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.interfaces.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleView implements View, Observer {
    Model model = null;
    Controller controller = null;

    public void sendEvent(String event) {
        System.out.println("Я въюшка. Получил оповещение через подписку ");
    }

    private String readString(int minLength, int maxLength, String captionMessage) {
        Scanner scanner = new Scanner(System.in);
        String s = "";
        while (true) {
            System.out.println(captionMessage + " (exit - возврат)");
            s = scanner.nextLine();
            if (s.equals("exit")) {
                return "";
            }

            if (s.length() > maxLength) {
                System.out.println("Ошибка. Длинна строки больше " + maxLength);
            } else if (s.length() < minLength) {
                System.out.println("Ошибка. Длинна строки меньше " + minLength);
            } else {
                return s;
            }
        }
    }

    private int readInteger(int min, int max, String captionMessage) {
        Scanner scanner = new Scanner(System.in);

        int i;
        while (true) {
            System.out.println(captionMessage);
            i = scanner.nextInt();

            if (i > max) {
                System.out.println("Ошибка. Число больше " + max);
            } else if (i < min) {
                System.out.println("Ошибка. Число меньше " + min);
            } else {
                return i;
            }
        }
    }

    private void mainMenu() {
        System.out.println("-- Добро пожадовать в музыкальную библиотеку --");
        System.out.println("Основное меню:");
        System.out.println("1 - Просмотр Треков");
        System.out.println("2 - Добавить Трек");
        System.out.println("3 - Изменить Трек");
        System.out.println("4 - Удалить Трек");
        System.out.println("0 - Выход");
    }

    private void updateMenu() {
        System.out.println("Выберите что менять:");
        System.out.println("1 - Название");
        System.out.println("2 - Исполнителя");
        System.out.println("3 - Жанр");
        System.out.println("0 - Выход");
    }

    private void runMainMenu() {
        while (true) {
            mainMenu();
            int i = readInteger(0, 4, "");
            if (i == 0) {
                break;
            }
            switch (i) {
                case 1: {
                    showAllTrack();
                    break;
                }
                case 2: {
                    runAppendMenu();
                    break;
                }
                case 3: {
                    runUpdateMenu();
                    break;
                }
                case 4: {
                    runDeleteMenu();
                    break;
                }
            }
        }
    }

    private void runDeleteMenu() {
        System.out.println("-- Удаление Трека --");
        while (true) {
            int i = 1;
            List<Track> trackList = model.getAll();
            for (Track track : trackList) {
                System.out.print(i++);
                System.out.print(track.getTrackName());
                System.out.print(track.getSinger());
                System.out.println(track.getGenre().getGenreName());
            }

            int r = readInteger(0, trackList.size(), "Введите номер Трека (0 - выход)");
            if (r == 0){
                break;
            }
            if (r != 0) {
                if (controller.validDelete(r - 1)){
                    break;
                }
            }
        }
    }

    private void runUpdateMenu() {
        System.out.println("-- Изменение Трека --");
        int i = 1;
        List<Track> trackList = model.getAll();
        for (Track track : trackList) {
            System.out.print(i++);
            System.out.print(track.getTrackName());
            System.out.print(track.getSinger());
            System.out.println(track.getGenre().getGenreName());
        }

        int r = readInteger(0, trackList.size(), "Введите номер Трека (0 - выход)");
        if (r != 0) {
            updateTrack(trackList.get(r - 1));

        }
    }

    private void updateTrack(Track track) {
        System.out.println("-- Изменение Трека --");
        while (true) {
            updateMenu();
            System.out.print(track.getTrackName());
            System.out.print(track.getSinger());
            System.out.println(track.getGenre().getGenreName());


            int i = readInteger(0, 3, "");
            if (i == 0) {
                break;
            }
            String s = readString(0, 20, "Введите новое значение");
            if (s != "") {

                if (controller.validUpdate(track, i, s)) {
                    break;
                }
            }


        }
    }

    private void showAllTrack() {
        System.out.println("-- Показываю все треки -- ");
        for (Track track : model.getAll()) {
            System.out.print(track.getTrackName());
            System.out.print(track.getSinger());
            System.out.println(track.getGenre().getGenreName());
        }
    }

    private void runAppendMenu() {
        List<Object> appendResult = appendMenu();
        if (appendResult != null) {
            if (appendResult.size() == 3) {
                controller.validAppend(appendResult.toArray());
            }

        }
    }

    private List<Object> appendMenu() {
        List<Object> objects = new ArrayList<>();
        System.out.println("-- Добавление Трека --");
        String s;
        s = readString(0, 20, "Введите название Трека");
        if (s == "") {
            return null;
        }
        objects.add(s);

        s = readString(0, 20, "Введите Исполнителя");
        if (s == "") {
            return null;
        }
        objects.add(s);

        s = readString(0, 20, "Введите Жанр");
        if (s == "") {
            return null;
        }
        objects.add(s);
        return objects;
    }

    @Override
    public void runView() {
        runMainMenu();
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
