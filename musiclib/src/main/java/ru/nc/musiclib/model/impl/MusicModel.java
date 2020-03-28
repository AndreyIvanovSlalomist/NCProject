package ru.nc.musiclib.model.impl;

import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.classes.Tracks;
import ru.nc.musiclib.exceptions.InvalidFieldValueException;
import ru.nc.musiclib.interfaces.Observable;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.Model;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static ru.nc.musiclib.utils.StreamUtils.loadObjectFromFileInputStream;
import static ru.nc.musiclib.utils.XMLUtils.loadFromXml;
import static ru.nc.musiclib.utils.XMLUtils.saveToXML;

public class MusicModel implements Model, Observable {
    private static final String TRACKS_XML = "tracks.xml";
    private static final String TRACKS_TXT = "tracks.txt";
    private final static MusicLibLogger logger = new MusicLibLogger(MusicModel.class);
    private List<Observer> observers = new ArrayList<>();
    private Tracks tracks = new Tracks();
    private List<Genre> genres = new ArrayList<>();

    public MusicModel() {
        addFromFile(TRACKS_XML);
        for (Track track : tracks.getTracks()) {
            if (findGenre(track.getGenre().getGenreName()) == null) {
                genres.add(track.getGenre());
            }
        }
    }

    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    @Override
    public List<Track> getAll() {
        return tracks.getTracks();
    }

    @Override
    public void saveTrack() {
        saveToXML(tracks, TRACKS_XML, Tracks.class, Track.class, Genre.class);
    }

    @Override
    public void setSort(int numberField, boolean isRevers) {
        Comparator comparator;
        switch (numberField) {
            case 1:
            default:
                comparator = Comparator.comparing(Track::getName);
                break;
            case 2:
                comparator = Comparator.comparing(Track::getSinger);
                break;
            case 3:
                comparator = Comparator.comparing(Track::getAlbum);
                break;
            case 4:
                comparator = Comparator.comparing(Track::getLengthInt);
                break;
            case 5:
                comparator = Comparator.comparing(Track::getGenreName);
                break;
        }
        if (isRevers) {
            comparator = comparator.reversed();
        }
        tracks.getTracks().sort(comparator);
    }

    @Override
    public boolean add(String name, String singer, String album, int length, String genreName, boolean isSendNotify) {

        if (findTrack(name, singer, album, length) != null) {
            if (isSendNotify) {
                logger.error("Трек уже существует.");
                notifyObservers("Трек уже существует.");
            }
            return false;
        }
        Track track = new Track();
        track.setName(name);
        track.setSinger(singer);
        track.setAlbum(album);
        try {
            track.setLength(length);
        } catch (InvalidFieldValueException ex) {
            if (isSendNotify) {
                logger.error("Неверный формат длины трека");
                notifyObservers("Неверный формат длины трека");
            }
            return false;
        }
        Genre genre = findGenre(genreName);
        if (genre == null) {
            genre = new Genre(genreName);
            genres.add(genre);
        }
        track.setGenre(genre);
        tracks.getTracks().add(track);

        if (isSendNotify) {
            logger.info("Трек добавлен.");
            notifyObservers("Трек добавлен.");
        }
        return true;
    }

    @Override
    public boolean saveToFile(String fileName){
        return true;
    }

    @Override
    public boolean addFromFile(String fileName) {
        List<Track> trackList = addFromXMLFile(fileName);
        if (trackList != null)
            for (Track track : trackList) {
                add(track.getName(), track.getSinger(), track.getAlbum(), track.getLengthInt(), track.getGenre().getGenreName(), false);
            }
        logger.info("Загрузка завершена.");
        notifyObservers("Загрузка завершена.");
        return true;
    }

    private List<Track> addFromXMLFile(String fileName) {
        Object object = loadFromXml(fileName, Tracks.class, Track.class, Genre.class);
        if (object instanceof Tracks)
            return ((Tracks) object).getTracks();
        return null;
    }

    private List<Track> addFromSerializableFile(String fileName) {
        Object object = loadObjectFromFileInputStream(fileName);
        if (object instanceof Tracks)
            return ((Tracks) object).getTracks();
        return null;
    }

    @Override
    public boolean update(Track oldTrack, String name, String singer, String album, int length, String genreName) {
        Track track = findTrack(oldTrack.getName(), oldTrack.getSinger(), oldTrack.getAlbum(), oldTrack.getLengthInt());
        if (track == null)
            return false;
        track.setName(name);
        track.setSinger(singer);
        track.setAlbum(album);
        track.setLength(length);
        Genre genre = findGenre(genreName);
        if (genre == null) {
            genre = new Genre(genreName);
            genres.add(genre);
        }
        track.setGenre(genre);
        return true;
    }

    @Override
    public boolean update(Track track, int colNumber, String newValue) {
        switch (colNumber) {
            case 1: {
                track.setName(newValue);
                break;
            }
            case 2: {
                track.setSinger(newValue);
                break;
            }
            case 3: {
                track.setAlbum(newValue);
                break;
            }
            case 4: {
                try {
                    track.setLength(Integer.parseInt(newValue));
                    break;
                } catch (InvalidFieldValueException ex) {
                    logger.error("Неверный формат длины трека");
                    notifyObservers("Неверный формат длины трека");
                    return false;
                }
            }
            case 5: {
                Genre genre = findGenre(newValue);
                if (genre == null) {
                    genre = new Genre(newValue);
                    genres.add(genre);
                }
                track.setGenre(genre);
                break;
            }
        }
        logger.info("Трек изменен.");
        notifyObservers("Трек изменен.");
        return true;
    }

    @Override
    public List<Track> find(int colNumber, String findValue) {
        List<Track> trackList = new ArrayList<>();
        String curValue;
        findValue = replaceFindValue(findValue);
        if (colNumber > 0) {
            for (Track track : tracks.getTracks()) {
                switch (colNumber) {
                    case 1: {
                        curValue = track.getName();
                        break;
                    }
                    case 2: {
                        curValue = track.getSinger();
                        break;
                    }
                    case 3: {
                        curValue = track.getAlbum();
                        break;
                    }
                    case 4: {
                        curValue = track.getGenreName();
                        break;
                    }
                    default:
                        curValue = "";
                }
                if (curValue.matches(findValue)) {
                    trackList.add(track);
                }
            }
        }
        return trackList;
    }

    @Override
    public boolean delete(int number) {
        tracks.getTracks().remove(number);
        logger.info("Трек удален.");
        notifyObservers("Трек удален.");
        return true;
    }

    public boolean delete(String name, String singer, String album, int length, String genreName) {
        tracks.getTracks().remove(findTrack(name, singer, album, length));
        logger.info("Трек удален.");
        notifyObservers("Трек удален.");
        return true;
    }

    private Track findTrack(String name, String singer, String album, int length) {
        for (Track track : tracks.getTracks()) {
            if (track.getName().equals(name) &&
                    track.getSinger().equals(singer) &&
                    track.getAlbum().equals(album) &&
                    track.getLengthInt() == (length)) {
                return track;
            }
        }
        return null;
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
        for (Track track : tracks.getTracks()) {
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
        return null;
    }

    private Genre findGenre(String genreName) {
        for (Genre genre : genres)
            if (genre.getGenreName().equals(genreName)) {
                return genre;
            }
        return null;
    }
}
