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
        Long poolId = request.get("poolId") != null ? ((Number) request.get("poolId")).longValue() : null;
        List<Integer> poolValues = (List<Integer>) request.get("poolValues");
        if (poolId == null) {
            throw new IllegalArgumentException("poolId must not be null");
        }

        String status = poolService.appendOrInsertPool(poolId, poolValues);
        return ResponseEntity.ok(Map.of("message", status));
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, Object>> queryPool(@RequestBody Map<String, Object> request) {
        Long poolId = request.get("poolId") != null ? ((Number) request.get("poolId")).longValue() : null;
        Double percentile = request.get("percentile") != null ? ((Number) request.get("percentile")).doubleValue() : null;
        if (poolId == null || percentile == null) {
            throw new IllegalArgumentException("poolId and percentile must not be null");
        }
        Map<String, Object> response = poolService.queryPool(poolId, percentile);
        return ResponseEntity.ok(response);
    }
}
