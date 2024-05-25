package com.codelace.codelace.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	// TODO getAllPosts
	// TODO createPost

	@Test
	public void testGetPostByStudent() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders
				.get("/posts/students/{student_id}", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
