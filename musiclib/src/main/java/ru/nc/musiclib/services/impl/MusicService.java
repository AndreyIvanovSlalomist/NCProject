package ru.nc.musiclib.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nc.musiclib.model.Genre;
import ru.nc.musiclib.model.Track;
import ru.nc.musiclib.repositories.GenreRepository;
import ru.nc.musiclib.repositories.TrackRepository;
import ru.nc.musiclib.services.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MusicService implements Model {

    @Autowired
    private TrackRepository trackRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Override
    public List<Track> getAll() {
        return trackRepository.findAll();
    }

    @Override
    public boolean add(String name, String singer, String album, int length, String genreName, boolean isSendNotif) {
        return trackRepository.save(Track.builder()
                .name(name)
                .singer(singer)
                .album(album)
                .length(length)
                .genre(findGenre(genreName)).build()) != null;
    }

    @Override
    public void addFromFile(String fileName) {

    }

    @Override
    public void saveToFile(String fileName) {

    }

    @Override
    public boolean update(Track track, int colNumber, String newValue) {
        return false;
    }

    @Override
    public boolean update(Track track, String name, String singer, String album, int length, String genreName) {
        Optional<Track> t = trackRepository.findById(track.getId());
        if(t.isPresent()){
            t.get().setName(name);
            t.get().setSinger(singer);
            t.get().setAlbum(album);
            t.get().setLengthInt(length);
            t.get().setGenre(findGenre(genreName));
            trackRepository.save(t.get());
            return true;
        }
        return false;
    }

    private Genre findGenre(String genreName) {
        Optional<Genre> genre = genreRepository.findByGenreName(genreName);
        return genre.orElseGet(() -> genreRepository.save(Genre.builder().genreName(genreName).build()));
    }

    @Override
    public List<Track> find(int colNumber, String findValue) {
        return null;
    }

    @Override
    public boolean delete(int number) {
        Optional<Track> track = trackRepository.findById(number);
        if(track.isPresent()){
            trackRepository.delete(track.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String name, String singer, String album, int length, String genreName) {
        return false;
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
        for (Track track : trackRepository.findAll()) {
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
        return trackRepository.findById(id);
    }

    @Override
    public Track save(Track track) {
        return null;
    }
}
