package ru.nc.musiclib.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
@Data
@Builder
@Entity
@Table(name = "lib_genre")
@XmlAccessorType(XmlAccessType.FIELD)
public class Genre implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @XmlElement(name = "genreName")
    private String genreName;

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
