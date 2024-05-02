package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ProjectMapper;
import com.codelace.codelace.mapper.ResourceMapper;
import com.codelace.codelace.model.dto.ProjectRequestDTO;
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.model.dto.ResourceRespondDTO;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Resource;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.ResourceRepository;
import com.codelace.codelace.repository.RouteRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProjectService {
	// Instance mapper
	private final ProjectMapper projectMapper;

	// Instances of the repositories
	private final ProjectRepository projectRepository;
	private final RouteRepository routeRepository;
	private final ResourceRepository resourceRepository;
	private final ResourceMapper resourceMapper;

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
		Route route = routeRepository.findById(projectRequestDTO.getRoute())
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

	public List<ResourceRespondDTO> getResourcesByProject(Long id){
		Project project = projectRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Not found"));
		List<Resource> resources = resourceRepository.findAllByProject(project);
		return resourceMapper.convertToListDTO(resources);
	}
}
