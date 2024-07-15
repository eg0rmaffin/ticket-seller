package ru.stmlabs.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Set;

@Data
public class UserDTO {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;
    private String username;
    private String password;
    private String fullName;
    private Set<String> roles;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private boolean deleted = false;
}
