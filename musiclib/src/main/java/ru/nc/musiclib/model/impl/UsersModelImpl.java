package ru.nc.musiclib.model.impl;

import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.logger.MusicLibLogger;
import ru.nc.musiclib.model.UserModel;
import ru.nc.musiclib.net.Role;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class UsersModelImpl implements UserModel {
    private final static MusicLibLogger logger = new MusicLibLogger(UsersModelImpl.class);
    Users users = new Users();

    public UsersModelImpl() {
        load();
        if (users.getUsers().size() == 0){
            users.getUsers().add(new User("admin", "admin"));
            setRole("admin", Role.administrator);
            logger.info("UsersModelImpl add admin");
            save();
        }
    }

    @Override
    public void add(String userName, String password) {
        if (findUser(userName) == null) {
            users.getUsers().add(new User(userName, password));
            save();
        }
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
    public boolean checkUser(String userName, String password) {
        User user = findUser(userName);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
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
