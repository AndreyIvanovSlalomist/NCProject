package ru.nc.musiclib.model;

import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.classes.Role;

import java.util.List;

public interface UserModel {
    boolean add(String userName, String password);
    void delete(String userName);
    void setRole(String userName, Role role);
    Role getRole(String userName);
    User findUser(String userName);
    boolean checkUser(String userName);
    boolean checkPassword(String userName, String password);
    String getSalt(String userName);
    List<User> getAllUser();
}
