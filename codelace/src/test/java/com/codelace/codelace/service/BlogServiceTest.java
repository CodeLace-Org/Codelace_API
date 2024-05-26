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

import com.codelace.codelace.exception.ResourceDuplicateException;
import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.BlogMapper;
import com.codelace.codelace.mapper.ProjectMapper;
import com.codelace.codelace.model.dto.BlogRequestDTO;
import com.codelace.codelace.model.dto.BlogResponseDTO;
import com.codelace.codelace.model.entity.Blog;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.repository.BlogRepository;
import com.codelace.codelace.repository.ProjectRepository;


@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {

    @Mock   
    private ProjectRepository projectRepository;

    @Mock
    private ProjectMapper projectMapper;

    @Mock
    private BlogRepository blogRepository;

    @Mock
    private BlogMapper blogMapper;

    @InjectMocks
    private BlogService blogService;

    @Test
    public void testGetAllBlogs(){

        // Arrange
        Blog blog1 = new Blog();
        blog1.setId(1L);
        Blog blog2 = new Blog();
        blog2.setId(2L);

        List<Blog> blogs = Arrays.asList(blog1, blog2);

        // Mocking repository
        when(blogRepository.findAll()).thenReturn(blogs);

        // Mocking mapper
        BlogResponseDTO responseDTO1 = new BlogResponseDTO();
        BlogResponseDTO responseDTO2 = new BlogResponseDTO();
        responseDTO1.setId(blog1.getId());
        responseDTO2.setId(blog2.getId());

        List<BlogResponseDTO> expectedResponse = Arrays.asList(responseDTO1, responseDTO2);

        when(blogMapper.convertToResponse(blogs)).thenReturn(expectedResponse);

        // Act
        List<BlogResponseDTO> result = blogService.getAllBlogs();

        // Assert
        assertNotNull(result);
        assertEquals(expectedResponse, result);

        // Verify
        verify(blogRepository, times(1)).findAll();
        verify(blogMapper, times(1)).convertToResponse(blogs);
        
    }

    @Test
    public void testGetBlog_Successfull(){
        Long id = 1L;
        Blog blog = new Blog();
        blog.setId(id);

        when(blogRepository.findById(id)).thenReturn(Optional.of(blog));

        BlogResponseDTO responseDTO = new BlogResponseDTO();
        responseDTO.setId(blog.getId());

        when(blogMapper.convertToResponse(blog)).thenReturn(responseDTO);

        // Act
        BlogResponseDTO result = blogService.getBlogById(id);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);

        // Verify
        verify(blogRepository, times(1)).findById(id);
        verify(blogMapper, times(1)).convertToResponse(blog);

    }

    @Test
    public void testGetBlog_BlogNotFound(){
        Long id = 1L;
        Blog blog = new Blog();
        blog.setId(id);

        when(blogRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> blogService.getBlogById(id));

        // Verify
        verify(blogRepository, times(1)).findById(id);
    }

    @Test
    public void testCreateBlog_Successfull(){

        // Arrange
        BlogRequestDTO requestDTO = new BlogRequestDTO();
        requestDTO.setTitle("Title");
        requestDTO.setProject(1L);

        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.of(project));
        when(blogRepository.findByTitle(requestDTO.getTitle())).thenReturn(Optional.empty());

        Blog blog = new Blog();
        blog.setTitle(requestDTO.getTitle());

        when(blogMapper.convertToEntity(requestDTO)).thenReturn(blog);
        when(blogRepository.save(blog)).thenReturn(blog);

        BlogResponseDTO responseDTO = new BlogResponseDTO();
        responseDTO.setTitle(blog.getTitle());

        when(blogMapper.convertToResponse(blog)).thenReturn(responseDTO);
        
        // Act
        BlogResponseDTO result = blogService.createBlog(requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(responseDTO, result);

        // Verify
        verify(projectRepository, times(1)).findById(requestDTO.getProject());
        verify(blogRepository, times(1)).findByTitle(requestDTO.getTitle());
        verify(blogMapper, times(1)).convertToEntity(requestDTO);
        verify(blogRepository, times(1)).save(blog);
        verify(blogMapper, times(1)).convertToResponse(blog);

    }

     @Test
     public void testCreateBlog_ProjectNotFound(){
         // Arrange
         BlogRequestDTO requestDTO = new BlogRequestDTO();
         requestDTO.setTitle("Title");
         requestDTO.setProject(1L);

         when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.empty());

         // Assert
         assertThrows(ResourceNotFoundException.class, () -> blogService.createBlog(requestDTO));

         // Verify
         verify(projectRepository, times(1)).findById(requestDTO.getProject());
     }

     @Test
    public void testCreateBlog_TitleAlreadyInUse(){
        // Arrange
        BlogRequestDTO requestDTO = new BlogRequestDTO();
        requestDTO.setTitle("Title");
        requestDTO.setProject(1L);

        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.of(project));
        when(blogRepository.findByTitle(requestDTO.getTitle())).thenReturn(Optional.of(new Blog()));

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> blogService.createBlog(requestDTO));

        // Verify
        verify(projectRepository, times(1)).findById(requestDTO.getProject());
        verify(blogRepository, times(1)).findByTitle(requestDTO.getTitle());
    }

    @Test
    public void testUpdateBlog_Successfull(){

        // Arrange
        Long id = 1L;

        Blog blog = new Blog();
        blog.setId(id);
        blog.setTitle("Blog Title");

        when(blogRepository.findById(id)).thenReturn(Optional.of(blog));

        BlogRequestDTO requestDTO = new BlogRequestDTO();
        requestDTO.setTitle("Title");
        requestDTO.setProject(1L);

        when(blogRepository.findByTitle(requestDTO.getTitle())).thenReturn(Optional.empty());

        Project project = new Project();
        project.setId(1L);

        when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.of(project));
        when(blogRepository.findByTitle(requestDTO.getTitle())).thenReturn(Optional.empty());
        when(blogRepository.save(blog)).thenReturn(blog);

        BlogResponseDTO responseDTO = new BlogResponseDTO();
        responseDTO.setTitle(blog.getTitle());

        when(blogMapper.convertToResponse(blog)).thenReturn(responseDTO);

        // Act
        BlogResponseDTO result = blogService.updateBlog(id, requestDTO);

        // Assert
        assertNotNull(result);
        assertEquals(blog.getTitle(), result.getTitle());

        // Verify
        verify(blogRepository, times(1)).findById(id);
        verify(blogRepository, times(1)).findByTitle(requestDTO.getTitle());
        verify(projectRepository, times(1)).findById(requestDTO.getProject());
        verify(blogMapper, times(1)).convertToResponse(blog);
        verify(blogRepository, times(1)).save(blog);

    }

    @Test
    public void testUpdateBlog_BlogNotFound(){
        Long id = 1L;

        when(blogRepository.findById(id)).thenReturn(Optional.empty());

        BlogRequestDTO requestDTO = new BlogRequestDTO();
        requestDTO.setTitle("Title");

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> blogService.updateBlog(id, requestDTO));

        // Verify
        verify(blogRepository, times(1)).findById(id);
    }

    @Test
    public void testUpdateBlog_ProjectNotFound(){
        Long id = 1L;

        Blog blog = new Blog();
        blog.setId(id);

        when(blogRepository.findById(id)).thenReturn(Optional.of(blog));

        BlogRequestDTO requestDTO = new BlogRequestDTO();
        requestDTO.setTitle("Title");
        requestDTO.setProject(1L);

        when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.empty());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> blogService.updateBlog(id, requestDTO));

        // Verify
        verify(blogRepository, times(1)).findById(id);
        verify(projectRepository, times(1)).findById(requestDTO.getProject());

    }

    @Test
    public void testUpdateBlog_TitleAlreadyInUse(){

        // Arrange
        Long id = 1L;

        Blog blog = new Blog();
        blog.setId(id);
        blog.setTitle("Blog Title");

        when(blogRepository.findById(id)).thenReturn(Optional.of(blog));

        BlogRequestDTO requestDTO = new BlogRequestDTO();
        requestDTO.setTitle("Title");
        requestDTO.setProject(1L);

        when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.of(new Project()));
        when(blogRepository.findByTitle(requestDTO.getTitle())).thenReturn(Optional.of(new Blog()));

        // Assert
        assertThrows(ResourceDuplicateException.class, () -> blogService.updateBlog(id, requestDTO));

        // Verify
        verify(blogRepository, times(1)).findById(id);
        verify(projectRepository, times(1)).findById(requestDTO.getProject());
        verify(blogRepository, times(1)).findByTitle(requestDTO.getTitle());
    }

    @Test
    public void testDeleteBlog_Successfull(){
        Long id = 1L;

        Blog blog = new Blog();
        blog.setId(id);

        when(blogRepository.findById(id)).thenReturn(Optional.of(blog));

        // Act
        blogService.deleteBlog(id);

        // Verify
        verify(blogRepository, times(1)).findById(id);
        verify(blogRepository, times(1)).delete(blog);
    }

    @Test
    public void testDeleteBlog_BlogNotFound(){
        Long id = 1L;

        when(blogRepository.findById(id)).thenReturn(Optional.empty());

        // Assert
        assertThrows(ResourceNotFoundException.class, () -> blogService.deleteBlog(id));

        // Verify
        verify(blogRepository, times(1)).findById(id);
    }
}
