package ru.nc.musiclib.db.dao;

import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.Track;

import java.util.Map;

public interface TrackDao extends CrudDao<Track> {
    Track findTrack(String name, String singer, String album, int length);
    Map<Integer, Genre> findAllGenre();
}
