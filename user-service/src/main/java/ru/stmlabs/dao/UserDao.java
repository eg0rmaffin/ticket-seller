package ru.stmlabs.dao;

import ru.stmlabs.entity.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();
    User getUserById(Long id);
    void createUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
    User findByUsername(String username);
}
