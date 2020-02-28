package ru.nc.musiclib.model.impl;

import ru.nc.musiclib.classes.Role;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.db.dao.UsersDao;
import ru.nc.musiclib.db.dao.impl.UsersDaoImpl;
import ru.nc.musiclib.model.UserModel;
import ru.nc.musiclib.utils.PasswordUtils;

import java.util.List;

public class UsersModelWithDao  implements UserModel {
    private UsersDao usersDao = new UsersDaoImpl();

    @Override
    public List<User> getAllUser() {
        return usersDao.findAll();
    }

    @Override
    public User findUser(String userName) {
        return usersDao.findByName(userName);
    }

    @Override
    public boolean add(String userName, String password) {
        if(findUser(userName)==null){
            usersDao.save(new User(userName, password, new Role(Role.ROLE_USER)));
            return true;
        }
        return false;
    }

    @Override
    public void delete(String userName) {
        usersDao.deleteByName(userName);
    }

    @Override
    public void setRole(String userName, Role role) {
        User user = findUser(userName);
        user.setRole(role);
        usersDao.update(user);
    }

    @Override
    public Role getRole(String userName) {
        return findUser(userName).getRole();
    }

    @Override
    public boolean checkUser(String userName) {
        return findUser(userName)!=null;
    }

    @Override
    public boolean checkPassword(String userName, String password) {
        return PasswordUtils.verifyPassword(password, findUser(userName).getPassword());
    }

    @Override
    public String getSalt(String userName) {
        return PasswordUtils.getSalt(findUser(userName).getPassword());
    }
}
