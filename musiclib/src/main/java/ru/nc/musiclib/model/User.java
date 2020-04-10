package ru.nc.musiclib.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;


@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
    @XmlElement(name = "userName")
    private String userName;
    @XmlElement(name = "password")
    private String password;

    public User(String userName, String password, Role role, Integer id) {
        this.userName = userName;
        this.password = password;
        this.role = role;
        this.id = id;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @XmlElement(name = "role")
    private Role role;
    private Integer id;

    public User(String userName, String password, Role role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }
    public User() {
    }
    @Override
    public String toString() {
        return "user [userName=" + userName + ", password = секрет, Role=" + role.toString() + "]";
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public Role getRole() {
        return role;
    }

    public String getPassword() {
        return password;
    }
}
