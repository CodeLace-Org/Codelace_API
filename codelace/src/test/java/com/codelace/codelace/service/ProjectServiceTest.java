package com.codelace.codelace.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ProjectMapper;
import com.codelace.codelace.mapper.ResourceMapper;
import com.codelace.codelace.model.dto.ProjectRequestDTO;
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.model.dto.ResourceResponseDTO;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Resource;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.ResourceRepository;
import com.codelace.codelace.repository.RouteRepository;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
	// Instance mapper
	@Mock
	private ProjectMapper projectMapper;
	// Instances of the repositories
	@Mock
	private ProjectRepository projectRepository;
	@Mock
	private RouteRepository routeRepository;
	@Mock
	private ResourceRepository resourceRepository;
	@Mock
	private ResourceMapper resourceMapper;

	// Test Service
	@InjectMocks
	private ProjectService projectService;

	// Tests

	// Method that returns all the projects
	@Test
	public void testGetAllProjects() {

		// Arrange
		Project project1 = new Project();
		project1.setId(1L);
		project1.setTitle("Title");
		project1.setDescription("Description");
		project1.setImage(null);
		project1.setLevel(1);

		Project project2 = new Project();
		project2.setId(2L);
		project2.setTitle("Title");
		project2.setDescription("Description");
		project2.setImage(null);
		project2.setLevel(1);

		List<Project> projects = List.of(project1, project2);
		// Mocking Repository
		when(projectRepository.findAll()).thenReturn(projects);

		// Mocking mapper
		ProjectResponseDTO responseDTO1 = new ProjectResponseDTO();
		responseDTO1.setId(project1.getId());
		ProjectResponseDTO responseDTO2 = new ProjectResponseDTO();
		responseDTO2.setId(project2.getId());

		List<ProjectResponseDTO> expectedResponse = Arrays.asList(responseDTO1, responseDTO2);
		when(projectMapper.convertToListDTO(projects)).thenReturn(expectedResponse);

		// Act
		List<ProjectResponseDTO> result = projectService.getAllProjects();

		// Assert
		assertNotNull(result);
		assertEquals(expectedResponse.size(), result.size());

		// Verify that repository and mapper methods were called
		verify(projectRepository, times(1)).findAll();
		verify(projectMapper, times(1)).convertToListDTO(projects);
	}

	// Method that returns a project by id
	@Test
	void testGetProjectById_Succesfully() {
		Long id = 1L;

		Project project = new Project();
		project.setId(id);

		// Arrange
		when(projectRepository.findById(id)).thenReturn(Optional.of(project));

		// Mocking mapper
		ProjectResponseDTO responseDTO = new ProjectResponseDTO();
		responseDTO.setId(project.getId());
		when(projectMapper.convertToDTO(project)).thenReturn(responseDTO);

		// Act
		ProjectResponseDTO projectResponseDTO = projectService.getProjectById(id);

		// Assert
		assertNotNull(projectResponseDTO);
		assertEquals(id, projectResponseDTO.getId());
	}

	@Test
	void testGetProjectById_ProjectNotFound() {
		// Arrange
		Long id = 1L;
		Project project = new Project();
		project.setId(id);

		// Mocking Repository
		when(projectRepository.findById(id)).thenReturn(Optional.empty());
		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> projectService.getProjectById(id));
	}

	// Method that creates a project
	@Test
	public void testCreateProject_Successfully() {
		// Arrange
		ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO();
		projectRequestDTO.setTitle("Title");
		projectRequestDTO.setDescription("Description");
		projectRequestDTO.setImage(null);
		projectRequestDTO.setLevel(1);
		projectRequestDTO.setRoute(1L);

		ProjectResponseDTO projectResponseDTO = new ProjectResponseDTO();
		projectResponseDTO.setId(1L);
		projectResponseDTO.setTitle("Title");
		projectResponseDTO.setDescription("Description");
		projectResponseDTO.setImage(null);
		projectResponseDTO.setLevel(1);

		Route route = new Route();
		route.setId(1L);
		route.setName("Name");
		route.setDescription("Description route");
		route.setIcon("<svg>Icon<svg>");

		Project project = new Project();
		project.setId(1L);
		project.setTitle("Title");
		project.setDescription("Description");
		project.setImage(null);
		project.setLevel(1);

		// Mock Repository
		when(projectRepository.save(project)).thenReturn(project);
		when(routeRepository.findById(projectRequestDTO.getRoute())).thenReturn(Optional.of(route));

		// Mock Mapper
		when(projectMapper.convertToEntity(projectRequestDTO)).thenReturn(project);
		when(projectMapper.convertToDTO(project)).thenReturn(projectResponseDTO);

		// Act
		ProjectResponseDTO result = projectService.createProject(projectRequestDTO);

		// Assert
		assertNotNull(result);
		assertEquals(project.getId(), result.getId());
		assertEquals(project.getTitle(), result.getTitle());
		assertEquals(project.getDescription(), result.getDescription());
		assertEquals(project.getImage(), result.getImage());
		assertEquals(project.getLevel(), result.getLevel());

		// Verify
		verify(projectMapper, times(1)).convertToEntity(projectRequestDTO);
		verify(projectRepository, times(1)).save(project);
		verify(projectMapper, times(1)).convertToDTO(project);
	}

	@Test
	public void testCreateProject_RouteNotFound() {
		// Arrange
		ProjectRequestDTO projectRequestDTO = new ProjectRequestDTO();
		projectRequestDTO.setTitle("Title");
		projectRequestDTO.setDescription("Description");
		projectRequestDTO.setImage(null);
		projectRequestDTO.setLevel(1);
		projectRequestDTO.setRoute(1L);

		Project project = new Project();
		project.setId(1L);

		// Mock Repository
		when(routeRepository.findById(projectRequestDTO.getRoute())).thenReturn(Optional.empty());

		// Mock Mapper
		when(projectMapper.convertToEntity(projectRequestDTO)).thenReturn(project);

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> projectService.createProject(projectRequestDTO));
	}

	// Method that deletes a route
	@Test
	public void testDeleteProject_Succesfully() {
		// Arrange
		Long id = 1L;
		Project project = new Project();
		project.setId(id);

		// Mock Repository
		when(projectRepository.findById(id)).thenReturn(Optional.of(project));

		// Act
		assertDoesNotThrow(() -> projectService.deleteProject(id));
	}

	@Test
	public void testDeleteProject_ResourceNotFound() {
		// Arrange
		Long id = 1L;

		// Mock Repository
		when(projectRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> projectService.deleteProject(id));
	}

	@Test
	public void testGetResourcesByProject_Succesfully() {
		// Arrange
		Long id = 1L;
		Project project = new Project();
		project.setId(id);
		Resource r1 = new Resource();
		r1.setId(1L);
		Resource r2 = new Resource();
		r2.setId(2L);

		ResourceResponseDTO res1 = new ResourceResponseDTO();
		r1.setId(1L);
		ResourceResponseDTO res2 = new ResourceResponseDTO();
		r2.setId(2L);

		List<Resource> resources = Arrays.asList(r1, r2);
		List<ResourceResponseDTO> resourcesResponse = Arrays.asList(res1, res2);

		// Mock Repository
		when(projectRepository.findById(id)).thenReturn(Optional.of(project));
		when(resourceRepository.findAllByProject(project)).thenReturn(resources);
		// Mock Mapper
		when(resourceMapper.convertToListDTO(resources)).thenReturn(resourcesResponse);

		// Act
		List<ResourceResponseDTO> response = projectService.getResourcesByProject(id);

		// Assert
		assertNotNull(response);
		assertEquals(resources.size(), response.size());

		// Verify
		verify(projectRepository, times(1)).findById(id);
		verify(resourceRepository, times(1)).findAllByProject(project);
	}

	@Test
	public void testGetResourcesByProject_ProjectNotFound() {
		// Arrange
		Long id = 1L;
		// Mock Repository
		when(projectRepository.findById(id)).thenReturn(Optional.empty());
		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> projectService.getResourcesByProject(id));
	}

}
