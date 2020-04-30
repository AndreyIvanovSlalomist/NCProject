package ru.nc.musiclib;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.*;
import java.util.Properties;

import static ru.nc.musiclib.utils.FindProperty.getProperty;

@Configuration
public class DataSourceConfig {


    @Bean
    public DataSource getDataSource() {
        System.out.println("Создаем свой DataSours");
        DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
        String ip = getProperty("db.ip");
        String port = getProperty("db.port");
        String baseName = getProperty("db.baseName");
        String username = getProperty("db.username");
        String password = getProperty("db.Password");
        driverManagerDataSource.setDriverClassName("org.postgresql.Driver");
        driverManagerDataSource.setUrl("jdbc:postgresql://" + ip + ":" + port + "/" + baseName);
        driverManagerDataSource.setUsername(username);
        driverManagerDataSource.setPassword(password);

        return driverManagerDataSource;
    }

}
