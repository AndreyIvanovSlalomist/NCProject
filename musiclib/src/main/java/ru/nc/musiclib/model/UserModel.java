package ru.nc.musiclib.model;

import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.net.Role;

import java.util.List;

public interface UserModel {
    boolean add(String userName, String password);
    void setRole(String userName, Role role);
    Role getRole(String userName);
    User findUser(String userName);
    boolean checkUser(String userName, String password);
    void save();
    void load();
    List<User> getAllUser();
}
