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

import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.ResourceMapper;
import com.codelace.codelace.model.dto.ResourceRequestDTO;
import com.codelace.codelace.model.dto.ResourceResponseDTO;
import com.codelace.codelace.model.entity.Inscription;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Resource;
import com.codelace.codelace.model.entity.Route;
import com.codelace.codelace.repository.InscriptionRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.ResourceRepository;
import com.codelace.codelace.repository.RouteRepository;

@ExtendWith(MockitoExtension.class)
public class ResourceServiceTest {

    @Mock
    private ProjectRepository projectrepository;

    @Mock
    private ResourceRepository resourcerepository;

    @Mock
    private ResourceMapper resourcemapper;

    @Mock
    private RouteRepository routerepository;

    @Mock
    private InscriptionRepository inscriptionrepository;

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
        responseDTO1.setId(resource1.getId());
        responseDTO1.setTitle(resource1.getTitle());
        responseDTO1.setLink(resource1.getLink());
        responseDTO1.setProjectId(project1.getId());

        ResourceResponseDTO responseDTO2 = new ResourceResponseDTO();
        responseDTO2.setId(resource2.getId());
        responseDTO2.setTitle(resource2.getTitle());
        responseDTO2.setLink(resource2.getLink());
        responseDTO2.setProjectId(project2.getId());
        
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

        Project project = new Project();
        project.setId(1L);

        Resource resource = new Resource();
        resource.setId(1L);
        resource.setTitle("Resource1");
        resource.setLink("Link.com");
        resource.setProject(project);

        when(resourcerepository.findById(id)).thenReturn(Optional.of(resource));

        ResourceResponseDTO responseDTO = new ResourceResponseDTO();
        responseDTO.setId(resource.getId());
        responseDTO.setTitle(resource.getTitle());
        responseDTO.setLink(resource.getLink());
        responseDTO.setProjectId(project.getId());

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
    public void getAllResourcesByProject_Successfully(){
        Long id = 1L;

        Route route = new Route();
        route.setId(1L);

        Inscription inscription = new Inscription();
        inscription.setRoute(route);

        Project project = new Project();
        project.setId(1L);
        project.setRoute(route);

        Resource resource1 = new Resource();
        resource1.setId(1L);
        resource1.setTitle("Resource1");
        resource1.setLink("Link1.com");
        resource1.setProject(project);

        Resource resource2 = new Resource();
        resource2.setId(2L);
        resource2.setTitle("Resource2");
        resource2.setLink("Link2.com");
        resource2.setProject(project);

        when(projectrepository.findById(id)).thenReturn(Optional.of(project));
        when(inscriptionrepository.findByRoute(project.getRoute())).thenReturn(Optional.of(inscription));

        List<Resource> resources = Arrays.asList(resource1, resource2);

        when(resourcerepository.findAllByProject(project)).thenReturn(resources);

        ResourceResponseDTO responseDTO1 = new ResourceResponseDTO();
        responseDTO1.setId(resource1.getId());
        responseDTO1.setTitle(resource1.getTitle());
        responseDTO1.setLink(resource1.getLink());
        responseDTO1.setProjectId(project.getId());

        ResourceResponseDTO responseDTO2 = new ResourceResponseDTO();
        responseDTO2.setId(resource2.getId());
        responseDTO2.setTitle(resource2.getTitle());
        responseDTO2.setLink(resource2.getLink());
        responseDTO2.setProjectId(project.getId());

        List<ResourceResponseDTO> expectResponse = Arrays.asList(responseDTO1, responseDTO2);

        when(resourcemapper.convertToListDTO(resources)).thenReturn(expectResponse);

        List<ResourceResponseDTO> result = resourceservice.getAllByProject(id);

        assertNotNull(result);
        assertEquals(expectResponse.size(), result.size());
        
        verify(projectrepository, times(1)).findById(id);
        verify(inscriptionrepository, times(1)).findByRoute(route);
        verify(resourcerepository, times(1)).findAllByProject(project);
        verify(resourcemapper, times(1)).convertToListDTO(resources);
    }

    @Test
    public void getAllResourcesByProject_ProjectNotFound(){
        Long id = 1L;

        Project project = new Project();
        project.setId(999L);

        when(projectrepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> resourceservice.getAllByProject(id));
    }

    @Test
    public void getAllResourcesByProject_InscriptionNotFound(){
        Long id = 1L;

        Route route = new Route();
        route.setId(1L);

        Project project = new Project();
        project.setId(1L);
        project.setRoute(route);

        when(projectrepository.findById(id)).thenReturn(Optional.of(project));
        when(inscriptionrepository.findByRoute(project.getRoute())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceservice.getAllByProject(id));
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

        when(resourcemapper.convertToEntity(requestDTO)).thenReturn(resource);
        when(resourcerepository.save(resource)).thenReturn(resource);
        when(resourcemapper.convertToDTO(resource)).thenReturn(new ResourceResponseDTO(resource.getId(), resource.getTitle(), resource.getLink(), resource.getProject().getId()));
        
        ResourceResponseDTO result = resourceservice.createResource(requestDTO);

        assertNotNull(result);
        assertEquals(resource.getId(), result.getId());
        assertEquals(resource.getTitle(), result.getTitle());
        assertEquals(resource.getLink(), result.getLink());
        assertEquals(resource.getProject().getId(),result.getProjectId());

        verify(projectrepository, times(1)).findById(requestDTO.getProject());
        verify(resourcemapper, times(1)).convertToEntity(requestDTO);
        verify(resourcerepository, times(1)).save(resource);
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

    @Test
    public void createResourceTest_ResourceAlreadyExists(){
        ResourceRequestDTO requestDTO = new ResourceRequestDTO();
        requestDTO.setTitle("Project1");
        requestDTO.setLink("link.com");
        requestDTO.setProject(1L);

        Project project =  new Project();
        project.setId(1L);

        Resource resource1 = new Resource();
        resource1.setId(1L);
        resource1.setTitle("Project1");
        resource1.setLink("link.com");
        resource1.setProject(project);

        when(projectrepository.findById(requestDTO.getProject())).thenReturn(Optional.of(project));
        when(resourcerepository.findByTitleAndProject(requestDTO.getTitle(), project)).thenReturn(Optional.of(resource1));

        assertThrows(ResourceDuplicateException.class, () -> resourceservice.createResource(requestDTO));

        verify(projectrepository, times(1)).findById(requestDTO.getProject());
        verify(resourcerepository, times(1)).findByTitleAndProject(requestDTO.getTitle(), project);
    }
}
