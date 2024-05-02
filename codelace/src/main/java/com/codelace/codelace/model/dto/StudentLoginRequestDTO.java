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
public class StudentLoginRequestDTO {
    @NotBlank (message = "Email cannot be empty.")
    @Email
    private String email;

    @NotBlank (message = "Password cannot be empty.")
    @Size(min = 8, message = "Password must have at least 8 characters.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$", message = "Password must have at least one uppercase letter, one lowercase letter, and one number.")
    private String pwd;
}