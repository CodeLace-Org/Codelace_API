package com.codelace.codelace.model.dto;

import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RocketResponseDTO {
    private String id;
    private Post post;
    private Student student;
}
