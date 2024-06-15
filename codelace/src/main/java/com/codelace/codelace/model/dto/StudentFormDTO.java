package com.codelace.codelace.model.dto;

import com.codelace.codelace.model.entity.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentFormDTO {
    @NotBlank (message = "Username cannot be empty.")
    @Pattern (regexp = "^[a-zA-Z0-9_]*$", message = "Username can only contain letters, numbers, and underscores.")
    private String username;

    @NotBlank (message = "Email cannot be empty.")
    @Email
    private String email;

    @NotBlank (message = "Password cannot be empty.")
    @Size(min = 8, message = "Password must have at least 8 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must have at least one uppercase letter, one lowercase letter, and one number.")
    private String pwd;

    @NotBlank (message = "Password cannot be empty.")
    @Size(min = 8, message = "Password must have at least 8 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must have at least one uppercase letter, one lowercase letter, and one number.")
    private String confirmPassword;

    @NotNull
    private Role role;

}
