package ru.nc.musiclib.view.impl;

import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.interfaces.Observable;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsoleView implements View, Observer {
    private Model model = null;
    private Controller controller = null;

    private void showTitle(int number) {
        if (number != 0) {
            System.out.printf("%-4s%-30s%-20s%-20s%-20s%-20s%n", "№", "Название", "Исполнитель", "Альбом", "Длина трека", "Жанр");
        } else {
            System.out.printf("%-30s%-20s%-20s%-20s%-20s%n", "Название", "Исполнитель", "Альбом", "Длина трека", "Жанр");
        }
    }

    private void showTrack(Track track, int number) {
        if (number != 0) {
            System.out.printf("%-4s", number);
        }
        System.out.printf("%-30s", track.getName());
        System.out.printf("%-20s", track.getSinger());
        System.out.printf("%-20s", track.getAlbum());
        System.out.printf("%-20s", track.getLength());
        System.out.printf("%-20s%n", track.getGenre().getGenreName());
    }

    public void update(String event) {
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
        String inputString;
        while (true) {
            System.out.println(captionMessage + " (exit - возврат)");
            inputString = scanner.nextLine();
            if (inputString.equals("exit")) {
                return "";
            }

            if (inputString.length() > maxLength) {
                System.out.println("Ошибка. Длина строки больше " + maxLength);
            } else if (inputString.length() < minLength) {
                System.out.println("Ошибка. Длина строки меньше " + minLength);
            } else {
                return inputString;
            }
        }
    }

    private int readInteger(int min, int max, String captionMessage) {
        Scanner scanner = new Scanner(System.in);

        int inputInt;
        while (true) {
            System.out.println(captionMessage);
            try {
                inputInt = Integer.parseInt(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка. Введите целое число от " + min + " до " + max);
                continue;
            }

            if (inputInt > max) {
                System.out.println("Ошибка. Число больше " + max);
            } else if (inputInt < min) {
                System.out.println("Ошибка. Число меньше " + min);
            } else {
                return inputInt;
            }
        }
    }

    private double readDouble(int min, int max, String captionMessage) {
        Scanner scanner = new Scanner(System.in);

        double inputDouble;
        while (true) {
            System.out.println(captionMessage + " (0 - возврат)");
            try {
                inputDouble = Double.parseDouble(scanner.next());
            } catch (NumberFormatException e) {
                System.out.println("Ошибка. Введите число от " + min + " до " + max);
                continue;
            }
            if (inputDouble == 0) {
                return 0.0;
            } else if (inputDouble > max) {
                System.out.println("Ошибка. Число больше " + max);
            } else if (inputDouble < min) {
                System.out.println("Ошибка. Число меньше " + min);
            } else {
                return inputDouble;
            }
        }
    }

    private void mainMenu() {
        System.out.println("-- Добро пожаловать в музыкальную библиотеку --");
        System.out.println("Основное меню:");
        System.out.println("1 - Просмотр Треков");
        System.out.println("2 - Добавить Трек");
        System.out.println("3 - Изменить Трек");
        System.out.println("4 - Удалить Трек");
        System.out.println("5 - Загрузить терки из файла");
        System.out.println("6 - Сортировка");
        System.out.println("7 - Поиск");
        System.out.println("0 - Выход");
    }

    private void updateMenu() {
        System.out.println("Редактировать:");
        System.out.println("1 - Название");
        System.out.println("2 - Исполнителя");
        System.out.println("3 - Альбом");
        System.out.println("4 - Длина трека");
        System.out.println("5 - Жанр");
        System.out.println("0 - Выход");
    }

    private void runMainMenu() {
        while (true) {
            mainMenu();
            switch (readInteger(0, 7, "")) {
                case 0: {
                    System.exit(1);
                }
                case 1: {
                    showAllTrack();
                    break;
                }
                case 2: {
                    runAddMenu();
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
                case 5: {
                    runAddFromFileMenu();
                    break;
                }
                case 6: {
                    runSortMenu();
                    break;
                }
                case 7: {
                    runFindMenu();
                    break;
                }
                default:
                    System.out.println("Ошибка.");
            }
        }
    }

    private void runFindMenu() {
        System.out.println("-- Поиск --");
        while (true) {
            findMenu();

            int inputInt = readInteger(0, 4, "");
            if (inputInt == 0) {
                break;
            } else {
                String findValue = readString(0, 30, "Введите значение для поиска :");
                if (findValue.equals("")) {
                    break;
                } else {
                    List<Track> trackList = model.find(inputInt, findValue);
                    if (trackList.size() == 0) {
                        System.out.println("Ничего не найдено");
                    } else {

                        showTitle(0);
                        for (Track track : trackList) {
                            showTrack(track, 0);
                        }
                    }
                }
            }
        }
    }

    private void findMenu() {
        System.out.println("По какому полю искать?:");
        System.out.println("1 - Название");
        System.out.println("2 - Исполнителя");
        System.out.println("3 - Альбом");
     //   System.out.println("4 - Длина трека");
        System.out.println("4 - Жанр");
        System.out.println("0 - Выход");
    }

    private void runSortMenu() {

        System.out.println("-- Сортировка --");
        while (true) {
            sortMenu();
            int inputInt = readInteger(0, 5, "");
            if (inputInt == 0) {
                break;
            } else {
                sortMenu2();
                int inputInt2 = readInteger(0, 2, "");
                if (inputInt2 == 0) {
                    break;
                } else {
                    if (controller.isValidSort(inputInt, inputInt2 == 2)) {
                        break;
                    }
                }
            }
        }
    }


    private void sortMenu() {
        System.out.println("По какому полю сортировать?:");
        System.out.println("1 - Название");
        System.out.println("2 - Исполнитель");
        System.out.println("3 - Альбом");
        System.out.println("4 - Длина трека");
        System.out.println("5 - Жанр");
        System.out.println("0 - Выход");
    }

    private void sortMenu2() {
        System.out.println("Как сортировать?:");
        System.out.println("1 - По возрастанию");
        System.out.println("2 - По убыванию");
        System.out.println("0 - Выход");
    }

    private void runAddFromFileMenu() {
        String fileName = addFromFileMenu();
        if (fileName != null) {
            controller.isValidAddFromFile(fileName);
        }
    }

    private String addFromFileMenu() {
        String fileName;
        while (true) {
            System.out.println("-- Загрузить терки из файла --");
            fileName = readString(0, 30, "Введите полный путь к файлу");
            if (fileName.equals("")) {
                return null;
            }
            if ((new File(fileName)).exists()) {
                return fileName;
            } else {
                System.out.println("Файл не существует");
            }
        }
    }

    private void runDeleteMenu() {
        System.out.println("-- Удаление Трека --");
        while (true) {
            int number = 1;
            List<Track> trackList = model.getAll();
            if (trackList.size() == 0) {
                System.out.println("Нет треков");
                break;
            }
            showTitle(number);
            for (Track track : trackList) {
                showTrack(track, number);
                number++;
            }

            int inputInt = readInteger(0, trackList.size(), "Введите номер Трека (0 - выход):");
            if (inputInt == 0) {
                break;
            } else {
                if (controller.isValidDelete(inputInt - 1)) {
                    break;
                }
            }

        }
    }


    private void runUpdateMenu() {
        System.out.println("-- Изменение Трека --");
        int number = 1;
        List<Track> trackList = model.getAll();
        if (trackList.size() == 0) {
            System.out.println("Нет треков");
        } else {

            showTitle(number);
            for (Track track : trackList) {
                showTrack(track, number);
                number++;
            }

            int inputInt = readInteger(0, trackList.size(), "Введите номер Трека (0 - выход):");
            if (inputInt != 0) {
                updateTrack(trackList.get(inputInt - 1));
            }
        }
    }

    private void updateTrack(Track track) {
        System.out.println("-- Изменение Трека --");
        while (true) {
            updateMenu();
            showTitle(0);
            showTrack(track, 0);

            int inputInt = readInteger(0, 5, "");
            if (inputInt == 0) {
                break;

            }
            if (inputInt == 4) {
                Integer newValueInt = readInteger(0, Integer.MAX_VALUE, "Введите новое значение в секундах");
                if (newValueInt != 0) {
                    if (controller.isValidUpdate(track, inputInt, newValueInt.toString())) {
                        break;
                    }
                }

            } else {
                String newValue = readString(0, 30, "Введите новое значение:");
                if (!newValue.equals("")) {
                    if (controller.isValidUpdate(track, inputInt, newValue)) {
                        break;
                    }
                }
            }
        }
    }

    private void showAllTrack() {
        System.out.println("-- Список треков -- ");
        showTitle(0);
        for (Track track : model.getAll()) {
            showTrack(track, 0);
        }
    }

    private void runAddMenu() {
        List<String> addResult = addMenu();
        if (addResult != null) {
            if (addResult.size() == 5) {
                controller.isValidAdd(addResult.get(0), addResult.get(1), addResult.get(2), Integer.parseInt(addResult.get(3)), addResult.get(4));
            }
        }
    }

    private List<String> addMenu() {
        List<String> objects = new ArrayList<>();
        System.out.println("-- Добавление Трека --");
        String inputValue;
        inputValue = readString(0, 30, "Введите название Трека:");
        if (inputValue.equals("")) {
            return null;
        }
        objects.add(inputValue);

        inputValue = readString(0, 20, "Введите Исполнителя:");
        if (inputValue.equals("")) {
            return null;
        }
        objects.add(inputValue);

        inputValue = readString(0, 20, "Введите Альбом:");
        if (inputValue.equals("")) {
            return null;
        }
        objects.add(inputValue);

        int inputValueInt = readInteger(0, Integer.MAX_VALUE, "Введите Длину трека (в секундах):");
        if (inputValueInt == 0) {
            return null;
        }
        objects.add(inputValue);

        inputValue = readString(0, 20, "Введите Жанр:");
        if (inputValue.equals("")) {
            return null;
        }
        objects.add(inputValue);
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
