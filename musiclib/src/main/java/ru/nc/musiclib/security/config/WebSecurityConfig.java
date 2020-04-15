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
        http
                .authorizeRequests()
                .antMatchers("/users/**").hasAuthority(Role.ROLE_ADMINISTRATOR)
                .antMatchers("/user/**").hasAuthority(Role.ROLE_ADMINISTRATOR)
                .antMatchers("/tracks/{id}/update","/tracks/add","/tracks/{id}/delete").hasAnyAuthority(Role.ROLE_ADMINISTRATOR, Role.ROLE_MODERATOR)
                .antMatchers("/signUp/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/setAdmin").permitAll()
                .antMatchers("/tracks","/tracks/{id}").authenticated()
                .antMatchers("/css/**").permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/tracks")
                .and()
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .defaultSuccessUrl("/tracks")
                .loginPage("/signIn")
                .permitAll();

        http.csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

}
