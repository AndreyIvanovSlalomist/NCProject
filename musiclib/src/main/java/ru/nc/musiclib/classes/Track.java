package ru.nc.musiclib.classes;

import java.io.Serializable;

public class Track implements Serializable {
    private String trackName;
    private String singer;

    private Genre genre;

    public String getTrackName() {
        return trackName;
    }

    public String getSinger() {
        return singer;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public Track(String trackName, String singer, Genre genre) {
        this.trackName = trackName;
        this.singer = singer;
        this.genre = genre;
    }
}
