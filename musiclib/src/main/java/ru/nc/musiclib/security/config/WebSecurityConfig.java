package ru.nc.musiclib.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.server.transport.HttpsRedirectWebFilter;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.security.filters.TokenAuthFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserDetailsService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private TokenAuthFilter tokenAuthFilter;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
/*          http
                .addFilterBefore(tokenAuthFilter, BasicAuthenticationFilter.class)
                .antMatcher("/api/**")
                .authenticationProvider(authenticationProvider);
               .authorizeRequests()
                //.antMatchers("/users/**").hasAuthority("USER")
                .antMatchers("/signIn").permitAll();
        http.csrf().disable();*/
       http
               .addFilterBefore(tokenAuthFilter, BasicAuthenticationFilter.class)
             //  .antMatcher("/api/**")
               .authenticationProvider(authenticationProvider)
                .authorizeRequests()
                .antMatchers("/users/**", "/api/users/**").hasAuthority(Role.ROLE_ADMINISTRATOR)
                .antMatchers("/user/**", "/api/user/**").hasAuthority(Role.ROLE_ADMINISTRATOR)
                .antMatchers("/tracks/{id}/update","/tracks/add","/api/tracks/{id}/delete", "/api/tracks/{id}/update","/api/tracks/add","/api/tracks/{id}/delete").hasAnyAuthority(Role.ROLE_ADMINISTRATOR, Role.ROLE_MODERATOR)
                .antMatchers("/signUp/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/setAdmin").permitAll()
                .antMatchers("/tracks","/tracks/{id}", "/api/tracks","/api/tracks/{id}").authenticated()
                .antMatchers("/api/login").permitAll()
               // .antMatchers("/api/**").permitAll()
               // .antMatchers("/api/tracks","/api/track/{id}").authenticated()
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
