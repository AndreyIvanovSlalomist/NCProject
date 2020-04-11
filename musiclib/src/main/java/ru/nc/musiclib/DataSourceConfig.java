package ru.nc.musiclib;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.io.*;
import java.util.Properties;

@Configuration
public class DataSourceConfig {
    private static final String PROPERTIES_PATH = "musiclib.properties";
    private static final String CUSTOM_PROPERTIES_PATH = "musiclib_custom.properties";

    private static String getPropertiesInFile(String nameProperty, String propertiesPath) throws IOException {
        Properties properties = new Properties();
        InputStream fileInputStream;
        try {
            fileInputStream = new FileInputStream(propertiesPath);
            properties.load(fileInputStream);
            return properties.getProperty(nameProperty);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден " + propertiesPath);
        }
        return null;
    }

    public static String getProperty(String nameProperty) {
        String filePath = System.getenv().get("CATALINA_HOME") + File.separator + "conf" + File.separator + CUSTOM_PROPERTIES_PATH;
        try {
            String valueProperty = getPropertiesInFile(nameProperty, filePath);
            if (valueProperty == null) {
                filePath = DataSourceConfig.class.getClassLoader().getResource(PROPERTIES_PATH).getPath();
                valueProperty = getPropertiesInFile(nameProperty, filePath);
            }
            System.out.println(" Имя параметра " + nameProperty + " Значение параметра " + valueProperty + " Из файла " + filePath);
            return valueProperty;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Bean
    public DataSource getDataSource() {
        System.out.println("Создаем свой DataSours");
        String ip = getProperty("db.ip");
        String port = getProperty("db.port");
        String baseName = getProperty("db.baseName");
        String username = getProperty("db.username");
        String password = getProperty("db.Password");

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.driverClassName("org.postgresql.Driver");
        dataSourceBuilder.url("jdbc:postgresql://" + ip + ":" + port + "/" + baseName);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);

        DataSource dataSource = dataSourceBuilder.build();
        return dataSource;
    }

}
