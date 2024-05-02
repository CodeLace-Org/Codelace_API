package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.RequirementRequestDTO;
import com.codelace.codelace.model.dto.RequirementResponseDTO;
import com.codelace.codelace.model.entity.Requirement;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RequirementMapper {
	private final ModelMapper modelMapper;

	// Request Requirement DTO to Entity;
	public Requirement convertToEntity(RequirementRequestDTO requirementRequestDTO) {
		return modelMapper.map(requirementRequestDTO, Requirement.class);
	}

	// Requirement Entity to response DTO
	public RequirementResponseDTO convertToDTO(Requirement requirement) {
		return modelMapper.map(requirement, RequirementResponseDTO.class);
	}

	// List Requirement Entity to List response DTO
	public List<RequirementResponseDTO> convertToListDTO(List<Requirement> requirements) {
		return requirements.stream()
				.map(this::convertToDTO)
				.toList();
	}

}
