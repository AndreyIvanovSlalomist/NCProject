package ru.nc.musiclib.model.impl;

import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.UserModel;
import ru.nc.musiclib.utils.Role;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class UsersModelImpl implements UserModel {
    private final static MusicLibLogger logger = new MusicLibLogger(UsersModelImpl.class);
    private Users users = new Users();

    public UsersModelImpl() {
        load();
        if (users.getUsers().isEmpty()){
            users.getUsers().add(new User("admin", "admin", Role.administrator));
            logger.info("UsersModelImpl add admin");
            save();
        }
    }

    @Override
    public boolean add(String userName, String password) {
        if (findUser(userName) == null) {
            users.getUsers().add(new User(userName, password, Role.user));
            logger.info("Пользователь "+userName+" добавлен");
            save();
            return true;
        }else {
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
       return findUser(login)!=null;
    }

    @Override
    public void save() {

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Users.class, User.class, Role.class);
        } catch (JAXBException e) {
            logger.error("newInstance Exception");
            return;
        }
        Marshaller jaxbMarshaller = null;
        try {
            jaxbMarshaller = jaxbContext.createMarshaller();
        } catch (JAXBException e) {
            logger.error("createMarshaller Exception");
            return;
        }

        try {
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        } catch (PropertyException e) {
            logger.error("setProperty Exception");
            return;
        }

        try {
            jaxbMarshaller.marshal(users, new File("users.xml"));
        } catch (JAXBException e) {
            logger.error("marshal Exception");
            return;
        }

    }

    @Override
    public void load() {

        JAXBContext jaxbContext = null;
        try {
            jaxbContext = JAXBContext.newInstance(Users.class, User.class, Role.class);
        } catch (JAXBException e) {
            logger.error("newInstance Exception");
            return;
        }
        Unmarshaller jaxbUnmarshaller = null;
        try {
            jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        } catch (JAXBException e) {
            logger.error("createUnmarshaller Exception");
            return;
        }

        try {
            users.setUsers(((Users) jaxbUnmarshaller.unmarshal(new FileInputStream("users.xml"))).getUsers());
        } catch (JAXBException | FileNotFoundException e) {
            logger.error("unmarshal Exception"+ e.getLocalizedMessage());
        }

        logger.info("Пользователей Загружено = " + users.getUsers().size());
    }

    @Override
    public List<User> getAllUser() {
        return users.getUsers();
    }

    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlRootElement(name = "users")
    public static class Users {
        @XmlElement(name = "users")
        private List<User> users = new ArrayList<>();

        public synchronized List<User> getUsers() {
            return users;
        }

        public void setUsers(List<User> users) {
            this.users = users;
        }

        @Override
        public String toString() {
            return "users [users=" + users + "]";
        }
    }
}
