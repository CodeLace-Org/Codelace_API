package com.codelace.codelace.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.codelace.codelace.model.dto.PostRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetPostById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/posts/{id}", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetAllPosts() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/posts"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testCreatePost() throws Exception {
		PostRequestDTO postRequestDTO = new PostRequestDTO();
		postRequestDTO.setStudent(1L);
		postRequestDTO.setProject(1L);
		postRequestDTO.setDemoUrl("demo.com");
		postRequestDTO.setRepoUrl("github.com");
		postRequestDTO.setDescription("descripcion del post");

		Path path = Paths.get(
				"D://OneDrive - Universidad Peruana de Ciencias/Documents/Carrera/2024-1/Software Engineering/TP/Codelace_API/mediafiles/gato3.jpg");
		String originalFileName = "gato3.jpg";
		String contentType = MediaType.IMAGE_JPEG_VALUE;
		byte[] content = null;
		try {
			content = Files.readAllBytes(path);
		} catch (final IOException e) {
			throw new RuntimeException("Failed to store file.", e);
		}
		MockMultipartFile result = new MockMultipartFile("image", originalFileName, contentType, content);

		// Perform the POST request
		mockMvc.perform(
				MockMvcRequestBuilders.multipart("/posts")
						.file(result)
						.param("student", "1")
						.param("project", "1")
						.param("demoUrl", "demo.com")
						.param("repoUrl", "github.com")
						.param("description", "descripcion del post")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.MULTIPART_FORM_DATA))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	public void testGetAllPostsByStudent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/posts/students/{student_id}", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetAllPostsByProjectId() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/posts/projects/{project_id}", 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetPostByStudentAndProject() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/posts/students/{student_id}/projects/{project_id}", 1, 1))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}