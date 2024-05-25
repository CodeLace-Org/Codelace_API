package com.codelace.codelace.service;

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
import com.codelace.codelace.mapper.RequirementMapper;
import com.codelace.codelace.model.dto.RequirementRequestDTO;
import com.codelace.codelace.model.dto.RequirementResponseDTO;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Requirement;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RequirementRepository;

@ExtendWith(MockitoExtension.class)
public class RequirementsServiceTest {
	@Mock
	private ProjectRepository projectRepository;

	@Mock
	private RequirementRepository requirementRepository;

	@Mock
	private RequirementMapper requirementMapper;

	@InjectMocks
	private RequirementService requirementService;

	@Test
	public void testGetAllRequirements() {
		// Arrange
		Requirement requirement1 = new Requirement();
		requirement1.setId(1L);
		requirement1.setDescription("Requirement 1");
		requirement1.setProject(new Project());

		Requirement requirement2 = new Requirement();
		requirement2.setId(2L);
		requirement2.setDescription("Requirement 2");
		requirement2.setProject(new Project());

		List<Requirement> requirements = Arrays.asList(requirement1, requirement2);

		// Mock repository
		when(requirementRepository.findAll()).thenReturn(requirements);

		// Mock mapper
		RequirementResponseDTO dto1 = new RequirementResponseDTO();
		dto1.setId(1L);
		dto1.setDescription("Requirement 1");
		dto1.setProjectId(12L);

		RequirementResponseDTO dto2 = new RequirementResponseDTO();
		dto2.setId(2L);
		dto2.setDescription("Requirement 2");
		dto2.setProjectId(13L);

		List<RequirementResponseDTO> expected = Arrays.asList(dto1, dto2);

		when(requirementMapper.convertToListDTO(requirements)).thenReturn(expected);

		// Act
		List<RequirementResponseDTO> response = requirementService.getAllRequirements();

		// Assert
		assertNotNull(response);
		assertEquals(expected, response);

		// Verify
		verify(requirementRepository, times(1)).findAll();
		verify(requirementMapper, times(1)).convertToListDTO(requirements);

	}

	@Test
	public void testGetRequirementById() {
		// Arrange
		Long id = 1L;
		Requirement requirement = new Requirement();
		requirement.setId(id);
		requirement.setDescription("Requirement 1");
		requirement.setProject(new Project());

		// Mock repository
		when(requirementRepository.findById(id)).thenReturn(Optional.of(requirement));

		// Mock mapper
		RequirementResponseDTO expected = new RequirementResponseDTO();
		expected.setId(id);
		expected.setDescription("Requirement 1");
		expected.setProjectId(12L);

		when(requirementMapper.convertToDTO(requirement)).thenReturn(expected);

		// Act
		RequirementResponseDTO response = requirementService.getRequirementById(id);

		// Assert
		assertNotNull(response);
		assertEquals(expected, response);

		// Verify
		verify(requirementRepository, times(1)).findById(id);
		verify(requirementMapper, times(1)).convertToDTO(requirement);
	}

	@Test
	public void testGetRequirementById_RequirementNotFound() {
		// Arrange
		Long id = 1L;

		// Mock repository
		when(requirementRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> requirementService.getRequirementById(id));
	}

	@Test
	public void testCreateRequirement() {
		// Arrange
		RequirementRequestDTO requirementRequestDTO = new RequirementRequestDTO();
		requirementRequestDTO.setDescription("Requirement 1");
		requirementRequestDTO.setProject(12L);

		Requirement requirement = new Requirement();
		requirement.setId(1L);
		requirement.setDescription("Requirement 1");
		requirement.setProject(new Project());

		// Mock repository
		when(requirementMapper.convertToEntity(requirementRequestDTO)).thenReturn(requirement);

		Project project = new Project();
		project.setId(12L);

		when(projectRepository.findById(12L)).thenReturn(Optional.of(project));

		// Mock mapper
		RequirementResponseDTO expected = new RequirementResponseDTO();
		expected.setId(1L);
		expected.setDescription("Requirement 1");
		expected.setProjectId(12L);

		when(requirementMapper.convertToDTO(requirement)).thenReturn(expected);

		// Act
		RequirementResponseDTO response = requirementService.createRequirement(requirementRequestDTO);

		// Assert
		assertNotNull(response);
		assertEquals(expected, response);

		// Verify
		verify(requirementMapper, times(1)).convertToEntity(requirementRequestDTO);
		verify(projectRepository, times(1)).findById(12L);
		verify(requirementRepository, times(1)).save(requirement);
		verify(requirementMapper, times(1)).convertToDTO(requirement);
	}

	@Test
	public void testCreateRequirement_ProjectNotFound() {
		// Arrange
		RequirementRequestDTO requirementRequestDTO = new RequirementRequestDTO();
		requirementRequestDTO.setDescription("Requirement 1");
		requirementRequestDTO.setProject(12L);

		Requirement requirement = new Requirement();
		requirement.setId(1L);
		requirement.setDescription("Requirement 1");
		requirement.setProject(new Project());

		// Mock repository
		when(requirementMapper.convertToEntity(requirementRequestDTO)).thenReturn(requirement);

		// Mock repository
		when(projectRepository.findById(12L)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class,
				() -> requirementService.createRequirement(requirementRequestDTO));
	}

	@Test
	public void testUpdateRequirement() {
		// Arrange
		Long id = 1L;
		RequirementRequestDTO requirementRequestDTO = new RequirementRequestDTO();
		requirementRequestDTO.setDescription("Requirement 1");
		requirementRequestDTO.setProject(12L);

		Requirement requirement = new Requirement();
		requirement.setId(id);
		requirement.setDescription("Requirement 1");
		requirement.setProject(new Project());

		// Mock repository
		when(requirementRepository.findById(id)).thenReturn(Optional.of(requirement));

		Project project = new Project();
		project.setId(12L);

		when(projectRepository.findById(12L)).thenReturn(Optional.of(project));

		// Mock mapper
		RequirementResponseDTO expected = new RequirementResponseDTO();
		expected.setId(id);
		expected.setDescription("Requirement 1");
		expected.setProjectId(12L);

		when(requirementMapper.convertToDTO(requirement)).thenReturn(expected);

		// Act
		RequirementResponseDTO response = requirementService.updateRequirement(id, requirementRequestDTO);

		// Assert
		assertNotNull(response);
		assertEquals(expected, response);

		// Verify
		verify(requirementRepository, times(1)).findById(id);
		verify(requirementRepository, times(1)).save(requirement);
		verify(requirementMapper, times(1)).convertToDTO(requirement);
	}

	@Test
	public void testUpdateRequirement_RequirementNotFound() {
		// Arrange
		Long id = 1L;
		RequirementRequestDTO requirementRequestDTO = new RequirementRequestDTO();
		requirementRequestDTO.setDescription("Requirement 1");
		requirementRequestDTO.setProject(12L);

		// Mock repository
		when(requirementRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class,
				() -> requirementService.updateRequirement(id, requirementRequestDTO));
	}

	@Test
	public void testUpdateRequirement_ProjectNotFound() {
		// Arrange
		Long id = 1L;
		RequirementRequestDTO requirementRequestDTO = new RequirementRequestDTO();
		requirementRequestDTO.setDescription("Requirement 1");
		requirementRequestDTO.setProject(12L);

		Requirement requirement = new Requirement();
		requirement.setId(id);
		requirement.setDescription("Requirement 1");
		requirement.setProject(new Project());

		// Mock repository
		when(requirementRepository.findById(id)).thenReturn(Optional.of(requirement));

		// Mock repository
		when(projectRepository.findById(12L)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class,
				() -> requirementService.updateRequirement(id, requirementRequestDTO));
	}

	@Test
	public void testDeleteRequirement() {
		// Arrange
		Long id = 1L;
		Requirement requirement = new Requirement();
		requirement.setId(id);
		requirement.setDescription("Requirement 1");
		requirement.setProject(new Project());

		// Mock repository
		when(requirementRepository.findById(id)).thenReturn(Optional.of(requirement));

		// Act
		requirementService.deleteRequirement(id);

		// Verify
		verify(requirementRepository, times(1)).findById(id);
		verify(requirementRepository, times(1)).delete(requirement);
	}

	@Test
	public void testDeleteRequirement_RequirementNotFound() {
		// Arrange
		Long id = 1L;

		// Mock repository
		when(requirementRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> requirementService.deleteRequirement(id));
	}
}
