package ru.nc.musiclib.interfaces;

import ru.nc.musiclib.classes.Track;

import java.util.List;

/**Интерфейс для моделей
 * может не знать ни о въюшке ни о контроллере
 * имплиментирует Observable
 * имеет методы для добавления (вазможно по набору значений или по уже созданному объекту, но лучше первый вареант)
 *      для изменения (к примеру ID объекта, строковый идентификатор того какие данные меняем, и новое значение)
 *      для удаления (к примеру ID объекта)
 *
 *      для сериализации содержащихся в модели данных
 *
 *      для предоставления информации о сождержащихся данных (к примеру ID объекта или по всем объектам)
 *
 *
 *
 */
public interface Model {
    List<Track> getAll();
    boolean append(Object ... objects);
    boolean update(Object ... objects);
    boolean delete(int number);
    void saveTrack();
}
