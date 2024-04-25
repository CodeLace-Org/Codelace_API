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
public class AccountLoginRequestDTO {
    @NotBlank (message = "El email no puede estar vacío.")
    @Email
    private String email;

    @NotBlank (message = "La contraseña no puede estar vacía.")
    @Size(min = 8, message = "La contraseña debe tener, como mínimo, 8 caracteres.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "La contraseña debe tener, al menos, una letra minúscula, una letra mayúscula y un número.")
    private String password;
}
