package ru.nc.musiclib.model.impl;

import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.classes.Users;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.UserModel;
import ru.nc.musiclib.utils.PasswordUtils;
import ru.nc.musiclib.utils.Role;

import java.util.List;

import static ru.nc.musiclib.utils.XMLUtils.loadFromXml;
import static ru.nc.musiclib.utils.XMLUtils.saveToXML;

public class UsersModelImpl implements UserModel {
    private final static MusicLibLogger logger = new MusicLibLogger(UsersModelImpl.class);
    private static final String USERS_XML = "users.xml";
    private Users users = new Users();

    public UsersModelImpl() {
        load();
        if (users.getUsers().isEmpty()) {
            users.getUsers().add(new User("admin", PasswordUtils.hashPassword("admin"), Role.administrator));
            logger.info("UsersModelImpl add admin");
            save();
        }
    }

    @Override
    public boolean add(String userName, String password) {
        if (findUser(userName) == null) {
            users.getUsers().add(new User(userName, password, Role.user));
            logger.info("Пользователь " + userName + " добавлен");
            save();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void delete(String userName) {
        users.getUsers().remove(findUser(userName));
    }

    @Override
    public void setRole(String userName, Role role) {
        findUser(userName).setRole(role);
        save();
    }

    public String getSalt(String userName) {
        return PasswordUtils.getSalt(findUser(userName).getPassword());
    }

    @Override
    public Role getRole(String userName) {
        return findUser(userName).getRole();
    }

    @Override
    public User findUser(String userName) {
        for (User user : users.getUsers()) {
            if (user.getUserName().equals(userName)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public boolean checkUser(String login) {
        return findUser(login) != null;
    }

    public boolean checkPassword(String login, String password) {
        return PasswordUtils.verifyPassword(password, findUser(login).getPassword());
    }

    @Override
    public void save() {
        saveToXML(users, USERS_XML, Users.class, User.class, Role.class);
    }

    @Override
    public void load() {
        loadFromXml(USERS_XML, Users.class, User.class, Role.class);
    }

    @Override
    public List<User> getAllUser() {
        return users.getUsers();
    }
}
