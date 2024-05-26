package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.PlanMapper;
import com.codelace.codelace.model.dto.PlanResponseDTO;
import com.codelace.codelace.model.entity.Plan;
import com.codelace.codelace.repository.PlanRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PlanService {
	// Instance mapper
    private final PlanMapper planMapper;

    // Instance repository
    private final PlanRepository planRepository;

    public List<PlanResponseDTO> getAllPlans() {
        List<Plan> plans = planRepository.findAll();
        return planMapper.convertToListDTO(plans);
    }

    public PlanResponseDTO getPlanById(Long id) {
        Plan plan = planRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));
        
        return planMapper.convertEntityToResponseDTO(plan);
    }

}
