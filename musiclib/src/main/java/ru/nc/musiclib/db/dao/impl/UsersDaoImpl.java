package ru.nc.musiclib.db.dao.impl;

import org.springframework.stereotype.Component;
import ru.nc.musiclib.classes.Role;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.db.dao.UsersDao;
import ru.nc.musiclib.exceptions.InvalidConnection;
import ru.nc.musiclib.logger.MusicLibLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.nc.musiclib.db.ConnectionFormProperties.connectionFormProperties;
@Component
public class UsersDaoImpl implements UsersDao {

    private final static MusicLibLogger logger = new MusicLibLogger(UsersDaoImpl.class);
    private static final String SELECT_ROLE_BY_NAME = "select * from lib_role where roleName = ?";
    private static final String SELECT_USER_BY_NAME = "select * from lib_user where userName = ?";
    private static final String DELETE_USER_BY_NAME = "delete from lib_user where userName = ?";
    private static final String INSERT_USER = "insert into lib_user (userName, password,id_rile) values (?, ?, ?)";
    private static final String UPDATE_RILE_BY_USER_ID = "update lib_user set id_rile = ? where id = ?";
    private static final String SELECT_ALL_USER = "select * from lib_user";
    private static final String SELECT_ROLE_BY_ID = "select * from lib_role where id = ?";
    private Optional<Connection> connection;

    public UsersDaoImpl() {
        connection = connectionFormProperties().getConnection();
    }

    private Connection getConnection() {
        return connection.orElseThrow(InvalidConnection::new);
    }

    @Override
    public User findByName(String name) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECT_USER_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString("userName"),
                        resultSet.getString("password"),
                        findRole(resultSet.getInt("id_rile")),
                        resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }

        return null;
    }

    @Override
    public void deleteByName(String name) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(DELETE_USER_BY_NAME);
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }


    }

    @Override
    public Optional<User> find(Integer id) {
        return Optional.empty();
    }

    @Override
    public void save(User model) {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, model.getUserName());
            preparedStatement.setString(2, model.getPassword());
            preparedStatement.setInt(3, getRoleByName(model.getRole().getRoleName()).getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }


    }

    @Override
    public void update(User model) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(UPDATE_RILE_BY_USER_ID);
            preparedStatement.setInt(1, getRoleByName(model.getRole().getRoleName()).getId());
            preparedStatement.setInt(2, model.getId());
            preparedStatement.execute();
        }catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }


    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void delete(User mode) {

    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try {
            Statement statement = getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_ALL_USER);
            while (resultSet.next()) {
                users.add(new User(resultSet.getString("userName"),
                        resultSet.getString("password"),
                        findRole(resultSet.getInt("id_rile")),
                        resultSet.getInt("id")));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }

        return users;
    }

    private Role findRole(int id) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECT_ROLE_BY_ID);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) return new Role(resultSet.getString("roleName"),
                    resultSet.getInt("id"));
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }

        return null;
    }

    private Role getRoleByName(String name) {
        try {
            PreparedStatement preparedStatement = getConnection().prepareStatement(SELECT_ROLE_BY_NAME);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Role(resultSet.getString("roleName"), resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            logger.error(e.getLocalizedMessage());
        } catch (InvalidConnection e) {
            logger.error(e.getLocalizedMessage());
        }

        return null;
    }
}
