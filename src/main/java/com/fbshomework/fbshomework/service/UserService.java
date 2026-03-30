package com.fbshomework.fbshomework.service;

import com.fbshomework.fbshomework.entity.User;      // ← 加这行
import com.fbshomework.fbshomework.mapper.UserMapper; // ← 加这行
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public String register(User user) {
        if (userMapper.findByUsername(user.getUsername()) != null) {
            return "用户名已存在";
        }
        userMapper.insert(user);
        return "注册成功";
    }

    public User login(String username, String password) {
        User user = userMapper.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}