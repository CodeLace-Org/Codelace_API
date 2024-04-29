package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRegisterRequestDTO {
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

    @NotBlank (message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener, como mínimo, 8 caracteres.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe tener, al menos, una letra mayúscula, una letra minúscula y un número.")
    private String confirmPassword;
}