package ru.nc.musiclib.model;

import ru.nc.musiclib.classes.Track;

import java.util.List;

/**
 * Интерфейс для моделей
 * может не знать ни о въюшке ни о контроллере
 * имплиментирует Observable
 * имеет методы для добавления (вазможно по набору значений или по уже созданному объекту, но лучше первый вареант)
 * для изменения (к примеру ID объекта, строковый идентификатор того какие данные меняем, и новое значение)
 * для удаления (к примеру ID объекта)
 * <p>
 * для сериализации содержащихся в модели данных
 * <p>
 * для предоставления информации о сождержащихся данных (к примеру ID объекта или по всем объектам)
 */
public interface Model {
    List<Track> getAll();

    boolean add(String name, String singer, String album, int length, String genreName, boolean isSendNotif);

    boolean addFromFile(String fileName);

    boolean update(Track track, int colNumber, String newValue);

    boolean update(String name, String singer, String album, int length, String genreName, int colNumber, String newValue);


    List<Track> find(int colNumber, String findValue);

    boolean delete(int number);

    boolean delete(String name, String singer, String album, int length, String genreName);

    void saveTrack();

    void setSort(int numberField, boolean isRevers);
}
