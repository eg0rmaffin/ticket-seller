package ru.stmlabs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.stmlabs.dao.RoleDao;
import ru.stmlabs.dao.UserDao;
import ru.stmlabs.dto.UserDTO;
import ru.stmlabs.entity.Role;
import ru.stmlabs.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserDao userDao;
    private final KafkaTemplate<String, UserDTO> kafkaTemplate;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDao userDao, KafkaTemplate<String, UserDTO> kafkaTemplate, RoleDao roleDao, PasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.kafkaTemplate = kafkaTemplate;
        this.roleDao = roleDao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserDTO> getAllUsers() {
        return userDao.getAllUsers().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userDao.getUserById(id);
        return convertToDTO(user);
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userDao.createUser(user);
        UserDTO savedUserDTO = convertToDTO(user);
        kafkaTemplate.send("user-topic", savedUserDTO);
        System.out.println(userDTO);
        return savedUserDTO;
    }

    public UserDTO updateUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userDao.updateUser(user);
        kafkaTemplate.send("user-topic", userDTO);
        return userDTO;
    }

    public void deleteUser(Long id) {
        User user = userDao.getUserById(id);
        UserDTO userDTO = convertToDTO(user);
        userDTO.setDeleted(true);
        userDao.deleteUser(id);
        kafkaTemplate.send("user-topic", userDTO);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setFullName(user.getFullName());
        userDTO.setRoles(user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFullName(userDTO.getFullName());
        user.setRoles(userDTO.getRoles().stream().map(roleName -> {
            return roleDao.findByName(roleName).orElseGet(() -> {
                Role newRole = new Role();
                newRole.setName(roleName);
                roleDao.save(newRole);
                return newRole;
            });
        }).collect(Collectors.toSet()));
        return user;
    }
}
