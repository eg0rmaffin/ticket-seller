package ru.stmlabs.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.stmlabs.dao.UserDao;
import ru.stmlabs.dto.UserDTO;
import ru.stmlabs.entity.Role;
import ru.stmlabs.entity.User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserDao userDao, KafkaTemplate<String, UserDTO> kafkaTemplate) {
        return args -> {
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user"));
            user.setFullName("User One");
            Set<Role> userRoles = new HashSet<>();
            Role userRole = new Role();
            userRole.setId(1L);
            userRole.setName("ROLE_USER");
            userRoles.add(userRole);
            user.setRoles(userRoles);
            userDao.createUser(user);
            System.out.println("User ID after saving: " + user.getId());
            UserDTO userDTO = convertToDTO(user);
            System.out.println("UserDTO before sending to Kafka: " + userDTO);
            kafkaTemplate.send("user-topic", userDTO);
            System.out.println("User created and sent to Kafka: user");

            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setFullName("Admin User");
            Set<Role> adminRoles = new HashSet<>();
            Role adminRole = new Role();
            adminRole.setId(2L);
            adminRole.setName("ROLE_ADMIN");
            adminRoles.add(adminRole);
            admin.setRoles(adminRoles);
            userDao.createUser(admin);
            System.out.println("Admin ID after saving: " + admin.getId());
            UserDTO adminDTO = convertToDTO(admin);
            System.out.println("AdminDTO before sending to Kafka: " + adminDTO);
            kafkaTemplate.send("user-topic", adminDTO);
            System.out.println("Admin created and sent to Kafka: admin");
        };
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
}
