package ru.nc.musiclib.classes;

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
    @XmlElement(name = "role")
    private Role role;

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

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}
