package com.example.pool_api.controller;

import com.example.pool_api.service.PoolService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PoolController.class)
public class PoolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PoolService poolService;

    @Test
    public void testInsertNewPool() throws Exception {
        when(poolService.appendOrInsertPool(any(Long.class), any(List.class))).thenReturn("inserted");

        mockMvc.perform(post("/api/pool/append")
                        .contentType("application/json")
                        .content("{\"poolId\": 123456, \"poolValues\": [1, 2, 3, 4, 5]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("inserted"));
    }

    @Test
    public void testQueryPoolWithValidPercentile() throws Exception {
        Map<String, Object> response = new HashMap<>();
        response.put("quantile", 4.6);
        response.put("totalCount", 5);

        when(poolService.queryPool(any(Long.class), anyDouble())).thenReturn(response);

        mockMvc.perform(post("/api/pool/query")
                        .contentType("application/json")
                        .content("{\"poolId\": 123456, \"percentile\": 90.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantile").value(4.6))
                .andExpect(jsonPath("$.totalCount").value(5));
    }

    @Test
    public void testHandleNonExistentPool() throws Exception {
        when(poolService.queryPool(any(Long.class), anyDouble())).thenReturn(Map.of("error", "Pool not found"));

        mockMvc.perform(post("/api/pool/query")
                        .contentType("application/json")
                        .content("{\"poolId\": 999999, \"percentile\": 50.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Pool not found"));
    }

    @Test
    public void testQueryPoolWithOutOfBoundsPercentile() throws Exception {
        when(poolService.queryPool(any(Long.class), anyDouble())).thenReturn(Map.of("error", "Percentile out of range"));

        mockMvc.perform(post("/api/pool/query")
                        .contentType("application/json")
                        .content("{\"poolId\": 123456, \"percentile\": 105.0}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Percentile out of range"));
    }

    @Test
    public void testAppendToExistingPool() throws Exception {
        when(poolService.appendOrInsertPool(any(Long.class), any(List.class))).thenReturn("appended");

        mockMvc.perform(post("/api/pool/append")
                        .contentType("application/json")
                        .content("{\"poolId\": 123456, \"poolValues\": [4, 5, 6]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("appended"));
    }

    @Test
    public void testHandleEmptyPool() throws Exception {
        when(poolService.appendOrInsertPool(any(Long.class), any(List.class))).thenReturn("inserted");

        mockMvc.perform(post("/api/pool/append")
                        .contentType("application/json")
                        .content("{\"poolId\": 654321, \"poolValues\": []}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("inserted"));
    }
}
