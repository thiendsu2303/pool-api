package com.example.pool_api.controller;

import com.example.pool_api.controller.PoolController;
import com.example.pool_api.service.PoolService;
import com.example.pool_api.model.Pool;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import static org.mockito.Mockito.*;

import java.util.*;

@WebMvcTest(PoolController.class)
public class PoolControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private PoolController poolController;

    @Mock
    private PoolService poolService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(poolController).build();
    }

    @Test
    public void testAppendOrInsertPool_Insert() throws Exception {
        long poolId = 1;
        List<Integer> values = Arrays.asList(1, 2, 3);

        when(poolService.appendOrInsertPool(poolId, values)).thenReturn("inserted");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pool/append")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"poolId\": " + poolId + ", \"values\": [1, 2, 3]}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("inserted"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testAppendOrInsertPool_Append() throws Exception {
        long poolId = 1;
        List<Integer> newValues = Arrays.asList(4, 5, 6);

        when(poolService.appendOrInsertPool(poolId, newValues)).thenReturn("appended");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/pool/append")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"poolId\": " + poolId + ", \"values\": [4, 5, 6]}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("appended"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testQueryPool_PoolNotFound() throws Exception {
        long poolId = 999;
        double percentile = 50.0;

        when(poolService.queryPool(poolId, percentile)).thenReturn(Collections.singletonMap("error", "Pool not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pool/query")
                        .param("poolId", String.valueOf(poolId))
                        .param("percentile", String.valueOf(percentile)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Pool not found"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testQueryPool_OutOfBounds() throws Exception {
        long poolId = 1;
        double percentile = 200.0; // Out of bounds

        when(poolService.queryPool(poolId, percentile)).thenReturn(Collections.singletonMap("error", "Percentile index is out of bounds"));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pool/query")
                        .param("poolId", String.valueOf(poolId))
                        .param("percentile", String.valueOf(percentile)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Percentile index is out of bounds"))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testQueryPool_Success() throws Exception {
        long poolId = 1;
        double percentile = 50.0;

        Map<String, Object> response = new HashMap<>();
        response.put("quantile", 3.0);
        response.put("totalCount", 5);

        when(poolService.queryPool(poolId, percentile)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pool/query")
                        .param("poolId", String.valueOf(poolId))
                        .param("percentile", String.valueOf(percentile)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantile").value(3.0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalCount").value(5))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testQueryPool_CacheHit() throws Exception {
        long poolId = 1;
        double percentile = 50.0;

        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5);
        Map<String, Object> response = new HashMap<>();
        response.put("quantile", 3.0);
        response.put("totalCount", 5);

        when(poolService.queryPool(poolId, percentile)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pool/query")
                        .param("poolId", String.valueOf(poolId))
                        .param("percentile", String.valueOf(percentile)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalCount").value(5))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void testQueryPool_CacheMiss() throws Exception {
        long poolId = 2;
        double percentile = 50.0;

        List<Integer> values = Arrays.asList(1, 2, 3, 4, 5);
        Map<String, Object> response = new HashMap<>();
        response.put("quantile", 3.0);
        response.put("totalCount", 5);

        when(poolService.queryPool(poolId, percentile)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/pool/query")
                        .param("poolId", String.valueOf(poolId))
                        .param("percentile", String.valueOf(percentile)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalCount").value(5))
                .andDo(MockMvcResultHandlers.print());
    }
}
