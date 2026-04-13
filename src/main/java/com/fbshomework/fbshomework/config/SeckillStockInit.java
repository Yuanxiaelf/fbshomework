package com.fbshomework.fbshomework.config;

import com.fbshomework.fbshomework.mapper.SeckillMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeckillStockInit implements ApplicationRunner {

    private final SeckillMapper seckillMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void run(ApplicationArguments args) {
        Integer stock = seckillMapper.getStock(1L);
        if (stock != null) {
            redisTemplate.opsForValue().set("seckill:stock:1", stock);
            log.info("秒杀库存预热完成，库存：{}", stock);
        }
    }
}