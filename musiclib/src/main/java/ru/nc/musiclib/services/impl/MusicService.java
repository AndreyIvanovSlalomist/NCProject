package ru.nc.musiclib.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.nc.musiclib.model.Genre;
import ru.nc.musiclib.model.Track;
import ru.nc.musiclib.repositories.GenreRepository;
import ru.nc.musiclib.repositories.TrackRepository;
import ru.nc.musiclib.services.Model;

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
                .genre(Genre.builder().genreName(genreName).build()).build()) != null;
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
            t.get().setGenre(Genre.builder().genreName(genreName).build());
            trackRepository.save(t.get());
            return true;
        }
        return false;
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

    @Override
    public List<Track> filter(String filterName, String filterSinger, String filterAlbum, String filterGenreName) {
        return null;
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
