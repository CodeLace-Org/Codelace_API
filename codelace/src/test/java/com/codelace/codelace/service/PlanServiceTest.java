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
import com.codelace.codelace.mapper.PlanMapper;
import com.codelace.codelace.model.dto.PlanResponseDTO;
import com.codelace.codelace.model.entity.Plan;
import com.codelace.codelace.repository.PlanRepository;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {
    
    @Mock
    private PlanMapper planMapper;

    @Mock
    private PlanRepository planRepository;

    @InjectMocks
    private PlanService planService;

    @Test
    public void testGetAllPlans() {
        // Arrange
        Plan plan1 = new Plan();
        Plan plan2 = new Plan();
        plan1.setId(1L);
        plan2.setId(2L);

        List<Plan> plans = Arrays.asList(plan1, plan2);

        when(planRepository.findAll()).thenReturn(plans);

        PlanResponseDTO responseDTO1 = new PlanResponseDTO();
        PlanResponseDTO responseDTO2 = new PlanResponseDTO();
        responseDTO1.setId(plan1.getId());
        responseDTO2.setId(plan2.getId());

        List<PlanResponseDTO> expectedResponse = Arrays.asList(responseDTO1, responseDTO2);

        when(planMapper.convertToListDTO(plans)).thenReturn(expectedResponse);

        // Act
        List<PlanResponseDTO> result = planService.getAllPlans();

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse.size(), result.size());

        // Verify
        verify(planRepository, times(1)).findAll();
        verify(planMapper, times(1)).convertToListDTO(plans);
    
    }

    @Test
    public void testGetPlanById_ExistingId() {

        // Arrange
        Long id = 1L;
        Plan plan = new Plan();
        plan.setId(id);

        when(planRepository.findById(id)).thenReturn(Optional.of(plan));

        PlanResponseDTO responseDTO = new PlanResponseDTO();
        responseDTO.setId(plan.getId());

        when(planMapper.convertEntityToResponseDTO(plan)).thenReturn(responseDTO);

        // Act
        PlanResponseDTO result = planService.getPlanById(id);

        // Assert
        assertNotNull(result);
        assertEquals(plan.getId(), result.getId());

        // Verify
        verify(planRepository, times(1)).findById(id);
        verify(planMapper, times(1)).convertEntityToResponseDTO(plan);
    
    }

    @Test
    public void testGetPlanById_NonExistingId() {
        // Arrange
        Long id = 999L;

        when(planRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> planService.getPlanById(id));

    }
}
