package ru.nc.musiclib.controller;

import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.view.View;
import ru.nc.musiclib.model.Model;

/**
 * Интерфейс для контроллеров
 * Знает о модели
 * о въюшке знать неодязательно
 * через методы получает данные обробатывает и вызывает методы из Model
 */
public interface Controller {
    void setModel(Model model);

    void setView(View view);

    boolean isValidAdd(String name, String singer, String album, String length, String genreName);

    boolean isValidAddFromFile(String fileName);

    boolean isValidUpdate(Track track, int colNumber, String newValue);

    boolean isValidDelete(int number);
}
