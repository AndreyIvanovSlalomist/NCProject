package ru.nc.musiclib.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.nc.musiclib.model.Role;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/signUp").not().fullyAuthenticated()
                .antMatchers("/tracks/**").hasAnyRole(Role.ROLE_ADMINISTRATOR, Role.ROLE_MODERATOR)
                .antMatchers("/tracks", "/tracks/{id}").authenticated()
                .antMatchers("/users").hasRole(Role.ROLE_ADMINISTRATOR)
                .and()
                .formLogin().loginPage("/signIn")
                .defaultSuccessUrl("/tracks")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/signIn");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

}
