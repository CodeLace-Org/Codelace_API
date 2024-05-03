package com.codelace.codelace.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostRequestDTO {

    @NotNull(message = "Student ID is required.")
    private Long student;
    @NotNull(message = "Project ID is required.")
    private Long project;

    @NotBlank(message = "Demo Url is required.")
    private String demoUrl;
    @NotBlank(message = "Repo Url is required.")
    private String repoUrl;
    @NotBlank(message = "Description is required.")
    private String description;
    
    private byte[] image;
}
