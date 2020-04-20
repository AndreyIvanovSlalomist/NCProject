package ru.nc.musiclib.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nc.musiclib.model.Genre;
import ru.nc.musiclib.model.Track;
import ru.nc.musiclib.model.Tracks;
import ru.nc.musiclib.repositories.TrackDao;
import ru.nc.musiclib.services.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.nc.musiclib.utils.XMLUtils.loadFromXml;
import static ru.nc.musiclib.utils.XMLUtils.saveToXML;

@Service
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
            return trackDao.save(new Track(name, singer, album, length, new Genre(genreName)));
        }
        return false;
    }

    @Override
    public void addFromFile(String fileName) {
        List<Track> trackList = addFromXMLFile(fileName);
        if (trackList != null)
            for (Track track : trackList) {
                add(track.getName(), track.getSinger(), track.getAlbum(), track.getLengthInt(), track.getGenre().getGenreName(), false);
            }
    }

    @Override
    public void saveToFile(String fileName) {
        Tracks tracks = new Tracks();
        tracks.setTracks(trackDao.findAll());
        saveToXML(tracks, fileName, Tracks.class, Track.class, Genre.class);
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
        Track newTrack = trackDao.findTrack(name,singer,album,length);
        if (track == null||newTrack!=null)
            return false;
        track.setName(name);
        track.setSinger(singer);
        track.setAlbum(album);
        track.setLength(length);
        track.setGenre(new Genre(genreName));
        return trackDao.update(track);
    }

    @Override
    public List<Track> find(int colNumber, String findValue) {
        return null;
    }

    @Override
    public boolean delete(int number) {
        return trackDao.delete(number);
    }

    @Override
    public boolean delete(String name, String singer, String album, int length, String genreName) {
        return trackDao.delete(new Track(name, singer, album, length, new Genre(genreName)));
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
