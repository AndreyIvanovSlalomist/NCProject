package ru.nc.musiclib.db.dao;

import ru.nc.musiclib.classes.User;

import java.util.List;

public interface UsersDao extends CrudDao<User> {
    User findByName(String name);
    void deleteByName(String name);
}
