package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.RequirementMapper;
import com.codelace.codelace.model.dto.RequirementRequestDTO;
import com.codelace.codelace.model.dto.RequirementResponseDTO;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Requirement;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RequirementRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RequirementService {
	// Instance of the mapper
	private final RequirementMapper requirementMapper;

	// Instances of the repositories
	private final RequirementRepository requirementRepository;
	private final ProjectRepository projectRepository;

	// This method return all the requirements
	public List<RequirementResponseDTO> getAllRequirements() {
		List<Requirement> requirements = requirementRepository.findAll();
		return requirementMapper.convertToListDTO(requirements);
	}

	// This method return a requirement by its id
	public RequirementResponseDTO getRequirementById(Long id) {
		Requirement requirement = requirementRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Requirement not found"));
		return requirementMapper.convertToDTO(requirement);
	}

	// This method creates a requirement
	public RequirementResponseDTO createRequirement(RequirementRequestDTO requirementRequestDTO) {
		Requirement requirement = requirementMapper.convertToEntity(requirementRequestDTO);
		Project project = projectRepository.findById(requirementRequestDTO.getProject())
				.orElseThrow(() -> new ResourceNotFoundException("Project not found."));
		requirement.setProject(project);
		requirementRepository.save(requirement);
		return requirementMapper.convertToDTO(requirement);
	}

	// This method updates a requirement
	public RequirementResponseDTO updateRequirement(Long id, RequirementRequestDTO requirementRequestDTO) {
		Requirement requirement = requirementRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Requirement not found"));
		Project project = projectRepository.findById(requirementRequestDTO.getProject())
				.orElseThrow(() -> new ResourceNotFoundException("Project not found."));
		requirement.setProject(project);
		requirement.setDescription(requirementRequestDTO.getDescription());
		requirementRepository.save(requirement);
		return requirementMapper.convertToDTO(requirement);
	}

	// This method deletes a requirement
	public void deleteRequirement(Long id) {
		Requirement requirement = requirementRepository.findById(id).orElseThrow(
				() -> new ResourceNotFoundException("Requirement not found"));
		requirementRepository.delete(requirement);
	}
}
