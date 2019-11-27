package ru.nc.musiclib.controller;

/**
 * Класс контроллера
 * Для использование со стороны View используется методы валидации validAppend, validUpdate, validDelete
 */

import ru.nc.musiclib.interfaces.Controller;
import ru.nc.musiclib.interfaces.Model;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.interfaces.View;

public class MusicLibController implements Controller, Observer {
    Model model = null;
    View view = null;

    private void sendLog(String s) {
        // System.out.println(s);
    }

    ;

    @Override
    public void sendEvent(String event) {
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
    public boolean validAppend(Object... objects) {
        sendLog("Я контроллер. Получил данные на добавление ");
        if (model.append(objects)) {
            model.saveTrack();
            return true;
        }
        return false;
    }

    @Override
    public boolean validUpdate(Object... objects) {
        sendLog("Я контроллер. Получил данные на изминение ");
        if (model.update(objects)) {
            model.saveTrack();
            return true;
        }
        return false;
    }

    @Override
    public boolean validDelete(int n) {
        sendLog("Я контроллер. Получил данные на удаление ");
        if (model.delete(n)) {
            model.saveTrack();
            return true;
        }
        return false;
    }

}
