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
import com.codelace.codelace.model.dto.PostResponseDTO;
import com.codelace.codelace.model.entity.Post;
import com.codelace.codelace.model.entity.Student;
import com.codelace.codelace.repository.PostRepository;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
    // Instance mapper
	@Mock
	private PostMapper postMapper;

    @Mock
	private PostRepository postRepository;

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

	// Method that returns all the posts by project id
	public List<PostByProjectResponseDTO> getPostsByProjectId(Long id) {
		List<Object[]> posts = postRepository.findAllByProjectId(id)
				.orElseThrow(() -> new ResourceNotFoundException("Posts not found for project with id " + id));

		return posts.stream()
				.map(postMapper::convertToListDTO)
				.toList();
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

	// Method that returns a list of post by student id
	public List<PostResponseDTO> getPostsByStudent(Long id_student) {
		Student student = studentRepository
				.findById(id_student)
				.orElseThrow(() -> new ResourceNotFoundException("Student not found with id " + id_student));
		List<Post> posts = postRepository
				.findAllByStudent(student)
				.orElseThrow(() -> new ResourceNotFoundException("Posts not found for student with id " + id_student));
		return postMapper.convertToListDTO(posts);
	}

	@Test
	public void testGetPostsByStudent_SuccessfulGet() {
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
				new Object[] { 1L, student1, 10L, 5L, LocalDate.parse("2024-01-01"), null },
				new Object[] { 2L, student2, 15L, 10L, LocalDate.parse("2024-03-10"), null }));

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
	}
}
