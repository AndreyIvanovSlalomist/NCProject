package ru.nc.musiclib.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import static java.util.Collections.singletonList;
import static ru.nc.musiclib.utils.FindProperty.getProperty;

@SpringBootApplication
@ComponentScan(basePackages = "ru.nc.musiclib")
@EnableJpaRepositories(basePackages = "ru.nc.musiclib.repositories")
@EntityScan(basePackages = "ru.nc.musiclib.model")
public class Application {
    public Application(FreeMarkerConfigurer freeMarkerConfigurer) {
        freeMarkerConfigurer.getTaglibFactory().setClasspathTlds(singletonList("/META-INF/security.tld"));
    }

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", getProperty("context.path"));
        SpringApplication.run(Application.class);
    }
}
