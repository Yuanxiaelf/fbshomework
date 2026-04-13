package com.fbshomework.fbshomework.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SeckillOrder {
    private Long id;
    private Long userId;
    private Long seckillId;
    private Long productId;
    private Integer status;
    private LocalDateTime createTime;
}