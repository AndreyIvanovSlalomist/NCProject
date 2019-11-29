package ru.nc.musiclib.view.impl;

import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.interfaces.Observable;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleView implements View, Observer {
    Model model = null;
    Controller controller = null;

    private void showTitle(int i) {
        if (i != 0) {
            System.out.printf("%-4s%-30s%-20s%-20s%-20s%-20s%n", "№", "Название", "Исполнитель", "Альбом", "Длина трека", "Жанр");
        } else {
            System.out.printf("%-30s%-20s%-20s%-20s%-20s%n", "Название", "Исполнитель", "Альбом", "Длина трека", "Жанр");
        }
    }

    private void showTrack(Track track, int i) {
        if (i != 0) {
            System.out.printf("%-4s", i);
        }
        System.out.printf("%-30s", track.getTrackName());
        System.out.printf("%-20s", track.getSinger());
        System.out.printf("%-20s", track.getAlbum());
        System.out.printf("%-20s", track.getTrackLength());
        System.out.printf("%-20s%n", track.getGenre().getGenreName());
    }

    public void sendEvent(String event) {
        switch (event) {
            case "Трек добавлен.": {
                System.out.println("Добавление успешно завершено ");
                break;
            }
            case "Трек удален.": {
                System.out.println("Удаление успешно завершено ");
                break;
            }
            case "Трек изменен.": {
                System.out.println("Изменение успешно завершено ");
                break;
            }
            case "Неверный формат длины трека": {
                System.out.println("Неверный формат длины трека");
                break;
            }
            case "Трек уже существует.": {
                System.out.println("Трек уже существует.");
                break;
            }
        }
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
            try {
                i = Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка. Введите целое число от " + min + " до " + max);
                continue;
            }

            if (i > max) {
                System.out.println("Ошибка. Число больше " + max);
            } else if (i < min) {
                System.out.println("Ошибка. Число меньше " + min);
            } else {
                return i;
            }
        }
    }

    private Double readDouble(int min, int max, String captionMessage) {
        Scanner scanner = new Scanner(System.in);

        Double i = 0.0;
        while (true) {
            System.out.println(captionMessage + " (0 - возврат)");
            try {
                i = Double.parseDouble(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка. Введите число от " + min + " до " + max);
                continue;
            }
            if (i == 0) {
                return 0.0;
            } else if (i > max) {
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
        System.out.println("Редактировать:");
        System.out.println("1 - Название");
        System.out.println("2 - Исполнителя");
        System.out.println("3 - Альбом");
        System.out.println("4 - Длинна трека(c)");
        System.out.println("5 - Жанр");
        System.out.println("0 - Выход");
    }

    private void runMainMenu() {
        while (true) {
            mainMenu();
            switch (readInteger(0, 4, "")) {
                case 0: {
                    System.exit(1);
                }
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
                default:
                    System.out.println("Ошибка.");
            }
        }
    }

    private void runDeleteMenu() {
        System.out.println("-- Удаление Трека --");
        while (true) {
            int i = 1;
            List<Track> trackList = model.getAll();
            if (trackList.size() == 0) {
                System.out.println("Нет треков");
                break;
            }
            showTitle(i);
            for (Track track : trackList) {
                showTrack(track, i);
                i++;
            }

            int r = readInteger(0, trackList.size(), "Введите номер Трека (0 - выход):");
            if (r == 0) {
                break;
            }
            if (r != 0) {
                if (controller.validDelete(r - 1)) {
                    break;
                }
            }

        }
    }


    private void runUpdateMenu() {
        System.out.println("-- Изменение Трека --");
        int i = 1;
        List<Track> trackList = model.getAll();
        if (trackList.size() == 0) {
            System.out.println("Нет треков");
        } else {

            showTitle(i);
            for (Track track : trackList) {
                showTrack(track, i);
                i++;
            }

            int r = readInteger(0, trackList.size(), "Введите номер Трека (0 - выход):");
            if (r != 0) {
                updateTrack(trackList.get(r - 1));
            }
        }
    }

    private void updateTrack(Track track) {
        System.out.println("-- Изменение Трека --");
        while (true) {
            updateMenu();
            showTitle(0);
            showTrack(track, 0);

            int i = readInteger(0, 5, "");
            if (i == 0) {
                break;
//            }
//            if (i == 4) {
//                Double d = readDouble(0, 300, "Введите новое значение в секундах");
//                if (d != 0) {
//                    if (controller.validUpdate(track, i, d)) {
//                        break;
//                    }
//                }
            } else {
                String s = readString(0, 30, "Введите новое значение:");
                if (s != "") {
                    if (controller.validUpdate(track, i, s)) {
                        break;
                    }
                }
            }
        }
    }

    private void showAllTrack() {
        System.out.println("-- Cписок треков -- ");
        showTitle(0);
        for (Track track : model.getAll()) {
            showTrack(track, 0);
        }
    }

    private void runAppendMenu() {
        List<Object> appendResult = appendMenu();
        if (appendResult != null) {
            if (appendResult.size() == 5) {
                controller.validAppend(appendResult.toArray());
            }
        }
    }

    private List<Object> appendMenu() {
        List<Object> objects = new ArrayList<>();
        System.out.println("-- Добавление Трека --");
        String s;
        s = readString(0, 30, "Введите название Трека:");
        if (s == "") {
            return null;
        }
        objects.add(s);

        s = readString(0, 20, "Введите Исполнителя:");
        if (s == "") {
            return null;
        }
        objects.add(s);

        s = readString(0, 20, "Введите Альбом:");
        if (s == "") {
            return null;
        }
        objects.add(s);

        s = readString(0, 20, "Введите Длину трека (мм:сс):");
        if (s == "") {
            return null;
        }
        objects.add(s);

        s = readString(0, 20, "Введите Жанр:");
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
        if (this.model instanceof Observable) {
            ((Observable) this.model).addObserver(this);
        }
    }

    @Override
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
