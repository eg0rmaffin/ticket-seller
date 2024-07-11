package ru.stmlabs.dao;

import ru.stmlabs.entity.Role;

import java.util.Optional;

public interface RoleDao {
    Optional<Role> findByName(String name);
    void save(Role role);
}
