package com.codelace.codelace.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.codelace.codelace.model.dto.RouteRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class RouteControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testGetAllRoutes() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/routes"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetRouteById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/routes/{id}", "3"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testCreateRoute() throws Exception {
		RouteRequestDTO routeRequestDTO = new RouteRequestDTO();
		routeRequestDTO.setName("web");
		routeRequestDTO.setDescription("desarrollo web");
		routeRequestDTO.setIcon("<svg></svg>");

		mockMvc.perform(MockMvcRequestBuilders.post("/routes")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(routeRequestDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	public void testUpdateRoute() throws Exception {
		RouteRequestDTO routeRequestDTO = new RouteRequestDTO();
		routeRequestDTO.setName("web");
		routeRequestDTO.setDescription("desarrollo web");
		routeRequestDTO.setIcon("<svg></svg>");
		mockMvc.perform(MockMvcRequestBuilders.put("/routes/{id}", "3")
				.contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(routeRequestDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk());

	}

	// @Test
	// public void testDeleteRoute() throws Exception {
	// 	mockMvc.perform(MockMvcRequestBuilders.delete("/routes/{id}", "5"))
	// 			.andExpect(MockMvcResultMatchers.status().isNoContent());
	// }

	// TODO -> Sovero ero haz tu test sovero cabezero ero cervezero

	// Método auxiliar para convertir objetos a JSON
	private String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
