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
public class EstudianteRegisterRequestDTO {
    @NotBlank (message = "El nombre de usuario no puede estar vacío.")
    @Pattern (regexp = "^[a-zA-Z0-9_]*$", message = "El nombre de usuario solo puede contener letras, números y guiones bajos.")
    private String username;

    @NotBlank (message = "El email no puede estar vacío.")
    @Email
    private String email;

    @NotBlank (message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener, como mínimo, 8 caracteres.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe tener, al menos, una letra minúscula, una letra mayúscula y un número.")
    private String password;

    @NotBlank (message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener, como mínimo, 8 caracteres.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe tener, al menos, una letra minúscula, una letra mayúscula y un número.")
    private String confirmPassword;
}
