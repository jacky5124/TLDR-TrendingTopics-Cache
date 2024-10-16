package com.jackist.tldr.trendingtopics.cache.configuration;

import com.jackist.tldr.trendingtopics.cache.pojo.Result;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisOperations<String, Result> redisOperations(
            RedisConnectionFactory connectionFactory
    ) {
        RedisTemplate<String, Result> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Result.class));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
