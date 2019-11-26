package ru.nc.musiclib.model;

import ru.nc.musiclib.classes.Genre;
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

    public MusicModel() {}

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
    public void saveTrack(){
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tracks.txt"));
            out.writeObject(tracks);
            out.close();
        } catch (IOException e) {
            notifyObserver("Ошибка при сохранении!");
        }
    }

    private void loadTrack(){
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("tracks.txt"));
            tracks = (List<Track>)in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            notifyObserver("Ошибка при загрузке!");
        }
    }
    @Override
    public boolean append(Object... objects) {
        Genre genre = findGenre((String) objects[2]);
        if (genre == null) {
            genre = new Genre((String) objects[2]);
            genres.add(genre);
        }
        if (findTrack(objects) == null) {
            Track track = new Track((String) objects[0], (String) objects[1], genre);
            tracks.add(track);
        }else{
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
                switch ((Integer) objects[1]) {
                    case 1:
                        ((Track) objects[0]).setTrackName((String) objects[2]);
                        break;
                    case 2:
                        ((Track) objects[0]).setSinger((String) objects[2]);
                        break;
                    case 3:
                        Genre genre = findGenre((String) objects[2]);
                        if (genre == null) {
                            genre = new Genre((String) objects[2]);
                            genres.add(genre);
                        }
                        ((Track) objects[0]).setGenre(genre);
                        break;
                }
            }
        } else return false;
        return true;
    }

    @Override
    public boolean delete(int number) {
        tracks.remove(number);
        return true;
    }

    private Track findTrack(Object... objects) {
        Track track = null;
        for (Track track1 : tracks) {
            if (track1.getTrackName().equals(objects[0]) && track1.getSinger().equals(objects[1])) {
                track = track1;
                break;
            }
        }
        return track;
    }

    private Genre findGenre(String genreName) {
        Genre genre = null;
        for (Genre genre1 : genres) {
            if (genre1.getGenreName().equals(genreName)) {
                genre = genre1;
                break;
            }
        }
        return genre;
    }
}
