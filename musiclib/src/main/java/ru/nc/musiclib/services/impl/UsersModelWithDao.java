package ru.nc.musiclib.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.repositories.UsersDao;
import ru.nc.musiclib.services.UserModel;
import ru.nc.musiclib.utils.PasswordUtils;

import java.util.List;
@Component
public class UsersModelWithDao  implements UserModel {
    @Autowired
    private UsersDao usersDao;

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
            return usersDao.save(new User(userName, password, new Role(Role.ROLE_USER)));
        }
        return false;
    }

    @Override
    public boolean delete(String userName) {
        return usersDao.deleteByName(userName);
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
