package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.ProjectDetailResponseDTO;
import com.codelace.codelace.model.objects.RequirementList;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ProjectDetailMapper {
    
    private final ModelMapper modelMapper;
    
    public ProjectDetailResponseDTO convertToDTO(RequirementList projectDetail) {
        return modelMapper.map(projectDetail, ProjectDetailResponseDTO.class);
    }

    public List<ProjectDetailResponseDTO> convertToListDTO(List<RequirementList> projectsDetail) {
        return projectsDetail;
    }

}
