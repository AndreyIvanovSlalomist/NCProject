package ru.nc.musiclib.controller;

/**
 * Класс контроллера
 * Для использование со стороны View используется методы валидации validAppend, validUpdate, validDelete
 *
 *
 *
 *
 */

import ru.nc.musiclib.interfaces.Controller;
import ru.nc.musiclib.interfaces.Model;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.interfaces.View;

public class MusicLibController implements Controller, Observer {
    MusicLibController musicLibController = new MusicLibController();
    Model model = null;
    View view = null;


    @Override
    public void sendEvent(String event) {
        System.out.println("Я контроллер. Получил событие " + event);
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
        System.out.println("Я контроллер. Получил данные на добавление ");
        return false;
    }

    @Override
    public boolean validUpdate(Object... objects) {
        System.out.println("Я контроллер. Получил данные на изминение ");
        return false;
    }

    @Override
    public boolean validDelete(Object... objects) {
        System.out.println("Я контроллер. Получил данные на удаление ");
        return false;
    }

}
