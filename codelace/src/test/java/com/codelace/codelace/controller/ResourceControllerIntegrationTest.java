package com.codelace.codelace.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.codelace.codelace.model.dto.ResourceRequestDTO;
import com.codelace.codelace.model.entity.Project;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class ResourceControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getAllResourceTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/resources"))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getResourceByIdTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/resources/{id}", 2))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void getAllByProject() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/resources/project/{id}", 1))
        .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void createResourceTest() throws Exception{
        Project project = new Project();
        project.setId(1L);

        ResourceRequestDTO resourceRequestDTO = new ResourceRequestDTO();
        resourceRequestDTO.setTitle("Resource 2");
        resourceRequestDTO.setLink("Link2.com");
        resourceRequestDTO.setProject(1L);

        mockMvc.perform(MockMvcRequestBuilders.post("/resources")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(resourceRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
