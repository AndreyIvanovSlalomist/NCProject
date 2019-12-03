package ru.nc.musiclib.interfaces;

/**
 * Хочеш получать оповещение? Буть подписчиком и подпишись
 */
public interface Observer {
    void update(String event);
}
