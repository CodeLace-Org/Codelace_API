package com.codelace.codelace.mapper;

import java.util.List;

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

	public Project convertToEntity(ProjectRequestDTO projectRequestDTO) {
		return modelMapper.map(projectRequestDTO, Project.class);
	}

	public ProjectResponseDTO convertToDTO(Project project) {
		return modelMapper.map(project, ProjectResponseDTO.class);
	}

	public List<ProjectResponseDTO> convertToListDTO(List<Project> projects) {
		return projects.stream()
				.map(this::convertToDTO)
				.toList();
	}

}
