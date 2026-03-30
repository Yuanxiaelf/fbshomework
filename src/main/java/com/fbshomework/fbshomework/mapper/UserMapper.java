package com.fbshomework.fbshomework.mapper;

import com.fbshomework.fbshomework.entity.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user(username, password, email) VALUES(#{username}, #{password}, #{email})")
    int insert(User user);

    @Select("SELECT * FROM user WHERE username = #{username}")
    User findByUsername(String username);
}