package com.codelace.codelace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ResourceMapper;
import com.codelace.codelace.model.dto.ResourceRequestDTO;
import com.codelace.codelace.model.dto.ResourceResponseDTO;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Resource;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.ResourceRepository;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {

    @Mock
    private ProjectRepository projectrepository;

    @Mock
    private ResourceRepository resourcerepository;

    @Mock
    private ResourceMapper resourcemapper;

    @InjectMocks
    private ResourceService resourceservice;

    @Test
    public void getAllResourceTest(){
        Project project1 = new Project();
        project1.setId(1L);
        project1.setTitle("Project1");
        project1.setDescription("Description");
        project1.setImage(null);
        project1.setLevel(1);

        Project project2 = new Project();
        project2.setId(2L);
        project2.setTitle("Project2");
        project2.setDescription("Description");
        project2.setImage(null);
        project2.setLevel(1);

        Resource resource1 = new Resource();
        resource1.setId(1L);
        resource1.setTitle("Resource1");
        resource1.setLink("link.com");
        resource1.setProject(project1);

        Resource resource2 = new Resource();
        resource2.setId(2L);
        resource2.setTitle("Resource1");
        resource2.setLink("link.com");
        resource2.setProject(project2);

        List<Resource> resources = Arrays.asList(resource1, resource2);

        when(resourcerepository.findAll()).thenReturn(resources);

        ResourceResponseDTO responseDTO1 = new ResourceResponseDTO();
        responseDTO1.setId(project1.getId());
        ResourceResponseDTO responseDTO2 = new ResourceResponseDTO();
        responseDTO2.setId(project2.getId());
        
        List<ResourceResponseDTO> expectResponse = Arrays.asList(responseDTO1, responseDTO2);
        when(resourcemapper.convertToListDTO(resources)).thenReturn(expectResponse);

        List<ResourceResponseDTO> result = resourceservice.getAllResource();

        assertNotNull(result);
        assertEquals(expectResponse.size(), result.size());

        verify(resourcerepository, times(1)).findAll();
        verify(resourcemapper, times(1)).convertToListDTO(resources);
    }

    @Test
    public void getResourceByIdTest_ExistingId(){
        Long id = 1L;

        Resource resource = new Resource();
        resource.setId(1L);
        resource.setTitle("Resource1");
        resource.setLink("Link.com");

        when(resourcerepository.findById(id)).thenReturn(Optional.of(resource));

        ResourceResponseDTO responseDTO = new ResourceResponseDTO();
        responseDTO.setId(resource.getId());

        when(resourcemapper.convertToDTO(resource)).thenReturn(responseDTO);

        ResourceResponseDTO result = resourceservice.getResourceById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    public void getResourceByIdTest_NonExistingId(){
        Long id = 2L;

        Resource resource = new Resource();
        resource.setId(1L);
        resource.setTitle("Resource1");
        resource.setLink("Link.com");

        when(resourcerepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceservice.getResourceById(id));
    }

    @Test
    public void createResourceTest_Successfully(){
        ResourceRequestDTO requestDTO = new ResourceRequestDTO();
        requestDTO.setTitle("Resource1");
        requestDTO.setLink("link.com");
        requestDTO.setProject(1L);

        Project project = new Project();
        project.setId(1L);
        project.setTitle("Project1");
        project.setDescription("Description");
        project.setImage(null);
        project.setLevel(1);

        when(projectrepository.findById(requestDTO.getProject())).thenReturn(Optional.of(project));

        Resource resource = new Resource();
        resource.setId(1L);
        resource.setTitle(requestDTO.getTitle());
        resource.setLink(requestDTO.getLink());
        resource.setProject(project);

        when(resourcerepository.save(resource)).thenReturn(resource);
        when(resourcemapper.convertToEntity(requestDTO)).thenReturn(resource);
        when(resourcemapper.convertToDTO(resource)).thenReturn(new ResourceResponseDTO(resource.getId(), resource.getTitle(), resource.getLink(), resource.getProject().getId()));
        
        ResourceResponseDTO result = resourceservice.createResource(requestDTO);

        assertNotNull(result);
        assertEquals(resource.getId(), result.getId());
        assertEquals(resource.getTitle(), result.getTitle());
        assertEquals(resource.getLink(), result.getLink());
        assertEquals( resource.getProject().getId(),result.getProjectId());

        verify(projectrepository, times(1)).findById(requestDTO.getProject());
        verify(resourcerepository, times(1)).save(resource);
        verify(resourcemapper, times(1)).convertToEntity(requestDTO);
        verify(resourcemapper, times(1)).convertToDTO(resource);
    }

    @Test
    public void createResourceTest_ProjectNotFound(){
        ResourceRequestDTO requestDTO = new ResourceRequestDTO();
        requestDTO.setTitle("Project1");
        requestDTO.setLink("link.com");
        requestDTO.setProject(1L);

        when(projectrepository.findById(requestDTO.getProject())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceservice.createResource(requestDTO));
    }
}