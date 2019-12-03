package ru.nc.musiclib.model.impl;

import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.exceptions.InvalidFieldValueException;
import ru.nc.musiclib.interfaces.Observable;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.model.Model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MusicModel implements Model, Observable {
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
            observer.sendEvent(message);
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
            notifyObservers("Ошибка при сохранении!");
        }
    }

    private List<Track> loadTrack(String fileName) {
        List<Track> trackList = new ArrayList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(fileName));
            trackList = (List<Track>) in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            notifyObservers("Ошибка при загрузке!");
        }
        return trackList;
    }

    @Override
    public boolean add(String name, String singer, String album, String length, String genreName, boolean isSendNotify) {

        if (findTrack(name, singer, album, length) == null) {
            Track track = new Track();
            track.setTrackName(name);
            track.setSinger(singer);
            track.setAlbum(album);
            try {
                track.setTrackLength(length);
            } catch (InvalidFieldValueException ex) {
                if (isSendNotify)
                    notifyObservers("Неверный формат длины трека");
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
            if (isSendNotify)
                notifyObservers("Трек уже существует.");
            return false;
        }
        if (isSendNotify)
            notifyObservers("Трек добавлен.");
        return true;
    }

    @Override
    public boolean addFromFile(String fileName) {
        List<Track> trackList = new ArrayList<>();
        trackList = loadTrack(fileName);

        for (Track track : trackList) {
            add(track.getTrackName(), track.getSinger(), track.getAlbum(), track.getTrackLength(), track.getGenre().getGenreName(), false);
        }
        notifyObservers("Загрузка завершена.");
        return true;
    }

    @Override
    public boolean update(Track track, int colNumber, String newValue) {
        switch (colNumber) {
            case 1: {
                track.setTrackName(newValue);
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
                    track.setTrackLength(newValue);
                    break;
                } catch (InvalidFieldValueException ex) {
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

        notifyObservers("Трек изменен.");
        return true;
    }

    @Override
    public boolean delete(int number) {
        tracks.remove(number);
        notifyObservers("Трек удален.");
        return true;
    }

    private Track findTrack(String name, String singer, String album, String length) {
        Track track = null;
        for (Track track1 : tracks) {
            if (track1.getTrackName().equals(name) &&
                    track1.getSinger().equals(singer) &&
                    track1.getAlbum().equals(album) &&
                    track1.getTrackLength().equals(length)) {
                track = track1;
                break;
            }
        }
        return track;
    }

    private Genre findGenre(String genreName) {
        for (Genre genre1 : genres)
            if (genre1.getGenreName().equals(genreName)) {
                return genre1;
            }
        return null;
    }
}
