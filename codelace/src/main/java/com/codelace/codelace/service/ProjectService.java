package com.codelace.codelace.service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ProjectDetailMapper;
import com.codelace.codelace.mapper.ProjectMapper;
import com.codelace.codelace.model.dto.ProjectDetailRequestDTO;
import com.codelace.codelace.model.dto.ProjectDetailResponseDTO;
import com.codelace.codelace.model.dto.ProjectRequestDTO;
import com.codelace.codelace.model.dto.ProjectResponseDTO;
import com.codelace.codelace.model.entity.Progress;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Requirement;
import com.codelace.codelace.model.objects.RequirementList;
import com.codelace.codelace.repository.PostRepository;
import com.codelace.codelace.repository.ProgressRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.RequirementRepository;
import com.codelace.codelace.repository.ResourceRepository;

import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@AllArgsConstructor
public class ProjectService {
	
    private final RequirementRepository requirementRepository;
    private final ProgressRepository progressRepository;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ProjectDetailMapper projectDetailMapper;

    public List<ProjectResponseDTO> getProjectsByRouteID(ProjectRequestDTO projectDTO){
        List<Project> projects = projectRepository.findProjectsByRoute(projectDTO.getRouteID());
        return projectMapper.convertToListDTO(projects);
    }
    public List<ProjectDetailResponseDTO> getProjectByRouteIDAndUserID(ProjectDetailRequestDTO projectDetailDTO){
        Project project = projectRepository.findById(projectDetailDTO.getProjectID())
            .orElseThrow(() -> new ResourceNotFoundException("Doesn't exist project with this ID"));
        
        List<Requirement> requirements = requirementRepository.findByProjectID(projectDetailDTO.getProjectID());
        List<RequirementList> requirementList = new ArrayList<>();
        for (Requirement requirement : requirements) {
            List<Progress> progress = progressRepository.findByRequirementIDAndStudentID(requirement.getId(), projectDetailDTO.getUserID());
            RequirementList aux = new RequirementList(requirement.getId(), requirement.getDescription(), progress.get(0).isCompleted());
            requirementList.add(aux);
        }
        return requirementList.stream().map(projectDetailMapper::convertToListDTO).toList();
    }
}