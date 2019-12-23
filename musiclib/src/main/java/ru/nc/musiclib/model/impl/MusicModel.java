package ru.nc.musiclib.model.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.exceptions.InvalidFieldValueException;
import ru.nc.musiclib.interfaces.Observable;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.model.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicModel implements Model, Observable {
    private final static Logger logger = LogManager.getLogger(MusicModel.class);
    List<Observer> observers = new ArrayList<>();
    List<Track> tracks = new ArrayList<>();
    List<Genre> genres = new ArrayList<>();

    public MusicModel() {
        tracks = loadTrack("tracks.txt");
        for (Track track : tracks) {
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
        return tracks;
    }

    @Override
    public void saveTrack() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tracks.txt"));
            out.writeObject(tracks);
            out.close();
        } catch (IOException e) {
            logger.error("Ошибка при сохранении!");
            notifyObservers("Ошибка при сохранении!");
        }
    }

    @Override
    public void setSort(int numberField, boolean isRevers) {
        switch (numberField) {
            case 1:
                if (isRevers) {
                    tracks.sort(Comparator.comparing(Track::getName).reversed());
                } else {
                    tracks.sort(Comparator.comparing(Track::getName));
                }
                break;
            case 2:
                if (isRevers) {
                    tracks.sort(Comparator.comparing(Track::getSinger).reversed());
                } else {
                    tracks.sort(Comparator.comparing(Track::getSinger));
                }
                break;
            case 3:
                if (isRevers) {
                    tracks.sort(Comparator.comparing(Track::getAlbum).reversed());
                } else {
                    tracks.sort(Comparator.comparing(Track::getAlbum));
                }
                break;
            case 4:
                if (isRevers) {
                    tracks.sort(Comparator.comparing(Track::getLengthInt).reversed());
                } else {
                    tracks.sort(Comparator.comparing(Track::getLengthInt));
                }
                break;
            case 5:
                if (isRevers) {
                    tracks.sort(Comparator.comparing(Track::getGenreName).reversed());
                } else {
                    tracks.sort(Comparator.comparing(Track::getGenreName));
                }
                break;
        }
    }

    private List<Track> loadTrack(String fileName) {
        List<Track> trackList = new ArrayList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            trackList = (List<Track>) in.readObject();
            in.close();
        } catch (IOException e) {
            logger.error("Ошибка ввода/вывода при загрузке!");
            notifyObservers("Ошибка ввода/вывода при загрузке!");
        } catch (ClassNotFoundException e) {
            logger.error("Ошибка при загрузке, класс не найден!");
            notifyObservers("Ошибка при загрузке, класс не найден!");
        }
        return trackList;
    }

    @Override
    public boolean add(String name, String singer, String album, int length, String genreName, boolean isSendNotify) {

        if (findTrack(name, singer, album, length) == null) {
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
            tracks.add(track);
        } else {
            if (isSendNotify) {
                logger.error("Трек уже существует.");
                notifyObservers("Трек уже существует.");
            }
            return false;
        }
        if (isSendNotify) {
            logger.info("Трек добавлен.");
            notifyObservers("Трек добавлен.");
        }
        return true;
    }

    @Override
    public boolean addFromFile(String fileName) {
        List<Track> trackList;
        trackList = loadTrack(fileName);

        for (Track track : trackList) {
            add(track.getName(), track.getSinger(), track.getAlbum(), track.getLengthInt(), track.getGenre().getGenreName(), false);
        }
        logger.info("Загрузка завершена.");
        notifyObservers("Загрузка завершена.");
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
        findValue = findValue.replaceAll("\\*", ".*");
        findValue = findValue.replaceAll("\\?", ".?");

        if (colNumber > 0) {
            for (Track track : tracks) {
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
                  /*  case 4: {
                        curValue = track.getLength();
                        break;
                    }*/
                    case 4: {
                        curValue = track.getGenreName();
                        break;
                    }
                    default:
                        curValue = "";
                }
                Pattern pattern = Pattern.compile(findValue);
                Matcher matcher = pattern.matcher(curValue);
                if (matcher.find()) {
                    trackList.add(track);
                }
            }
        }


        return trackList;
    }

    @Override
    public boolean delete(int number) {
        tracks.remove(number);
        logger.info("Трек удален.");
        notifyObservers("Трек удален.");
        return true;
    }

    private Track findTrack(String name, String singer, String album, int length) {
        for (Track track : tracks) {
            if (track.getName().equals(name) &&
                    track.getSinger().equals(singer) &&
                    track.getAlbum().equals(album) &&
                    track.getLengthInt() == (length)) {
                return track;
            }
        }
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
