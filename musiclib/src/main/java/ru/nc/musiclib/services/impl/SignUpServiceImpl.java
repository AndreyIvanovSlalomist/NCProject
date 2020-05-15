package ru.nc.musiclib.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.nc.musiclib.forms.UserForm;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.repositories.RoleRepository;
import ru.nc.musiclib.repositories.UsersDao;
import ru.nc.musiclib.repositories.UsersRepository;
import ru.nc.musiclib.services.SignUpService;

@Service
public class SignUpServiceImpl implements SignUpService {

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean signUp(UserForm userForm) {
        if (!usersRepository.findByUserName(userForm.getUserName()).isPresent()){
            String hashPassword = passwordEncoder.encode(userForm.getPassword());
            User user = new User();
            user.setPassword(hashPassword);
            user.setUserName(userForm.getUserName());
            user.setRole(roleRepository.findByRoleName(Role.ROLE_USER).get());
            return usersRepository.save(user)!=null;
        }
        else {
            return false;
        }
    }
}
