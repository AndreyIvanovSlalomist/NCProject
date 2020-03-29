package ru.nc.musiclib.model.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.classes.Tracks;
import ru.nc.musiclib.db.dao.TrackDao;
import ru.nc.musiclib.model.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.nc.musiclib.utils.XMLUtils.loadFromXml;
import static ru.nc.musiclib.utils.XMLUtils.saveToXML;
@Component
public class MusicModelWithDao implements Model {
    @Autowired
    private TrackDao trackDao;

    @Override
    public List<Track> getAll() {
        return trackDao.findAll();
    }

    @Override
    public boolean add(String name, String singer, String album, int length, String genreName, boolean isSendNotif) {
        if (trackDao.findTrack(name, singer, album, length) == null) {
            trackDao.save(new Track(name, singer, album, length, new Genre(genreName)));
            return true;
        }
        return false;
    }

    @Override
    public boolean addFromFile(String fileName) {
        List<Track> trackList = addFromXMLFile(fileName);
        if (trackList != null)
            for (Track track : trackList) {
                add(track.getName(), track.getSinger(), track.getAlbum(), track.getLengthInt(), track.getGenre().getGenreName(), false);
            }
        return true;
    }

    @Override
    public boolean saveToFile(String fileName) {
        Tracks tracks = new Tracks();
        tracks.setTracks(trackDao.findAll());
        saveToXML(tracks, fileName, Tracks.class, Track.class, Genre.class);
        return false;
    }

    private List<Track> addFromXMLFile(String fileName) {
        Object object = loadFromXml(fileName, Tracks.class, Track.class, Genre.class);
        if (object instanceof Tracks)
            return ((Tracks) object).getTracks();
        return null;
    }

    @Override
    public boolean update(Track track, int colNumber, String newValue) {
        return false;
    }

    @Override
    public boolean update(Track oldTrack, String name, String singer, String album, int length, String genreName) {
        Track track = trackDao.findTrack(oldTrack.getName(), oldTrack.getSinger(), oldTrack.getAlbum(), oldTrack.getLengthInt());
        if (track == null)
            return false;
        track.setName(name);
        track.setSinger(singer);
        track.setAlbum(album);
        track.setLength(length);
        track.setGenre(new Genre(genreName));
        trackDao.update(track);
        return true;
    }

    @Override
    public List<Track> find(int colNumber, String findValue) {
        return null;
    }

    @Override
    public boolean delete(int number) {
        trackDao.delete(number);
        return true;
    }

    @Override
    public boolean delete(String name, String singer, String album, int length, String genreName) {
        trackDao.delete(new Track(name, singer, album, length, new Genre(genreName)));
        return true;
    }

    @Override
    public void saveTrack() {

    }

    @Override
    public void setSort(int numberField, boolean isRevers) {

    }

    private String replaceFindValue(String findValue) {
        if (findValue.isEmpty())
            return findValue;
        findValue = findValue.replaceAll("\\*", ".*");
        findValue = findValue.replaceAll("\\?", ".?");
        findValue = "^" + findValue + "$";
        return findValue.toUpperCase();
    }

    @Override
    public List<Track> filter(String name, String singer, String album, String genreName) {
        List<Track> trackList = new ArrayList<>();
        for (Track track : trackDao.findAll()) {
            if ((name.isEmpty() || track.getName().toUpperCase().matches(replaceFindValue(name))) &&
                    (singer.isEmpty() || track.getSinger().toUpperCase().matches(replaceFindValue(singer))) &&
                    (album.isEmpty() || track.getAlbum().toUpperCase().matches(replaceFindValue(album))) &&
                    (genreName.isEmpty() || track.getGenreName().toUpperCase().matches(replaceFindValue(genreName)))) {
                trackList.add(track);
            }
        }
        return trackList;
    }

    @Override
    public Optional<Track> find(Integer id) {
        return trackDao.find(id);
    }
}
