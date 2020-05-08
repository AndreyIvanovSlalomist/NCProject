package ru.nc.musiclib.services.impl;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nc.musiclib.forms.UserForm;
import ru.nc.musiclib.model.Token;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.repositories.TokensRepository;
import ru.nc.musiclib.repositories.UsersDao;
import ru.nc.musiclib.services.LoginService;
import ru.nc.musiclib.transfer.TokenDto;


@Component
public class LoginServiceImpl implements LoginService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersDao usersDao;

    @Autowired
    private TokensRepository tokensRepository;

    @Override
    public TokenDto login(UserForm userForm) {
        User userCandidate = usersDao.findByName(userForm.getUserName());
        if (userCandidate != null){
            if (passwordEncoder.matches (userForm.getPassword(), userCandidate.getPassword())){
                Token token = Token.builder()
                        .user(userCandidate)
                        .value(RandomStringUtils.random(10, true, true))
                        .build();
                tokensRepository.save(token);
                return TokenDto.from(token);
            }
        } else {
            throw new IllegalArgumentException("Пользователь не найден");
        }
        return null;
    }
}
