package ru.nc.musiclib.repositories;

import ru.nc.musiclib.model.Genre;
import ru.nc.musiclib.model.Track;

import java.util.Map;

public interface TrackDao extends CrudDao<Track> {
    Track findTrack(String name, String singer, String album, int length);
    Map<Integer, Genre> findAllGenre();
}
