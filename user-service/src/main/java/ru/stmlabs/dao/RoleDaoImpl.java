package ru.stmlabs.dao.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.stmlabs.dao.RoleDao;
import ru.stmlabs.entity.Role;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class RoleDaoImpl implements RoleDao {

    private final JdbcTemplate jdbcTemplate;

    public RoleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Role> roleRowMapper = new RowMapper<>() {
        @Override
        public Role mapRow(ResultSet rs, int rowNum) throws SQLException {
            Role role = new Role();
            role.setId(rs.getLong("id"));
            role.setName(rs.getString("name"));
            return role;
        }
    };

    @Override
    public Optional<Role> findByName(String name) {
        String sql = "SELECT * FROM roles WHERE name = ?";
        return jdbcTemplate.query(sql, new Object[]{name}, rs -> {
            if (rs.next()) {
                return Optional.ofNullable(roleRowMapper.mapRow(rs, 1));
            } else {
                return Optional.empty();
            }
        });
    }

    @Override
    public void save(Role role) {
        String sql = "INSERT INTO roles (name) VALUES (?)";
        jdbcTemplate.update(sql, role.getName());
    }
}
