package com.codelace.codelace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.PlanResponseDTO;
import com.codelace.codelace.service.PlanService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/plans")
@AllArgsConstructor
public class PlanController {
    private final PlanService planService;

    @GetMapping
    public ResponseEntity<List<PlanResponseDTO>> getAllPlans() {
        List<PlanResponseDTO> plans = planService.getAllPlans();
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponseDTO> getPlanById(@PathVariable Long id) {
        PlanResponseDTO plan = planService.getPlanById(id);
        return new ResponseEntity<>(plan, HttpStatus.OK);
    }
}
