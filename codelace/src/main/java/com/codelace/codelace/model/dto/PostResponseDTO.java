package com.codelace.codelace.model.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {
    private Long id;
    private String username;
    private Long rockets;
    private Long comments;
    private Date date;
    private byte[] image;
}
