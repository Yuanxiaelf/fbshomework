package com.fbshomework.fbshomework.service;

import cn.hutool.core.lang.Snowflake;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class SeckillService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final Snowflake snowflake;

    private static final String STOCK_KEY   = "seckill:stock:";
    private static final String TOKEN_KEY   = "seckill:token:";
    private static final String ORDERED_KEY = "seckill:ordered:";
    private static final String TOPIC       = "seckill-order";

    public String generateToken(Long userId, Long seckillId) {
        String token = snowflake.nextIdStr();
        String key = TOKEN_KEY + userId + ":" + seckillId;
        redisTemplate.opsForValue().set(key, token, 5, TimeUnit.MINUTES);
        return token;
    }

    public String doSeckill(Long userId, Long seckillId, Long productId, String token) {
        String tokenKey = TOKEN_KEY + userId + ":" + seckillId;
        String savedToken = (String) redisTemplate.opsForValue().get(tokenKey);
        if (savedToken == null || !savedToken.equals(token)) {
            return "请勿重复提交";
        }
        redisTemplate.delete(tokenKey);

        String orderedKey = ORDERED_KEY + userId + ":" + seckillId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(orderedKey))) {
            return "您已参与过该秒杀";
        }

        String stockKey = STOCK_KEY + seckillId;
        Long stock = redisTemplate.opsForValue().decrement(stockKey);
        if (stock == null || stock < 0) {
            redisTemplate.opsForValue().increment(stockKey);
            return "库存不足";
        }

        String orderId = snowflake.nextIdStr();
        String message = userId + "," + seckillId + "," + productId + "," + orderId;
        kafkaTemplate.send(TOPIC, String.valueOf(userId), message);

        redisTemplate.opsForValue().set(orderedKey, "1", 5, TimeUnit.MINUTES);
        return "下单成功，订单ID：" + orderId;
    }
}