package com.codelace.codelace.mapper;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.codelace.codelace.model.dto.PlanResponseDTO;
import com.codelace.codelace.model.entity.Plan;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PlanMapper {
	
    private ModelMapper modelMapper;

    public PlanResponseDTO convertEntityToResponseDTO(Plan plan) {
        return modelMapper.map(plan, PlanResponseDTO.class);
    }

    public List<PlanResponseDTO> convertToListDTO(List<Plan> plans) {
        return plans.stream()
                .map(this::convertEntityToResponseDTO)
                .toList();
    }
}
