package com.example.pool_api.model;

import java.util.ArrayList;
import java.util.List;

public class Pool {
    private long poolId;
    private List<Integer> poolValues;

    public Pool(long poolId) {
        this.poolId = poolId;
        this.poolValues = new ArrayList<>();
    }

    public long getPoolId() {
        return poolId;
    }

    public List<Integer> getPoolValues() {
        return poolValues;
    }

    public void appendValues(List<Integer> values) {
        this.poolValues.addAll(values);
    }
}
