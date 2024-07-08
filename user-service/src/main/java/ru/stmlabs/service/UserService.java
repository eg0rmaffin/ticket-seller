package ru.stmlabs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
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

    @Autowired
    public UserService(UserDao userDao, KafkaTemplate<String, UserDTO> kafkaTemplate) {
        this.userDao = userDao;
        this.kafkaTemplate = kafkaTemplate;
    }

    public List<UserDTO> getAllUsers() {
        return userDao.getAllUsers().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserDTO getUserById(Long id) {
        User user = userDao.getUserById(id);
        return convertToDTO(user);
    }

    public void createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        userDao.createUser(user);
        kafkaTemplate.send("user-topic", userDTO);
    }

    public void updateUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        userDao.updateUser(user);
        kafkaTemplate.send("user-topic", userDTO);
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
            Role role = new Role();
            role.setName(roleName);
            return role;
        }).collect(Collectors.toSet()));
        return user;
    }
}
