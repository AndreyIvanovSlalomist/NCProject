package ru.nc.musiclib.db.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.nc.musiclib.classes.Role;
import ru.nc.musiclib.classes.User;
import ru.nc.musiclib.db.dao.UsersDao;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component
public class UsersDaoJdbcTemplateImpl implements UsersDao {
    private static final String SELECT_ROLE_BY_NAME = "select * from lib_role where roleName = ?";
    private static final String SELECT_USER_BY_NAME = "select * from lib_user where userName = ?";
    private static final String DELETE_USER_BY_NAME = "delete from lib_user where userName = ?";
    private static final String INSERT_USER = "insert into lib_user (userName, password,id_rile) values (?, ?, ?)";
    private static final String UPDATE_RILE_BY_USER_ID = "update lib_user set id_rile = ? where id = ?";
    private static final String SELECT_ALL_USER = "select * from lib_user";
    private static final String SELECT_ROLE_BY_ID = "select * from lib_role where id = ?";
    JdbcTemplate jdbcTemplate;
    private RowMapper<Role> roleRowMapper = (resultSet, i) -> new Role(resultSet.getString("roleName"),
            resultSet.getInt("id"));
    private RowMapper<User> usersRowMapper = (resultSet, i) -> new User(
            resultSet.getString("userName"),
            resultSet.getString("password"),
            findRole(resultSet.getInt("id_rile")).orElse(null),
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

    @Override
    public User findByName(String name) {
        return null;
    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public Optional<User> find(Integer id) {
        return Optional.empty();
    }

    @Override
    public void save(User model) {

    }

    @Override
    public void update(User model) {

    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public void delete(User model) {

    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SELECT_ALL_USER, usersRowMapper);
    }
}
