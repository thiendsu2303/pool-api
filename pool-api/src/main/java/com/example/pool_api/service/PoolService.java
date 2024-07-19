package com.example.pool_api.service;

import com.example.pool_api.model.Pool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class PoolService {

    private final Map<Long, Pool> poolMap = new ConcurrentHashMap<>();

    @Autowired
    private RedisCacheService redisCacheService;

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
        List<Integer> values;

        if (redisCacheService.isPoolCached(poolId)) {
            values = redisCacheService.getPoolValues(poolId);
        } else {
            Pool pool = poolMap.get(poolId);
            if (pool == null) {
                return Map.of("error", "Pool not found");
            }
            values = new ArrayList<>(pool.getPoolValues());
            redisCacheService.cachePoolValues(poolId, values); // Cache lại giá trị pool
        }

        Map<String, Object> response = new HashMap<>();

        // Kiểm tra percentile có hợp lệ không
        if (percentile < 0 || percentile > 100) {
            response.put("error", "Invalid percentile value");
            return response;
        }

        Collections.sort(values);

        // Kiểm tra số lượng giá trị trong pool có đủ để tính toán không
        if (values.size() == 0) {
            response.put("error", "Pool is empty");
            return response;
        }

        double index = percentile / 100.0 * (values.size() - 1);
        int lower = (int) Math.floor(index);
        int upper = (int) Math.ceil(index);

        // Kiểm tra chỉ số lower và upper có hợp lệ không
        if (lower < 0 || lower >= values.size() || upper < 0 || upper >= values.size()) {
            response.put("error", "Index out of bounds");
            return response;
        }

        double quantile = values.get(lower) + (values.get(upper) - values.get(lower)) * (index - lower);

        response.put("quantile", quantile);
        response.put("totalCount", values.size());
        return response;
    }
}
