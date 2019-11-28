package ru.nc.musiclib.classes;

import java.io.Serializable;

public class Track implements Serializable {
    private String trackName;
    private String singer;
    private String album;
    private String trackLength;
    private Genre genre;
    public Track(){}

    public Track(String trackName, String singer, String album, String trackLength, Genre genre) {
        this.trackName = trackName;
        this.singer = singer;
        this.album = album;
        this.trackLength = trackLength;
        this.genre = genre;
    }
    private boolean checkTrackLength(String trackLength){
        String pattern = "[0-5]?[0-9]:[0-5][0-9]";
        return trackLength.matches(pattern);
    }
    public String getTrackLength() {
        return trackLength;
    }
    public void setTrackLength(String trackLength) {
        if(checkTrackLength(trackLength)){
            this.trackLength = trackLength;
        }else throw new InvalidFieldValueException(trackLength);
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
