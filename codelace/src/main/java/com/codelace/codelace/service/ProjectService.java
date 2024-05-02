package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ProjectMapper;
import com.codelace.codelace.model.dto.ProjectRequestDTO;
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RouteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProjectService {
	private final ProjectMapper projectMapper;
	private final ProjectRepository projectRepository;
	private final RouteRepository routeRepository;

	// Method that returns all the projects
	public List<ProjectResponseDTO> getAllProjects() {
		List<Project> projects = projectRepository.findAll();
		return projectMapper.convertToListDTO(projects);
	}

	// Method that returns a project by id
	public ProjectResponseDTO getProjectById(Long id) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found."));
		return projectMapper.convertToDTO(project);
	}

	// Method that creates a project
	public ProjectResponseDTO createProject(ProjectRequestDTO projectRequestDTO) {
		Project project = projectMapper.convertToEntity(projectRequestDTO);
		Route route =  routeRepository.findById(projectRequestDTO.getRoute())
				.orElseThrow(() -> new ResourceNotFoundException("Route not found."));
		project.setRoute(route);
		project = projectRepository.save(project);
		return projectMapper.convertToDTO(project);
	}

	// Methos that deletes a route
	public void deleteProject(Long id) {
		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Project not found."));
		projectRepository.delete(project);
	}
}
