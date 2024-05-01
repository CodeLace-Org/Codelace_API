package com.codelace.codelace.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ProjectDetailMapper;
import com.codelace.codelace.mapper.ProjectMapper;
import com.codelace.codelace.model.dto.ProjectDetailRequestDTO;
import com.codelace.codelace.model.dto.ProjectDetailResponseDTO;
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.model.entity.Inscription;
import com.codelace.codelace.model.entity.Progress;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Requirement;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.InscriptionRepository;
import com.codelace.codelace.repository.ProgressRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RequirementRepository;
import com.codelace.codelace.repository.RouteRepository;
import com.codelace.codelace.repository.StudentRepository;

import lombok.AllArgsConstructor;
@Service
@AllArgsConstructor
public class ProjectService {
	
    private final RequirementRepository requirementRepository;
    private final ProgressRepository progressRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectDetailMapper projectDetailMapper;
    private final StudentRepository studentRepository;
    private final RouteRepository routeRepository;
    private final InscriptionRepository inscriptionRepository;

    public ProjectDetailResponseDTO getProjectDetail(ProjectDetailRequestDTO projectDetailRequestDTO){
        
        Long studentId = projectDetailRequestDTO.getUserID();
        Long projectId = projectDetailRequestDTO.getProjectID();

        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Student not found."));

        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new ResourceNotFoundException("Project not found."));
        
        Route route = project.getRoute();

        Inscription inscription = inscriptionRepository.findByStudentAndRoute(student, route)
            .orElseThrow(() -> new ResourceNotFoundException("Inscription not found."));

        ProjectResponseDTO projectResponseDTO = projectMapper.convertToDTO(project);

        ProjectDetailResponseDTO projectDetailResponseDTO = projectDetailMapper.convertProjectDetailToResponse(project);

        //projectDetailResponseDTO.

        List<Requirement> requirements = requirementRepository.findAllByProject(project);
        
        
        

        //return requirementList.stream().map(projectDetailMapper::convertToListDTO).toList();
    }


}