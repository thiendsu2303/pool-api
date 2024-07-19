package com.example.pool_api.controller;

import com.example.pool_api.service.PoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pool")
public class PoolController {

    @Autowired
    private PoolService poolService;

    @PostMapping("/append")
    public ResponseEntity<Map<String, String>> appendOrInsertPool(@RequestBody Map<String, Object> request) {
        long poolId = ((Number) request.get("poolId")).longValue();
        List<Integer> poolValues = (List<Integer>) request.get("poolValues");
        String status = poolService.appendOrInsertPool(poolId, poolValues);
        return ResponseEntity.ok(Map.of("status", status));
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> queryPool(@RequestBody Map<String, Object> request) {
        long poolId = ((Number) request.get("poolId")).longValue();
        double percentile = ((Number) request.get("percentile")).doubleValue();
        Map<String, Object> response = poolService.queryPool(poolId, percentile);
        return ResponseEntity.ok(response);
    }
}
