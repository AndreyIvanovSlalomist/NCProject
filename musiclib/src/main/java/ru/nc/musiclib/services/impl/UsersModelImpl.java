package ru.nc.musiclib.services.impl;

import ru.nc.musiclib.model.User;
import ru.nc.musiclib.model.Users;
import ru.nc.musiclib.utils.MusicLibLogger;
import ru.nc.musiclib.services.UserModel;
import ru.nc.musiclib.utils.PasswordUtils;
import ru.nc.musiclib.model.Role;

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
            users.getUsers().add(new User("admin", PasswordUtils.hashPassword("admin"), new Role(Role.ROLE_ADMINISTRATOR)));
            logger.info("UsersModelImpl add admin");
            save();
        }
    }

    @Override
    public boolean add(String userName, String password) {
        if (findUser(userName) == null) {
            users.getUsers().add(new User(userName, password, new Role(Role.ROLE_USER)));
            logger.info("Пользователь " + userName + " добавлен");
            save();
            return true;
        }
        return false;
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

    private void save() {
        saveToXML(users, USERS_XML, Users.class, User.class, Role.class);
    }

    private void load() {
        Object object = loadFromXml(USERS_XML, Users.class, User.class, Role.class);
        if (object instanceof Users)
            users.setUsers(((Users) object).getUsers());
    }

    @Override
    public List<User> getAllUser() {
        return users.getUsers();
    }
}
