package com.jackist.tldr.trendingtopics.summarycache.service;

import com.jackist.tldr.trendingtopics.summarycache.pojo.Result;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

@Service
public class SummaryCacheService {
    private final RedisOperations<String, Result> redis;

    public SummaryCacheService(RedisOperations<String, Result> redis) {
        this.redis = redis;
    }

    public Result get(String mkt) {
        return redis.opsForValue().get(mkt);
    }

    public void put(String mkt, Result result) {
        redis.opsForValue().set(mkt, result);
    }
}
