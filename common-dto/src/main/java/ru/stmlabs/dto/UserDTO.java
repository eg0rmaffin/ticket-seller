package ru.stmlabs.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private Set<String> roles;
    private boolean deleted = false;
}
