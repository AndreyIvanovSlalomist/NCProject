package ru.nc.musiclib.interfaces;

/**
 * Дает возможность принимать подписчиков и оповещать их
 * Можно добавить разделение по видам оповещений,
 * но сейчас нам это врятли понадобится
 */
public interface Observable {
    void addObserver(Observer o);

    void removeObserver(Observer o);

    void notifyObservers(String message);
}
