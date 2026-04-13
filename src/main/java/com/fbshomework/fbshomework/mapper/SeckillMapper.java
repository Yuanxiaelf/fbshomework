package com.fbshomework.fbshomework.mapper;

import com.fbshomework.fbshomework.entity.SeckillOrder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface SeckillMapper {

    @Select("SELECT stock FROM seckill_product WHERE id = #{seckillId}")
    Integer getStock(Long seckillId);

    @Update("UPDATE seckill_product SET stock = stock - 1 WHERE id = #{seckillId} AND stock > 0")
    int decreaseStock(Long seckillId);

    @Insert("INSERT INTO seckill_order(id, user_id, seckill_id, product_id) VALUES(#{id}, #{userId}, #{seckillId}, #{productId})")
    int insertOrder(SeckillOrder order);
}