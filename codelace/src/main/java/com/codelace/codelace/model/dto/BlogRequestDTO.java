package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogRequestDTO {

    @NotBlank(message = "Blog title is required.")
    @Pattern(regexp = "^[a-zA-Zñ0-9\\s.,;:'\"¡¿!?()-áéíóúÁÉÍÓÚ]+", message = "Blog title can only contain letters.")
    private String title;

    @NotBlank(message = "Blog content is required.")
    @Pattern(regexp = "^[a-zA-Zñ0-9\\s.,;:'\"¡¿!?()-áéíóúÁÉÍÓÚ]+", message = "Blog content can only contain letters.")
    private String content;

    private byte[] image;

    @NotNull(message = "Project ID is required.")
    private Long project;
}
