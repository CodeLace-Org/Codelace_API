package com.codelace.codelace.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codelace.codelace.model.dto.ProjectDetailRequestDTO;
import com.codelace.codelace.model.dto.ProjectDetailResponseDTO;
import com.codelace.codelace.model.dto.ProjectRequestDTO;
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.service.ProjectService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/projects")
@AllArgsConstructor
public class ProjectController {
	
    private final ProjectService projectService;

    @GetMapping
    public ResponseEntity<List<ProjectResponseDTO>> getProjectsByRouteID(@RequestBody ProjectRequestDTO projectDTO){
        List<ProjectResponseDTO> project = projectService.getProjectsByRouteID(projectDTO);
        return new ResponseEntity<>(project, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ProjectDetailResponseDTO> getDetailProjectByProjectIDAndUserID(@RequestBody ProjectDetailRequestDTO projectDetailDTO){
        ProjectDetailResponseDTO projectDetail = projectService.getProjectByRouteIDAndUserID(projectDetailDTO);
        return new ResponseEntity<>(projectDetail, HttpStatus.OK);
    }
}
