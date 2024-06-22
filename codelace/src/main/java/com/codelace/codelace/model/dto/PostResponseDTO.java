package com.codelace.codelace.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private StudentResponseDTO student;
    private String demoUrl;
    private String repoUrl;
    private String description;
    private String title;
    private LocalDate date;
    private byte[] image;
}
