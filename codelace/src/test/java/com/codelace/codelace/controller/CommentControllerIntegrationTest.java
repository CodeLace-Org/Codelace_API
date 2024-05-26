package com.codelace.codelace.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.codelace.codelace.model.dto.CommentRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class CommentControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testCreateComment() throws Exception {
		CommentRequestDTO commentRequestDTO = new CommentRequestDTO();
		commentRequestDTO.setContent("Comment 1");
		commentRequestDTO.setPost(2L);
		commentRequestDTO.setStudent(1L);

		mockMvc.perform(MockMvcRequestBuilders.post("/comments")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(commentRequestDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	// Método auxiliar para convertir objetos a JSON
	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
