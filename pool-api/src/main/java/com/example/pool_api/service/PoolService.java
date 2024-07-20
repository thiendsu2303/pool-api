package com.example.pool_api.service;

import com.example.pool_api.model.Pool;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PoolService {

    private final Map<Long, Pool> poolMap = new HashMap<>();

    public String appendOrInsertPool(long poolId, List<Integer> poolValues) {
        Pool pool = poolMap.getOrDefault(poolId, new Pool(poolId));
        if (pool.getPoolValues().isEmpty()) {
            pool.appendValues(poolValues);
            poolMap.put(poolId, pool);
            return "inserted";
        } else {
            pool.appendValues(poolValues);
            return "appended";
        }
    }

    public Map<String, Object> queryPool(long poolId, double percentile) {
        Pool pool = poolMap.get(poolId);
        if (pool == null) {
            return Map.of("error", "Pool not found");
        }

        List<Integer> values = new ArrayList<>(pool.getPoolValues());
        Map<String, Object> response = new HashMap<>();
        Collections.sort(values);

        if (percentile < 0 || percentile > 100) {
            return Map.of("error", "Percentile out of range");
        }

        double index = percentile / 100.0 * (values.size() - 1);
        int lower = (int) Math.floor(index);
        int upper = (int) Math.ceil(index);
        double quantile = values.get(lower) + (values.get(upper) - values.get(lower)) * (index - lower);

        response.put("quantile", quantile);
        response.put("totalCount", values.size());
        return response;
    }
}
