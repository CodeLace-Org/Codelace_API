package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.ProjectDetailRequestDTO;
import com.codelace.codelace.model.dto.ProjectDetailResponseDTO;
import com.codelace.codelace.model.entity.Project;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ProjectDetailMapper {
    
    private final ModelMapper modelMapper;
    
    public Project convertProjectDTOToEntity(ProjectDetailRequestDTO projectDetailRequestDTO){
        return modelMapper.map(projectDetailRequestDTO, Project.class);
    }

    public ProjectDetailResponseDTO convertProjectDetailToResponse(Project project){
        return modelMapper.map(project, ProjectDetailResponseDTO.class);
    }

}
