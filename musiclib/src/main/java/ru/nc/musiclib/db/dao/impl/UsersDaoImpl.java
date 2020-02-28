package ru.nc.musiclib.db.dao.impl;

import ru.nc.musiclib.classes.Role;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.db.dao.UsersDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static ru.nc.musiclib.db.ConnectionFormProperties.connectionFormProperties;

public class UsersDaoImpl implements UsersDao {

    private Connection connection;
    public UsersDaoImpl() {
        connection = connectionFormProperties().getConnection();
    }
    @Override
    public User findByName(String name) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select * from lib_user where userName = ?");
            preparedStatement.setString(1,name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new User(resultSet.getString("userName"),
                        resultSet.getString("password"),
                        findRole(resultSet.getInt("id_rile")),
                        resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteByName(String name) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("delete from lib_user where userName = ?");
            preparedStatement.setString(1, name);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Optional<User> find(Integer id) {
        return Optional.empty();
    }

    @Override
    public void save(User model) {
        try(PreparedStatement preparedStatement = connection.prepareStatement("insert into lib_user (userName, password,id_rile) values (?, ?, ?)")){
            preparedStatement.setString(1, model.getUserName());
            preparedStatement.setString(2, model.getPassword());
            preparedStatement.setInt(3,getRoleByName(model.getRole().getRoleName()).getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void update(User model) {
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("update lib_user set userName = ?, password = ?, id_rile = ? where id = ?");
            preparedStatement.setString(1, model.getUserName());
            preparedStatement.setString(2, model.getPassword());
            preparedStatement.setInt(3, getRoleByName(model.getRole().getRoleName()).getId());
            preparedStatement.setInt(4,model.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
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
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("select * from lib_user");
            while(resultSet.next()){
                users.add(new User(resultSet.getString("userName"),
                        resultSet.getString("password"),
                        findRole(resultSet.getInt("id_rile")),
                        resultSet.getInt("id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private Role findRole(int id){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select * from lib_role where id = ?");
            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) return new Role(resultSet.getString("roleName"),
                    resultSet.getInt("id"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Role getRoleByName(String name){
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("select * from lib_role where roleName = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next())
            {
                return new Role(resultSet.getString("roleName"), resultSet.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
