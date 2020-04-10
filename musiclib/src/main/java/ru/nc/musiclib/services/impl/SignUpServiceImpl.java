package ru.nc.musiclib.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nc.musiclib.forms.UserForm;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.repositories.UsersDao;
import ru.nc.musiclib.services.SignUpService;

@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean signUp(UserForm userForm) {
        if (usersDao.findByName(userForm.getUserName())==null){
            String hashPassword = passwordEncoder.encode(userForm.getPassword());
            User user = new User();
            user.setPassword(hashPassword);
            user.setUserName(userForm.getUserName());
            user.setRole(new Role(Role.ROLE_USER));
            return usersDao.save(user);
        }
        else {
            return false;
        }
    }
}
