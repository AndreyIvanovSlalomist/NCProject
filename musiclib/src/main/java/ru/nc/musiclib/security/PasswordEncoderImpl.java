package ru.nc.musiclib.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.nc.musiclib.utils.PasswordUtils;

@Component
public class PasswordEncoderImpl implements PasswordEncoder {
    public String encode(CharSequence rawPassword) {
        return PasswordUtils.hashPassword(rawPassword.toString());
    }

    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return PasswordUtils.verifyPassword(PasswordUtils.hashPassword(rawPassword.toString(), PasswordUtils.getSalt(encodedPassword)), encodedPassword);
    }
}
