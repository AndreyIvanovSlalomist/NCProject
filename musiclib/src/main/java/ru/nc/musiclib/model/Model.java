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

    boolean append(String name, String singer, String album, String length, String genreName);

    boolean update(Track track, int colNumber, String newValue);

    boolean delete(int number);

    void saveTrack();
}
