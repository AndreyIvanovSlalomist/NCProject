package ru.nc.musiclib.classes;

import java.io.Serializable;

public class Genre implements Serializable {
    private String genreName;

    public String getGenreName() {
        return genreName;
    }

    public Genre(String genreName) {
        this.genreName = genreName;
    }
}
