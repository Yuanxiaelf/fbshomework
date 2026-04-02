package com.fbshomework.fbshomework.mapper;

import com.fbshomework.fbshomework.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProductMapper {

    @Select("SELECT * FROM product WHERE id = #{id}")
    Product selectById(Long id);
}