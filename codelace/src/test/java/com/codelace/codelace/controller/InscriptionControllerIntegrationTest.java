package com.codelace.codelace.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.codelace.codelace.model.dto.InscriptionRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class InscriptionControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllInscriptions() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetProjectById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/inscriptions/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testCreateInscription() throws Exception {
        InscriptionRequestDTO inscriptionRequestDTO = new InscriptionRequestDTO();
        inscriptionRequestDTO.setRoute(1L);
        inscriptionRequestDTO.setStudent(2L);

        mockMvc.perform(MockMvcRequestBuilders.post("/inscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(inscriptionRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated());

    }

    // @Test
    // public void testDeleteInscription() throws Exception {
    //     mockMvc.perform(MockMvcRequestBuilders.delete("/inscriptions/{id}", 1))
    //             .andExpect(MockMvcResultMatchers.status().isNoContent());
    // }

    // Método auxiliar para convertir objetos a JSON
	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
