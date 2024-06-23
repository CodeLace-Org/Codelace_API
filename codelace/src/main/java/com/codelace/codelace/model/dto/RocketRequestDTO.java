package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RocketRequestDTO {
    @NotNull (message = "Student ID is required.")
    private Long student;
    @NotNull (message = "Post ID is required.")
    private Long post;
}
