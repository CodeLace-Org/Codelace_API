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
public class PlanControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllPlans() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/plans"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetPlanById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/plans/{id}", 1))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
