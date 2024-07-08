package ru.stmlabs.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.stmlabs.entity.Role;
import ru.stmlabs.entity.User;

import javax.sql.DataSource;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        List<User> users = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class));
        for (User user : users) {
            user.setRoles(getRolesByUserId(user.getId()));
        }
        return users;
    }

    @Override
    public User getUserById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), id);
        user.setRoles(getRolesByUserId(id));
        return user;
    }

    @Override
    public void createUser(User user) {
        String sql = "INSERT INTO users (username, password, full_name) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getFullName());
        Long userId = jdbcTemplate.queryForObject("SELECT lastval()", Long.class);
        saveUserRoles(userId, user.getRoles());
    }

    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, full_name = ? WHERE id = ?";
        jdbcTemplate.update(sql, user.getUsername(), user.getPassword(), user.getFullName(), user.getId());
        deleteUserRoles(user.getId());
        saveUserRoles(user.getId(), user.getRoles());
    }

    @Override
    public void deleteUser(Long id) {
        deleteUserRoles(id);
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        User user = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<>(User.class), username);
        user.setRoles(getRolesByUserId(user.getId()));
        return user;
    }

    private void saveUserRoles(Long userId, Set<Role> roles) {
        if (roles != null) {
            String sql = "INSERT INTO user_roles (user_id, role_id) VALUES (?, ?)";
            for (Role role : roles) {
                jdbcTemplate.update(sql, userId, role.getId());
            }
        }
    }

    private void deleteUserRoles(Long userId) {
        String sql = "DELETE FROM user_roles WHERE user_id = ?";
        jdbcTemplate.update(sql, userId);
    }

    private Set<Role> getRolesByUserId(Long userId) {
        String sql = "SELECT r.* FROM roles r INNER JOIN user_roles ur ON r.id = ur.role_id WHERE ur.user_id = ?";
        List<Role> roles = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Role.class), userId);
        return new HashSet<>(roles);
    }
}
