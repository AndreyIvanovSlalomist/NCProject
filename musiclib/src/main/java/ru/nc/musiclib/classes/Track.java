package ru.nc.musiclib.classes;

import java.io.Serializable;

public class Track implements Serializable {
    private String trackName;
    private String singer;
    private String album;
    private Double trackLength;
    private Genre genre;

    public Track(String trackName, String singer, String album, Double trackLength, Genre genre) {
        this.trackName = trackName;
        this.singer = singer;
        this.album = album;
        this.trackLength = trackLength;
        this.genre = genre;
    }

    public Double getTrackLength() {
        return trackLength;
    }

    public void setTrackLength(Double trackLength) {
        this.trackLength = trackLength;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }
}
