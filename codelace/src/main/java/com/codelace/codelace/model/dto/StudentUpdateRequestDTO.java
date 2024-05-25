package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentUpdateRequestDTO {
    @NotBlank (message = "Username cannot be empty.")
    @Pattern (regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain letters, numbers, and underscores.")
    private String username;

    @Pattern (regexp = "^[a-zA-z0-9_]*", message = "Description can only contain letters, numbers, and underscores.")
    private String description;

    @Pattern (regexp = "^[a-zA-z0-9_]*", message = "Status can only contain letters, numbers, and underscores.")
    private String status;

    @NotBlank (message = "Email cannot be empty.")
    @Email
    private String email;
}
