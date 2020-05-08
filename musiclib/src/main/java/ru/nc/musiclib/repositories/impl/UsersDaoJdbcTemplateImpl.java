package ru.nc.musiclib.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.model.User;
import ru.nc.musiclib.repositories.UsersDao;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class UsersDaoJdbcTemplateImpl implements UsersDao {
    private static final String SELECT_ROLE_BY_NAME = "select * from lib_role where roleName = ?";
    private static final String SELECT_USER_BY_NAME = "select * from lib_user where userName = ?";
    private static final String SELECT_USER_BY_ID = "select * from lib_user where id = ?";
    private static final String DELETE_USER_BY_NAME = "delete from lib_user where userName = ?";
    private static final String INSERT_USER = "insert into lib_user (userName, password,id_role) values (?, ?, ?)";
    private static final String UPDATE_RILE_BY_USER_ID = "update lib_user set id_role = ? where id = ?";
    private static final String SELECT_ALL_USER = "select * from lib_user";
    private static final String SELECT_ROLE_BY_ID = "select * from lib_role where id = ?";
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Role> roleRowMapper = (resultSet, i) -> new Role(resultSet.getString("roleName"),
            resultSet.getInt("id"));
    private RowMapper<User> usersRowMapper = (resultSet, i) -> new User(
            resultSet.getString("userName"),
            resultSet.getString("password"),
            findRole(resultSet.getInt("id_role")).orElse(null),
            resultSet.getInt("id"));

    @Autowired
    public UsersDaoJdbcTemplateImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private Optional<Role> findRole(int id) {
        List<Role> result = jdbcTemplate.query(SELECT_ROLE_BY_ID, roleRowMapper, id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    private Role getRoleByName(String name){
        try {
            return jdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME, new Object[]{name}, roleRowMapper);
        }
        catch (DataAccessException ex){
            return null;
        }
    }

    @Override
    public User findByName(String name) {
        try {
            return jdbcTemplate.queryForObject(SELECT_USER_BY_NAME, new Object[]{name}, usersRowMapper);
        }
        catch (DataAccessException ex){
            return null;
        }
    }

    @Override
    public boolean deleteByName(String name) {
        return jdbcTemplate.update(DELETE_USER_BY_NAME, name) != 0;
    }

    @Override
    public Optional<User> find(Integer id) {
        List<User> result = jdbcTemplate.query(SELECT_USER_BY_ID, usersRowMapper, id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public boolean save(User model) {
        return jdbcTemplate.update(INSERT_USER, model.getUserName(), model.getPassword(), getRoleByName(model.getRole().getRoleName()).getId()) != 0;
    }

    @Override
    public boolean update(User model) {
        return jdbcTemplate.update(UPDATE_RILE_BY_USER_ID, getRoleByName(model.getRole().getRoleName()).getId(), model.getId()) != 0;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean delete(User model) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_USER, usersRowMapper);
    }
}
