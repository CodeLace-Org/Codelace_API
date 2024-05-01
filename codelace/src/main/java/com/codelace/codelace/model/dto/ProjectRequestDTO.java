package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectRequestDTO {
    @NotBlank(message = "Project ID cannot be empty.")
    private Long id;

    @NotBlank(message = "Student ID cannot be empty.")
    private Long student;

}
