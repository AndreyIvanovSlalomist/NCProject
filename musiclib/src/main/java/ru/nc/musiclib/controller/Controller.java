package ru.nc.musiclib.controller;

import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.view.View;
import ru.nc.musiclib.model.Model;

import java.util.List;

/**
 * Интерфейс для контроллеров
 * Знает о модели
 * о въюшке знать неодязательно
 * через методы получает данные обробатывает и вызывает методы из Model
 */
public interface Controller {
    void setModel(Model model);

    void setView(View view);

    boolean isValidAdd(String name, String singer, String album, int length, String genreName);

    boolean isValidAddFromFile(String fileName);

    boolean isValidUpdate(Track track, int colNumber, String newValue);

    boolean isValidUpdate(Track track, String name, String singer, String album, int length, String genreName);

    boolean isValidSort(int numberField, boolean isRevers);

    boolean isValidDelete(int number);

    boolean isValidDelete(String name, String singer, String album, int length, String genreName);
}
