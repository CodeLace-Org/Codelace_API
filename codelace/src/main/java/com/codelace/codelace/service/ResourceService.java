package com.codelace.codelace.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ResourceMapper;
import com.codelace.codelace.model.dto.ResourceRequestDTO;
import com.codelace.codelace.model.dto.ResourceResponseDTO;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Resource;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.ResourceRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ResourceService {
	private final ResourceMapper resourceMapper;
    private final ResourceRepository resourceRepository;
    private final ProjectRepository projectRepository;

    public List<ResourceResponseDTO> getAllResource(){
        List<Resource> resources = resourceRepository.findAll();
        return resourceMapper.convertToListDTO(resources);
    }

    public ResourceResponseDTO getResourceById(Long id){
        Resource resource = resourceRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Resource not fount"));
        return resourceMapper.convertToDTO(resource);
    }

    public ResourceResponseDTO createResource(ResourceRequestDTO resourceRequestDto) {
        Resource resource = resourceMapper.convertToEntity(resourceRequestDto);
        Project project = projectRepository.findById(resourceRequestDto.getProject()).orElseThrow(()->new ResourceNotFoundException("Not found"));
        resource.setProject(project);
        resourceRepository.save(resource);
        return resourceMapper.convertToDTO(resource);
    }
}
