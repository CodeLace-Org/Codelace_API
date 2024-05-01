package com.codelace.codelace.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.ProjectRequestDTO;
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.model.entity.Project;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ProjectMapper {
	private final ModelMapper modelMapper;

    // Request Project DTO to Entity
    public Project convertProjectRequestToEntity(ProjectRequestDTO projectRequestDTO) {
        return modelMapper.map(projectRequestDTO, Project.class);
    }

    // Project Entity to Response DTO
    public ProjectResponseDTO convertProjectToResponse(Project project) {
        return modelMapper.map(project, ProjectResponseDTO.class);
    }

}
