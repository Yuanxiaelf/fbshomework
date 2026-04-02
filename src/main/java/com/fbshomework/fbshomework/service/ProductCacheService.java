package com.fbshomework.fbshomework.service;

import cn.hutool.core.util.RandomUtil;
import com.fbshomework.fbshomework.entity.Product;
import com.fbshomework.fbshomework.mapper.ProductMapper;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class ProductCacheService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RedissonClient redissonClient;
    private final ProductMapper productMapper;

    public ProductCacheService(RedisTemplate<String, Object> redisTemplate,
                               RedissonClient redissonClient,
                               ProductMapper productMapper) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.productMapper = productMapper;
    }

    private static final String CACHE_KEY = "product:";
    private static final String NULL_VALUE = "NULL";
    private static final long TTL_MINUTES = 30;
    private static final long NULL_TTL_MINUTES = 2;
    private static final long LOCK_TIMEOUT = 10;

    public Product getProductById(Long id) {
        String key = CACHE_KEY + id;

        Object cached = redisTemplate.opsForValue().get(key);
        if (cached != null) {
            if (NULL_VALUE.equals(cached)) return null;
            return (Product) cached;
        }

        RLock lock = redissonClient.getLock("lock:product:" + id);
        try {
            if (lock.tryLock(3, LOCK_TIMEOUT, TimeUnit.SECONDS)) {
                try {
                    cached = redisTemplate.opsForValue().get(key);
                    if (cached != null) {
                        if (NULL_VALUE.equals(cached)) return null;
                        return (Product) cached;
                    }

                    Product product = productMapper.selectById(id);

                    if (product == null) {
                        redisTemplate.opsForValue().set(
                                key, NULL_VALUE, NULL_TTL_MINUTES, TimeUnit.MINUTES);
                        return null;
                    }

                    long ttl = TTL_MINUTES + RandomUtil.randomLong(0, 10);
                    redisTemplate.opsForValue().set(key, product, ttl, TimeUnit.MINUTES);
                    return product;

                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return productMapper.selectById(id);
    }

    public void evictProductCache(Long id) {
        redisTemplate.delete(CACHE_KEY + id);
    }
}