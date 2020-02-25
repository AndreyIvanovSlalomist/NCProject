package ru.nc.musiclib.classes;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

@XmlAccessorType(XmlAccessType.FIELD)
public class Role implements Serializable {
    public static final String ROLE_ADMINISTRATOR = "administrator";
    public static final String ROLE_MODERATOR = "moderator";
    public static final String ROLE_USER = "user";

    @XmlElement(name = "roleName")
    private String roleName;

    private Integer id;

    public Role(String roleName, Integer id) {
        this.roleName = roleName;
        this.id = id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public Role() {
    }

    public Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String genreName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        if (isUser())
            return "Пользователь";
        if (isModerator())
            return "Модератор";
        if (isAdministrator())
            return "Администратор";
        return "Не понятно кто :)";
    }

    public boolean isAdministrator() {
        return this.roleName.equals(ROLE_ADMINISTRATOR);
    }

    public boolean isModerator() {
        return this.roleName.equals(ROLE_MODERATOR);
    }

    public boolean isUser() {
        return this.roleName.equals(ROLE_USER);
    }
}
