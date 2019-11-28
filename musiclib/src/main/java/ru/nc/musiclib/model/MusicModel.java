package ru.nc.musiclib.model;

import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.InvalidFieldValueException;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.interfaces.Model;
import ru.nc.musiclib.interfaces.Observable;
import ru.nc.musiclib.interfaces.Observer;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MusicModel implements Model, Observable {
    List<Observer> observers = new ArrayList<>();
    List<Track> tracks = new ArrayList<>();
    List<Genre> genres = new ArrayList<>();

    public MusicModel() {
        loadTrack();
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
    public void notifyObserver(String message) {
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
            notifyObserver("Ошибка при сохранении!");
        }
    }

    private void loadTrack() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("tracks.txt"));
            tracks = (List<Track>) in.readObject();
            in.close();
            for (Track track : tracks) {
                if (findGenre(track.getGenre().getGenreName()) == null) {
                    genres.add(track.getGenre());
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            notifyObserver("Ошибка при загрузке!");
        }
    }

    @Override
    public boolean append(Object... objects) {
        Genre genre = findGenre((String) objects[4]);
        if (genre == null) {
            genre = new Genre((String) objects[4]);
            genres.add(genre);
        }
        if (findTrack(genre, objects) == null) {
            //Track track = new Track((String) objects[0], (String) objects[1], (String) objects[2], (String) objects[3], genre);
            Track track = new Track();
            track.setTrackName((String)objects[0]);
            track.setSinger((String)objects[1]);
            track.setAlbum((String)objects[2]);
            track.setTrackLength((String)objects[3]);
            track.setGenre(genre);
            tracks.add(track);
        } else {
            notifyObserver("Трек уже существует.");
            return false;
        }
        notifyObserver("Трек добавлен.");
        return true;
    }

    @Override
    public boolean update(Object... objects) {
        if (objects.length == 3) {
            if (objects[0] instanceof Track) {
                try{
                    switch ((Integer) objects[1]) {
                        case 1: {
                            ((Track) objects[0]).setTrackName((String) objects[2]);
                            break;
                        }
                        case 2: {
                            ((Track) objects[0]).setSinger((String) objects[2]);
                            break;
                        }
                        case 3: {
                            ((Track) objects[0]).setAlbum((String) objects[2]);
                            break;
                        }
                        case 4: {
                            ((Track) objects[0]).setTrackLength((String) objects[2]);
                            break;
                        }
                        case 5: {
                            Genre genre = findGenre((String) objects[2]);
                            if (genre == null) {
                                genre = new Genre((String) objects[2]);
                                genres.add(genre);
                            }
                            ((Track) objects[0]).setGenre(genre);
                            break;
                        }
                    }
                }catch(InvalidFieldValueException ex){
                    System.out.println(ex.getMessage());
                    return false;
                }
            }
        } else return false;
        notifyObserver("Трек изменен.");
        return true;
    }

    @Override
    public boolean delete(int number) {
        tracks.remove(number);
        notifyObserver("Трек удален.");
        return true;
    }

    private Track findTrack(Genre genre, Object... objects) {
        Track track = null;
        for (Track track1 : tracks) {
            if (track1.getTrackName().equals(objects[0]) &&
                    track1.getSinger().equals(objects[1]) &&
                    track1.getAlbum().equals(objects[2]) &&
                    track1.getTrackLength().equals(objects[3]) &&
                    track1.getGenre().getGenreName().equals(genre.getGenreName())) {
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
