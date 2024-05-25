package com.codelace.codelace.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.codelace.codelace.exception.ResourceNotFoundException;
import com.codelace.codelace.mapper.PostMapper;
import com.codelace.codelace.model.dto.PostByProjectResponseDTO;
import com.codelace.codelace.model.dto.PostRequestDTO;
import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Project;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.PostRepository;
import com.codelace.codelace.repository.ProjectRepository;
import com.codelace.codelace.repository.StudentRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    // Instance mapper
	@Mock
	private PostMapper postMapper;

    @Mock
	private PostRepository postRepository;

	@Mock
	private StudentRepository studentRepository;

	@Mock
	private ProjectRepository projectRepository;
    // Test Service
	@InjectMocks
	private PostService postService;

    @Test
    public void testGetAllPosts(){
        // Arrange
		Post post1 = new Post();
		post1.setId(1L);
		post1.setDemoUrl("desplegado.com");
		post1.setRepoUrl("github.com");
		post1.setDescription("Proyecto bello");
		post1.setDate(LocalDate.now());
        post1.setImage(null);

		Post post2 = new Post();
        post2.setId(2L);
        post2.setDemoUrl("desplegado2.com");
        post2.setRepoUrl("github2.com");
        post2.setDescription("Proyecto bello2");
        post2.setDate(LocalDate.now());
        post2.setImage(null);

        List<Post> posts = List.of(post1, post2);

        // Mocking Repository
		when(postRepository.findAll()).thenReturn(posts);

        // Mocking mapper
		PostResponseDTO responseDTO1 = new PostResponseDTO();
		responseDTO1.setId(post1.getId());
		PostResponseDTO responseDTO2 = new PostResponseDTO();
		responseDTO2.setId(post2.getId());

        List<PostResponseDTO> expectedResponse = Arrays.asList(responseDTO1, responseDTO2);
		when(postMapper.convertToListDTO(posts)).thenReturn(expectedResponse);

        // Act
		List<PostResponseDTO> result = postService.getAllPosts();

		// Assert
		assertNotNull(result);
		assertEquals(expectedResponse.size(), result.size());

		// Verify that repository and mapper methods were called
		verify(postRepository, times(1)).findAll();
		verify(postMapper, times(1)).convertToListDTO(posts);
    }

	@Test public void testGetPostsByProjectId_ExistingId(){
		// Arrange
		Long id = 1L;

		Student student1 = new Student();
		Student student2 = new Student();
		student1.setId(1L);
		student1.setUsername("student1");
		student1.setEmail("student1@gmail.com");
		student1.setPwd("contrasenafacilita");

		student2.setId(2L);
		student2.setUsername("student2");
		student2.setEmail("student2@gmail.com");
		student2.setPwd("contrasenafacilita2");

		// Agregar datos simulados a posts
		List<Object[]> posts = new ArrayList<>(List.of(
			new Object[]{1L, student1, 10L, 5L, LocalDate.parse("2024-01-01"), null}, 
			new Object[]{2L, student2, 15L, 10L, LocalDate.parse("2024-03-10"), null}
		));

		// Crear posts esperados simulados
		List<PostByProjectResponseDTO> expectedPosts = posts.stream()
				.map(postData -> {
					PostByProjectResponseDTO postByProjectResponse = new PostByProjectResponseDTO();
					postByProjectResponse.setId((Long) postData[0]);
					postByProjectResponse.setStudent((Student) postData[1]);
					postByProjectResponse.setRockets((Long) postData[2]);
					postByProjectResponse.setComments((Long) postData[3]);
					postByProjectResponse.setDate((LocalDate) postData[4]);
					postByProjectResponse.setImage((byte[]) postData[5]);
					return postByProjectResponse;
				})
				.collect(Collectors.toList());

		// Configurar el comportamiento simulado del repositorio
		when(postRepository.findAllByProjectId(id)).thenReturn(Optional.of(posts));

		// Act
		List<PostByProjectResponseDTO> result = postService.getPostsByProjectId(id);

		// Assert
		assertEquals(expectedPosts.size(), result.size());

		// Verify that repository and mapper methods were called
		verify(postRepository, times(1)).findAllByProjectId(id);
	}

	@Test
	public void testGetPostsByProjectId_NonExistingId() {
		// Arrange
		Long id = 999L;

		// Configurar el comportamiento simulado del repositorio
		when(postRepository.findAllByProjectId(id)).thenReturn(Optional.empty());

		// Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> postService.getPostsByProjectId(id));
	}

	@Test
	public void testGetPostsByStudent_Successful() {
		// Arrange
		Long id = 1L;

		Student student = new Student();
		student.setId(id);
		student.setUsername("student1");
		student.setEmail("student1@gmail.com");
		student.setPwd("contrasenafacilita");

		Post post1 = new Post();
		post1.setId(1L);
		post1.setDemoUrl("desplegado.com");
		post1.setRepoUrl("githubpro.com");
		post1.setDescription("gran proyecto");
		post1.setDate(LocalDate.parse("2024-03-10"));
		post1.setImage(null);
		Post post2 = new Post();
		post2.setId(2L);
		post2.setDemoUrl("desplegado2.com");
		post2.setRepoUrl("githubpro2.com");
		post2.setDescription("gran proyecto2");
		post2.setDate(LocalDate.parse("2024-03-19"));
		post2.setImage(null);
		List<Post> posts = Arrays.asList(post1, post2);

		// Mocking repository
		when(studentRepository.findById(id)).thenReturn(Optional.of(student));
		when(postRepository.findAllByStudent(student)).thenReturn(Optional.of(posts));

		// Mocking mapper
		PostResponseDTO responseDTO1 = new PostResponseDTO();
		responseDTO1.setId(post1.getId());
		responseDTO1.setDemoUrl(post1.getDemoUrl());
		responseDTO1.setRepoUrl(post1.getRepoUrl());
		responseDTO1.setDescription(post1.getDescription());
		responseDTO1.setDate(post1.getDate());
		responseDTO1.setImage(null);
		PostResponseDTO responseDTO2 = new PostResponseDTO();
		responseDTO2.setId(post2.getId());
		responseDTO2.setDemoUrl(post2.getDemoUrl());
		responseDTO2.setRepoUrl(post2.getRepoUrl());
		responseDTO2.setDescription(post2.getDescription());
		responseDTO2.setDate(post2.getDate());
		responseDTO2.setImage(null);

		List<PostResponseDTO> expectedResponse = Arrays.asList(responseDTO1, responseDTO2);
		when(postMapper.convertToListDTO(posts)).thenReturn(expectedResponse);

		// Act
		List<PostResponseDTO> result = postService.getPostsByStudent(id);

		// Assert
		assertNotNull(result);
		assertEquals(expectedResponse.size(), result.size());

		// Verify that repository and mapper methods were called
		verify(studentRepository, times(1)).findById(id);
		verify(postRepository, times(1)).findAllByStudent(student);
		verify(postMapper, times(1)).convertToListDTO(posts);
	}

	@Test
	public void testGetPostsByStudent_StudentNotFound() {
		// Arrange
		Long id = 999L;

		// Mocking repository
		when(studentRepository.findById(id)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> postService.getPostsByStudent(id));
	}

	@Test
	public void testGetPostsByStudent_PostsNotFound() {
		// Arrange
		Long id = 1L;

		Student student = new Student();
		student.setId(id);
		student.setUsername("student1");
		student.setEmail("student1@gmail.com");
		student.setPwd("contrasenafacilita");

		// Simular que el estudiante existe pero los posts no
		when(studentRepository.findById(id)).thenReturn(Optional.of(student));
		when(postRepository.findAllByStudent(student)).thenReturn(Optional.empty());

		// Act & Assert
		assertThrows(ResourceNotFoundException.class, () -> postService.getPostsByStudent(id));
	}

	@Test
	public void testCreatePost_Successfully() {

		LocalDate localDate = LocalDate.now();
        String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
		PostRequestDTO requestDTO = new PostRequestDTO();
		requestDTO.setStudent(1L);
		requestDTO.setProject(1L);
		requestDTO.setDemoUrl("demoUrl");
		requestDTO.setRepoUrl("repoUrl");
		requestDTO.setDescription("Description");
		requestDTO.setImage(Base64.getDecoder().decode(base64Image));
	
		Student student = new Student();
		student.setId(1L);
	
		Project project = new Project();
		project.setId(1L);
		
		when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.of(project));
		when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
		
		Post post = new Post();
		post.setId(1L);
		post.setStudent(student);
		post.setProject(project);
		post.setDemoUrl(requestDTO.getDemoUrl());
		post.setRepoUrl(requestDTO.getRepoUrl());
		post.setDescription(requestDTO.getDescription());
		post.setDate(localDate);
		post.setImage(requestDTO.getImage());
	
		when(postMapper.convertToEntity(requestDTO)).thenReturn(post);
		when(postRepository.save(post)).thenReturn(post);
		when(postMapper.convertToDTO(post)).thenReturn(new PostResponseDTO(post.getId(), post.getDemoUrl(), post.getRepoUrl(), post.getDescription(), post.getDate(), post.getImage()));
	
		PostResponseDTO result = postService.createPost(requestDTO);
	
		assertNotNull(result);
		assertEquals(post.getId(), result.getId());
		assertEquals(post.getDemoUrl(), result.getDemoUrl());
		assertEquals(post.getRepoUrl(), result.getRepoUrl());
		assertEquals(post.getDescription(), result.getDescription());
		assertEquals(post.getDate(), result.getDate());
		assertEquals(post.getImage(), result.getImage());
	
		verify(projectRepository, times(1)).findById(requestDTO.getProject());
		verify(studentRepository,times(1)).findById(requestDTO.getStudent());
		verify(postMapper, times(1)).convertToEntity(requestDTO);
		verify(postRepository, times(1)).save(post);
		verify(postMapper, times(1)).convertToDTO(post);
	}

	@Test
	public void testCreatePost_StudentNotFound(){

		String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
		PostRequestDTO requestDTO = new PostRequestDTO();
		requestDTO.setStudent(2L);
		requestDTO.setProject(1L);
		requestDTO.setDemoUrl("demoUrl");
		requestDTO.setRepoUrl("repoUrl");
		requestDTO.setDescription("Description");
		requestDTO.setImage(Base64.getDecoder().decode(base64Image));

		Project project = new Project();
		project.setId(1L);

		//Long studentId = 999L;
		Student student = new Student();
		student.setId(1L);

		when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.of(project));
		when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> postService.createPost(requestDTO));

		verify(projectRepository, times(1)).findById(requestDTO.getProject());
		verify(studentRepository, times(1)).findById(requestDTO.getStudent());
	}


	@Test
	public void testCreatePost_ProjectNotFound(){
		String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
		PostRequestDTO requestDTO = new PostRequestDTO();
		requestDTO.setStudent(1L);
		requestDTO.setProject(2L);
		requestDTO.setDemoUrl("demoUrl");
		requestDTO.setRepoUrl("repoUrl");
		requestDTO.setDescription("Description");
		requestDTO.setImage(Base64.getDecoder().decode(base64Image));

		Project project = new Project();
		project.setId(1L);

		when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.empty());
		
		assertThrows(ResourceNotFoundException.class, () -> postService.createPost(requestDTO));

		verify(projectRepository, times(1)).findById(requestDTO.getProject());
	}


	/*@Test
	public void testCreatePost_PostAlreadyExists(){
		String base64Image = "iVBORw0KGgoAAAANSUhEUgAAAOEAAADhCAMAAAAJbSJIAAAAM1BMVEXk5ueutLeqsbTn6eqpr7PJzc/j5ebf4eLZ3N2wtrnBxsjN0NLGysy6v8HT1tissra8wMNxTKO9AAAFDklEQVR4nO2d3XqDIAxAlfivoO//tEOZWzvbVTEpic252W3PF0gAIcsyRVEURVEURVEURVEURVEURVEURVEURVEURVEURflgAFL/AirAqzXO9R7XNBVcy9TbuMHmxjN6lr92cNVVLKEurVfK/zCORVvW8iUBnC02dj+Wpu0z0Y6QlaN5phcwZqjkOkK5HZyPAjkIjSO4fIdfcOwFKkJlX4zPu7Ha1tIcwR3wWxyFhRG6g4Je0YpSPDJCV8a2Sv2zd1O1x/2WMDZCwljH+clRrHfWCLGK8REMiql//2si5+DKWKcWeAGcFMzzNrXC/0TUwQ2s6+LhlcwjTMlYsUIQzPOCb7YBiyHopyLXIEKPEkI/TgeuiidK/R9FniUDOjRDpvm0RhqjMyyXNjDhCfIMYl1gGjIMIuYsnGEYRMRZOMMunaLVwpWRW008v6fYKDIzxCwVAeNSO90BJW6emelYBRF/kHpYGVaoxTDAaxOFsfP9y8hpJ4xd7gOcij7JNGQ1EYFgkPJa1jQEiYZXRaRINKxSDUW9n+FT82lSKadkiru9/4XPqSLWOekGPoY05TAvLm9orm+YWuwHoBHkZKijNBJGmeb61eL6Ff/6q7bLr7yvv3vKGhpDRjvgjGaPz+gUg6YgcvpyAR2FIZ9U6nEEyZRTovmEU32KichpGn7C17XrfyH9gK/c0CMP05HZIM2uf9sEveizKveBy9/6Qt7o89ne33D525cfcIMW6ab+TMEukQbQbu+xu7X3A9bChmWaCeAkG17bpntwXgWxHaMzGPmUaR5dQZiKqRVeUZ3047fi3nAu28h4CHxCsZAgmEH8Y27jJAhm8c+5RQzRQNVGhVFSfxOYIjp/pP7RxzjevYXVGf4eLt+BJ1vCuLuLkrgABgCGXZ2wik5uty+oBvNirI6mkzhAf4Gsb58Hcm67Jzd+KwD10BYPLL3e0MjvKrgAULnOfveF/O4N2Xb9BZom3gJes3F9X5Zze8/6Yt09b4CrqsEjUv8oFBaR2rl+6CZr2xVrp24o/WitBKuGrrpl1+bFkmK2qXTON4VpbdfLa7o7y/WdLxG7lm2Lqh2clOwTegbvc/vj2U78CwhA87Bn8G5Nk3eOb0Nsr9flz3sG78UUtue4kpv1xvjg3TMay62BMlTlP+vrOMnJsRmt/ze0jsfkPPYdAH57hK+34PeOyc8XIXu5xT2HsUkdZz+adwg8HGFfQ3K5jtDvbUiO4Di9/ywHGrL88pDizZ++oTp+an+SMX/ndymUCwmHMdO7yuOx83pUx/eEMU0AvxWndwgidAqOZ8ypCwdEfvvEo6D9HwpA8wzvmOJEqAg9ySu8g4x0Hb9hSB/BANEKJ+LbPBU0lzbAJs4xt1AoshKkUGQmiH8/jJ0gdhTTLmSegHlPE0oOdXALnqDjKYh3px//fSgSWG8UqfrrIICzYYSJXRr9BSPbpNzw7gBjKjKOYI7ReIGqQRIap5+5MdjyvuDkExvGeXSlONWZAP3/AZBwJohU7QJRGU+cTVH18ELmRPNBmibW6MT/k1b0XhdkRBvyT6SB6EYv/GvhSmRNpGngRULsAlxMCGNXp7w3FfdEbTEEDdLI9TdIKRUzUesa3I461ER8cpNT7gMRhpKmYVS9ELOgCUQsa4SsulciKiLbY+AnHD8cpuhISsnxpamI84sbDq9qYJgf8wiiOBrC7Ml7M7ZECCqKoiiKoiiKoiiKoijv5AvJxlZRyNWWLwAAAABJRU5ErkJggg==";
		PostRequestDTO requestDTO = new PostRequestDTO();
		requestDTO.setStudent(1L);
		requestDTO.setProject(1L);
		requestDTO.setDemoUrl("demoUrl");
		requestDTO.setRepoUrl("repoUrl");
		requestDTO.setDescription("Description");
		requestDTO.setImage(Base64.getDecoder().decode(base64Image));

		Project project = new Project();
		project.setId(1L);

		Student student = new Student();
		student.setId(1L);

		Post post = new Post();
		post.setId(1L);
		post.setDemoUrl("demoUrl");
		post.setRepoUrl("repoUrl");

		when(projectRepository.findById(requestDTO.getProject())).thenReturn(Optional.of(project));
		when(studentRepository.findById(requestDTO.getStudent())).thenReturn(Optional.of(student));
		when(postRepository.findByDemoUrlAndRepoUrl(requestDTO.getDemoUrl(), requestDTO.getRepoUrl())).thenReturn(Optional.of(post));

		assertThrows(ResourceDuplicateException.class,() -> postService.createPost(requestDTO));

		verify(projectRepository, times(1)).findById(requestDTO.getProject());
		verify(studentRepository, times(1)).findById(requestDTO.getStudent());
		//verify(postRepository, times(1)).findByDemoUrlAndRepoUrl(requestDTO.getDemoUrl(), requestDTO.getRepoUrl());
	}*/
}
