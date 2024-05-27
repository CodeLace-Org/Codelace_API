package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ResourceMapper;
import com.codelace.codelace.model.dto.ResourceRequestDTO;
import com.codelace.codelace.model.dto.ResourceResponseDTO;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Resource;
import com.codelace.codelace.repository.InscriptionRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.ResourceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ResourceService {
	private final ResourceMapper resourceMapper;
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;
    private final InscriptionRepository inscriptionrepository;

    public List<ResourceResponseDTO> getAllResource(){
        List<Resource> resources = resourceRepository.findAll();
        return resourceMapper.convertToListDTO(resources);
    }

    public ResourceResponseDTO getResourceById(Long id){
        Resource resource = resourceRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Resource not found"));
        return resourceMapper.convertToDTO(resource);
    }

    public List<ResourceResponseDTO> getAllByProject(Long id){
        Project project = projectRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Project not found"));
        inscriptionrepository.findByRoute(project.getRoute()).orElseThrow(()-> new ResourceNotFoundException("Inscription not found"));
        List<Resource> resources = resourceRepository.findAllByProject(project);
        return resourceMapper.convertToListDTO(resources); 
    }

    public ResourceResponseDTO createResource(ResourceRequestDTO resourceRequestDto) {
        Project project = projectRepository.findById(resourceRequestDto.getProject()).orElseThrow(()->new ResourceNotFoundException("Project Not found"));
        if(resourceRepository.findByTitleAndProject(resourceRequestDto.getTitle(), project).isPresent()) throw new ResourceDuplicateException("This resource already exists");
        Resource resource = resourceMapper.convertToEntity(resourceRequestDto);
        resource.setProject(project);
        resourceRepository.save(resource);
        return resourceMapper.convertToDTO(resource);
    }
}
