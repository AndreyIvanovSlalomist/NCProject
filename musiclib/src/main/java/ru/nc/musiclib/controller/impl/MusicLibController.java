package ru.nc.musiclib.controller.impl;

/**
 * Класс контроллера
 * Для использование со стороны View используется методы валидации validAppend, validUpdate, validDelete
 */

import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.controller.Controller;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.model.Model;
import ru.nc.musiclib.view.View;

public class MusicLibController implements Controller, Observer {
    private Model model = null;
    private View view = null;

    private void sendLog(String s) {
        // System.out.println(s);
    }

    @Override
    public void update(String event) {
        sendLog("Я контроллер. Получил событие " + event);
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public void setView(View view) {
        this.view = view;
    }

    @Override
    public boolean isValidAdd(String name, String singer, String album, String length, String genreName) {
        sendLog("Я контроллер. Получил данные на добавление ");
        if (model.add(name, singer, album, length, genreName, true)) {
            model.saveTrack();
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidAddFromFile(String fileName) {
        model.addFromFile(fileName);
        model.saveTrack();
        return true;
    }

    @Override
    public boolean isValidUpdate(Track track, int colNumber, String newValue) {
        sendLog("Я контроллер. Получил данные на изминение ");
        if (model.update(track, colNumber, newValue)) {
            model.saveTrack();
            return true;
        }
        return false;
    }

    @Override
    public boolean isValidSort(int numberField, boolean isRevers) {
        sendLog("Я контроллер. Получил команду для сортировки ");
        model.setSort(numberField, isRevers);
        model.saveTrack();
        return true;
    }

    @Override
    public boolean isValidDelete(int n) {
        sendLog("Я контроллер. Получил данные на удаление ");
        if (model.delete(n)) {
            model.saveTrack();
            return true;
        }
        return false;
    }

}
