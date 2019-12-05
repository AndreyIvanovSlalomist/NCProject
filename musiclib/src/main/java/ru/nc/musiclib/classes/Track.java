package ru.nc.musiclib.classes;

import java.io.Serializable;

public class Track implements Serializable {
    private String name;
    private String singer;
    private String album;
    private int length;
    private Genre genre;

    public Track() {
    }

    public Track(String name, String singer, String album, int length, Genre genre) {
        this.name = name;
        this.singer = singer;
        this.album = album;
        this.length = length;
        this.genre = genre;
    }

    public String getLength() {
        return String.format("%d:%02d:%02d", this.length / 3600, this.length % 3600 / 60, this.length % 60);
    }

    public int getLengthInt() {
        return this.length;
    }
    public void setLength(int length) {
        this.length = length;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getGenreName() {
        return genre.getGenreName();
    }
}
