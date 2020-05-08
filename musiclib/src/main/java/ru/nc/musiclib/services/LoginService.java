package ru.nc.musiclib.services;

import ru.nc.musiclib.forms.UserForm;
import ru.nc.musiclib.transfer.TokenDto;

public interface LoginService {
    TokenDto login(UserForm loginForm);
}
