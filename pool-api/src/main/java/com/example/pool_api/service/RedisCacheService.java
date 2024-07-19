package com.example.pool_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RedisCacheService {

    @Autowired
    private JedisPool jedisPool;

    private static final String CACHE_PREFIX = "pool:";
    private static final int LRU_MAX_SIZE = 100; // Số lượng pool ID tối đa trong cache

    public void cachePoolValues(long poolId, List<Integer> values) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = CACHE_PREFIX + poolId;
            jedis.rpush(key, values.stream().map(String::valueOf).toArray(String[]::new));
            jedis.ltrim(key, -LRU_MAX_SIZE, -1); // Giữ lại LRU_MAX_SIZE phần tử cuối cùng
        }
    }

    public List<Integer> getPoolValues(long poolId) {
        try (Jedis jedis = jedisPool.getResource()) {
            String key = CACHE_PREFIX + poolId;
            return jedis.lrange(key, 0, -1).stream().map(Integer::valueOf).collect(Collectors.toList());
        }
    }

    public boolean isPoolCached(long poolId) {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.exists(CACHE_PREFIX + poolId);
        }
    }
}
