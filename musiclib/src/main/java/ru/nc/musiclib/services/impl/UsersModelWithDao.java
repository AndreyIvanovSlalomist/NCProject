package ru.nc.musiclib.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.repositories.RoleDao;
import ru.nc.musiclib.repositories.UsersDao;
import ru.nc.musiclib.services.UserModel;
import ru.nc.musiclib.utils.PasswordUtils;

import java.util.List;
import java.util.Optional;

import static ru.nc.musiclib.DataSourceConfig.getProperty;

@Service
public class UsersModelWithDao  implements UserModel {
    @Autowired
    private UsersDao usersDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public void admin(){
        String adminUsername = getProperty("admin.username");
        String adminPassword = getProperty("admin.Password");
        User user = usersDao.findByName(adminUsername);
        if (user!=null) {
            if (!passwordEncoder.matches(adminPassword, user.getPassword())){
                delete(adminUsername);
                user = null;
            }
        }
        if (user==null){
            System.out.println("Создаем нового администратора "+ adminUsername);
            String hashPassword = passwordEncoder.encode(adminPassword);
            user = new User();
            user.setPassword(hashPassword);
            user.setUserName(adminUsername);
            user.setRole(new Role(Role.ROLE_ADMINISTRATOR));
            usersDao.save(user);
        }
    }

    @Override
    public List<User> getAllUser() {
        return usersDao.findAll();
    }

    @Override
    public List<Role> getAllRole() {
        return roleDao.findAll();
    }

    @Override
    public User findUser(String userName) {
        return usersDao.findByName(userName);
    }

    @Override
    public User findUser(Integer id) {
        Optional<User> user = usersDao.find(id);
        return user.orElse(null);
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
