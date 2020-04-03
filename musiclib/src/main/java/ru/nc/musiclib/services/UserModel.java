package ru.nc.musiclib.services;

import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.User;

import java.util.List;

public interface UserModel {
    boolean add(String userName, String password);
    boolean delete(String userName);
    void setRole(String userName, Role role);
    Role getRole(String userName);
    User findUser(String userName);
    boolean checkUser(String userName);
    boolean checkPassword(String userName, String password);
    String getSalt(String userName);
    List<User> getAllUser();
}
