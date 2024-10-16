package com.jackist.tldr.trendingtopics.cache.service;

import com.jackist.tldr.trendingtopics.cache.pojo.Result;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private final RedisOperations<String, Result> redis;

    public CacheService(RedisOperations<String, Result> redis) {
        this.redis = redis;
    }

    public Result get(String mkt) {
        return redis.opsForValue().get(mkt);
    }

    public void put(String mkt, Result result) {
        redis.opsForValue().set(mkt, result);
    }
}
