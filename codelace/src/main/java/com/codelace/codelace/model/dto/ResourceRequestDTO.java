package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceRequestDTO {
    
    @NotBlank(message = "Title resource cannot be empty")
    private String title;

    @NotBlank(message="Resource link cannot be empty")
    private String link;

    @NotNull(message = "Project cannot be empty")
    private Long project;
}