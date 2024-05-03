package com.codelace.codelace.model.dto;

import java.time.LocalDate;

import com.codelace.codelace.model.entity.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostByProjectResponseDTO {
    private Long id;
    private Student student;
    private Long rockets;
    private Long comments;
    private LocalDate date;
    private byte[] image;
}
