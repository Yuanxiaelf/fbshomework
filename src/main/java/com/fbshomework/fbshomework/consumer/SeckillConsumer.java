package com.fbshomework.fbshomework.consumer;

import com.fbshomework.fbshomework.entity.SeckillOrder;
import com.fbshomework.fbshomework.mapper.SeckillMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SeckillConsumer {

    private final SeckillMapper seckillMapper;
    private final RedisTemplate<String, Object> redisTemplate;

    @KafkaListener(topics = "seckill-order", groupId = "seckill-group")
    public void onMessage(ConsumerRecord<String, String> record,
                          Acknowledgment ack) {
        String message = record.value();
        log.info("收到秒杀消息：{}", message);

        try {
            String[] parts  = message.split(",");
            Long userId     = Long.parseLong(parts[0]);
            Long seckillId  = Long.parseLong(parts[1]);
            Long productId  = Long.parseLong(parts[2]);
            Long orderId    = Long.parseLong(parts[3]);

            int rows = seckillMapper.decreaseStock(seckillId);
            if (rows > 0) {
                SeckillOrder order = new SeckillOrder();
                order.setId(orderId);
                order.setUserId(userId);
                order.setSeckillId(seckillId);
                order.setProductId(productId);
                seckillMapper.insertOrder(order);

                redisTemplate.opsForValue()
                        .set("seckill:ordered:" + userId + ":" + seckillId, "1", 24, TimeUnit.HOURS);
                log.info("订单落库成功，订单ID：{}", orderId);
            } else {
                log.warn("库存不足，消息丢弃：{}", message);
            }
            ack.acknowledge();

        } catch (Exception e) {
            log.error("消费失败，消息：{}，原因：{}", message, e.getMessage());
        }
    }
}