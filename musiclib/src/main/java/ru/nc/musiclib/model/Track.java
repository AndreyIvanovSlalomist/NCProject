package ru.nc.musiclib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
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


    public Track(Integer id, String name, String singer, String album, int length, Genre genre) {
        this.name = name;
        this.singer = singer;
        this.album = album;
        this.length = length;
        this.genre = genre;
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    private Integer id;

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
            ((Track) track).setLengthInt(this.getLengthInt());
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
    public void setLength(String length){
        String[] str = length.split(":");
        if(str.length==3)
            this.length = Integer.parseInt(str[0])*3600+Integer.parseInt(str[1])*60+Integer.parseInt(str[2]);
        else if(str.length==2)
            this.length = Integer.parseInt(str[0])*60+Integer.parseInt(str[1]);
        else
            this.length = Integer.parseInt(str[0]);
    }
    public int getLengthInt() {
        return this.length;
    }
    public void setLengthInt(int length) {
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

    @JsonIgnore
    public String getGenreName() {
        return genre.getGenreName();
    }
}
