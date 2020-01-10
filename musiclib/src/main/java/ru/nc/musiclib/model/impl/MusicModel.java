package ru.nc.musiclib.model.impl;

import ru.nc.musiclib.classes.Genre;
import ru.nc.musiclib.classes.Track;
import ru.nc.musiclib.exceptions.InvalidFieldValueException;
import ru.nc.musiclib.interfaces.Observable;
import ru.nc.musiclib.interfaces.Observer;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.Model;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MusicModel implements Model, Observable {
    private final static MusicLibLogger logger = new MusicLibLogger(MusicModel.class);
    List<Observer> observers = new ArrayList<>();
    //List<Track> tracks = new ArrayList<>();
    Tracks tracks = new Tracks();
    List<Genre> genres = new ArrayList<>();

    public MusicModel() {
        addFromFile("tracks.xml");
        //tracks.setTracks(loadTrack("tracks.txt"));
        for (Track track : tracks.getTracks()) {
            if (findGenre(track.getGenre().getGenreName()) == null) {
                genres.add(track.getGenre());
            }
        }
    }

    private static void saveToXML(Tracks tracks) {

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Tracks.class, Track.class, Genre.class);
        } catch (JAXBException e) {
            logger.error("newInstance Exception");
            return;
        }
        Marshaller jaxbMarshaller = null;
        try {
            jaxbMarshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            logger.error("createMarshaller Exception");
            return;
        }

        try {
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (PropertyException e) {
            logger.error("setProperty Exception");
            return;
        }

        try {
            jaxbMarshaller.marshal(tracks, new File("tracks.xml"));
        } catch (JAXBException e) {
            logger.error("marshal Exception");
            return;
        }

    }

    @Override
    public FileInputStream getFIle(String fileName) {
        try {
            return new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            logger.error("Ошибка при чтении из файла");
        }
        return null;
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
        saveToXML(tracks);
    }

    public void saveToSerializable() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("tracks.txt"));
            out.writeObject(tracks.getTracks());
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
                    tracks.getTracks().sort(Comparator.comparing(Track::getName).reversed());
                } else {
                    tracks.getTracks().sort(Comparator.comparing(Track::getName));
                }
                break;
            case 2:
                if (isRevers) {
                    tracks.getTracks().sort(Comparator.comparing(Track::getSinger).reversed());
                } else {
                    tracks.getTracks().sort(Comparator.comparing(Track::getSinger));
                }
                break;
            case 3:
                if (isRevers) {
                    tracks.getTracks().sort(Comparator.comparing(Track::getAlbum).reversed());
                } else {
                    tracks.getTracks().sort(Comparator.comparing(Track::getAlbum));
                }
                break;
            case 4:
                if (isRevers) {
                    tracks.getTracks().sort(Comparator.comparing(Track::getLengthInt).reversed());
                } else {
                    tracks.getTracks().sort(Comparator.comparing(Track::getLengthInt));
                }
                break;
            case 5:
                if (isRevers) {
                    tracks.getTracks().sort(Comparator.comparing(Track::getGenreName).reversed());
                } else {
                    tracks.getTracks().sort(Comparator.comparing(Track::getGenreName));
                }
                break;
        }
    }

    private Tracks loadFromXml(String fileName) {
        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Tracks.class, Track.class, Genre.class);
        } catch (JAXBException e) {
            logger.error("newInstance Exception");
            return null;
        }
        Unmarshaller jaxbUnmarshaller = null;
        try {
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            logger.error("createUnmarshaller Exception");
            return null;
        }

        try {
            return (Tracks) jaxbUnmarshaller.unmarshal(new FileInputStream(fileName));
        } catch (JAXBException | FileNotFoundException e) {
            logger.error("unmarshal Exception");
            return null;
        }
    }

    private List<Track> loadTrack(FileInputStream fileInputStream) {
        List<Track> trackList = new ArrayList<>();
        try {
            ObjectInputStream in = new ObjectInputStream(fileInputStream);
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
            tracks.getTracks().add(track);
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
        return loadFromXml(fileName).getTracks();
    }

    private List<Track> addFromSerializableFile(FileInputStream fileInputStream) {
        return loadTrack(fileInputStream);
    }

    @Override
    public boolean update(Track oldTrack, String name, String singer, String album, int length, String genreName) {
        Track track = findTrack(oldTrack.getName(), oldTrack.getSinger(), oldTrack.getAlbum(), oldTrack.getLengthInt());
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
        findValue = findValue.replaceAll("\\*", ".*");
        findValue = findValue.replaceAll("\\?", ".?");

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

    private Genre findGenre(String genreName) {
        for (Genre genre : genres)
            if (genre.getGenreName().equals(genreName)) {
                return genre;
            }
        return null;
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "musiclib")
    public static class Tracks {
        @XmlElement(name = "track")
        private List<Track> tracks = new ArrayList<>();

        public synchronized List<Track> getTracks() {
            return tracks;
        }

        public void setTracks(List<Track> tracks) {
            this.tracks = tracks;
        }

        @Override
        public String toString() {
            return "musiclib [tracks=" + tracks + "]";
        }
    }
}
