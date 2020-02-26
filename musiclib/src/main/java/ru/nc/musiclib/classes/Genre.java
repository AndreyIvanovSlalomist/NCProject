package ru.nc.musiclib.classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class Genre implements Serializable {
    @XmlElement(name = "genreName")
    private String genreName;
    private Integer id;

    public Genre(Integer id, String genreName) {
        this.genreName = genreName;
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Genre(){
    }
    public Genre(String genreName) {
        this.genreName = genreName;
    }

    public String getGenreName() {
        return genreName;
    }

    public void setGenreName(String genreName) {
        this.genreName = genreName;
    }
    @Override
    public String toString() {
        return "genre [genreName=" + genreName + "]";
    }
}
