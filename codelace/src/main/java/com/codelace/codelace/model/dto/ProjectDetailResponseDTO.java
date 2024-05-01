package com.codelace.codelace.model.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDetailResponseDTO {
    private ProjectResponseDTO project;
    private List<RequirementResponseDTO> requirements;
}