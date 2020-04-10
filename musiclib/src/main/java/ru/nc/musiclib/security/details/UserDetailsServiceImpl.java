package ru.nc.musiclib.security.details;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.services.UserModel;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserModel userModel;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userModel.findUser(username);
        if(user == null) {
            throw new UsernameNotFoundException("Пользователь не найден!");
        }
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        return new org.springframework.security.core.userdetails.User(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }
}
