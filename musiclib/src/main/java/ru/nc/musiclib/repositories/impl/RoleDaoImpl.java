package ru.nc.musiclib.repositories.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.nc.musiclib.model.Role;
import ru.nc.musiclib.repositories.RoleDao;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {
    private static final String SELECT_All_ROLES = "select * from lib_role";
    private static final String SELECT_ROLE_BY_ID = "select * from lib_role where id = ?";

    private JdbcTemplate jdbcTemplate;
    private RowMapper<Role> roleRowMapper = (resultSet, i) -> new Role(resultSet.getString("roleName"),
            resultSet.getInt("id"));


    @Autowired
    public RoleDaoImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<Role> find(Integer id) {
        List<Role> result = jdbcTemplate.query(SELECT_ROLE_BY_ID, roleRowMapper, id);
        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }

    @Override
    public boolean save(Role model) {
        return false;
    }

    @Override
    public boolean update(Role model) {
        return false;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @Override
    public boolean delete(Role model) {
        return false;
    }

    @Override
    public List<Role> findAll() {
        return jdbcTemplate.query(SELECT_All_ROLES, roleRowMapper);
    }
}
