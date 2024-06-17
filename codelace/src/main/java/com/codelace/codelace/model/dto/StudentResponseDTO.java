package com.codelace.codelace.model.dto;

import com.codelace.codelace.model.entity.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {
    private Long id;
    private String email;
    private String username;
    private String status;
    private String description;
    private byte[] profile_picture;
    private Role role;
}
