package com.codelace.codelace.model.dto;

import com.codelace.codelace.model.entity.Project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BlogResponseDTO {
    private Long id;
    private String title;
    private String content;
    private Project project;
    private byte[] image;
}
