package ru.nc.musiclib.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
/**
 * в конструкторе DAO'шки инициализируем Connection
 * Connection connection = ConnectionFormProperties.getConnection();
 *
 */
public class ConnectionFormProperties {
    private static final ConnectionFormProperties connectionFormProperties;
    private static final String PROPERTIES_PATH = System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes" + File.separator + "db.properties";

    static {
        connectionFormProperties = new ConnectionFormProperties();
    }

    private Connection connection;

    private ConnectionFormProperties() {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(PROPERTIES_PATH));
            System.out.println();
            String dbUser = properties.getProperty("db.username");
            String dbPassword = properties.getProperty("db.Password");
            String dbUrl = properties.getProperty("db.Url");
            Class.forName(properties.getProperty("db.driverClassName"));
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ConnectionFormProperties connectionFormProperties() {
        return connectionFormProperties;
    }
    public Connection getConnection() {
        return connection;
    }
}
