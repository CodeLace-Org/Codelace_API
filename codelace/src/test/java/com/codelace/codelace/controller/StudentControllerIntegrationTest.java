package com.codelace.codelace.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.codelace.codelace.model.dto.StudentRegisterRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllStudents() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetStudentById() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/students/{id}", 2))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateStudent() throws Exception {
        StudentRegisterRequestDTO studentRequestDTO = new StudentRegisterRequestDTO();
        studentRequestDTO.setEmail("pepito1234@gmail.com");
        studentRequestDTO.setUsername("pepito1234");
        studentRequestDTO.setPwd("Hola1234567!");
        studentRequestDTO.setConfirmPassword("Hola1234567!");

        mockMvc.perform(MockMvcRequestBuilders.post("/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(studentRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    public void testGetDetailsByStudentAndProject() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
            "/students/{id_student}/projects/{id_project}/details", 2, 1))
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
