package ru.nc.musiclib.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.repositories.RoleRepository;
import ru.nc.musiclib.repositories.UsersRepository;
import ru.nc.musiclib.services.UserModel;
import ru.nc.musiclib.utils.PasswordUtils;

import java.util.List;
import java.util.Optional;

import static ru.nc.musiclib.utils.FindProperty.getProperty;

@Service
public class UsersService implements UserModel {
    @Autowired
    private
    UsersRepository userRepository;
    @Autowired
    private
    RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean add(String userName, String password) {
        return userRepository.save(User.builder()
                .userName(userName)
                .password(password)
                .role(new Role(Role.ROLE_USER)).build()) != null;
    }

    @Override
    public boolean delete(String userName) {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isPresent()) {
            userRepository.delete(user.get());
            return true;
        }
        return false;
    }

    @Override
    public void setRole(String userName, Role role) {
        Optional<User> user = userRepository.findByUserName(userName);
        if (user.isPresent()) {
            user.get().setRole(role);
            userRepository.save(user.get());
        }
    }

    @Override
    public Role getRole(String userName) {
        return findUser(userName).getRole();
    }

    @Override
    public User findUser(String userName) {
        return userRepository.findByUserName(userName).orElse(null);
    }

    @Override
    public User findUser(Integer id) {
        return userRepository.getOne(id);
    }

    @Override
    public boolean checkUser(String userName) {
        return findUser(userName) != null;
    }

    @Override
    public boolean checkPassword(String userName, String password) {
        return PasswordUtils.verifyPassword(password, findUser(userName).getPassword());
    }

    @Override
    public String getSalt(String userName) {
        return PasswordUtils.getSalt(findUser(userName).getPassword());
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    @Override
    public void admin() {

        String adminUsername = getProperty("admin.username");
        String adminPassword = getProperty("admin.Password");
        Optional<User> user = userRepository.findByUserName(adminUsername);
        if (user.isPresent()) {
            if (!passwordEncoder.matches(adminPassword, user.get().getPassword())) {
                delete(adminUsername);
                user = Optional.empty();
            }
        }
        if (!user.isPresent()) {
            System.out.println("Создаем нового администратора " + adminUsername);
            String hashPassword = passwordEncoder.encode(adminPassword);
            User newUser = new User();
            newUser.setPassword(hashPassword);
            newUser.setUserName(adminUsername);
            newUser.setRole(new Role(Role.ROLE_ADMINISTRATOR));
            userRepository.save(newUser);
        }
    }
}
