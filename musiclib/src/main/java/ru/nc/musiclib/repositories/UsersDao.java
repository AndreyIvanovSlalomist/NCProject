package ru.nc.musiclib.repositories;

import ru.nc.musiclib.model.User;

public interface UsersDao extends CrudDao<User> {
    User findByName(String name);
    void deleteByName(String name);
}
