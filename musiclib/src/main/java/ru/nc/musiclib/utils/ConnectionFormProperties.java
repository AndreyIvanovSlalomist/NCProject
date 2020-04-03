package ru.nc.musiclib.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Properties;
/**
 * в конструкторе DAO'шки инициализируем Connection
 * Connection connection = connectionFormProperties().getConnection();
 *
 */
public class ConnectionFormProperties {
    private final static MusicLibLogger logger = new MusicLibLogger(ConnectionFormProperties.class);
    private static final ConnectionFormProperties connectionFormProperties;
    private static final String PROPERTIES_PATH = "db.properties";
    private static final String CUSTOM_PROPERTIES_PATH = System.getProperty("user.dir") + File.separator + "customDb.properties";

    static {
        connectionFormProperties = new ConnectionFormProperties();
    }

    private Connection connection;

    private ConnectionFormProperties() {
        Properties properties = new Properties();
        try {
            try{
                properties.load(new FileInputStream(CUSTOM_PROPERTIES_PATH));
                System.out.println(CUSTOM_PROPERTIES_PATH);
            } catch (FileNotFoundException e){
                properties.load(getClass().getClassLoader().getResourceAsStream(PROPERTIES_PATH));
                System.out.println(getClass().getClassLoader().getResourceAsStream(PROPERTIES_PATH));
            }
            System.out.println();
            String dbUser = properties.getProperty("db.username");
            String dbPassword = properties.getProperty("db.Password");
            String dbUrl = properties.getProperty("db.Url");
            Class.forName(properties.getProperty("db.driverClassName"));
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (IOException e) {
            logger.error(e.getLocalizedMessage());
        } catch (ClassNotFoundException e) {
            logger.error(e.getLocalizedMessage());
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        }
    }

    public static ConnectionFormProperties connectionFormProperties() {
        return connectionFormProperties;
    }
    public Optional<Connection> getConnection() {
        return Optional.ofNullable(connection);
    }
}
