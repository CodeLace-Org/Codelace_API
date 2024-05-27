package com.codelace.codelace.model.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseIdDTO {
    private Long id;
    private StudentResponseDTO student;
    private Long rockets;
    private Long comments;
    private String demoUrl;
    private String repoUrl;
    private String title;
    private String description;
    private LocalDate date;
    private byte[] image;
}
