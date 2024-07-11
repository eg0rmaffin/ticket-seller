package ru.stmlabs.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.stmlabs.dto.UserDTO;
import ru.stmlabs.entity.UserDetailsImpl;

import java.util.HashMap;
import java.util.Map;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, UserDTO> usersCache = new HashMap<>();

    @KafkaListener(topics = "user-topic", groupId = "security-group")
    public void handleUserEvents(UserDTO userDTO) {
        if (userDTO.isDeleted()) {
            usersCache.remove(userDTO.getUsername());
            System.out.println("Removed UserDTO: " + userDTO.getUsername());
        } else {
            // Удаление старой записи, если username изменился
            usersCache.entrySet().removeIf(entry -> entry.getValue().getId().equals(userDTO.getId()));

            // Добавление или обновление записи
            usersCache.put(userDTO.getUsername(), userDTO);
            System.out.println("Received UserDTO: " + userDTO);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = usersCache.get(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new UserDetailsImpl(user);
    }
}
