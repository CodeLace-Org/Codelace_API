package com.codelace.codelace.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ProjectMapper;
import com.codelace.codelace.model.dto.ProgressResponseDTO;
import com.codelace.codelace.model.dto.ProjectRequestDTO;
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.model.entity.Progress;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Requirement;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.InscriptionRepository;
import com.codelace.codelace.repository.ProgressRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RequirementRepository;
import com.codelace.codelace.repository.StudentRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProjectService {
	private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;

    private final StudentRepository studentRepository;
    private final InscriptionRepository inscriptionRepository;
    private final RequirementRepository requirementRepository;
    private final ProgressRepository progressRepository;

    // Method that returns a Project by id
    public ProjectResponseDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found."));
        return projectMapper.convertProjectToResponse(project);
    }

    // Method that returns a Project by RequestDTO
    public ProjectResponseDTO getProjectByRequestDTO(ProjectRequestDTO projectRequestDTO) {
        Long projectId = projectRequestDTO.getId();
        Long studentId = projectRequestDTO.getStudent();

        Student student = studentRepository.findById(studentId).
                orElseThrow(() -> new ResourceNotFoundException("Student not found."));

        Project project = projectRepository.findById(projectId).
                orElseThrow(() -> new ResourceNotFoundException("Project not found."));

        Route route = project.getRoute();

        inscriptionRepository.findByStudentAndRoute(student, route).
                orElseThrow(() -> new ResourceNotFoundException("Inscription not found."));

        List<Requirement> requirements = requirementRepository.findAllByProject(project);

        List<ProgressResponseDTO> progressResponseDTOList = new ArrayList<>();

        for (Requirement requirement : requirements) {
            Progress progress = progressRepository.findByStudentAndRequirement(student, requirement);

            ProgressResponseDTO progressResponseDTO = new ProgressResponseDTO();

            progressResponseDTO.setId(progress.getId());
            progressResponseDTO.setCompleted(progress.getCompleted());
            progressResponseDTO.setDescription(requirement.getDescription());

            progressResponseDTOList.add(progressResponseDTO);
        }

        ProjectResponseDTO projectResponseDTO = projectMapper.convertProjectToResponse(project);

        projectResponseDTO.setProgress(progressResponseDTOList);

        return projectResponseDTO;
    }


}
