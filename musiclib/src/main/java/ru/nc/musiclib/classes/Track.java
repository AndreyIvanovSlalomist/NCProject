package ru.nc.musiclib.classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class Track implements Serializable, Cloneable {
    @XmlElement(name = "name")
    private String name;
    @XmlElement(name = "singer")
    private String singer;
    @XmlElement(name = "album")
    private String album;
    @XmlElement(name = "length")
    private int length;
    @XmlElement(name = "genre")
    private Genre genre;

    @Override
    public String toString() {
        return "track [name=" + name + ", singer=" + singer + ", album=" + album + ", length=" + length + ", genre=" + genre + "]";
    }

    public Track() {
    }

    @Override
    public Object clone()  {
        Object track = null;
        try {
            track = super.clone();
            ((Track) track).setName(this.getName());
            ((Track) track).setSinger(this.getSinger());
            ((Track) track).setAlbum(this.getAlbum());
            ((Track) track).setLength(this.getLengthInt());
            ((Track) track).setGenre(new Genre(this.getGenreName()));
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return track;
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
