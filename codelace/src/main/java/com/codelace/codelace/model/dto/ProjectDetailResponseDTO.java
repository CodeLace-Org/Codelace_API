package com.codelace.codelace.model.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailResponseDTO {
    private String title;
    private String description;
    private byte[] image;
    private Long level;
    private List<RequirementResponseDTO> requirements;
}